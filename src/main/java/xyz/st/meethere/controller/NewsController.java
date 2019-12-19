package xyz.st.meethere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.st.meethere.entity.News;
import xyz.st.meethere.service.NewsService;

import java.util.List;

@RestController
@ResponseBody
public class NewsController {

    @Autowired
    NewsService newsService;

    @GetMapping("/news")
    List<News> getAllNews() {
        return newsService.getAllNews();
    }

    @PostMapping("/news")
    News addNews(@RequestBody News news){
        int result = newsService.addNews(news);
        if (result == 1)
            return news;
        else
            return null;
    }

    @PutMapping("/news")
    News updateNews(@RequestBody News news){
        int result = newsService.updateNews(news);
        if (result == 1)
            return news;
        else
            return null;
    }

    @DeleteMapping("/news/{newsId}")
    int deleteNews(@PathVariable("newsId") Integer id){
        int result = newsService.deleteNews(id);
        if (result == 1)
            return 200;
        else
            return 500;
    }
}
