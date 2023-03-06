package me.silloy.expression;

public interface SyntaxStatement {

    String translateTo(TargetDialectTypeEnum targetSqlType);

}