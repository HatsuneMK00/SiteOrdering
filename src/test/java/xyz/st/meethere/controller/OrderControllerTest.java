package xyz.st.meethere.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.st.meethere.entity.Ground;
import xyz.st.meethere.entity.PreOrder;
import xyz.st.meethere.service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService, userService, adminService,
                groundService)).build();
    }

    @Test
    public void get_all_order_test() throws Exception {
        when(orderService.getOrders()).thenReturn(new ArrayList<PreOrder>());
        mockMvc.perform(get("/order"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).getOrders();
    }

    @Test
    public void get_uer_order_success() throws Exception {
        when(orderService.checkUserExistence(1)).thenReturn(true);
        when(orderService.getAllPreOrdersOfUser(1)).thenReturn(new ArrayList<PreOrder>());
        mockMvc.perform(get("/order/user/1/preOrder"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).getAllPreOrdersOfUser(1);
        verify(orderService).checkUserExistence(1);
    }

    @Test
    public void get_user_order_fail() throws Exception {
        when(orderService.checkUserExistence(1)).thenReturn(false);
        mockMvc.perform(get("/order/user/1/preOrder"))
                .andExpect(jsonPath("$.status").value(404));
        verify(orderService).checkUserExistence(1);
    }

    @Test
    void get_user_specific_order_success() throws Exception {
        when(orderService.getPreOrder(1, 1)).thenReturn(new PreOrder());
        mockMvc.perform(get("/order/user/1/order/1"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).getPreOrder(1, 1);
    }

    @Test
    void get_user_specific_order_fail() throws Exception {
        when(orderService.getPreOrder(1, 1)).thenReturn(null);
        mockMvc.perform(get("/order/user/1/order/1"))
                .andExpect(jsonPath("$.status").value(404));
        verify(orderService).getPreOrder(1, 1);
    }

    @Test
    void post_order_success() throws Exception {
        when(orderService.validatePreOrder(1, "2019-12-30 12:30:00.0", 2)).thenReturn(false);
        when(orderService.getGroundPrice(1)).thenReturn(2);
        when(orderService.addPreOrder(any(PreOrder.class))).thenReturn(1);
        mockMvc.perform(post("/order/user/1/order")
                .param("groundId", "1")
                .param("startTime", "2019-12-30 12:30:00.0")
                .param("duration", "2")
                .param("userNum","1"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).validatePreOrder(1, "2019-12-30 12:30:00.0", 2);
        verify(orderService).getGroundPrice(1);
        verify(orderService).addPreOrder(any(PreOrder.class));
    }

    @Test
    void post_order_fail_collision() throws Exception {
        when(orderService.validatePreOrder(1, "2019-12-30 12:30:00.0", 2)).thenReturn(true);
        mockMvc.perform(post("/order/user/1/order")
                .param("groundId", "1")
                .param("startTime", "2019-12-30 12:30:00.0")
                .param("duration", "2")
                .param("userNum","1"))
                .andExpect(jsonPath("$.status").value(510));
        verify(orderService).validatePreOrder(1, "2019-12-30 12:30:00.0", 2);
    }

    @Test
    public void post_order_fail() throws Exception {
        when(orderService.validatePreOrder(1, "2019-12-30 12:30:00.0", 2)).thenReturn(false);
        when(orderService.getGroundPrice(1)).thenReturn(2);
        when(orderService.addPreOrder(any(PreOrder.class))).thenReturn(0);
        mockMvc.perform(post("/order/user/1/order")
                .param("groundId", "1")
                .param("startTime", "2019-12-30 12:30:00.0")
                .param("duration", "2")
                .param("userNum","1"))
                .andExpect(jsonPath("$.status").value(500));
        verify(orderService).validatePreOrder(1, "2019-12-30 12:30:00.0", 2);
        verify(orderService).getGroundPrice(1);
        verify(orderService).addPreOrder(any(PreOrder.class));
    }

    @Test
    public void post_admin_order_success() throws Exception {
        when(orderService.validatePreOrder(1, "2019-12-30 12:30:00.0", 2)).thenReturn(false);
        when(orderService.getGroundPrice(1)).thenReturn(2);
        when(orderService.addPreOrder(any(PreOrder.class))).thenReturn(1);
        mockMvc.perform(post("/order/admin/1/order")
                .param("groundId", "1")
                .param("startTime", "2019-12-30 12:30:00.0")
                .param("duration", "2")
                .param("userNum","1"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).validatePreOrder(1, "2019-12-30 12:30:00.0", 2);
        verify(orderService).getGroundPrice(1);
        verify(orderService).addPreOrder(any(PreOrder.class));
    }

    @Test
    void post_admin_order_fail_collision() throws Exception {
        when(orderService.validatePreOrder(1, "2019-12-30 12:30:00.0", 2)).thenReturn(true);
        mockMvc.perform(post("/order/admin/1/order")
                .param("groundId", "1")
                .param("startTime", "2019-12-30 12:30:00.0")
                .param("duration", "2")
                .param("userNum","1"))
                .andExpect(jsonPath("$.status").value(510));
        verify(orderService).validatePreOrder(1, "2019-12-30 12:30:00.0", 2);
    }

    @Test
    public void post_admin_order_fail() throws Exception {
        when(orderService.validatePreOrder(1, "2019-12-30 12:30:00.0", 2)).thenReturn(false);
        when(orderService.getGroundPrice(1)).thenReturn(2);
        when(orderService.addPreOrder(any(PreOrder.class))).thenReturn(0);
        mockMvc.perform(post("/order/admin/1/order")
                .param("groundId", "1")
                .param("startTime", "2019-12-30 12:30:00.0")
                .param("duration", "2")
                .param("userNum","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(500));
        verify(orderService).validatePreOrder(1, "2019-12-30 12:30:00.0", 2);
        verify(orderService).getGroundPrice(1);
        verify(orderService).addPreOrder(any(PreOrder.class));
    }

    @Test
    public void delete_order_success() throws Exception {
        when(orderService.deletePreOrder(1)).thenReturn(1);
        mockMvc.perform(delete("/order/1"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).deletePreOrder(1);
    }

    @Test
    public void delete_order_fail() throws Exception {
        when(orderService.deletePreOrder(1)).thenReturn(0);
        mockMvc.perform(delete("/order/1"))
                .andExpect(jsonPath("$.status").value(500));
        verify(orderService).deletePreOrder(1);
    }

    @Test
    public void delete_order_butch_success() throws Exception {
        Map<String, List<Integer>> requestParam = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        requestParam.put("ids", list);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParam);
        when(orderService.deletePreOrder(1)).thenReturn(1);
        mockMvc.perform(delete("/order/deleteByBatch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).deletePreOrder(1);
    }

    @Test
    public void delete_order_butch_fail() throws Exception {
        Map<String, List<Integer>> requestParam = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        requestParam.put("ids", list);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParam);
        when(orderService.deletePreOrder(1)).thenReturn(0);
        mockMvc.perform(delete("/order/deleteByBatch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(jsonPath("$.status").value(500));
        verify(orderService).deletePreOrder(1);
    }

    @Test
    public void get_order_time_success() throws Exception {
        when(orderService.checkGroundExistence(1)).thenReturn(true);
        when(orderService.getOrderTime(1)).thenReturn(new ArrayList<List>());
        mockMvc.perform(get("/order/ground/1/orderTime"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).checkGroundExistence(1);
        verify(orderService).getOrderTime(1);
    }

    @Test
    public void get_order_time_fail() throws Exception {
        when(orderService.checkGroundExistence(1)).thenReturn(false);
        mockMvc.perform(get("/order/ground/1/orderTime"))
                .andExpect(jsonPath("$.status").value(404));
        verify(orderService).checkGroundExistence(1);
    }

    @Test
    public void get_ground_order_success() throws Exception {
        when(orderService.checkGroundExistence(1)).thenReturn(true);
        when(orderService.getGroundOrders(1)).thenReturn(new ArrayList<PreOrder>());
        mockMvc.perform(get("/order/ground/1/order"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).checkGroundExistence(1);
        verify(orderService).getGroundOrders(1);
    }

    @Test
    public void get_ground_order_fail() throws Exception {
        when(orderService.checkGroundExistence(1)).thenReturn(false);
        mockMvc.perform(get("/order/ground/1/order"))
                .andExpect(jsonPath("$.status").value(404));
        verify(orderService).checkGroundExistence(1);
    }

    @Test
    public void get_unchecked_order_success() throws Exception {
        when(orderService.getUncheckedOrders()).thenReturn(new ArrayList<PreOrder>());
        mockMvc.perform(get("/order/uncheckedOrder"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).getUncheckedOrders();
    }

    @Test
    public void check_order_success() throws Exception {
        when(orderService.checkPreOrderExistence(1)).thenReturn(true);
        when(orderService.checkPreOrder(1)).thenReturn(1);
        mockMvc.perform(put("/order/check/1"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).checkPreOrderExistence(1);
        verify(orderService).checkPreOrder(1);
    }

    @Test
    public void check_order_fail() throws Exception {
        when(orderService.checkPreOrderExistence(1)).thenReturn(false);
        mockMvc.perform(put("/order/check/1"))
                .andExpect(jsonPath("$.status").value(404));
        verify(orderService).checkPreOrderExistence(1);
    }

    @Test
    public void uncheck_order_success() throws Exception {
        when(orderService.checkPreOrderExistence(1)).thenReturn(true);
        when(orderService.checkPreOrderFail(1)).thenReturn(1);
        mockMvc.perform(put("/order/uncheck/1"))
                .andExpect(jsonPath("$.status").value(200));
        verify(orderService).checkPreOrderExistence(1);
        verify(orderService).checkPreOrderFail(1);
    }

    @Test
    public void uncheck_order_fail() throws Exception {
        when(orderService.checkPreOrderExistence(1)).thenReturn(false);
        mockMvc.perform(put("/order/uncheck/1"))
                .andExpect(jsonPath("$.status").value(404));
        verify(orderService).checkPreOrderExistence(1);
    }

    @ParameterizedTest
    @MethodSource("requestParamsProviderHappyPath")
    public void happy_path_when_get_order_by_match(Map<String, String> requestParams) throws Exception {
        List<PreOrder> retOrders = new ArrayList<>();
        retOrders.add(new PreOrder(1, 1));
        retOrders.add(new PreOrder(1, 2));
        PreOrder preOrder = new PreOrder(2, 1);
        when(orderService.getOrders()).thenReturn(retOrders);
        when(orderService.getGroundOrders(anyInt())).thenReturn(retOrders);
        when(orderService.getAllPreOrdersOfUser(anyInt())).thenReturn(retOrders);
        when(orderService.getPreOrderByTimeMatch(anyString())).thenReturn(retOrders);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(post("/order/match")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    static Stream<Map<String, String>> requestParamsProviderHappyPath() {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("match", "");
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("match", "gid:1");
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("match", "uid:1");
        HashMap<String, String> map4 = new HashMap<>();
        map4.put("match", "time:2019-12-31");

        return Stream.of(map1, map2, map3, map4);
    }

    @ParameterizedTest
    @MethodSource("requestParamsProviderCornerCase")
    public void corner_case_when_get_order_by_match(Map<String, String> requestParams) throws Exception {
        when(orderService.getOrders()).thenReturn(new ArrayList<>());
        when(orderService.getGroundOrders(anyInt())).thenReturn(new ArrayList<>());
        when(orderService.getAllPreOrdersOfUser(anyInt())).thenReturn(new ArrayList<>());
        when(orderService.getPreOrderByTimeMatch(anyString())).thenReturn(new ArrayList<>());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(post("/order/match")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    static Stream<Map<String, String>> requestParamsProviderCornerCase() {

        HashMap<String, String> map1 = new HashMap<>();
        map1.put("match", "wrong");
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("match", "gid:45642");
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("match", "uid:1234563");
        HashMap<String, String> map4 = new HashMap<>();
        map4.put("match", "time: 1111-12-31");


        return Stream.of(map1, map2, map3, map4);
    }
}