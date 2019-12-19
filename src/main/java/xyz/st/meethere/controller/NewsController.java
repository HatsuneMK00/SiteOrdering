package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("获取所有新闻")
    @GetMapping("/news")
    List<News> getAllNews() {
        return newsService.getAllNews();
    }

    @ApiOperation("添加一条新闻")
    @PostMapping("/news")
    News addNews(@RequestBody News news){
        int result = newsService.addNews(news);
        if (result == 1)
            return news;
        else
            return null;
    }

    @ApiOperation(value = "更改现有的新闻", notes = "需要在json中填写newsId")
    @PutMapping("/news")
    News updateNews(@RequestBody News news){
        int result = newsService.updateNews(news);
        if (result == 1)
            return news;
        else
            return null;
    }

    @ApiOperation("删除一个新闻")
    @DeleteMapping("/news/{newsId}")
    int deleteNews(@PathVariable("newsId") Integer id){
        int result = newsService.deleteNews(id);
        if (result == 1)
            return 200;
        else
            return 500;
    }
}
