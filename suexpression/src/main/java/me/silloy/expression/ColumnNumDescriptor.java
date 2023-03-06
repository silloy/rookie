package com.yuanqisenlin.metric.grid.standard.query.expression;

import lombok.ToString;

@ToString
public class ColumnNumDescriptor {

    private int columnNum;

    private String keyword;

    public ColumnNumDescriptor(int columnNumFromZero, String keyword) {
        this.columnNum = columnNumFromZero + 1;
        this.keyword = keyword;
    }

    public static ColumnNumDescriptor newData(int columnNum, String data) {
        return new ColumnNumDescriptor(columnNum, data);
    }

    public static ColumnNumDescriptor newData(int columnNum, char dataChar) {
        return new ColumnNumDescriptor(columnNum, dataChar + "");
    }

    public int getColumnNum() {
        return columnNum;
    }

    public String getKeyword() {
        return keyword;
    }

}