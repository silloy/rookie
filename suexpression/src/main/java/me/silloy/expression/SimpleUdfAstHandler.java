package com.yuanqisenlin.metric.grid.standard.query.expression;

import java.util.Iterator;

/**
 * 功能描述: 自定义函数实现示例
 */
public class SimpleUdfAstHandler
        extends CommonConditionAstBranch
        implements SyntaxStatement {

    public SimpleUdfAstHandler(TokenDescriptor masterToken,
                               Iterator<TokenDescriptor> candidates,
                               TokenTypeEnum tokenType) {
        super(masterToken, candidates, tokenType);
    }

    @Override
    protected int getFixedExtTokenNum() {
        return 2;
    }

    @Override
    public String translateTo(TargetDialectTypeEnum targetSqlType) {
        String usage = masterToken.getRawWord();
        int paramStart = usage.indexOf('(');
        StringBuilder fieldBuilder = new StringBuilder();
        for (int i = paramStart; i < usage.length(); i++) {
            char ch = usage.charAt(i);
            if(ch == ' ') {
                continue;
            }
            if(ch == '$') {
                fieldBuilder.append(ch);
                while (++i < usage.length()) {
                    ch = usage.charAt(i);
                    if(ch >= '0' && ch <= '9') {
                        fieldBuilder.append(ch);
                        continue;
                    }
                    break;
                }
                break;
            }
        }
        String separator = " ";
        StringBuilder resultBuilder
                = new StringBuilder(fieldBuilder.toString())
                .append(separator);
        switch (targetSqlType) {
            case ES:
            case HIVE:
            case SPARK:
            case PRESTO:
            case PG:
            case RAW:
                extendTokens.forEach(r -> resultBuilder.append(r.getRawWord()).append(separator));
                return resultBuilder.toString();
        }
        throw new RuntimeException("unknown target dialect");
    }
}