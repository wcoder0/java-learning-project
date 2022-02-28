package com.java.daily.model;

import java.util.*;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("equipment_repire")
public class EquipmentRepair {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableId(value = "eid")
    private String eid;

    @TableId(value = "equipment_id")
    private String equipmentId;

    @TableId(value = "equipment_name")
    private String equipmentName;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("reason")
    private String reason;

    @TableField("instruction")
    private String instruction;
}
