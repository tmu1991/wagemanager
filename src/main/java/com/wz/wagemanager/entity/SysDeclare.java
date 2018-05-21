package com.wz.wagemanager.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.sun.istack.internal.Nullable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/1/10 14:56
 * @description 工资申报
 */
@Data
@Builder
@NoArgsConstructor (access = AccessLevel.PUBLIC)
@AllArgsConstructor (access = AccessLevel.PUBLIC)
@Table(name = "sys_declare")
@Entity
public class SysDeclare {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator (name="uuid",strategy="uuid")
    @Column(name = "id",length = 32)
    private String id;

    @Column(name = "declare_name",length = 100)
    private String declareName;

    @Column(name = "declare_date")
    @Temporal (TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date declareDate;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH },fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private SysUser user;

    // 0 未提交 1审核中 2审核通过 3调整中
    @Column(length = 1)
    private Integer status;

    @Column(name = "proc_inst_id",length = 64)
    private String processInstanceId;

    //如果不写mappedBy会生成关系表
    @OneToMany(targetEntity = ActSalary.class,cascade = {CascadeType.ALL},mappedBy = "declare")
    private List<ActSalary> salaryList;

    @Transient
    private String taskId;

}
