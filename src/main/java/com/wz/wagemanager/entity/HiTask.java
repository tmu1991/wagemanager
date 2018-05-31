package com.wz.wagemanager.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author WindowsTen
 * @date 2018/5/31 11:59
 * @description
 */
@Entity
@Table(name = "hi_task")
@Data
@Builder
@NoArgsConstructor (access = AccessLevel.PUBLIC)
@AllArgsConstructor (access = AccessLevel.PUBLIC)
public class HiTask {
    @Id
    @GeneratedValue (generator="uuid")
    @GenericGenerator (name="uuid",strategy="uuid")
    @Column (name = "id",length = 32)
    private String id;

    @Column(name = "dept_id",length = 32)
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

    @Column(length = 1)
    private Integer type;

    @Column(name = "loan_date")
    @Temporal (TemporalType.TIMESTAMP)
    private Date loanDate;

}
