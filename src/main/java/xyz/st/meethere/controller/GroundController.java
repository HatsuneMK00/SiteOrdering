package xyz.st.meethere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.st.meethere.entity.Ground;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.service.GroundService;

import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
public class GroundController {
    @Autowired
    GroundService groundService;

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

    @PostMapping("/ground")
    ResponseMsg addGround(@RequestBody Map params){
        Ground ground = new Ground(params);

        /*
        * 有效性检查
        * */

        Ground ground1 = groundService.addGround(ground);
        ResponseMsg responseMsg = new ResponseMsg();
        if (ground1 != null)
            responseMsg.setStatus(200);
        else
            responseMsg.setStatus(500);
        responseMsg.getResponseMap().put("result",ground1);
        return responseMsg;
    }

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
