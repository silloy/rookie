package me.silloy.expression;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 简单语法解析器实现示例
 */
@Slf4j
public class SimpleSyntaxParser {

    /**
     * 严格模式解析语法
     */
    public static ParsedClauseAst parse(String rawClause) {
        return parse(rawClause, true);
    }

    /**
     * 解析传入词为db可识别的语法
     * @param rawClause 原始语法
     * @param strictMode 是否是严格模式, true:是, false:否
     * @return 解析后的结构
     */
    public static ParsedClauseAst parse(String rawClause, boolean strictMode) {
        List<TokenDescriptor> tokens = tokenize(rawClause, strictMode);
        Map<String, Object> idList = enhanceTokenType(tokens);
        return buildAst(tokens, idList);
    }

    /**
     * 构建抽象语法树对象
     */
    private static ParsedClauseAst buildAst(List<TokenDescriptor> tokens,
                                            Map<String, Object> idList) {
        List<SyntaxStatement> treesFlat = new ArrayList<>(tokens.size());
        Iterator<TokenDescriptor> tokenItr = tokens.iterator();
        while (tokenItr.hasNext()) {
            TokenDescriptor token = tokenItr.next();
            String word = token.getRawWord();
            TokenTypeEnum tokenType = token.getTokenType();
            SyntaxStatement branch;
            switch (tokenType) {
                case FUNCTION_SYS_CUSTOM:
                    String funcName = word.substring(0, word.indexOf('(')).trim();
                    SyntaxStatementHandlerFactory handlerFactory
                            = SyntaxSymbolTable.getUdfHandlerFactory(funcName);
                    branch = handlerFactory.newHandler(token, tokenItr, tokenType);
                    treesFlat.add(branch);
                    break;
                case KEYWORD_SYS_CUSTOM:
                    branch = SyntaxSymbolTable.getSysKeywordHandlerFactory()
                            .newHandler(token, tokenItr, tokenType);
                    treesFlat.add(branch);
                    break;
                case KEYWORD_SQL:
                    branch = SyntaxSymbolTable.getSqlKeywordHandlerFactory()
                            .newHandler(token, tokenItr, tokenType);
                    treesFlat.add(branch);
                    break;
                case WORD_NORMAL:
                case WORD_NUMBER:
                case WORD_STRING:
                case CLAUSE_SEPARATOR:
                case SIMPLE_MATH_OPERATOR:
                case WORD_ARRAY:
                case COMPARE_OPERATOR:
                case FUNCTION_NORMAL:
                case LABEL_ID:
                case FUNCTION_SQL:
                default:
                    branch = SyntaxSymbolTable.getCommonHandlerFactory().newHandler(token, tokenItr, tokenType);
                    treesFlat.add(branch);
                    break;
            }
        }
        return new ParsedClauseAst(idList, treesFlat);
    }

    /**
     * 语义增强处理, 加强token类型描述，并返回 id 信息
     */
    private static Map<String, Object> enhanceTokenType(List<TokenDescriptor> tokens) {
        Map<String, Object> idList = new HashMap<>();
        for (TokenDescriptor token : tokens) {
            String word = token.getRawWord();
            TokenTypeEnum newTokenType = token.getTokenType();
            switch (token.getTokenType()) {
                case WORD_NORMAL:
                    if(word.startsWith("$")) {
                        newTokenType = TokenTypeEnum.LABEL_ID;
                        idList.put(word, word.substring(1));
                    }
                    else if(StringUtils.isNumeric(word)) {
                        newTokenType = TokenTypeEnum.WORD_NUMBER;
                    }
                    else {
                        newTokenType = SyntaxSymbolTable.keywordTypeOf(word);
                    }
                    token.changeTokenType(newTokenType);
                    break;
                case WORD_STRING:
                    String innerSysCustomKeyword = readSplitWord(
                            word.toCharArray(), 1, "#{", "}");
                    if(innerSysCustomKeyword.length() > 3) {
                        newTokenType = TokenTypeEnum.KEYWORD_SYS_CUSTOM;
                    }
                    token.changeTokenType(newTokenType);
                    break;
                case FUNCTION_NORMAL:
                    newTokenType = SyntaxSymbolTable.functionTypeOf(word);
                    token.changeTokenType(newTokenType);
                    break;
            }
        }
        return idList;
    }

