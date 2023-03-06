package me.silloy.expression.keyword;



import me.silloy.expression.SysCustomKeywordAstHandler;
import me.silloy.expression.TargetDialectTypeEnum;
import me.silloy.expression.TokenDescriptor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * 功能描述: day 关键词定义
 */
@Keyword
public class DayDefinerImpl implements SysKeywordDefiner {

    private static final String KEYWORD_NAME = "day";

    static {
        SysCustomKeywordAstHandler.registerDefiner(new DayDefinerImpl(), KEYWORD_NAME);
    }

    @Override
    public String translate(List<TokenDescriptor> tokens,
                            TargetDialectTypeEnum targetSqlType) {
        String separator = " ";
        String usage = tokens.get(0).getRawWord();
        List<TokenDescriptor> innerTokens = SysCustomKeywordAstHandler
                .parseSysCustomKeywordInnerTokens(usage, "#{", "}");
        switch (targetSqlType) {
            case ES:
            case SPARK:
            case HIVE:
            case PRESTO:
            case PG:
                int dayAmount = 0;
                if(innerTokens.size() > 1) {
                    String comparator = innerTokens.get(1).getRawWord();
                    switch (comparator) {
                        case "-":
                            dayAmount = -Integer.valueOf(innerTokens.get(2).getRawWord());
                            break;
                        case "+":
                            dayAmount = Integer.valueOf(innerTokens.get(2).getRawWord());
                            break;
                        default:
                            throw new RuntimeException("day关键字不支持的操作符: " + comparator);
                    }
                }
                return "'"
                        + LocalDate.now().plusDays(dayAmount).format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "'" + separator;
            case RAW:
            default:
                StringBuilder sb = new StringBuilder();
                tokens.forEach(r -> sb.append(r.getRawWord()).append(separator));
                return sb.toString();
        }
    }

}