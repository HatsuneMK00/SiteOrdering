package xyz.st.meethere.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.annotation.Order;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.st.meethere.service.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OrderControllerTest {
    private OrderService orderService;
    private FileService fileService;
    private AdminService adminService;
    private UserService userService;
    private GroundService groundService;
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        fileService = mock(FileService.class);
        adminService = mock(AdminService.class);
        userService = mock(UserService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService,userService,adminService,groundService)).build();
    }

}