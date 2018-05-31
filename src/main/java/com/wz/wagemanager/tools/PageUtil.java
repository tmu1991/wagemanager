package com.wz.wagemanager.tools;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe: 分页工具类
 * @author:
 * @creat_date: 2017/10/21
 * @creat_time: 10:40
 **/

public class PageUtil {

    public static Pageable pageable(Integer curPage, Integer pageSize){
        return new PageRequest(curPage-1, pageSize);
    }

    private static Pageable pageable(Integer curPage, Integer pageSize,Sort sort){
        return new PageRequest (curPage-1,pageSize,sort);
    }
    public static Pageable pageable(Integer curPage, Integer pageSize,String sortOrder, String... sortFields){
        return pageable (curPage,pageSize,getSort (sortOrder,sortFields));
    }

    private static Sort getSort(String sortOrder, String... sortFields){
        List<Sort.Order> orders=new ArrayList<>(sortFields.length);
        final Sort.Direction direction = "asc".equals(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
        for (String sortField : sortFields) {
            orders.add(new Sort.Order(direction, sortField).nullsLast ());
        }
        return new Sort(orders);
    }

    private static Integer getStart(Integer pageSize, Integer page){
        return calculationStart(pageSize,page);
    }

    public static Page getPage(Integer totalCount, Integer pageSize, Integer page,String sortField,String sortOrder){
        Integer pageCount=calculationPageCount(totalCount,pageSize);
        page=calculationPage(pageCount,page);
        return Page.builder().pageCount(pageCount).sortField (sortField).sortOrder (sortOrder)
                .currentPage(page).pageSize(pageSize).totalCount(totalCount)
                .start(calculationStart(pageSize,page)).end(calculationEnd(pageSize,page)).build();
    }

    public static Page getPage(Integer totalCount, Integer pageSize, Integer page){
        Integer pageCount=calculationPageCount(totalCount,pageSize);
        page=calculationPage(pageCount,page);
        return Page.builder().pageCount(pageCount)
                .currentPage(page).pageSize(pageSize).totalCount(totalCount)
                .start(calculationStart(pageSize,page)).end(calculationEnd(pageSize,page)).build();
    }

    public static Page getPage(Long totalCount, Integer pageSize, Integer page){
        return getPage(totalCount.intValue(),pageSize,page);
    }

    /**
     * 计算总页数
     * @param totalCount
     * @param pageSize
     * @date:2017/10/21 11:04
     * @return: java.lang.Integer
    **/
    private static Integer calculationPageCount(Integer totalCount,Integer pageSize){
        return (totalCount - 1) / pageSize + 1;
    }

    /**
     * 计算当前多少页，如果小于1则为1，如果大于总页数则为总页数
     * @param pageCount
     * @param page
     * @date:2017/10/21 11:03
     * @return: java.lang.Integer
    **/
    private static Integer calculationPage(Integer pageCount,Integer page){
        if (page < 1) {
            page = 1;
        }
        if (page > pageCount) {
            page = pageCount;
        }
        return page;
    }

    /**
     * 计算开始条数  从0开始计数
     * @param pageSize
     * @param page
     * @date:2017/10/21 11:02
     * @return: java.lang.Integer
    **/
    private static Integer calculationStart(Integer pageSize,Integer page){
        return (page - 1) * pageSize;
    }

    /**
     * 计算结尾条数
     * @param pageSize
     * @param page
     * @date:2017/10/21 11:02
     * @return: java.lang.Integer
    **/
    private static Integer calculationEnd(Integer pageSize,Integer page){
        return page*pageSize-1;
    }

}
