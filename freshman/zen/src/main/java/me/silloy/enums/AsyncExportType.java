package me.silloy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: shop-operation
 * @Package: com.zj.shop.operation.enums
 * @ClassName: AsyncExportType
 * @Author: yunshang
 * @Description: 异步导出类型
 * @Date: 2018/12/6 4:53 PM
 * @Version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
public enum AsyncExportType {
    LIST_TYPE("1","反射返回list集合方式"),
    WORKBOOK_TYPE("2","反射返回workbook对象方式");
    @Getter
    private String code;
    @Getter
    private String msg;
}
