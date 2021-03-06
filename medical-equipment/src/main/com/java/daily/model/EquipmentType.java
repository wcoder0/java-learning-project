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
@TableName("equipment_type")
public class EquipmentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("instruction")
    private String instruction;

    @TableField("state")
    private Integer state;


}
