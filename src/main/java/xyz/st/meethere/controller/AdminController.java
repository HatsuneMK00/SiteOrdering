package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.entity.User;
import xyz.st.meethere.exception.FileException;
import xyz.st.meethere.service.AdminService;
import xyz.st.meethere.service.FileService;

import java.util.Map;

@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private FileService fileService;

    @ResponseBody
    @ApiOperation("通过userName查找管理员")
    @GetMapping("/admin/getByName")
    ResponseMsg getUserByName(@RequestParam("userName") String userName){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        User user = adminService.getAdminByName(userName);
        if(user != null){
            msg.setStatus(200);
        }
        msg.getResponseMap().put("result",user);
        return msg;
    }

    @ResponseBody
    @ApiOperation("通过userId查找管理员")
    @GetMapping("/admin/getById")
    ResponseMsg getUserById(@RequestParam("userId") int userId){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        User user = adminService.getAdminById(userId);
        if(user != null){
            msg.setStatus(200);
        }
        msg.getResponseMap().put("result",user);
        return msg;
    }

    @ResponseBody
    @ApiOperation("通过userName和password登陆")
    @GetMapping("/admin/enter")
    ResponseMsg loginAdmin(@RequestParam("userName") String userName,@RequestParam("password") String password){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        boolean isPwdCorrect=adminService.checkAdminPassword(userName, password);
        if(isPwdCorrect){
            msg.setStatus(200);
        }
        return msg;
    }

    @ResponseBody
    @ApiOperation("修改管理员信息，使用userId识别管理员")
    @PostMapping("/admin/updateById")
    ResponseMsg updateById(@RequestBody Map params){
        ResponseMsg msg = new ResponseMsg();
        msg.setStatus(404);
        if(!(params.containsKey("userId"))){
            return msg;
        }
        User user=adminService.getAdminById((int)params.get("userId"));
        if(user==null) return msg;
        user.updateUser(params);
        int ret = adminService.updateAdminByModel(user);
        if(ret>0) {
            msg.setStatus(200);
            msg.getResponseMap().put("user",user);
        }
        return msg;
    }

    @ResponseBody
    @ApiOperation("修改管理员信息，使用userName识别管理员")
    @PostMapping("/admin/updateByName")
    ResponseMsg updateByName(@RequestBody Map params){
        ResponseMsg msg = new ResponseMsg();
        msg.setStatus(404);
        if(!(params.containsKey("userName"))){
            return msg;
        }
        User user=adminService.getAdminByName((String)params.get("userName"));
        if(user==null) return msg;
        user.updateUser(params);
        int ret = adminService.updateAdminByModel(user);
        if(ret>0) {
            msg.setStatus(200);
            msg.getResponseMap().put("user",user);
        }
        return msg;
    }

    /*
     * 个人信息头像管理
     * */
    @ResponseBody
    @ApiOperation("更新管理员头像")
    @PostMapping("/admin/profilePic")
    ResponseMsg updateProfilePic(@RequestParam("image") MultipartFile file, @RequestParam("userId")Integer id){
        /*
         * 封装图片路径
         * */
        String storeFile = null;
        try {
            storeFile = fileService.storeFile(file);
        } catch (FileException e) {
            e.printStackTrace();
        }
        assert storeFile != null;
        int result = adminService.updateAdminProfilePicByAdminId(storeFile,id);
        User user = adminService.getAdminById(id);
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setStatus(result);
        responseMsg.getResponseMap().put("user",user);

        return responseMsg;
    }
}
