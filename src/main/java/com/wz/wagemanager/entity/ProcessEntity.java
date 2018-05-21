package com.wz.wagemanager.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.util.Date;

/**
 * @author WindowsTen
 * @date 2018/2/6 16:02
 * @description
 */
@Data
@Builder
@NoArgsConstructor (access = AccessLevel.PUBLIC)
@AllArgsConstructor (access = AccessLevel.PUBLIC)
public class ProcessEntity {

    private String id;

    private String activityName;

    @JSONField (format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JSONField (format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String processInstanceId;

    private String assignUser;

    private String assignMsg;

}
