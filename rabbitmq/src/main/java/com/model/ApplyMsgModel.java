package com.model;

import lombok.Data;

/**
 * @Author: Hannibal
 * @Date: 2021/10/11 18:09
 * @Version 1.0
 * @description 申请消息统一消息格式
 * 系统——>流程服务
 */
@Data
public class ApplyMsgModel {

    /**
     * 源id，由源系统携带
     */
    private Long sourceId;

    /**
     * 策略id
     */
    private Integer strategyId;

    /**
     * 用户请求标识，用于验证原系统
     */
    private String token;

    /**
     * 申请人
     */
    private String applicant;

    /**
     * 申请凭证
     */
    private String certificate;

    /**
     * 数据项
     */
    private String data;

    /**
     * 用于反馈的apiId
     */
    private Integer feedbackApiId;

    /**
     * 是否为撤回
     */
    private boolean isRevoke;

}
