package com.wz.wagemanager.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "act_task")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ActTask implements Serializable {
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

//    @Column(name = "salary_id",length = 32)
//    private String salaryId;

    //状态为0表示扣款中,1表示未扣款
    private Integer status;

//    private Integer status;

}
