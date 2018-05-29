package com.wz.wagemanager.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

//根据给定时间就任务状态跟改成1 添加状态时为0 获得当天时间和给定的定时时间比对
@Entity
@Table(name = "act_task")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ActTask {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator (name="uuid",strategy="uuid")
    @Column(name = "id",length = 32)
    private String id;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "work_no")
    private String workNo;

    private String username;

    private String note;

    @Column(name = "task_date")
    private String taskDate;

    private BigDecimal amount;

    private Integer type;

    @Column(name = "salary_id",length = 32)
    private String salaryId;

//    private Integer status;

}
