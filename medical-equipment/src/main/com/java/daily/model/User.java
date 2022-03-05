package com.java.daily.model;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("age")
    private Integer age;

    @TableField("sex")
    private String sex;

    @TableField("address")
    private String address;

    @TableField("phone")
    private String phone;

    @TableField("password")
    private String password;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("username")
    private String username;

    @TableField("type")
    private String type;

    @TableField("state")
    private Integer state;

    @TableField("email")
    private String email;

    @TableField("department_id")
    private Integer departmentId;

    @TableField("department")
    private String department;

    @TableField(value = "instruction")
    private String instruction;

}
