package xyz.st.meethere.controller;

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
import xyz.st.meethere.entity.News;
import xyz.st.meethere.service.NewsService;

import java.util.ArrayList;
import java.util.Arrays;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(new NewsController(newsService)).build();
    }

    @Test
    public void happy_path_when_get_news_by_newsId() throws Exception {
        int id = 1;
//        System.out.println(newsService.getClass());
        when(newsService.getNewsByNewsId(1)).thenReturn(new News("testNews", "this is a test news", null, 1));
        mockMvc.perform(get("/news/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMap.result.newsId").value(1))
                .andExpect(jsonPath("$.responseMap.result.content").value("this is a test news"))
                .andExpect(jsonPath("$.responseMap.result.title").value("testNews"))
                .andExpect(jsonPath("$.status").value(200));
        verify(newsService).getNewsByNewsId(1);
    }

    @Test
    public void should_return_404_when_news_doesnt_exist() throws Exception {
        when(newsService.getNewsByNewsId(1)).thenReturn(null);
        mockMvc.perform(get("/news/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(newsService).getNewsByNewsId(anyInt());
    }

    @Test
    public void happy_path_when_delete_a_news() throws Exception {
        when(newsService.deleteNews(1)).thenReturn(1);
        when(newsService.getNewsByNewsId(1)).thenReturn(new News("testNews", "this is a test news", null, 1));
        mockMvc.perform(delete("/news/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result.newsId").value(1))
                .andExpect(jsonPath("$.responseMap.result.content").value("this is a test news"))
                .andExpect(jsonPath("$.responseMap.result.title").value("testNews"));
        InOrder order = inOrder(newsService);
        order.verify(newsService).getNewsByNewsId(1);
        order.verify(newsService).deleteNews(1);
    }

    @Test
    public void news_doesnt_exist_when_delete_a_news() throws Exception {
        when(newsService.getNewsByNewsId(2)).thenReturn(null);
        when(newsService.deleteNews(2)).thenReturn(0);
        mockMvc.perform(delete("/news/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        InOrder order = inOrder(newsService);
        order.verify(newsService).getNewsByNewsId(2);
        order.verify(newsService).deleteNews(2);
    }

    @Test
    public void happy_path_when_update_a_news() throws Exception {
        News news = new News("test news for update",
                "this is a news for update test",
                null,
                1);
//        这边我觉得是我没有为news重载equals函数，导致如果写一个具体的对象的话
//        永远都匹配不上，这个打桩就没有用了
//        这边使用参数匹配器语法
        when(newsService.updateNews(any(News.class))).thenReturn(1);

        /*
        * 封装一个json请求体
        * */
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(news);
        mockMvc.perform(put("/news")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMap.result.title").value("test news for update"))
                .andExpect(jsonPath("$.responseMap.result.content")
                        .value("this is a news for update test"))
                .andExpect(jsonPath("$.responseMap.result.newsId").value(1))
                .andExpect(jsonPath("$.status").value(200));
        ArgumentCaptor<News> newsArgumentCaptor = ArgumentCaptor.forClass(News.class);
        verify(newsService).updateNews(newsArgumentCaptor.capture());
        News actualArgNews = newsArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(news.getTitle(), actualArgNews.getTitle()),
                () -> assertEquals(news.getContent(), actualArgNews.getContent()),
                () -> assertEquals(news.getNewsId(), actualArgNews.getNewsId())
        );
    }

    @Test
    public void news_doesnt_exist_when_update_a_news() throws Exception {
        /*
        * 当news不存在时，逻辑上会返回传过来的参数给前端用于查看
        * 这里就不考虑回传的news了
        * */
        when(newsService.updateNews(any(News.class))).thenReturn(0);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new News());
        mockMvc.perform(put("/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(500));
        verify(newsService).updateNews(any(News.class));
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void happy_path_when_add_a_news() throws Exception {
        when(newsService.addNews(any(News.class))).thenReturn(1);
        when(newsService.hasAllRequiredContent(any())).thenReturn(true);
        News news = new News("test news for update",
                "this is a news for update test",
                null,
                1);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(news);
        mockMvc.perform(post("/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
        ArgumentCaptor<News> newsArgumentCaptor = ArgumentCaptor.forClass(News.class);
        verify(newsService).addNews(newsArgumentCaptor.capture());
        News actualArgNews = newsArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(news.getTitle(), actualArgNews.getTitle()),
                () -> assertEquals(news.getContent(), actualArgNews.getContent()),
                () -> assertEquals(news.getNewsId(), actualArgNews.getNewsId())
        );
    }

    @Test
    public void fail_when_add_a_news() throws Exception {
        when(newsService.addNews(any(News.class))).thenReturn(0);
        when(newsService.hasAllRequiredContent(any())).thenReturn(true);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new News());
        mockMvc.perform(post("/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(500));

        verify(newsService).addNews(any(News.class));
    }

    @Test
    public void params_not_enough_when_add_a_news() throws Exception{
        when(newsService.hasAllRequiredContent(any())).thenReturn(false);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new News());
        mockMvc.perform(post("/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400));
        verify(newsService).hasAllRequiredContent(any());
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void happy_path_when_get_all_news() throws Exception {
        List<News> stubNewsResult = new ArrayList<>();
        stubNewsResult.add(new News("test news for update",
                "this is a news for update test",
                null,
                1));
        stubNewsResult.add(new News("test news for update",
                "this is another news for update test",
                null,
                2));
        when(newsService.getAllNews()).thenReturn(stubNewsResult);
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result[0].newsId").value(1))
                .andExpect(jsonPath("$.responseMap.result[1].newsId").value(2));
        verify(newsService).getAllNews();
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void get_null_when_get_all_news() throws Exception {
        when(newsService.getAllNews()).thenReturn(null);
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(newsService).getAllNews();
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void get_nothing_when_get_all_news() throws Exception {
        when(newsService.getAllNews()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result").exists());
        verify(newsService).getAllNews();
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void happy_path_when_delete_news_by_batch() throws Exception {
        when(newsService.deleteNews(anyInt())).thenReturn(1);
        HashMap<String, List<Integer>> requestParams = new HashMap<>();
        requestParams.put("ids", Arrays.asList(1, 2, 3));

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(delete("/news/deleteByBatch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
        verify(newsService, times(3)).deleteNews(anyInt());
    }

    @Test
    public void fail_once_when_delete_news_by_batch() throws Exception {
        when(newsService.deleteNews(anyInt())).thenReturn(1).thenReturn(0);
        HashMap<String, List<Integer>> requestParams = new HashMap<>();
        requestParams.put("ids", Arrays.asList(1, 2, 3));

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(delete("/news/deleteByBatch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(newsService, times(3)).deleteNews(anyInt());
    }
}