    /**
     * 查询语句分词操作
     * @param rawClause 原始查询语句
     * @param strictMode 是否是严格模式, true:是, false:否
     * @return token化的单词
     */
    private static List<TokenDescriptor> tokenize(String rawClause, boolean strictMode) {
        char[] clauseItr = rawClause.toCharArray();
        List<TokenDescriptor> parsedTokenList = new ArrayList<>();
        Stack<ColumnNumDescriptor> specialSeparatorStack = new Stack<>();
        int clauseLength = clauseItr.length;
        StringBuilder field;
        String fieldGot;
        char nextChar;

        outer:
        for (int i = 0; i < clauseLength; ) {
            char currentChar = clauseItr[i];
            switch (currentChar) {
                case '\'':
                case '\"':
                    fieldGot = readSplitWord(clauseItr, i,
                            currentChar, currentChar);
                    i += fieldGot.length();
                    parsedTokenList.add(
                            new TokenDescriptor(fieldGot, TokenTypeEnum.WORD_STRING));
                    continue;
                case '[':
                case ']':
                case '(':
                case ')':
                case '{':
                case '}':
                    if(specialSeparatorStack.empty()) {
                        specialSeparatorStack.push(ColumnNumDescriptor.newData(i, currentChar));
                        parsedTokenList.add(new TokenDescriptor(currentChar, TokenTypeEnum.CLAUSE_SEPARATOR));
                        break;
                    }
                    parsedTokenList.add(new TokenDescriptor(currentChar, TokenTypeEnum.CLAUSE_SEPARATOR));
                    char topSpecial = specialSeparatorStack.peek().getKeyword().charAt(0);
                    if(topSpecial == '(' && currentChar == ')'
                            || topSpecial == '[' && currentChar == ']'
                            || topSpecial == '{' && currentChar == '}') {
                        specialSeparatorStack.pop();
                        break;
                    }
                    specialSeparatorStack.push(ColumnNumDescriptor.newData(i, currentChar));
                    break;
                case ' ':
                    break;
                case '@':
                    nextChar = clauseItr[i + 1];
                    if(nextChar == '{') {
                        fieldGot = readSplitWord(clauseItr, i, "@{", "}@");
                        i += fieldGot.length();
                        parsedTokenList.add(new TokenDescriptor(fieldGot, TokenTypeEnum.LABEL_ID));
                        continue;
                    }
                    break;
                case '#':
                    nextChar = clauseItr[i + 1];
                    if(nextChar == '{') {
                        fieldGot = readSplitWord(clauseItr, i, "#{", "}");
                        i += fieldGot.length();
                        parsedTokenList.add(new TokenDescriptor(fieldGot, TokenTypeEnum.KEYWORD_SYS_CUSTOM));
                        continue;
                    }
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                    nextChar = clauseItr[i + 1];
                    if(currentChar == '-'
                            && nextChar >= '0' && nextChar <= '9') {
                        StringBuilder numberBuff = new StringBuilder(currentChar + "" + nextChar);
                        ++i;
                        while ((i + 1) < clauseLength){
                            nextChar = clauseItr[i + 1];
                            if(nextChar >= '0' && nextChar <= '9'
                                    || nextChar == '.') {
                                ++i;
                                numberBuff.append(nextChar);
                                continue;
                            }
                            break;
                        }
                        parsedTokenList.add(
                                new TokenDescriptor(numberBuff.toString(),
                                        TokenTypeEnum.WORD_NUMBER));
                        break;
                    }
                    parsedTokenList.add(
                            new TokenDescriptor(currentChar,
                                    TokenTypeEnum.SIMPLE_MATH_OPERATOR));
                    break;
                case '=':
                case '>':
                case '<':
                case '!':
                    nextChar = clauseItr[i + 1];
                    if(nextChar == '='
                            || currentChar == '<' && nextChar == '>') {
                        ++i;
                        parsedTokenList.add(new TokenDescriptor(currentChar + "" + nextChar, TokenTypeEnum.COMPARE_OPERATOR));
                        break;
                    }
                    parsedTokenList.add(new TokenDescriptor(currentChar, TokenTypeEnum.COMPARE_OPERATOR));
                    break;
                default:
                    field = new StringBuilder();
                    TokenTypeEnum tokenType = TokenTypeEnum.WORD_NORMAL;
                    do {
                        currentChar = clauseItr[i];
                        field.append(currentChar);
                        if(i + 1 <= clauseLength) {
                            if(SyntaxSymbolTable.isUdfPrefix(field.toString())) {
                                do {
                                    if(clauseItr[i + 1] != ' ') {
                                        break;
                                    }
                                    ++i;
                                } while (i + 1 < clauseLength);
                            }
                            if ( i + 1 == clauseLength) break;
                            nextChar = clauseItr[i + 1];
                            if(nextChar == '(') {
                                fieldGot = readSplitWord(clauseItr, i + 1,
                                        nextChar, ')');
                                field.append(fieldGot);
                                tokenType = TokenTypeEnum.FUNCTION_NORMAL;
                                i += fieldGot.length();
                                break;
                            }
                            if(nextChar == '[') {
                                fieldGot = readSplitWord(clauseItr, i + 1,
                                        nextChar, ']');
                                field.append(fieldGot);
                                tokenType = TokenTypeEnum.WORD_ARRAY;
                                i += fieldGot.length();
                                break;
                            }
                            if(isSpecialChar(nextChar)) {
                                // 严格模式下，要求 -+ 符号前后必须带空格, 即会将所有字母后紧连的 -+ 视为字符连接号
                                // 非严格模式下, 即只要是分隔符即停止字符解析(非标准分隔)
                                if(!strictMode || nextChar != '-' && nextChar != '+') {
                                    break;
                                }
                            }
                            ++i;
                        }
                    } while (i < clauseLength);
                    parsedTokenList.add(
                            new TokenDescriptor(field.toString(), tokenType));
                    break;
            }
            i++;
        }
        if(!specialSeparatorStack.empty()) {
            ColumnNumDescriptor lineNumTableTop = specialSeparatorStack.peek();
            throw new RuntimeException("检测到未闭合的符号, near '"
                    + lineNumTableTop.getKeyword()+ "' at column "
                    + lineNumTableTop.getColumnNum());
        }
        return parsedTokenList;
    }

