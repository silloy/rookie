package com.zj.web.model;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: SuShaohua
 * Date: 2018/8/23
 * Time: 15:34
 * Description: MAIN
 */
@Data
public class City {
    /**
     * 城市编号
     */
    private Long id;

    /**
     * 省份编号
     */
    private Long provinceId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 描述
     */
    private String description;

}
