package com.rick.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rick.pojo.Admin;
import com.rick.pojo.LoginForm;
import com.rick.pojo.Student;
import com.rick.pojo.Teacher;
import com.rick.service.AdminService;
import com.rick.service.StudentService;
import com.rick.service.TeacherService;
import com.rick.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {
    @Autowired
   private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    /*

    * 修改密码的处理器
    * POST  /sms/system/updatePwd/123456/admin
    *       /sms/system/updatePwd/{oldPwd}/{newPwd}
    *       请求参数
                oldpwd
                newPwd
                token 头
            响应的数据
                Result OK data= null

    * */

    @ApiOperation("更新用户密码的处理器")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            // token过期
            return Result.fail().message("token失效,请重新登录后修改密码");
        }
        // 获取用户ID和用类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd= MD5.encrypt(oldPwd);
        newPwd= MD5.encrypt(newPwd);

        switch (userType) {
            case 1:
                QueryWrapper<Admin> queryWrapper1=new QueryWrapper<>();
                queryWrapper1.eq("id",userId.intValue());
                queryWrapper1.eq("password",oldPwd);
                Admin admin =adminService.getOne(queryWrapper1);
                if (admin != null){
                    // 修改
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else{
                    return Result.fail().message("原密码有误!");
                }
                break;

            case 2:
                QueryWrapper<Student> queryWrapper2=new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue());
                queryWrapper2.eq("password",oldPwd);
                Student student =studentService.getOne(queryWrapper2);
                if (student != null){
                    // 修改
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else{
                    return Result.fail().message("原密码有误!");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3=new QueryWrapper<>();
                queryWrapper3.eq("id",userId.intValue());
                queryWrapper3.eq("password",oldPwd);
                Teacher teacher =teacherService.getOne(queryWrapper3);
                if (teacher != null){
                    // 修改
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else{
                    return Result.fail().message("原密码有误!");
                }
                break;

        }
        return Result.ok();
    }






    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
     public Result headerImgUpload(
        @RequestPart("multipartFile")  MultipartFile multipartFile,
        HttpServletRequest request
    ){
     String uuid   =UUID.randomUUID().toString().replace("-","").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String newFileName = uuid.concat(originalFilename.substring(1));

        String portraitPath="C:/Users/rick/Desktop/myzhxy/target/classes/public/upload/".concat(newFileName);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path="upload/".concat(newFileName);
        return Result.ok(path);

    }

    @GetMapping("getInfo")
    public  Result getInfoToker(@RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return  Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        Long userId=JwtHelper.getUserId(token);
        Integer userType=JwtHelper.getUserType(token);

        Map<String,Object> map  =new LinkedHashMap<>();
        switch (userType){
            case 1:
                  Admin admin=adminService.getAdminById(userId);
                  map.put("userType",1);
                  map.put("user",admin);
                break;
            case 2:
                Student student=studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher=teacherService.getTeachById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;

        }


        return Result.ok(map);
    }


    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        HttpSession session = request.getSession();
        String sessionverifiCode=(String)session.getAttribute("verifiCode");
        String loginFormVerifiCode= loginForm.getVerifiCode();
        if("".equals(sessionverifiCode) || null==sessionverifiCode){
            return  Result.fail().message("验证码失效,请刷新后重试");

        }if (!sessionverifiCode.equalsIgnoreCase(loginFormVerifiCode)){
            return  Result.fail().message("验证码有误,请小心后重试");
        }
        session.removeAttribute("verifiCode");

        Map<String,Object> map  =new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin =adminService.login(loginForm);
                    if (null != admin) {
                        map.put("token",JwtHelper.createToken(admin.getId().longValue(),1));
                    }else {
                        throw new RuntimeException("用户名或密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student =studentService.login(loginForm);
                    if (null != student) {
                        map.put("token",JwtHelper.createToken(student.getId().longValue(),2));
                    }else {
                        throw new RuntimeException("用户名或密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher =teacherService.login(loginForm);
                    if (null != teacher) {
                        map.put("token",JwtHelper.createToken(teacher.getId().longValue(),3));
                    }else {
                        throw new RuntimeException("用户名或密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

        }
         return Result.fail().message("查无此用户");

    }




      @GetMapping("getVerifiCodeImage")
      public void getVerCodeImage(HttpServletRequest request,HttpServletResponse response){
          BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
          String verifiCode  =new String(CreateVerifiCodeImage.getVerifiCode());
          HttpSession session = request.getSession();
          session.setAttribute("verifiCode",verifiCode);

          try {
              ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
          } catch (IOException e) {
              e.printStackTrace();
          }


      }



}
