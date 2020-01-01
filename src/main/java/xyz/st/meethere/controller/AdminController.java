package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
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
    private final AdminService adminService;
    private final FileService fileService;

    public AdminController(AdminService adminService, FileService fileService) {
        this.adminService = adminService;
        this.fileService = fileService;
    }

    @ResponseBody
    @ApiOperation("通过userId查找管理员")
    @GetMapping("/admin/getById")
    ResponseMsg getUserById(@RequestParam("userId") int userId) {
        ResponseMsg msg = new ResponseMsg();
        msg.setStatus(404);
        User user = adminService.getAdminById(userId);
        if (user != null) {
            msg.setStatus(200);
        }
        msg.getResponseMap().put("result", user);
        return msg;
    }

    @ResponseBody
    @ApiOperation("通过userName和password登陆")
    @GetMapping("/admin/enter")
    ResponseMsg loginAdmin(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        ResponseMsg msg = new ResponseMsg();
        msg.setStatus(404);
        boolean isPwdCorrect = adminService.checkAdminPassword(userName, password);
        if (isPwdCorrect) {
            User user = adminService.getAdminByName(userName);
            if (user != null) {
                msg.setStatus(200);
            }
            msg.getResponseMap().put("result", user);
            return msg;
        }
        return msg;
    }

    @ResponseBody
    @ApiOperation("修改管理员信息，使用userId识别管理员")
    @PostMapping("/admin/updateById")
//    FIXME:参数接受的方法最好改成PathVariable或者RequestParam
    ResponseMsg updateById(@RequestBody Map params) {
        ResponseMsg msg = new ResponseMsg();
//        FIXME:参数传递错误返回400
        msg.setStatus(400);

        if (
                (!(params.containsKey("userId"))||!(params.containsKey("password")))
                        &&
                        (!(params.containsKey("userId")) || !(params.containsKey("email")) || !(params.containsKey("description")))
        )
        {
            return msg;
        }

        User user = adminService.getAdminById(Integer.parseInt((params.get("userId").toString())));
        if (user == null) {
            msg.setStatus(404);
            return msg;
        }
        user.updateUser(params);
        int ret = adminService.updateAdminByModel(user);
        if (ret > 0) {
            msg.setStatus(200);
            //        FIXME: 统一返回值名称
            msg.getResponseMap().put("user", user);
        } else {
            msg.setStatus(500);
        }
        return msg;
    }

    /*
     * 个人信息头像管理
     * */
    @ResponseBody
    @ApiOperation("更新管理员头像")
    @PostMapping("/admin/profilePic")
    ResponseMsg updateProfilePic(@RequestParam("image") MultipartFile file, @RequestParam("userId") Integer id) throws FileException {
        /*
         * 封装图片路径
         * */
        String storeFile = null;
        storeFile = fileService.storeFile(file);
        int result = adminService.updateAdminProfilePicByAdminId(storeFile, id);
        User user = adminService.getAdminById(id);
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setStatus(result);
//        FIXME:统一返回内容名称
        responseMsg.getResponseMap().put("user", user);

        return responseMsg;
    }
}
