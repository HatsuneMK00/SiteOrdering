package xyz.st.meethere.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.st.meethere.entity.PreOrder;

public class OrderServiceTest {
    private static OrderService orderService;

    @BeforeAll
    static void init(){
        orderService = new OrderService();
    }

    @Test
    public void setDateTest(){
        PreOrder preOrder = new PreOrder();
        orderService.setDate(preOrder);
        System.out.println(preOrder.getOrderTime());
    }
    @Test
    public void getOrderTest(){
        PreOrder preOrder = orderService.getPreOrder(1,9);
        System.out.println(preOrder.getOrderTime());
    }
}
