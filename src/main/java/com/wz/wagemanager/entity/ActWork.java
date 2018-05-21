package com.wz.wagemanager.entity;

import lombok.*;

//考勤历史表
@Data
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ActWork {
    private String deptName;
    private String workNo;
    private String customNo;
    private String username;
    //应到
    private String arrive;
    //实到
    private String reality;
/*    //迟到
    private String late;
    //早退
    private String leaveEar;
    //旷工
    private String absenteeism;
    //加班
    private String overtime;
    //外出
    private String goOut;
    //因公外出
    private String busOut;
    //工作时间
    private String workTime;
    //应签次数
    private String signed;
    //签到
    private String signIn;
    //签退
    private String signOut;
    //未签到
    private String notSignIn;
    //未签退
    private String notSignOut;
    //请假
    private String leave;
    //公出
    private String busOff;
    //病假
    private String sickLeave;
    //事假
    private String comLeave;
    //探亲家
    private String homeLeave;
    //平日
    private String weekday;
    //周末
    private String weekend;
    //节假日
    private String holiday;
    //出勤时间
    private String dutyDate;
    //平日加班
    private String  dayWork;
    //周末加班
    private String  weekendWork;
    //节假日加班
    private String  holidayWork;*/
}
