package xyz.st.meethere.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import xyz.st.meethere.entity.PreOrder;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.service.AdminService;
import xyz.st.meethere.service.GroundService;
import xyz.st.meethere.service.OrderService;
import xyz.st.meethere.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
public class OrderController {
    final
    OrderService orderService;
    final
    UserService userService;
    final
    AdminService adminService;
    final
    GroundService groundService;

    public OrderController(OrderService orderService, UserService userService, AdminService adminService,
                           GroundService groundService) {
        this.orderService = orderService;
        this.userService = userService;
        this.adminService = adminService;
        this.groundService = groundService;
    }

    @ApiOperation(value = "获取所有订单, 包括用户的信息和场地的信息")
    @GetMapping("/order")
    ResponseMsg getOrders() {
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setStatus(404);
        // fixme: 返回的内容里应该有userName,groundName
        List<PreOrder> preOrders = orderService.getOrders();

        responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", preOrders);
        return responseMsg;
    }

    /*
     * 支持4种搜索方式：
     * 1. "time:"开头: 返回当天的所有订单
     *   例如: time: 2019-12-23
     * 2. "gid:"开头: 返回对应gid的订单
     *   例如: gid: 1,2,3,4,5
     * 3. "uid:"开头: 返回对应uid的订单
     * 4. 搜索内容为空: 返回全部
     *
     * */
    @PostMapping("/order/match")
    @ApiOperation("通过搜索内容获取订单")
    ResponseMsg getOrderByTimeMatch(@RequestBody Map<String,String> params) {
        String searchParam = params.get("match");
        if (searchParam.equals("")) {
            return getOrders();
        } else if (searchParam.startsWith("gid:")) {
            String param = searchParam.split(":")[1];
            String[] ids = param.split(",");
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(200);
            ArrayList<PreOrder> retOrder = new ArrayList<>();
            for (String id : ids) {
//                FIXME: 返回的内容应该是一层的数组
                retOrder.addAll(orderService.getGroundOrders(Integer.valueOf(id.trim())));
            }
            if (retOrder.size() == 0)
                responseMsg.setStatus(404);
            responseMsg.getResponseMap().put("result", retOrder);
            return responseMsg;
        } else if (searchParam.startsWith("uid:")) {
            String param = searchParam.split(":")[1];
            String[] ids = param.split(",");
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(200);
            ArrayList<PreOrder> retOrder = new ArrayList<>();
            for (String id : ids) {
                retOrder.addAll(orderService.getAllPreOrdersOfUser(Integer.valueOf(id.trim())));
            }
            if (retOrder.size() == 0)
                responseMsg.setStatus(404);
            responseMsg.getResponseMap().put("result", retOrder);
            return responseMsg;
        } else if (searchParam.startsWith("time:")){
            String time = searchParam.split(":")[1].trim();
            List<PreOrder> orders = orderService.getPreOrderByTimeMatch(time);
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(200);
            if (orders.size() == 0)
                responseMsg.setStatus(404);
            responseMsg.getResponseMap().put("result",orders);
            return responseMsg;
        } else {
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(500);
            return responseMsg;
        }
    }

    @ApiOperation(value = "获取用户的所有订单", notes = "如果返回404，则用户不存在")
    @GetMapping("/order/user/{userId}/preOrder")
    ResponseMsg getOrdersOfUSer(@PathVariable("userId") Integer id) {
        ResponseMsg responseMsg = new ResponseMsg();
        if (!orderService.checkUserExistence(id)) {
            responseMsg.setStatus(404);
            return responseMsg;
        }
        // fixme: 返回的内容里应该有userName,groundName
        List<PreOrder> preOrders = orderService.getAllPreOrdersOfUser(id);
        responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", preOrders);
        return responseMsg;
    }

