package com.wz.wagemanager.tools;

/**
 * @author WindowsTen
 * @date 2018/2/2 10:42
 * @description
 */
public class LoginException extends RuntimeException {
    public LoginException(){
        super();
    }
    public LoginException(String msg){
        super(msg);
    }
}
