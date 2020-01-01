package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.st.meethere.entity.Ground;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.exception.FileException;
import xyz.st.meethere.service.FileService;
import xyz.st.meethere.service.GroundService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/*
 * GroundController 和 NewsController用了两种不同的传参和返回值方式
 * GroundController中使用了ResponseMsg，里面有状态码，可能会方便测试一些
 * NewsController中直接使用的Model对象，在api文档中会更加好看一些(也没多好看)
 * */
@RestController
@ResponseBody
public class GroundController {
    final
    GroundService groundService;
    final
    FileService fileService;
    Logger logger = LoggerFactory.getLogger(getClass());

    public GroundController(GroundService groundService, FileService fileService) {
        this.groundService = groundService;
        this.fileService = fileService;
    }

    @ApiOperation(value = "获取所有场馆信息")
    @GetMapping("/ground")
    ResponseMsg getAllGroundsInfo() {
        List<Ground> grounds = groundService.getAllGrounds();
        ResponseMsg responseMsg = new ResponseMsg();
//        FIXME: 只有grounds为null时返回404
        if (grounds == null)
            responseMsg.setStatus(404);
        else {
            responseMsg.getResponseMap().put("result", grounds);
            responseMsg.setStatus(200);
        }
        return responseMsg;
    }

    @ApiOperation(value = "根据场馆id获取场馆信息")
    @GetMapping("/ground/{groundId}")
    ResponseMsg getGroundByGroundId(@PathVariable("groundId") Integer groundId) {
        Ground ground = groundService.getGroundById(groundId);
        ResponseMsg responseMsg = new ResponseMsg();
        if (ground == null)
            responseMsg.setStatus(404);
        else
            responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", ground);
        return responseMsg;
    }


    /*
     * 这个POST应该是用表单提交的
     * */
    @ApiOperation(value = "增加一个场馆信息")
    @PostMapping("/ground")
    ResponseMsg addGround(
            @RequestParam("groundName") String groundName,
            @RequestParam("pricePerHour") int pricePerHour,
            @RequestParam("address") String address,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile file
    ) {

        Ground ground = new Ground(groundName, pricePerHour, address, description);
        /*
         * 有效性检查
         * */
        if (groundService.verifyGround(ground)) {
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(500);
            return responseMsg;
        }

        /*
         * 封装图片路径
         * */
        String storeFile;
        try {
            storeFile = fileService.storeFile(file);
            ground.setPhoto(storeFile);
        } catch (FileException e) {
            logger.error(e.getMessage(), e);
        }
        int result = groundService.addGround(ground);
        ResponseMsg responseMsg = new ResponseMsg();
        if (result == 1)
            responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", ground);
        return responseMsg;
    }

    /*
     * 这个POST应该是用表单提交的
     * */
    @ApiOperation(value = "增加一个场馆信息, 直接提供图片的url")
    @PostMapping("/groundWOFileOperation")
    ResponseMsg addGround(@RequestBody Ground ground
    ) {
        /*
         * 有效性检查
         * */
        if (groundService.verifyGround(ground)) {
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(500);
            return responseMsg;
        }

        int result = groundService.addGroundWOFileOperation(ground);
        ResponseMsg responseMsg = new ResponseMsg();
        if (result == 1)
            responseMsg.setStatus(200);
        else
            responseMsg.setStatus(500);
        responseMsg.getResponseMap().put("result", ground);
        return responseMsg;
    }


    @ApiOperation(value = "对现有场馆信息进行编辑")
    @PutMapping("/ground")
    ResponseMsg updateGround(@RequestBody Ground ground) {
        /*
         * 有效性检查
         * */
        if (groundService.verifyGround(ground)) {
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(500);
            return responseMsg;
        }

        int result = groundService.updateGround(ground);
        ResponseMsg responseMsg = new ResponseMsg();
        if (result == 1) {
            responseMsg.setStatus(200);
//                FIXME:需要返回更新后的内容
            responseMsg.getResponseMap().put("result", ground);
        } else
//            FIXME: 没有找到该ground时返回404
            responseMsg.setStatus(404);
        return responseMsg;
    }

    @ApiOperation(value = "删除一个场馆")
    @DeleteMapping("/ground/{groundId}")
    ResponseMsg deleteGround(@PathVariable("groundId") Integer groundId) {
        Ground ground = groundService.getGroundById(groundId);
        int result = groundService.deleteGround(groundId);
        ResponseMsg responseMsg = new ResponseMsg();
//        FIXME:删除时需要返回被删除内容
        responseMsg.getResponseMap().put("result", ground);
        if (result == 1)
            responseMsg.setStatus(200);
        else
//            FIXME:没有找到删除对象返回404
            responseMsg.setStatus(404);
        return responseMsg;
    }

    @ResponseBody
    @ApiOperation("通过groundId批量删除场馆")
    @DeleteMapping("/ground/deleteByBatch")
    ResponseMsg deleteGroundByBatch(@RequestBody Map<String, List<Integer>> data) {
        ResponseMsg msg = new ResponseMsg();
        List<Integer> ids = data.get("ids");
        ResponseMsg tempMsg;
        msg.setStatus(200);
        for (Integer id : ids) {
            tempMsg = deleteGround(id);
            if (tempMsg.getStatus() == 404 && msg.getStatus() != 404) {
                msg.setStatus(404);
            }
        }
        return msg;
    }

    /*
     * 支持3种搜索方式：
     * 1. 纯文字: 标题匹配
     * 2. "gid:"开头: 返回对应gid的Ground
     *   例如: gid: 1,2,3,4,5
     * 3. 搜索内容为空: 返回全部
     *
     * */
    @ResponseBody
    @ApiOperation("通过搜索返回对应ground")
    @PostMapping("/ground/match")
//        TODO: 感觉这些逻辑都应该在Service层里面，而不是在Controller里
    ResponseMsg getGroundByMatch(@RequestBody Map<String, String> params) {
        String searchParam = params.get("match");
        if (searchParam.equals("")) {
            return getAllGroundsInfo();
        } else if (searchParam.startsWith("gid:")) {
            String param = searchParam.split(":")[1];
            String[] ids = param.split(",");
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(200);
            ArrayList<Ground> retGround = new ArrayList<>();
            for (String id : ids) {
                retGround.add(groundService.getGroundById(Integer.valueOf(id.trim())));
            }
//            这里根本就不可能是0，null也会被添加进去的
//            if (retGround.size() == 0)
//                responseMsg.setStatus(404);
            responseMsg.getResponseMap().put("result", retGround);
            return responseMsg;
        } else {
            List<Ground> grounds = groundService.getGroundsByMatch(searchParam);
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(200);
            if (grounds.size() == 0)
                responseMsg.setStatus(404);
            responseMsg.getResponseMap().put("result", grounds);
            return responseMsg;
        }
    }
}
