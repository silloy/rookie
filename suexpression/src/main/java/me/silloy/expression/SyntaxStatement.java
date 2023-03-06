package com.yuanqisenlin.metric.grid.standard.query.expression;

public interface SyntaxStatement {

    String translateTo(TargetDialectTypeEnum targetSqlType);

}