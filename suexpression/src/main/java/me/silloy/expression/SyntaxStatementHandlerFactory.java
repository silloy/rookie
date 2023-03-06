package com.yuanqisenlin.metric.grid.standard.query.expression;

import java.util.Iterator;

/**
 * 组合标签语句处理器 工厂类
 */
public interface SyntaxStatementHandlerFactory {

    /**
     * 生成一个新的语句处理器实例
     *
     * @param masterToken 主控token, 如关键词，函数调用...
     * @param candidates  候选词组（后续词组）, 此实现基于本解析器无全局说到底关联性
     * @param handlerType 处理器类型，如函数、关键词、sql...
     * @return 对应的处理器实例
     */
    SyntaxStatement newHandler(TokenDescriptor masterToken,
                               Iterator<TokenDescriptor> candidates,
                               TokenTypeEnum handlerType);
}