    @ApiOperation("获取某用户指定订单")
    @GetMapping("/order/user/{userId}/order/{preOrderId}")
    ResponseMsg getOrderByIdOfUSer(@PathVariable("userId") Integer uid, @PathVariable("preOrderId") Integer oid) {
        // fixme: 返回的内容里应该有userName,groundName
        PreOrder preOrder = orderService.getPreOrder(uid, oid);
        ResponseMsg responseMsg = new ResponseMsg();
        if (preOrder == null) {
            responseMsg.setStatus(404);
            return responseMsg;
        }
        responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", preOrder);
        return responseMsg;
    }

    @ApiOperation(value = "新增用户订单", notes = "若返回510则说明用户输入的开始时间和duration与该场地现有预约单冲突")
    @PostMapping("/order/user/{userId}/order")
    ResponseMsg addAnOrder(
            @RequestParam("groundId") Integer gid,
            @PathVariable("userId") Integer uid,
            @RequestParam("startTime") String startTime,
            @RequestParam("duration") Integer duration,
            @RequestParam("userNum") Integer userNum
    ) {
        ResponseMsg responseMsg = new ResponseMsg();
        if (orderService.validatePreOrder(gid, startTime, duration)) {
            responseMsg.setStatus(510);
            return responseMsg;
        }
        PreOrder preOrder = new PreOrder();
        preOrder.setGroundId(gid);
        preOrder.setUserId(uid);
        preOrder.setStartTime(startTime);
        preOrder.setDuration(duration);
        preOrder.setUserNum(userNum);
        preOrder.setPrice(duration * orderService.getGroundPrice(gid));
        preOrder.setPayed(0);
        preOrder.setChecked(0);
        if (orderService.addPreOrder(preOrder) == 1) {
            responseMsg.setStatus(200);
            responseMsg.getResponseMap().put("result", preOrder);
            return responseMsg;
        }
        responseMsg.setStatus(500);
        return responseMsg;
    }

    @ApiOperation(value = "新增管理员订单，用来限制场地的可用时间", notes = "若返回510则说明管理员输入的开始时间和duration与该场地现有预约单冲突")
    @PostMapping("/order/admin/{userId}/order")
    ResponseMsg addAnOrderByAdmin(
            @RequestParam("groundId") Integer gid,
            @PathVariable("userId") Integer uid,
            @RequestParam("startTime") String startTime,
            @RequestParam("duration") Integer duration,
            @RequestParam("userNum") Integer userNum
    ) {
        ResponseMsg responseMsg = new ResponseMsg();
        if (orderService.validatePreOrder(gid, startTime, duration)) {
            responseMsg.setStatus(510);
            return responseMsg;
        }
        PreOrder preOrder = new PreOrder();
        preOrder.setGroundId(gid);
        preOrder.setUserId(uid);
        preOrder.setStartTime(startTime);
        preOrder.setDuration(duration);
        preOrder.setUserNum(userNum);
        preOrder.setPrice(duration * orderService.getGroundPrice(gid));
        //确保订单生效
        preOrder.setPayed(1);
        preOrder.setChecked(1);
        if (orderService.addPreOrder(preOrder) == 1) {
            responseMsg.setStatus(200);
            responseMsg.getResponseMap().put("result", preOrder);
            return responseMsg;
        }
        responseMsg.setStatus(500);
        return responseMsg;
    }


    @ApiOperation("删除用户指定订单")
    @DeleteMapping("/order/{preOrderId}")
    ResponseMsg deleteOrder(@PathVariable("preOrderId") Integer oid) {
        ResponseMsg responseMsg = new ResponseMsg();
        if (orderService.deletePreOrder(oid) == 1)
            responseMsg.setStatus(200);
        else responseMsg.setStatus(500);
        return responseMsg;
    }

