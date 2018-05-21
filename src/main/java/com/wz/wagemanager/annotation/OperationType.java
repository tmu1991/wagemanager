package com.wz.wagemanager.annotation;

public enum OperationType {
    ADD("添加"),DELETE("删除"),UPDATE("修改"),QUERY("查询");
    private String type;
    private OperationType(String type){
        this.type=type;
    }
    public String getType(){
        return type;
    }
}
