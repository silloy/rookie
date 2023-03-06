package me.silloy.expression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleSyntaxParserTest {

    @Test
    void parse() {
        String rawClause = "$15573 = 123 and (my_udf($123568, $82949) = 1) or $39741 = #{day+1} and my_udf($35289) = -1";
//        rawClause = "({@sj_real_stock_taking_st1c[最近库存盘点日实盘库存量]}/({@sj_real_stock_taking_st4c[倒数第四个盘点日（天数）实盘库存量]} + {@sign_quantity_st4c[倒数第四个盘点日（天数）物流签收数量]} - {@sj_real_stock_taking_st1c[最近库存盘点日实盘库存量]}) ) * 30";
        ParsedClauseAst clauseAst = SimpleSyntaxParser.parse(rawClause);
        assertEquals("$15573 = 123 and ( $123568 = 1 ) or $39741 = '2020-10-07' and $35289 = -1", clauseAst.translateTo(TargetDialectTypeEnum.PG), "解析成目标语言不正确");
    }

}