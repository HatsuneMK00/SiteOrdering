package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.st.meethere.entity.PreOrder;
import xyz.st.meethere.entity.User;
import xyz.st.meethere.service.OrderService;

import java.util.List;

@RestController
@ResponseBody
public class OrderController {
    @Autowired
    OrderService orderService;

    @ApiOperation("获取用户的所有订单")
    @GetMapping("/user/{userid}/order")
    List<PreOrder> getOrdersOfUSer(@PathVariable("userid") Integer id){
        return orderService.getAllPreOrdersOfUser(id);
    }

    @ApiOperation("获取某用户指定订单")
    @GetMapping("/user/{userid}/order/{orderid}")
    PreOrder getOrderByIdOfUSer(@PathVariable("userid") Integer uid,@PathVariable("orderid") Integer oid){
        return orderService.getPreOrder(uid, oid);
    }
    @ApiOperation("新增用户订单")
    @PostMapping("/user/{userid}/order")
    PreOrder addAnOrder(@RequestBody PreOrder preOrder){
        if(orderService.addPreOrder(preOrder) == 1){
            return preOrder;
        }
        return  null;
    }

}
