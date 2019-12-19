package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.entity.User;
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

    @ApiOperation("通过userName查找用户，用户名Unique")
    @GetMapping("/user/getByName/{userName}")
    ResponseMsg getUserByName(@PathVariable("userName") String userName){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        User user = userService.getUserByName(userName);
        if(user != null){
            msg.setStatus(200);
        }
        msg.getResponseMap().put("result",user);
        return msg;
    }

    @ApiOperation("通过userID查找用户")
    @GetMapping("/user/getById/{userId}")
    ResponseMsg getUserById(@PathVariable("userId") int userId){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        User user = userService.getUserById(userId);
        if(user != null){
            msg.setStatus(200);
        }
        msg.getResponseMap().put("result",user);
        return msg;
    }

    @ApiOperation("查找所有用户")
    @GetMapping("/user/traverseUser")
    ResponseMsg traverseUser(){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        List<User> user = userService.traverseUser();
        if(user != null){
            msg.setStatus(200);
        }
        msg.getResponseMap().put("result",user);
        return msg;
    }

    @ApiOperation("通过userName和password登陆")
    @GetMapping("/user/login")
    ResponseMsg loginUser(@RequestParam("userName") String userName,@RequestParam("password") String password){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        boolean isPwdCorrect=userService.checkUserPassword(userName, password);
        if(isPwdCorrect){
            msg.setStatus(200);
        }
        return msg;
    }

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
            msg.setStatus(200);
        }
        return msg;
    }

    @ApiOperation("通过userId删除")
    @DeleteMapping("/user/deleteById/{userId}")
    ResponseMsg deleteUser(@PathVariable("userId") int userId){
        ResponseMsg msg = new ResponseMsg();msg.setStatus(404);
        int registerStatus=userService.deleteUserById(userId);
        if(registerStatus>0){
            msg.setStatus(200);
        }
        return msg;
    }

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

    @ApiOperation("修改用户信息，使用userId识别用户")
    @PostMapping("/user/update")
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
        }
        return msg;
    }
}
