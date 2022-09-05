package com.java.daily.service.impl;

import com.java.daily.model.Department;
import com.java.daily.dao.DepartmentMapper;
import com.java.daily.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wm
 * @since 2022-02-27
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

}
