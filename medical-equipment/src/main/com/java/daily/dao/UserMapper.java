package com.java.daily.dao;

import com.java.daily.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wm
 * @since 2022-02-27
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
