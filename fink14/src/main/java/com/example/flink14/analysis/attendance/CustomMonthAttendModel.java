package com.example.flink14.analysis.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 假勤月报自定义数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomMonthAttendModel implements Serializable {

    // id 自增主键
    private Long id;

    //id 为 monthstat的id
    private String monthstatId;

    private String tenantid;

    private String ytenantId;

    private String buId;

    private String orgId;

    private String deptId;

    private String staffId;

    private String tsyear;

    private String tsmonth;

    private String staffCode;

    /**
     * 自定义项列名
     */
    private String itemCode;
    /**
     * 自定义项描述
     */
    private String itemName;

    /**
     * 自定义项值
     */
    private Integer iItemValue;

    private String vItemValue;

    private Date dItemValue;

    private BigDecimal nItemValue;

    private String bItemValue;

    /**
     * 单位
     */
    private String unit;

    // 考勤方案id 和 name
    private String schemeId;

    // 自定义项类型 1 统计自定义项  2 业务请假自定义项  3 班次统计 4出差  5外出
    private Integer cusType;

}
