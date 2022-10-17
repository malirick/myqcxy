package com.rick.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rick.mapper.StudentMapper;
import com.rick.pojo.Admin;
import com.rick.pojo.Clazz;
import com.rick.pojo.LoginForm;
import com.rick.pojo.Student;
import com.rick.service.StudentService;
import com.rick.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**

 */
@Service("stuService")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {


    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student>  queryWrapper=new QueryWrapper();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student>  queryWrapper=new QueryWrapper();
        queryWrapper.eq("id",userId);
        return  baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> page, Student student) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(student.getName())) {
            queryWrapper.like("name", student.getName());
        }
        if (!StringUtils.isEmpty(student.getClazzName())) {
            queryWrapper.like("clazz_name", student.getClazzName());
        }
        queryWrapper.orderByDesc("id");
        Page<Student> studenPage = baseMapper.selectPage(page, queryWrapper);

        return studenPage;
    }
}
