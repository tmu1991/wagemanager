package com.wz.wagemanager.tools;


import com.wz.wagemanager.exception.HandThrowException;
import lombok.*;

/**
 * @author WindowsTen
 * @date 2017/10/18 11:48
 * @description
 */
@Data
@Builder
@NoArgsConstructor (access = AccessLevel.PUBLIC)
@AllArgsConstructor (access = AccessLevel.PUBLIC)
public class PageBean<T> {

    private Page page;

    private T data;

    private String msg="success";

    private int code= GlobalConstant.SUCCESS_CODE;

    public PageBean(int code){
        this.code=code;
    }
    public PageBean(Page page, T data) {
        this.page=page;
        this.data = data;
    }

    public PageBean(String msg,Page page,T data){
        this.msg=msg;
        this.page=page;
        this.data=data;
    }

    public PageBean(String msg, T data) {
        this.msg=msg;
        this.data = data;
    }

    public PageBean(T data) {
        this.data=data;
    }

    public PageBean(Throwable throwable){
        if(throwable instanceof HandThrowException){
            this.msg=throwable.getMessage ();
        }else{
            this.msg=GlobalConstant.DEFAULT_FAIL_MSG;
        }
        this.code=GlobalConstant.ERROR_CODE;
    }

}