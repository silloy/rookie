package com.yuanqisenlin.metric.grid.standard.query.expression;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 语法符号表
 */
public class SyntaxSymbolTable {

    /**
     * 所有操作符
     */
    public static final String OPERATOR_ALL = "'\"[ ](){}=+-*/><!";

    private static final Map<String, SyntaxStatementHandlerFactory> handlers
            = new ConcurrentHashMap<>();

    private static final String SYS_CUSTOM_KEYWORD_REF_NAME = "__sys_keyword_handler";

    private static final String SQL_KEYWORD_REF_NAME = "__sql_keyword_handler";

    private static final String COMMON_HANDLER_REF_NAME = "__common_handler";

    static {
        registerUdf(
                (masterToken, candidates, handlerType)
                        -> new SimpleUdfAstHandler(masterToken, candidates,
                        TokenTypeEnum.FUNCTION_SYS_CUSTOM),
                "my_udf", "fact.my_udf", "default.my_udf");

        handlers.putIfAbsent(SYS_CUSTOM_KEYWORD_REF_NAME, SysCustomKeywordAstHandler::new);
        handlers.putIfAbsent(COMMON_HANDLER_REF_NAME, CommonConditionAstBranch::new);
    }

    /**
     * 判断给定词汇的 keyword 类型
     */
    public static TokenTypeEnum keywordTypeOf(String keyword) {
        if("datepart".equals(keyword)) {
            return TokenTypeEnum.KEYWORD_SYS_CUSTOM;
        }
        if("and".equals(keyword)
                || "or".equals(keyword)
                || "in".equals(keyword)) {
            return TokenTypeEnum.KEYWORD_SQL;
        }
        return TokenTypeEnum.WORD_NORMAL;
    }

    /**
     * 注册一个 udf 处理器实例
     *
     * @param handlerFactory 处理器工厂类
     *              tokens 必要参数列表，说到底自定义
     * @param callNameAliases 函数调用别名, 如 my_udf, fact.my_udf...
     */
    public static void registerUdf(SyntaxStatementHandlerFactory handlerFactory,
                                   String... callNameAliases) {
        for (String alias : callNameAliases) {
            handlers.put(alias, handlerFactory);
        }
    }

    /**
     * 获取udf处理器的工厂类
     */
    public static SyntaxStatementHandlerFactory getUdfHandlerFactory(String udfFunctionName) {
        SyntaxStatementHandlerFactory factory= handlers.get(udfFunctionName);
        if(factory == null) {
            throw new RuntimeException("Unsupported udf function: " + udfFunctionName);
        }
        return factory;
    }

    /**
     * 获取系统自定义关键字处理器的工厂类  应固定格式为 #{xxx+1}
     *
     * @return 对应的工厂类
     */
    public static SyntaxStatementHandlerFactory getSysKeywordHandlerFactory() {
        return handlers.get(SYS_CUSTOM_KEYWORD_REF_NAME);
    }

    /**
     * 获取sql关键字处理器的工厂类  遵守 sql 协议
     *
     * @return 对应的工厂类
     */
    public static SyntaxStatementHandlerFactory getSqlKeywordHandlerFactory() {
        return handlers.get(COMMON_HANDLER_REF_NAME);
    }

    /**
     * 获取通用处理器的工厂类（兜底）
     *
     * @return 对应的工厂类
     */
    public static SyntaxStatementHandlerFactory getCommonHandlerFactory() {
        return handlers.get(COMMON_HANDLER_REF_NAME);
    }


    /**
     * 检测名称是否是udf 函数前缀
     *
     * @param udfFunctionName 函数名称
     * @return true:是, false:其他关键词
     */
    public static boolean isUdfPrefix(String udfFunctionName) {
        return handlers.get(udfFunctionName) != null;
    }

    /**
     * 判断给定词汇的 keyword 类型
     *
     * @param functionFullDesc 函数整体使用方式
     * @return 系统自定义函数，系统函数、未知
     */
    public static TokenTypeEnum functionTypeOf(String functionFullDesc) {
        String funcName = functionFullDesc.substring(0, functionFullDesc.indexOf('('));
        funcName = funcName.trim();
        if("my_udf".equals(funcName)) {
            return TokenTypeEnum.FUNCTION_SYS_CUSTOM;
        }
        return TokenTypeEnum.FUNCTION_NORMAL;
    }

}