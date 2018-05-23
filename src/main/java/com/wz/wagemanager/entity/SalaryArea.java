package com.wz.wagemanager.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author WindowsTen
 * @date 2018/5/23 11:32
 * @description
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SalaryArea {

    private String deptId;
    private String deptName;
    private Integer year;
    private Integer month;
    private String declareId;
    private BigDecimal grossPay;
    private BigDecimal subWork;
    private BigDecimal allowance;
    private BigDecimal insurance;
    private BigDecimal accuFund;
    private BigDecimal incomeTax;
    private BigDecimal payroll;
    private BigDecimal late;
    private BigDecimal otherDebit;
    private BigDecimal partyDue;
    private BigDecimal loan;
    private BigDecimal other;
    private BigDecimal otherEl;

}
