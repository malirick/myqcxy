package com.rick.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rick.pojo.Admin;
import com.rick.pojo.LoginForm;
import com.rick.pojo.Student;
import com.rick.pojo.Teacher;

public interface AdminService extends IService<Admin> {


    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);


    IPage<Admin> getAdminsByOpr(Page<Admin> pageParam, String adminName);
}
