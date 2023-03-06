package me.silloy.expression;


// ------------- TokenTypeEnum -----------------
/**
 * 单个不可分割的token 类型定义
 */
public enum TokenTypeEnum {

    LABEL_ID("基础id如$123"),

    FUNCTION_NORMAL("是函数但类型未知（未解析）"),

    FUNCTION_SYS_CUSTOM("系统自定义函数如my_udf(a)"),

    FUNCTION_SQL("sql中自带函数如date_diff(a)"),

    KEYWORD_SYS_CUSTOM("系统自定义关键字如datepart"),

    KEYWORD_SQL("sql中自带的关键字如and"),

    CLAUSE_SEPARATOR("语句分隔符，如'\"(){}[]"),

    SIMPLE_MATH_OPERATOR("简单数学运算符如+-*/"),

    COMPARE_OPERATOR("比较运算符如=><!=>=<="),

    WORD_ARRAY("数组类型字段如 arr['key1']"),

    WORD_STRING("字符型具体值如 '%abc'"),

    WORD_NUMBER("数字型具体值如 123.4"),

    WORD_NORMAL("普通字段可以是数据库字段也可以是用户定义的字符"),
    ;


    TokenTypeEnum(String remark) {
    }
}