package com.wz.wagemanager.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/5/29 15:37
 * @description
 */
@Data
public class ActForm {

    private String deptName;

    private String workNo;

    private String username;
    
    private BigDecimal late;

    private BigDecimal due;

    private BigDecimal other;

    private BigDecimal otherEl;

    private List<ActTask> tasks;

    private String deptId;

}
