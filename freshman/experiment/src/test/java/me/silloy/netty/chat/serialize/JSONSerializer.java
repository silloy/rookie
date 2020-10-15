package me.silloy.netty.chat.serialize;

import com.alibaba.fastjson.JSON;

/**
 * @author shaohuasu
 * @date 2018-12-26 22:06
 * @since 1.8
 */
public class JSONSerializer implements Serializer {
    /**
     * 序列化算法
     */
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    /**
     * java对象转换成二进制
     *
     * @param o
     */
    @Override
    public byte[] serialize(Object o) {
        return JSON.toJSONBytes(o);
    }

    /**
     * 二进制转换成java对象
     *
     * @param clazz
     * @param bytes
     */
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
