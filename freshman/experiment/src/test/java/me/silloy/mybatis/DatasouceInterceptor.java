package me.silloy.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author shaohuasu
 * @date 2019-05-27 21:00
 * @since 1.8
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class})}
)
public class DatasouceInterceptor implements Interceptor {
    /**
     * 插件方法更新
     */
    private static final String UPDATE = "update";

    /**
     * 插件方法查询
     */
    private static final String QUERY = "query";

    Logger logger = LoggerFactory.getLogger(DatasouceInterceptor.class);

    /**
     * SQL方言 可选MySql，Oracle
     */
    private String diacect;

    /**
     * mybatis插件拦截方法，用于分页,加解密
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];

        if(QUERY.equals(methodName)) {
//            DBContextHolder.setDbType(DBContextHolder.DB_TYPE_RD);
//            return doQuery(invocation, fields, coditions);
        }else if(UPDATE.equals(methodName)) {
//            DBContextHolder.setDbType(DBContextHolder.DB_TYPE_WR);
//            return doUpdate(invocation, fields);
        }
        return null;
    }

//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        return null;
//    }
//
    @Override
    public Object plugin(Object target) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
