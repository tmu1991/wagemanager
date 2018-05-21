package com.wz.wagemanager.tools;

import lombok.*;

import java.io.Serializable;

/**
 * @describe: 分页实体
 * @author:
 * @creat_date: 2017/10/21
 * @creat_time: 10:51
 **/
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Page implements Serializable{
    /**当前页数*/
    private Integer currentPage;
    /**每页多少条*/
    private Integer pageSize;
    /**共多少页*/
    private Integer pageCount;
    /**总条数*/
    private Integer totalCount;
    /**从多少条开始*/
    private Integer start;
    /**到多少条结束*/
    private Integer end;
    /**排序的字段*/
    private String sortField;
    /**排序规则*/
    private String sortOrder;

}
