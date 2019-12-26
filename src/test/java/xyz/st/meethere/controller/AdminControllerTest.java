package xyz.st.meethere.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.st.meethere.entity.Comment;
import xyz.st.meethere.entity.Ground;
import xyz.st.meethere.entity.News;
import xyz.st.meethere.entity.User;
import xyz.st.meethere.exception.FileException;
import xyz.st.meethere.service.AdminService;
import xyz.st.meethere.service.FileService;
import xyz.st.meethere.service.GroundService;
import xyz.st.meethere.service.NewsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    private AdminService adminService;
    private FileService fileService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        adminService = mock(AdminService.class);
        fileService = mock(FileService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminController(adminService, fileService)).build();
    }

//    @Test
//    public void happy_path_when_get_admin_by_name(){
//
//    }
//
//    @Test
//    public void admin_not_exist_when_get_admin_by_name() {
//
//    }

    @Test
    public void happy_path_when_get_admin_by_id() throws Exception {
        User admin = new User(1, "user1", "123", null, null, null, 100, true);
        when(adminService.getAdminById(1)).thenReturn(admin);
        mockMvc.perform(get("/admin/getById")
                .param("userId","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result").isNotEmpty());
        verify(adminService).getAdminById(1);
    }

    @Test
    public void admin_not_exist_when_get_admin_by_id() throws Exception {
        when(adminService.getAdminById(1)).thenReturn(null);
        mockMvc.perform(get("/admin/getById")
                .param("userId","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(adminService).getAdminById(1);
    }

    @Test
    public void happy_path_when_login_admin() throws Exception {
        when(adminService.checkAdminPassword("user", "123")).thenReturn(true);
        mockMvc.perform(get("/admin/enter")
                .param("userName","user")
                .param("password","123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
        verify(adminService).checkAdminPassword("user", "123");
    }

    @Test
    public void pwd_wrong_when_login_admin() throws Exception {
        when(adminService.checkAdminPassword("user1", "123")).thenReturn(false);
        mockMvc.perform(get("/admin/enter")
                .param("userName","user1")
                .param("password","123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(adminService).checkAdminPassword("user1", "123");
    }

    @Test
    public void happy_path_when_update_admin_info_by_id() throws Exception {
        User admin = new User(1, "user1", "123", null, null, null, 100, true);
        when(adminService.getAdminById(1)).thenReturn(admin);
        when(adminService.updateAdminByModel(admin)).thenReturn(200);

        Map<String, Integer> requestParam = new HashMap<>();
        requestParam.put("userId",1);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParam);

        mockMvc.perform(post("/admin/updateById")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.user").isNotEmpty());
        InOrder order = inOrder(adminService);
        order.verify(adminService).getAdminById(1);
        order.verify(adminService).updateAdminByModel(any());
    }

    @Test
    public void no_id_passed_when_update_admin_info_by_id() throws Exception {
        mockMvc.perform(post("/admin/updateById"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400));
        verifyNoInteractions(adminService);
    }

    @Test
    public void admin_not_exist_when_update_admin_info_by_id() throws Exception {
        when(adminService.getAdminById(1)).thenReturn(null);

        Map<String, Integer> requestParam = new HashMap<>();
        requestParam.put("userId",1);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParam);

        mockMvc.perform(post("/admin/updateById")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(adminService).getAdminById(1);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    public void happy_path_when_update_admin_profile_pic() throws Exception {
        when(fileService.storeFile(any())).thenReturn("filename");
        when(adminService.updateAdminProfilePicByAdminId(anyString(),anyInt())).thenReturn(200);

        mockMvc.perform(multipart("/admin/profilePic")
                .file(new MockMultipartFile("image", "image.png", "image/png", "this is image".getBytes()))
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.user").exists());
        InOrder order = inOrder(adminService, fileService);
        order.verify(fileService).storeFile(any());
        order.verify(adminService).updateAdminProfilePicByAdminId(anyString(), anyInt());
        order.verify(adminService).getAdminById(1);
    }

}