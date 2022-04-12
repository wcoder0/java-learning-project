package com.config;

/**
 * @Author: Hannibal
 * @Date: 2022/2/17 9:42
 * @Version 1.0
 * @description rabbitmq队列命名
 */
public enum RabbitQueueEnum {
    /**
     * 离线队列前缀
     */
    OFFLINE_QUEUE_PREFIX("offline_"),

    /**
     * 事务队列
     */
    TRANSACTION_QUEUE_PREFIX("transaction_"),

    /**
     * 实时队列
     */
    REALTIME_QUEUE_PREFIX("realtime_"),

    /**
     * 申请队列名称
     */
    APPLY_QUEUE("process.engine.apply"),

    /**
     * 反馈队列名称
     */
    FEEDBACK_QUEUE("process.engine.feedback"),
    ;

    private final String queue;

    RabbitQueueEnum(String queue) {
        this.queue = queue;
    }

    public String queue() {
        return queue;
    }
}
