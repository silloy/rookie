package me.silloy.expression.keyword;


import me.silloy.expression.TargetDialectTypeEnum;
import me.silloy.expression.TokenDescriptor;

import java.util.List;

public interface SysKeywordDefiner {

    /**
     * 转换成目标语言表示
     *
     *
     *
     * @param tokens 所有必要词组
     * @param targetSqlType 目标语言类型 es|hive|presto|spark
     * @return 翻译后的语言表示
     */
    String translate(List<TokenDescriptor> tokens,
                     TargetDialectTypeEnum targetSqlType);

}