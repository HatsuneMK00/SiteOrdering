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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.st.meethere.entity.Ground;
import xyz.st.meethere.entity.News;
import xyz.st.meethere.service.FileService;
import xyz.st.meethere.service.GroundService;
import xyz.st.meethere.service.NewsService;

import java.util.ArrayList;
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
        mockMvc = MockMvcBuilders.standaloneSetup(new GroundController(groundService,fileService)).build();
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
                .andExpect(jsonPath("$.resonseMap.result").isArray())
                .andExpect(jsonPath("$.responseMap.result[0].groundId").value(1))
                .andExpect(jsonPath("$.responseMap.result[1].groundId").value(2));
        verify(groundService).getAllGrounds();
        verifyNoMoreInteractions();
    }

    @Test
    public void get_null_when_get_all_grounds(){
        when(groundService.getAllGrounds()).thenReturn(null);
    }

    @Test
    public void get_nothing_when_get_all_grounds(){

    }

}