    /**
     * 从源数组中读取某类词数据
     *
     * @param src 数据源
     * @param offset 要搜索的起始位置 offset
     * @param openChar word 的开始字符，用于避免循环嵌套 如: '('
     * @param closeChar word 的闭合字符 如: ')'
     * @return 解析出的字符
     * @throws RuntimeException 解析不到正确的单词时抛出
     */
    private static String readSplitWord(char[] src, int offset,
                                        char openChar, char closeChar)
            throws RuntimeException {
        StringBuilder builder = new StringBuilder();
        for (int i = offset; i < src.length; i++) {
            if(openChar == src[i]) {
                int aroundOpenCharNum = -1;
                do {
                    builder.append(src[i]);
                    if(src[i] == openChar) {
                        aroundOpenCharNum++;
                    }
                    if(src[i] == closeChar) {
                        aroundOpenCharNum--;
                    }
                } while (++i < src.length
                        && (aroundOpenCharNum > 0 || src[i] != closeChar));
                if(aroundOpenCharNum > 0
                        || (openChar == closeChar && aroundOpenCharNum != -1)) {
                    throw new RuntimeException("syntax error, un closed clause near '"
                            + builder.toString() + "' at column " + --i);
                }
                builder.append(closeChar);
                return builder.toString();
            }
        }
        // 未找到匹配
        return "";
    }

    /**
     * 重载另一版，适用特殊场景 (不支持嵌套)
     *
     * @see #readSplitWord(char[], int, char, char)
     */
    private static String readSplitWord(char[] src, int offset,
                                        String openChar, String closeChar)
            throws RuntimeException {
        StringBuilder builder = new StringBuilder();
        for (int i = offset; i < src.length; i++) {
            if(openChar.charAt(0) == src[i]) {
                int j = 0;
                while (++j < openChar.length() && ++i < src.length) {
                    if(openChar.charAt(j) != src[i]) {
                        break;
                    }
                }
                // 未匹配开头
                if(j < openChar.length()) {
                    continue;
                }
                builder.append(openChar);
                while (++i < src.length){
                    int k = 0;
                    if(src[i] == closeChar.charAt(0)) {
                        while (++k < closeChar.length() && ++i < src.length) {
                            if(closeChar.charAt(k) != src[i]) {
                                break;
                            }
                        }
                        if(k < closeChar.length()) {
                            throw new RuntimeException("un closed syntax, near '"
                                    + new String(src, i - k, k)
                                    + ", at column " + (i - k));
                        }
                        builder.append(closeChar);
                        break;
                    }
                    builder.append(src[i]);
                }
                return builder.toString();
            }
        }
        return " ";
    }

    /**
     * 检测字符是否特殊运算符
     *
     * @param value 给定检测字符
     * @return true:是特殊字符, false:普通
     */
    private static boolean isSpecialChar(char value) {
        return SyntaxSymbolTable.OPERATOR_ALL.indexOf(value) != -1;
    }

}