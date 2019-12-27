package xyz.st.meethere.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import xyz.st.meethere.exception.FileException;
import xyz.st.meethere.service.FileService;
import xyz.st.meethere.service.GroundService;
import xyz.st.meethere.service.NewsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

class GroundControllerTest {

    private GroundService groundService;
    private FileService fileService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        groundService = mock(GroundService.class);
        fileService = mock(FileService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new GroundController(groundService, fileService)).build();
    }

    @Test
    public void happy_path_when_get_all_grounds() throws Exception {
        List<Ground> retGrounds = new ArrayList<>();
        retGrounds.add(new Ground(
                "ground 1",
                1,
                null,
                10,
                "ground address",
                "this is a test ground"
        ));
        retGrounds.add(new Ground(
                "ground 2",
                2,
                null,
                10,
                "ground address",
                "this is another test ground"
        ));
        when(groundService.getAllGrounds()).thenReturn(retGrounds);
        mockMvc.perform(get("/ground"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result").exists())
                .andExpect(jsonPath("$.responseMap.result[0].groundId").value(1))
                .andExpect(jsonPath("$.responseMap.result[1].groundId").value(2));
        verify(groundService).getAllGrounds();
        verifyNoMoreInteractions(groundService);
    }

    @Test
    public void get_null_when_get_all_grounds() throws Exception {
        when(groundService.getAllGrounds()).thenReturn(null);

        mockMvc.perform(get("/ground"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(groundService).getAllGrounds();
    }

    @Test
    public void get_nothing_when_get_all_grounds() throws Exception {
        when(groundService.getAllGrounds()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/ground"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
        verify(groundService).getAllGrounds();
    }

    @Test
    public void happy_path_when_get_ground_by_groundId() throws Exception {
        Ground ground = new Ground(
                "ground 1",
                1,
                null,
                10,
                "ground address",
                "this is a test ground"
        );
        when(groundService.getGroundById(anyInt())).thenReturn(ground);
        mockMvc.perform(get("/ground/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMap.result").exists());
        verify(groundService).getGroundById(1);
    }

    @Test
    public void ground_not_exist_when_get_ground_by_groundId() throws Exception {
        when(groundService.getGroundById(1)).thenReturn(null);

        mockMvc.perform(get("/ground/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.responseMap.result").doesNotExist());
        verify(groundService).getGroundById(1);
    }

    @Test
    public void happy_path_when_add_a_ground() throws Exception {
        Ground ground = new Ground(
                "ground 1",
                1,
                null,
                10,
                "ground address",
                "this is a test ground"
        );
        when(groundService.verifyGround(any(Ground.class))).thenReturn(false);
        when(fileService.storeFile(any())).thenReturn("filename");
        when(groundService.addGround(any(Ground.class))).thenReturn(1);

        mockMvc.perform(multipart("/ground")
                .file(new MockMultipartFile("image", "image.png", "image/png", "this is image".getBytes()))
                .param("groundName", "test ground")
                .param("pricePerHour", "10")
                .param("address", "the address")
                .param("description", "the description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result").exists());
        InOrder order = inOrder(groundService, fileService);
        order.verify(groundService).verifyGround(any());
        order.verify(fileService).storeFile(any());
        order.verify(groundService).addGround(any());
    }

    @Test
    public void no_image_when_add_a_ground() throws Exception {
        mockMvc.perform(multipart("/ground")
                .param("groundName", "test ground")
                .param("pricePerHour", "10")
                .param("address", "the address")
                .param("description", "the description"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void ground_param_not_verified_when_add_a_ground() throws Exception {
        Ground ground = new Ground(
                "ground 1",
                1,
                null,
                10,
                "ground address",
                "this is a test ground"
        );
        when(groundService.verifyGround(any(Ground.class))).thenReturn(true);

        mockMvc.perform(multipart("/ground")
                .file(new MockMultipartFile("image", "image.png", "image/png", "this is image".getBytes()))
                .param("groundName", "test ground")
                .param("pricePerHour", "10")
                .param("address", "the address")
                .param("description", "the description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(500));
        verify(groundService).verifyGround(any());
        verifyNoMoreInteractions(groundService);
    }

    @Test
    public void happy_path_when_update_a_ground() throws Exception {
        when(groundService.verifyGround(any())).thenReturn(false);
        when(groundService.updateGround(any())).thenReturn(1);

        Ground ground = new Ground(
                "ground 1",
                1,
                null,
                10,
                "ground address",
                "this is a test ground"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(ground);

        mockMvc.perform(put("/ground")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result").exists());
        InOrder order = inOrder(groundService);
        order.verify(groundService).verifyGround(any());
        order.verify(groundService).updateGround(any());
    }

    @Test
    public void ground_not_exist_when_update_a_ground() throws Exception {
        when(groundService.updateGround(any())).thenReturn(0);
        when(groundService.verifyGround(any())).thenReturn(false);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new Ground());

        mockMvc.perform(put("/ground")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        InOrder order = inOrder(groundService);
        order.verify(groundService).verifyGround(any());
        order.verify(groundService).updateGround(any());
    }

    @Test
    public void ground_param_not_verified_when_update_a_ground() throws Exception {
        when(groundService.verifyGround(any())).thenReturn(true);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new Ground());

        mockMvc.perform(put("/ground")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(500));
        verify(groundService).verifyGround(any());
        verifyNoMoreInteractions(groundService);
    }

    @Test
    public void happy_path_when_delete_a_ground() throws Exception {
        when(groundService.deleteGround(1)).thenReturn(1);
        Ground ground = new Ground(
                "ground 1",
                1,
                null,
                10,
                "ground address",
                "this is a test ground"
        );
        when(groundService.getGroundById(1)).thenReturn(ground);
        mockMvc.perform(delete("/ground/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result").exists());
        InOrder order = inOrder(groundService);
        order.verify(groundService).getGroundById(1);
        order.verify(groundService).deleteGround(1);
    }

    @Test
    public void ground_not_exist_when_delete_a_ground() throws Exception {
        when(groundService.deleteGround(1)).thenReturn(0);

        mockMvc.perform(delete("/ground/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));

        verify(groundService).deleteGround(1);
    }
}