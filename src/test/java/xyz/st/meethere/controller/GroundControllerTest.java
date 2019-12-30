package xyz.st.meethere.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InOrder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.st.meethere.entity.Ground;
import xyz.st.meethere.exception.FileException;
import xyz.st.meethere.service.FileService;
import xyz.st.meethere.service.GroundService;

import java.util.*;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void image_save_fail_when_add_a_ground() throws Exception {
        when(groundService.verifyGround(any())).thenReturn(false);
        when(fileService.storeFile(any())).thenThrow(new FileException("file exception"));

        mockMvc.perform(multipart("/ground")
                .file(new MockMultipartFile("image", "image.png", "image/png", "this is image".getBytes()))
                .param("groundName", "test ground")
                .param("pricePerHour", "10")
                .param("address", "the address")
                .param("description", "the description"));
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

    @Test
    public void happy_path_when_add_ground_file_operation() throws Exception {
        when(groundService.verifyGround(any(Ground.class))).thenReturn(false);
        when(groundService.addGroundWOFileOperation(any(Ground.class))).thenReturn(1);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new Ground());

        mockMvc.perform(post("/groundWOFileOperation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
        InOrder order = inOrder(groundService);
        order.verify(groundService).verifyGround(any());
        order.verify(groundService).addGroundWOFileOperation(any());
    }

    @Test
    public void ground_not_valid_when_add_ground_file_operation() throws Exception {
        when(groundService.verifyGround(any(Ground.class))).thenReturn(true);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new Ground());

        mockMvc.perform(post("/groundWOFileOperation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(500));
        verify(groundService).verifyGround(any());
    }

    @Test
    public void add_ground_file_operation_fail_when_add_ground_file_operation() throws Exception {
        when(groundService.verifyGround(any())).thenReturn(false);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new Ground());

        mockMvc.perform(post("/groundWOFileOperation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(500));
        InOrder order = inOrder(groundService);
        order.verify(groundService).verifyGround(any());
        order.verify(groundService).addGroundWOFileOperation(any());
    }

    @Test
    public void happy_path_when_delete_ground_by_batch() throws Exception {
//        when(groundService.getCommentByCommentId(anyInt())).thenReturn(null);
        when(groundService.deleteGround(anyInt())).thenReturn(1);

        HashMap<String, List<Integer>> requestParams = new HashMap<>();
        requestParams.put("ids", Arrays.asList(1, 2, 3));

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(delete("/ground/deleteByBatch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
        verify(groundService, times(3)).deleteGround(anyInt());
    }

    @Test
    public void fail_once_when_delete_ground_by_batch() throws Exception {
        when(groundService.deleteGround(anyInt())).thenReturn(1).thenReturn(0);
        HashMap<String, List<Integer>> requestParams = new HashMap<>();
        requestParams.put("ids", Arrays.asList(1, 2, 3));

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(delete("/ground/deleteByBatch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(groundService, times(3)).deleteGround(anyInt());
    }

    @ParameterizedTest
    @MethodSource("requestParamsProviderHappyPath")
    public void happy_path_when_get_ground_by_match(Map<String, String> requestParams) throws Exception {
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
        Ground ground = new Ground(
                "ground 1",
                1,
                null,
                10,
                "ground address",
                "this is a test ground"
        );
        when(groundService.getGroundById(anyInt())).thenReturn(ground);
        when(groundService.getGroundsByMatch(anyString())).thenReturn(retGrounds);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(post("/ground/match")
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
        map3.put("match", "hello");

        return Stream.of(map1, map2, map3);
    }

    @ParameterizedTest
    @MethodSource("requestParamsProviderCornerCase")
    public void corner_case_when_get_ground_by_match(Map<String, String> requestParams) throws Exception {
        when(groundService.getGroundById(anyInt())).thenReturn(null);
        when(groundService.getAllGrounds()).thenReturn(null);
        when(groundService.getGroundsByMatch(anyString())).thenReturn(new ArrayList<>());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(post("/ground/match")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    static Stream<Map<String, String>> requestParamsProviderCornerCase() {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("match", "");
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("match", "gid:6578");
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("match", "null");

        return Stream.of(map1, map2, map3);
    }
}