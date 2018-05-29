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

    private String deptId;

    private String deptName;

    private String workNo;

    private String username;
    
    private BigDecimal late;

    private BigDecimal due;

    private List<ActTask> tasks;

    private BigDecimal other;

    private BigDecimal otherEl;

}
