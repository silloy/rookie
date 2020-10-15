package me.silloy.study.aaa.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


@Data
public class RuleConfig implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleConfig.class);


    private Long planid;
    private Long sellerId;

    private String audience;
    private String cname;
    private String dateType;
    private Integer deleted;
    private Long effectiveTime;
    private Long expireTime;
    private Long entryId;
    private String entryName;
    private String entry;
    private String touch;
    private String state;
    private String type;
    private Long createTime;
    private String updateTime;

    private String node;

    public static RuleConfig buildConfig(String configStr) {

        LOGGER.info("configStr={}", configStr);
        RuleConfig ruleConfig = JSON.parseObject(configStr, RuleConfig.class);
        LOGGER.info("configStr.parse={}", JSON.toJSONString(ruleConfig));

        return ruleConfig;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
