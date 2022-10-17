package com.rick.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rick.pojo.Grade;
import com.rick.service.GradeService;
import com.rick.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级管理器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;


    @ApiOperation("获取全部年级")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> grades =gradeService.getGrades();
        return Result.ok(grades);
    }


    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("要删除的所有的grade的id的JSON集合") @RequestBody List<Integer>ids)
    {
        gradeService.removeByIds(ids);
       return  Result.ok();
    }

    @ApiOperation("新增或修改grade,有id属性是修改,没有则是增加")
    @PostMapping("/saveOrUpdateGrade")
    public  Result saveOrUpdate(
            @ApiParam("JSON的Grade对象")    @RequestBody Grade grade
    ){
        gradeService.saveOrUpdate(grade);
        return Result.ok();

    }



    @ApiOperation("根据年级名称模糊查询,带分页")
    @GetMapping("getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
       @PathVariable("pageNo") Integer pageNo,
       @PathVariable("pageSize") Integer pageSize,
       String gradeName

    ){

        Page<Grade> page=new  Page<>(pageNo,pageSize);
        IPage<Grade> pageRs  = gradeService.getGradeBy(page,gradeName);

        return Result.ok(pageRs);
    }


}
