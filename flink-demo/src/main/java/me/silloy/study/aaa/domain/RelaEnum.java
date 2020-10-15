package me.silloy.study.aaa.domain;

import com.alibaba.fastjson.JSONArray;

import java.util.Iterator;

public enum RelaEnum {

    and {
        @Override
        public boolean calculate(JSONArray array) {

            Iterator<Object> iter = array.iterator ();
            while (iter.hasNext ()) {
                String json = iter.next ().toString ();
                Node node = Node.buildNode (json);
                if (!node.calculate ()) {
                    return false;
                }
            }
            return true;
        }
    },
    or {
        @Override
        public boolean calculate(JSONArray array) {

            Iterator<Object> iter = array.iterator ();
            while (iter.hasNext ()) {
                String json = iter.next ().toString ();
                Node node = Node.buildNode (json);
                if (node.calculate ()) {
                    return true;
                }
            }
            return false;
        }
    };

    public abstract boolean calculate(JSONArray array
    );
}
