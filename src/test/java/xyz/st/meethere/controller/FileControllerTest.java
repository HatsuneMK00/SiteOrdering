package xyz.st.meethere.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.st.meethere.exception.FileException;
import xyz.st.meethere.service.FileService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FileControllerTest {

    private FileService fileService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        fileService = mock(FileService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new FileController(fileService)).build();
    }

    @Test
    public void happy_path_when_update_profile_pic() throws Exception {
        when(fileService.storeFile(any())).thenReturn("filename");

        mockMvc.perform(multipart("/file/uploadImage")
                .file(new MockMultipartFile("image", "image.png", "image/png", "this is image".getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.url").exists());
    }

    @Test
    public void fail_to_save_file_when_update_profile_pic() throws Exception {
        when(fileService.storeFile(any())).thenReturn(null);

        mockMvc.perform(multipart("/file/uploadImage")
                .file(new MockMultipartFile("image", "image.png", "image/png", "this is image".getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    public void file_exception_happened_when_update_profile_pic() throws Exception {
        when(fileService.storeFile(any())).thenThrow(new FileException("file exception"));

        mockMvc.perform(multipart("/file/uploadImage")
                .file(new MockMultipartFile("image", "image.png", "image/png", "this is image".getBytes())));
    }
}