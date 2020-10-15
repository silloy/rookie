//package me.silloy.study.aaa;
//
//import me.silloy.study.aaa.domain.User;
//import org.apache.flink.api.common.serialization.DeserializationSchema;
//import org.apache.flink.api.common.serialization.SerializationSchema;
//import org.apache.flink.api.common.typeinfo.TypeInformation;
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.api.java.typeutils.TupleTypeInfo;
//import org.apache.flink.util.Preconditions;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//
///**
// * @author: lizelong
// * @date: 2019/11/2
// * @description:
// **/
//public class ObjSerializationSchema implements DeserializationSchema<Tuple2<Boolean, User>>, SerializationSchema<Tuple2<Boolean, User>> {
//    private static final long serialVersionUID = 1L;
//    private transient Charset charset;
//
//    public ObjSerializationSchema() {
//        this(StandardCharsets.UTF_8);
//    }
//
//    public ObjSerializationSchema(Charset charset) {
//        this.charset = (Charset) Preconditions.checkNotNull(charset);
//    }
//
//    public Charset getCharset() {
//        return this.charset;
//    }
//
//    @Override
//    public Tuple2<Boolean, User> deserialize(byte[] message) {
//        return new Tuple2(true, new String(message, this.charset));
//    }
//
//    @Override
//    public boolean isEndOfStream(Tuple2<Boolean, User> booleanUserTuple2) {
//        return false;
//    }
//
//    @Override
//    public byte[] serialize(Tuple2<Boolean, User> t) {
//        return t.toString ().getBytes ();
//    }
//
//    @Override
//    public TypeInformation<Tuple2<Boolean, User>> getProducedType() {
//        return TupleTypeInfo.getBasicTupleTypeInfo (Boolean.class, User.class);
//    }
//
//    private void writeObject(ObjectOutputStream out) throws IOException {
//        out.defaultWriteObject();
//        out.writeUTF(this.charset.name());
//    }
//
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//        in.defaultReadObject();
//        String charsetName = in.readUTF();
//        this.charset = Charset.forName(charsetName);
//    }
//}
