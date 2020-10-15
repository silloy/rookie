package me.silloy.netty.chat.serialize;

/**
 * @author shaohuasu
 * @date 2018-12-26 22:01
 * @since 1.8
 */
public interface Serializer {

    byte JSON_SERIALIZER = 1;

    Serializer DEFAULT = new JSONSerializer();


    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();

    /**
     * java对象转换成二进制
     */
    byte[] serialize(Object o);

    /**
     * 二进制转换成java对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
