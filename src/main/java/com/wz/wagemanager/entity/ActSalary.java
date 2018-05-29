package com.wz.wagemanager.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.wz.wagemanager.annotation.ParmDesc;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/5/9 11:13
 * @description 活动工资表
 */
@Data
@Builder
@Entity
@Table (name = "act_salary")
@AllArgsConstructor (access = AccessLevel.PUBLIC)
public class ActSalary {
    @Id
    @GeneratedValue (generator="uuid")
    @GenericGenerator (name="uuid",strategy="uuid")
    @Column(name = "id",length = 32)
    private String id;

    @Column(name = "work_no")
    private String workNo;

    @Column(name = "dept_name")
    private String deptName;

    private String username;
    @ParmDesc(desc = "基本工资")
    private BigDecimal base;

    @ParmDesc(desc = "系数")
    private BigDecimal coeff;
    //日工资
    @Column(name = "daily_wage")
    private BigDecimal dailyWage;

    @ParmDesc(desc = "出勤")
    private BigDecimal attendance;

    @ParmDesc(desc = "出差")
    @Column(name = "bus_travel")

    private BigDecimal busTravel;
    @ParmDesc(desc = "公休")
    private BigDecimal holiday;
    //工时合计
    @Column(name = "work_total")
    private BigDecimal workTotal;

    @ParmDesc(desc = "工龄工资")
    private BigDecimal seniority;

    @ParmDesc(desc = "顶班天数")
    @Column(name = "sub_day")
    private BigDecimal subDay;
    //顶班工资
    @Column(name = "sub_work")
    private BigDecimal subWork;

    @ParmDesc(desc = "津贴")
    private BigDecimal allowance;
    //应发工资
    @Column(name = "gross_pay")
    private BigDecimal grossPay;

    @ParmDesc(desc = "四险")
    private BigDecimal insurance;

    @ParmDesc(desc = "一金")
    @Column(name = "accu_fund")
    private BigDecimal accuFund;
    //个人所得税
    @Column(name = "income_tax")
    private BigDecimal incomeTax;
    @ParmDesc(desc = "奖金")
    private BigDecimal bonus;
    @ParmDesc(desc = "迟到早退")
    private BigDecimal late;
    @ParmDesc(desc = "其他扣款")
    @Column(name = "other_debit")
    private BigDecimal otherDebit;
    @ParmDesc(desc = "党费")
    @Column(name = "party_due")
    private BigDecimal partyDue;
    @ParmDesc(desc = "借款")
    private BigDecimal loan;
    @ParmDesc(desc = "其他")
    private BigDecimal other;
    @ParmDesc(desc = "其他")
    @Column(name = "other_el")
    private BigDecimal otherEl;
    //实发工资
    private BigDecimal payroll;

    private Integer year;
    private Integer month;

    @Column(name = "dept_id",length = 32)
    private String deptId;

    @Column(name = "user_id",length = 32)
    private String userId;

    @Column(name = "update_date")
    @Temporal (TemporalType.TIMESTAMP)
    @JSONField (format = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    @ParmDesc (desc = "银行卡号")
    @Column(name = "credit_card")
    private String creditCard;

    public ActSalary(){
        this.accuFund=BigDecimal.ZERO;
        this.allowance=BigDecimal.ZERO;
        this.attendance=BigDecimal.ZERO;
        this.base=BigDecimal.ZERO;
        this.bonus=BigDecimal.ZERO;
        this.busTravel=BigDecimal.ZERO;
        this.coeff=BigDecimal.ONE;
        this.dailyWage=BigDecimal.ZERO;
        this.grossPay=BigDecimal.ZERO;
        this.holiday=BigDecimal.ZERO;
        this.incomeTax=BigDecimal.ZERO;
        this.insurance=BigDecimal.ZERO;
        this.late=BigDecimal.ZERO;
        this.loan=BigDecimal.ZERO;
        this.other=BigDecimal.ZERO;
        this.otherDebit=BigDecimal.ZERO;
        this.partyDue=BigDecimal.ZERO;
        this.payroll=BigDecimal.ZERO;
        this.seniority=BigDecimal.ZERO;
        this.subDay=BigDecimal.ZERO;
        this.subWork=BigDecimal.ZERO;
        this.workTotal=BigDecimal.ZERO;
        this.otherEl=BigDecimal.ZERO;
    }

    @Column(name = "declare_id",length = 32)
    private String declareId;

}
