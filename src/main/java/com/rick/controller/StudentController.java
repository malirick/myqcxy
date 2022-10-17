package com.rick.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rick.pojo.Clazz;
import com.rick.pojo.Student;
import com.rick.service.StudentService;
import com.rick.util.MD5;
import com.rick.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @ApiOperation("删除单个和多个学生")
    @DeleteMapping("/delStudentById")
    public Result deleteClazz(
            @ApiParam("要删除的多个学生的ID的JSON数组")@RequestBody List<Integer> ids
    ){
        studentService.removeByIds(ids);

        return Result.ok();
    }


    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
   public Result getStudentByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询页大小")@PathVariable("pageSize") Integer pageSize,
            Student student
    ){
        Page<Student> page =new Page<>(pageNo,pageSize);

        IPage<Student> iPage=studentService.getStudentByOpr(page,student);

        return  Result.ok(iPage);
    }


    @PostMapping("addOrUpdateStudent")
    public Result addOrUpdateStudent(

          @RequestBody Student student
    ){
        Integer id = student.getId();
        if (null ==id || 0==id) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }


}
