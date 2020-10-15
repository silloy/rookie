package me.silloy.study.bean;

import lombok.Data;

/**
 * @author shaohuasu
 * @date 2019/12/11 7:35 PM
 * @since 1.8
 */
@Data
public class WebEvent {

    private String oneId;

    //商户id
    private String mid;

    // 应用id
    private String appId;

    //应用名
    private String appN;

    //客户系统用户id
    private String uid;

    //客户系统用户类型
    private String ute;

    //客户性别
    private String sex;

    //页面类型：1:组件化 2:活动
    private String pType;

    //渠道id
    private String chaId;

    //渠道名
    private String chaN;


    // 维度
    private String lat;

    // 经度
    private String lon;

    //省
    private String province;

    //市
    private String city;

    //区
    private String area;


    //埋点方案版本
    private String ver;

    // 用户访问时的ip
    private String ip;

    //用户访问数据的时间
    private String time;

    // 客户端本地时区
    private String timezone;

    // MAC地址
    private String mac;

    //分辨率
    private String scr;

    // 客户端信息
    private String agent;

    //浏览器名称
    private String b;

    // 操作系统名称
    private String o;

    //操作系统语言
    private String ing;

    // 网络类型 WIFI,MOBILE
    private String net;

    // 运营商
    private String carrier;

    //设备型号
    private String model;

    // 会话ID
    private String sid;

    // 客户端cookie唯一标识
    private String cna;

    //flash版本
    private String fv;

    //当前页面地址
    private String cUrl;

    //当前页面id
    private String pId;

    //当前页面名称
    private String pN;

    //跳转来源地址
    private String fUrl;

    //跳转来源页面名称
    private String fpN;

    //跳转来源页面id
    private String fpId;

    //页面进入退出配对id
    private String vid;

    //页面停留时长
    private String vtime;

    // 事件id
    private String a;

    //事件名称
    private String an;

    //事件类型 click:点击 view:浏览/曝光 share:分享 leave离开
    private String aType;

    //事件来源 page:页面 unit:组件单元
    private String aSource;

    //业务参数
    private String params;


}
