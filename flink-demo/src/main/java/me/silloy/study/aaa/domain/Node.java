package me.silloy.study.aaa.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: lizelong
 * @date: 2019/11/21
 * @description:
 **/
@Data
public class Node {

    private static final Logger LOG = LoggerFactory.getLogger (Node.class);

    private String type;
    private String relation;
    private JSONObject item;
    private JSONArray rules;

    public static Node buildNode(String nodeJson) {

        Node node = new Node ();
        JSONObject obj;

        try {
            obj = JSONObject.parseObject (nodeJson);
            String type = obj.getString (ItemEnum.type.toString ());
            node.setType (type);

            if (type.equals (TypeEnum.rules_relation.toString ())) {
                node.setRelation (obj.getString (ItemEnum.relation.toString ()));
                node.setRules (obj.getJSONArray (ItemEnum.rules.toString ()));
            } else {
                node.setItem (obj);
            }
        } catch (Exception e) {
            LOG.error ("buildNode has exception : ",e);
            LOG.error ("nodeJson is: {}",nodeJson);
        }
        return node;
    }

    public boolean calculate() {
        if (this.type.equals (TypeEnum.rules_relation.toString ())) {
            return RelaEnum.valueOf (relation).calculate (rules);
        } else {
            return calculate(item);
        }
    }

    private boolean calculate(JSONObject item) {

        String type = item.getString (ItemEnum.type.toString ());
        String field = item.getString (ItemEnum.field.toString ());
        String function = item.getString (ItemEnum.function.toString ());
        String params = item.getString (ItemEnum.params.toString ());
        String sql = "select * from "+type+" where "+field+function+"'"+params+"'";

        return excute(sql);
    }

    private boolean excute(String sql) {
        try {
            return ReflectMaker.query (sql);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return false;
    }
}
