package com.config;

/**
 * rabbitMQ 交换机 枚举配置
 *
 * @author lemon
 */

public enum RabbitExchangeEnum {
    /**
     * 视频采集交换机
     */
    COLLECTION_VIDEO_EXCHANGE("collection.video.exchange", "视频采集交换机"),
    /**
     * 实时数据交换机
     */
    COLLECTION_REALTIME_EXCHANGE("collection.realtime.exchange", "实时数据交换机"),
    /**
     * 离线采集交换机
     */
    COLLECTION_OFFLINE_EXCHANGE("collection.offline.exchange", "离线采集交换机"),
    /**
     * 事务采集交换机
     */
    COLLECTION_TRANSACTION_EXCHANGE("collection.transaction.exchange", "事务采集交换机"),
    /**
     * 可靠通道交换机
     */
    COLLECTION_CMD_EXCHANGE("collection.cmd.exchange", "可靠通信交换机"),

    PROCESS_ENGINE_EXCHANGE("process.engine.exchange", "事务采集交换机");

    private final String exchange;

    private final String desc;

    RabbitExchangeEnum(String exchange, String desc) {
        this.exchange = exchange;
        this.desc = desc;
    }

    public String exchange() {
        return exchange;
    }

    public String getDesc() {
        return desc;
    }
}
