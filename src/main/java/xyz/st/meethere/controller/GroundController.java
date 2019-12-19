package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.st.meethere.entity.Ground;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.service.GroundService;

import java.util.HashMap;
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
    @Autowired
    GroundService groundService;

    @ApiOperation(value="获取所有场馆信息")
    @GetMapping("/ground")
    ResponseMsg getAllGroundsInfo(){
        List<Ground> grounds = groundService.getAllGrounds();
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.getResponseMap().put("result",grounds);
        if (grounds.size() == 0)
            responseMsg.setStatus(404);
        else
            responseMsg.setStatus(200);
        return responseMsg;
    }

    @ApiOperation(value="根据场馆id获取场馆信息")
    @GetMapping("/ground/{groundId}")
    ResponseMsg getGroundByGroundId(@PathVariable("groundId") Integer groundId){
        Ground ground = groundService.getGroundById(groundId);
        ResponseMsg responseMsg = new ResponseMsg();
        if (ground == null)
            responseMsg.setStatus(404);
        else
            responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result",ground);
        return responseMsg;
    }


    /*
    * 这边还没有写photo的存储逻辑
    * */
    @ApiOperation(value = "增加一个场馆信息")
    @PostMapping("/ground")
    ResponseMsg addGround(@RequestBody Map params){
        Ground ground = new Ground(params);

        /*
        * 有效性检查
        * */

        int result = groundService.addGround(ground);
        ResponseMsg responseMsg = new ResponseMsg();
        if (result == 1)
            responseMsg.setStatus(200);
        else
            responseMsg.setStatus(500);
        responseMsg.getResponseMap().put("result",ground);
        return responseMsg;
    }

    @ApiOperation(value = "对现有场馆信息进行编辑")
    @PutMapping("/ground")
    ResponseMsg updateGround(@RequestBody Map params){
        Ground ground = new Ground(params);
        ground.setGroundId((Integer) params.get("groundId"));

        int result = groundService.updateGround(ground);
        ResponseMsg responseMsg = new ResponseMsg();
        if (result == 1)
            responseMsg.setStatus(200);
        else
            responseMsg.setStatus(500);
        return responseMsg;
    }

    @ApiOperation(value = "删除一个场馆")
    @DeleteMapping("/ground/{groundId}")
    ResponseMsg deleteGround(@PathVariable("groundId") Integer groundId){
        int result = groundService.deleteGround(groundId);
        ResponseMsg responseMsg = new ResponseMsg();
        if (result == 1)
            responseMsg.setStatus(200);
        else
            responseMsg.setStatus(500);
        return responseMsg;
    }
}
