package xyz.st.meethere.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.st.meethere.entity.News;
import xyz.st.meethere.service.NewsService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class NewsControllerTest {
    private NewsService newsService;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        newsService = mock(NewsService.class);
        System.out.println("1");
        this.mockMvc = MockMvcBuilders.standaloneSetup(new NewsController()).build();
    }

    @Test
    public void happy_path_when_get_news_by_newsId() throws Exception {
        int id = 1;
        System.out.println("2");
        when(newsService.getNewsByNewsId(id)).thenReturn(new News("testNews", "this is a test news", null, 1));
        mockMvc.perform(get("/news/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsId").value(1))
                .andExpect(jsonPath("$.content").value("this is a test news"))
                .andExpect(jsonPath("$.title").value("testNews"))
                .andExpect(jsonPath("$.status").value(200));
    }
}