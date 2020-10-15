package me.silloy.study.aaa.domain;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

/**
 * @author: lizelong
 * @date: 2019/11/26
 * @description:
 **/
public class EvaluatedResultSchema implements DeserializationSchema<EvaluatedResult>,
        SerializationSchema<EvaluatedResult> {

  @Override
  public byte[] serialize(EvaluatedResult result) {
    return result.toString().getBytes();
  }

  @Override
  public EvaluatedResult deserialize(byte[] message) throws IOException {
    return EvaluatedResult.fromString(new String(message));
  }

  @Override
  public boolean isEndOfStream(EvaluatedResult nextElement) {
    return false;
  }

  @Override
  public TypeInformation<EvaluatedResult> getProducedType() {
    return TypeInformation.of(EvaluatedResult.class);
  }
}
