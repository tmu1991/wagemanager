package com.wz.wagemanager.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author WindowsTen
 */
@Slf4j
@ControllerAdvice
public class BaseExceptionController{

    @ExceptionHandler(value = {Throwable.class})
    public @ResponseBody PageBean exception(Throwable e){
        e.printStackTrace ();
        return new PageBean (e);
    }
}
