package xyz.st.meethere.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.st.meethere.entity.News;
import xyz.st.meethere.service.NewsService;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
/*
* 这边不能用SpringBootTest 因为代码中使用了ApplicationHome 而这个东西我觉得不把服务器跑起来是获取不到的
* */
public class NewsControllerTest {
//    加上这个注解才知道要注入的是什么对象
    @Mock
    private NewsService newsService;
    private MockMvc mockMvc;
//    需要被注入的对象
//    但是并不能注入进去
//    @InjectMocks
//    private NewsController newsController;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        newsService = mock(NewsService.class);
        when(newsService.getNewsByNewsId(1)).thenReturn(new News("testNews", "this is a test news", null, 1));
        this.mockMvc = MockMvcBuilders.standaloneSetup(new NewsController(newsService)).build();
    }

    @Test
    public void happy_path_when_get_news_by_newsId() throws Exception {
        int id = 1;
//        System.out.println(newsService.getClass());
        mockMvc.perform(get("/news/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.responseMap.result.newsId").value(1))
                .andExpect(jsonPath("$.responseMap.result.content").value("this is a test news"))
                .andExpect(jsonPath("$.responseMap.result.title").value("testNews"))
                .andExpect(jsonPath("$.status").value(200));
        verify(newsService).getNewsByNewsId(1);
    }
}