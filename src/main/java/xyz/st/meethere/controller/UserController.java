package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.entity.User;
import xyz.st.meethere.exception.FileException;
import xyz.st.meethere.service.FileService;
import xyz.st.meethere.service.MailService;
import xyz.st.meethere.service.UserService;

import java.util.List;
import java.util.Map;

// user表以userId为主键
// userName为登陆的的主键，数据库中设置UNIQEUE索引
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @ResponseBody
    @ApiOperation("通过userName查找用户，用户名Unique")
    @GetMapping("/user/getByName")
    ResponseMsg getUserByName(@RequestParam("userName") String userName){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        User user = userService.getUserByName(userName);
        if(user != null){
            msg.setStatus(200);
        }
        msg.getResponseMap().put("result",user);
        return msg;
    }

    @ResponseBody
    @ApiOperation("通过userId查找用户")
    @GetMapping("/user/getById")
    ResponseMsg getUserById(@RequestParam("userId") int userId){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        User user = userService.getUserById(userId);
        if(user != null){
            msg.setStatus(200);
        }
        msg.getResponseMap().put("result",user);
        return msg;
    }

    @ResponseBody
    @ApiOperation("查找所有用户")
    @GetMapping("/user/getAll")
    ResponseMsg traverseUser(){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        List<User> user = userService.traverseUser();
        if(user != null){
            msg.setStatus(200);
        }
        msg.getResponseMap().put("result",user);
        return msg;
    }

    @ResponseBody
    @ApiOperation("通过userName和password登陆")
    @GetMapping("/user/enter")
    ResponseMsg loginUser(@RequestParam("userName") String userName,@RequestParam("password") String password){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        boolean isPwdCorrect=userService.checkUserPassword(userName, password);
        if(isPwdCorrect){
            msg.setStatus(200);
        }
        return msg;
    }

    @ResponseBody
    @ApiOperation("通过email userName password注册")
    @PutMapping("/user/register")
    ResponseMsg registerUser(@RequestBody Map params){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        if(!(params.containsKey("email")&&params.containsKey("userName")&&params.containsKey("password"))){
            return msg;
        }
        String email = params.get("email").toString();
        String userName = params.get("userName").toString();
        String password = params.get("password").toString();
        int registerStatus=userService.addUser(email, userName, password);
        if(registerStatus>0){
            emailUser(email, userName);
            msg.setStatus(200);
        }
        return msg;
    }

    @ResponseBody
    @ApiOperation("通过userId删除")
    @DeleteMapping("/user/deleteById")
    ResponseMsg deleteUser(@RequestParam("userId") int userId){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        int registerStatus=userService.deleteUserById(userId);
        if(registerStatus>0){
            msg.setStatus(200);
        }
        return msg;
    }

    @ResponseBody
    @ApiOperation("发送邮件给email，用户userName")
    @GetMapping("/user/email")
    ResponseMsg emailUser(@RequestParam("email") String email,@RequestParam("userName") String userName){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        boolean emailStatus=new MailService().sendmail(email,userName);
        if(emailStatus){
            msg.setStatus(200);
        }
        return msg;
    }

    @ResponseBody
    @ApiOperation("修改用户信息，使用userId识别用户")
    @PostMapping("/user/updateById")
    ResponseMsg updateById(@RequestBody Map params){
        ResponseMsg msg = new ResponseMsg();
        msg.setStatus(404);
        if(!(params.containsKey("userId"))){
            return msg;
        }
        User user=userService.getUserById((int)params.get("userId"));
        if(user==null) return msg;
        user.updateUser(params);
        int ret = userService.updateUserByModel(user);
        if(ret>0) {
            msg.setStatus(200);
            msg.getResponseMap().put("user",user);
        }
        return msg;
    }

    @ResponseBody
    @ApiOperation("修改用户信息，使用userName识别用户")
    @PostMapping("/user/updateByName")
    ResponseMsg updateByName(@RequestBody Map params){
        ResponseMsg msg = new ResponseMsg();
        msg.setStatus(404);
        if(!(params.containsKey("userName"))){
            return msg;
        }
        User user=userService.getUserByName((String)params.get("userName"));
        if(user==null) return msg;
        user.updateUser(params);
        int ret = userService.updateUserByModel(user);
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
    @ApiOperation("更新用户头像")
    @PostMapping("/user/profilePic")
    ResponseMsg updateProfilePic(@RequestParam("image")MultipartFile file, @RequestParam("userId")Integer id){
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
        int result = userService.updateUserProfilePicByUserId(storeFile,id);
        User user = userService.getUserById(id);
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setStatus(result);
        responseMsg.getResponseMap().put("user",user);

        return responseMsg;
    }
}
