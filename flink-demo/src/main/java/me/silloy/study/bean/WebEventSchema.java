package me.silloy.study.bean;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.*;

/**
 * @author shaohuasu
 * @date 2019/12/11 8:51 PM
 * @since 1.8
 */
public class WebEventSchema implements DeserializationSchema<WebEvent>, SerializationSchema<WebEvent> {
    @Override
    public WebEvent deserialize(byte[] message) throws IOException {
        ByteArrayInputStream bi = new ByteArrayInputStream(message);
        ObjectInputStream oi = new ObjectInputStream(bi);
        WebEvent obj = null;
        try {
            obj = (WebEvent) oi.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        bi.close();
        oi.close();
        return obj;
    }

    @Override
    public boolean isEndOfStream(WebEvent nextElement) {
        return false;
    }

    @Override
    public byte[] serialize(WebEvent element) {
        byte[] bytes = null;
        try {

            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(element);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public TypeInformation<WebEvent> getProducedType() {
        return TypeInformation.of(new TypeHint<WebEvent>() {
        });
    }
}
