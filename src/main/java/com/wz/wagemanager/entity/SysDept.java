package com.wz.wagemanager.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sys_dept")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SysDept {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator (name="uuid",strategy="uuid")
    @Column(name = "id",length = 32)
    private String id;
    @Column(name = "dept_name")
    private String deptName;
    /**部门编号*/
    @Column(name = "dept_no")
    private String deptNo;

    @Column(name = "create_time")
    @Temporal (TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "status",length = 1)
    private Integer status;


}
