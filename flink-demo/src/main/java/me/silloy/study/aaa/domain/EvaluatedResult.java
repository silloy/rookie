package me.silloy.study.aaa.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: lizelong
 * @date: 2019/11/26
 * @description:
 **/
@Data
public class EvaluatedResult implements Serializable{

  private String oneId;
  private String eventId;

  public static EvaluatedResult fromString(String result) {

    JSONObject json = JSONObject.parseObject(result);
    EvaluatedResult r = new EvaluatedResult();
    r.oneId = json.getString("one_id");
    r.eventId = json.getString("event_id");
    return r;
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }

}