    @ResponseBody
    @ApiOperation("通过preOrderId批量删除订单")
    @DeleteMapping("/order/deleteByBatch")
    ResponseMsg deleteOrderByBatch(@RequestBody Map<String,List<Integer>> data) {
        ResponseMsg msg = new ResponseMsg();
        List<Integer> ids = data.get("ids");
        msg.setStatus(200);
        ResponseMsg tempMsg;
        for (Integer id : ids) {
            tempMsg = deleteOrder(id);
            if (tempMsg.getStatus() == 500 && msg.getStatus() != 500){
                msg.setStatus(500);
                return msg;
            }
        }
        msg.setStatus(200);
        return msg;
    }

    @ApiOperation("获取某场地在目前时间之后所有预约单的开始时间和持续时间，并按开始时间升序排序")
    @GetMapping("/order/ground/{groundId}/orderTime")
    ResponseMsg getGroundOrderTime(@PathVariable("groundId") Integer gid) {
        ResponseMsg responseMsg = new ResponseMsg();
        if (!orderService.checkGroundExistence(gid)) {
            responseMsg.setStatus(404);
            return responseMsg;
        }
        List<List> lists = orderService.getOrderTime(gid);
        responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", lists);
        return responseMsg;
    }

    @ApiOperation(value = "获取某场地的所有订单", notes = "若返回404则表明场地不存在")
    @GetMapping("/order/ground/{groundId}/order")
    ResponseMsg getGroundOrders(@PathVariable("groundId") Integer gid) {
        ResponseMsg responseMsg = new ResponseMsg();
        if (!orderService.checkGroundExistence(gid)) {
            responseMsg.setStatus(404);
            return responseMsg;
        }
        responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", orderService.getGroundOrders(gid));
        return responseMsg;
    }

    @ApiOperation("更新订单信息")
    @PutMapping("/order")
    ResponseMsg updateOrderInfo(@RequestBody PreOrder order) {
        ResponseMsg responseMsg = new ResponseMsg();

        if (order.getUserNum() == 0 || order.getPreOrderId() == 0) {
            responseMsg.setStatus(400);
            return responseMsg;
        }

        PreOrder newOrder = orderService.getPreOrderById(order.getPreOrderId());
        if (newOrder == null) {
            responseMsg.setStatus(404);
            return responseMsg;
        }
//        当前只有修改使用人数的需求 这里就写一个 如果以后要加在这里加就行
        newOrder.setUserNum(order.getUserNum());

        int result = orderService.updatePreOrder(newOrder);
        if (result == 1)
            responseMsg.setStatus(200);
        else
            responseMsg.setStatus(500);
        responseMsg.getResponseMap().put("result", newOrder);
        return responseMsg;
    }

    //管理员用接口
    @ApiOperation("获取所有未审核订单")
    @GetMapping("/order/uncheckedOrder")
    ResponseMsg getAllUncheckedComment() {
        ResponseMsg responseMsg = new ResponseMsg();
        List<PreOrder> orders = null;
        // fixme: 返回的内容里应该有userName,groundName
//        fixme: 不通过的订单也要返回
        orders = orderService.getUncheckedOrders();
        responseMsg.getResponseMap().put("result", orders);
        responseMsg.setStatus(200);
        return responseMsg;
    }

    @ApiOperation("将指定订单审核状态标记为通过")
    @PutMapping("/order/check/{preOrderId}")
    ResponseMsg checkOrder(@PathVariable("preOrderId") Integer pid) {
        ResponseMsg responseMsg = new ResponseMsg();
        if (!orderService.checkPreOrderExistence(pid)) {
            responseMsg.setStatus(404);
            return responseMsg;
        }
        responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", orderService.checkPreOrder(pid));
        return responseMsg;
    }

    @ApiOperation("将指定订单审核状态标记为未通过")
    @PutMapping("/order/uncheck/{preOrderId}")
     ResponseMsg uncheckOrder(@PathVariable("preOrderId") Integer pid) {
        ResponseMsg responseMsg = new ResponseMsg();
        if (!orderService.checkPreOrderExistence(pid)) {
            responseMsg.setStatus(404);
            return responseMsg;
        }
        responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", orderService.checkPreOrderFail(pid));
        return responseMsg;
    }
}
