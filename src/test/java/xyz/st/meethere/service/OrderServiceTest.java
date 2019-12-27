package xyz.st.meethere.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import xyz.st.meethere.entity.PreOrder;
import xyz.st.meethere.mapper.OrderMapper;

import static org.mockito.Mockito.mock;

public class OrderServiceTest {
    private static OrderService orderService;
    @Mock
    private OrderMapper orderMapper;

    @BeforeEach
    public void init(){
        orderMapper = mock(OrderMapper.class);
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
