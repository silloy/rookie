package com.yuanqisenlin.metric.grid.standard.query.expression;

import com.yuanqisenlin.metric.grid.standard.query.expression.keyword.Keyword;
import com.yuanqisenlin.metric.grid.standard.query.expression.keyword.SysKeywordDefiner;
import com.yuanqisenlin.metric.grid.util.ClassLoaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统自定义常量解析类
 */
@Slf4j
public class SysCustomKeywordAstHandler
        extends CommonConditionAstBranch
        implements SyntaxStatement {

    private static final Map<String, SysKeywordDefiner>
            keywordDefinerContainer = new ConcurrentHashMap<>();

    static {
        try {
            // auto load keyword definer
            String currentPackage = SysCustomKeywordAstHandler.class.getPackage().getName();
            ClassLoaderUtils.loadPackageClasses(currentPackage, Keyword.class);
        } catch (Throwable e) {
            log.error("加载包路径下文件失败", e);
        }
    }

    public SysCustomKeywordAstHandler(TokenDescriptor masterToken,
                                      Iterator<TokenDescriptor> candidates,
                                      TokenTypeEnum tokenType) {
        super(masterToken, candidates, tokenType);
    }

    @Override
    public String translateTo(TargetDialectTypeEnum targetSqlType) {
        String usage = masterToken.getRawWord();
        String keywordName = parseSysKeywordName(usage);
        SysKeywordDefiner definer = getKeywordDefiner(keywordName);
        List<TokenDescriptor> mergedToken = new ArrayList<>(extendTokens);
        mergedToken.add(0, masterToken);
        if (definer == null) {
            // throw new BizException("不支持的关键字: " + keywordName);
            // 在未完全替换所有关键字功能之前，不得抛出以上异常
            log.warn("系统关键字[{}]定义未找到,降级使用原始语句,请尽快补充功能.", keywordName);
            return translateToDefaultRaw(mergedToken);
        }
        return definer.translate(mergedToken, targetSqlType);
    }

    private String parseSysKeywordName(String usage) {
        if ('\'' == usage.charAt(0)) {
            String keywordName = getSysKeywordNameWithPreLikeStr(usage);
            if (keywordName == SYS_CUSTOM_EMPTY_KEYWORD_NAME) {
                throw new RuntimeException("系统关键词定义非法, 请以 #{} 使用关键词2");
            }
            return keywordName;
        }
        return getSysKeywordNameNormal(usage);
    }

    private static final String SYS_CUSTOM_EMPTY_KEYWORD_NAME = "";


    public static String getSysKeywordNameWithPreLikeStr(String usage) {
        if ('\'' != usage.charAt(0)) {
            return SYS_CUSTOM_EMPTY_KEYWORD_NAME;
        }
        StringBuilder keywordBuilder = new StringBuilder();
        String separatorChars = " -+(){}[],";
        for (int i = 1; i < usage.length(); i++) {
            char ch = usage.charAt(i);
            if (ch == '%') {
                continue;
            }
            if (ch != '#'
                    || usage.charAt(++i) != '{') {
                return SYS_CUSTOM_EMPTY_KEYWORD_NAME;
            }

            while (++i < usage.length()) {
                ch = usage.charAt(i);
                keywordBuilder.append(ch);
                if (i + 1 < usage.length()) {
                    char nextChar = usage.charAt(i + 1);
                    if (separatorChars.indexOf(nextChar) != -1) {
                        break;
                    }
                }
            }
            break;
        }
        return keywordBuilder.length() == 0
                ? SYS_CUSTOM_EMPTY_KEYWORD_NAME
                : keywordBuilder.toString();
    }


    /**
     * 解析关键词特别用法法为一个个token
     *
     * @param usage  原始使用方式如: #{day+1}
     * @param prefix 字符开头
     * @param suffix 字符结尾
     * @return 拆分后的token, 已去除分界符 #{}
     */
    public static List<TokenDescriptor> parseSysCustomKeywordInnerTokens(String usage,
                                                                         String prefix,
                                                                         String suffix) {
        String separatorChars = " ,{}()[]-+";
        if (!usage.startsWith(prefix)
                || !usage.endsWith(suffix)) {
            throw new RuntimeException("关键字使用格式不正确: " + usage);
        }
        List<TokenDescriptor> innerTokens = new ArrayList<>(2);
        TokenDescriptor token;
        for (int i = prefix.length();
             i < usage.length() - suffix.length(); i++) {
            char ch = usage.charAt(i);
            if (ch == ' ') {
                continue;
            }
            if (ch == '}') {
                break;
            }
            if (ch == '-' || ch == '+') {
                token = new TokenDescriptor(ch, TokenTypeEnum.SIMPLE_MATH_OPERATOR);
                innerTokens.add(token);
                continue;
            }
            StringBuilder wordBuilder = new StringBuilder();
            do {
                ch = usage.charAt(i);
                wordBuilder.append(ch);
                if (i + 1 < usage.length()) {
                    char nextChar = usage.charAt(i + 1);
                    if (separatorChars.indexOf(nextChar) != -1) {
                        break;
                    }
                    ++i;
                }
            } while (i < usage.length());
            String word = wordBuilder.toString();
            TokenTypeEnum tokenType = TokenTypeEnum.WORD_STRING;
            if (StringUtils.isNumeric(word)) {
                tokenType = TokenTypeEnum.WORD_NUMBER;
            }
            innerTokens.add(new TokenDescriptor(wordBuilder.toString(), tokenType));
        }
        return innerTokens;
    }

    /**
     * 解析普通关键字定义 #{day+1}
     */
    public static String getSysKeywordNameNormal(String usage) {
        if (!usage.startsWith("#{")) {
            throw new RuntimeException("系统关键词定义非法, 请以 #{} 使用关键词");
        }
        StringBuilder keywordBuilder = new StringBuilder();
        for (int i = 2; i < usage.length(); i++) {
            char ch = usage.charAt(i);
            if (ch == ' ' || ch == ','
                    || ch == '+' || ch == '-'
                    || ch == '(' || ch == ')') {
                break;
            }
            keywordBuilder.append(ch);
        }
        return keywordBuilder.toString();
    }

    /**
     * 默认使用原始语句返回（）
     */
    private String translateToDefaultRaw(List<TokenDescriptor> tokens) {
        String separator = " ";
        StringBuilder sb = new StringBuilder();
        tokens.forEach(r -> sb.append(r.getRawWord()).append(separator));
        return sb.toString();
    }

    private SysKeywordDefiner getKeywordDefiner(String keyword) {
        return keywordDefinerContainer.get(keyword);
    }

    /**
     * 注册新的关键词
     *
     * @param definer      词定义器
     * @param keywordNames 关键词别名（支持多个，目前只有一个的场景）
     */
    public static void registerDefiner(SysKeywordDefiner definer, String... keywordNames) {
        for (String key : keywordNames) {
            keywordDefinerContainer.putIfAbsent(key, definer);
        }
    }
}


