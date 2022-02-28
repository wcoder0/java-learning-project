package com.java.daily.model;

import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author wm
 * @since 2022-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("equipment")
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableId(value = "eid")
    private String eid;

    @TableId(value = "quantity")
    private String quantity;

    @TableId(value = "standard")
    private String standard;

    @TableId(value = "number")
    private String number;

    @TableId(value = "provider")
    private String provider;

    @TableId(value = "approve")
    private String approve;

    @TableId(value = "instruction")
    private String instruction;

    @TableField("type_id")
    private Integer typeId;

    @TableField("name")
    private String name;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("type")
    private String type;

    @TableField("state")
    private Integer state;

    @TableField("equipment_type_id")
    private Integer equipmentTypeId;

    @TableField("equipment_type")
    private String equipmentType;

    @TableField("department_id")
    private Integer departmentId;

    @TableField("department")
    private String department;

}
