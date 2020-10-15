package me.silloy.study.bean;

import lombok.Data;

import java.util.Map;

/**
 * @author shaohuasu
 * @date 2019/11/26 10:36 AM
 * @since 1.8
 */
@Data
public class Metric {
    public String name;
    public long timestamp;
    public Map<String, Object> fields;
    public Map<String, String> tags;
}
