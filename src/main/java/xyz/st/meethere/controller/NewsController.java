package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import xyz.st.meethere.entity.News;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.service.NewsService;

import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
public class NewsController {

    final
    NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @ApiOperation("获取所有新闻")
    @GetMapping("/news")
    ResponseMsg getAllNews() {
        ResponseMsg responseMsg = new ResponseMsg();
        List<News> news = newsService.getAllNews();
//        FIXME: 当返回null时为404，返回空时为200
        if (news == null)
            responseMsg.setStatus(404);
        else {
            responseMsg.setStatus(200);
            responseMsg.getResponseMap().put("result", news);
        }
        return responseMsg;
    }

    @ApiOperation("添加一条新闻")
    @PostMapping("/news")
    ResponseMsg addNews(@RequestBody News news) {
        ResponseMsg responseMsg = new ResponseMsg();
        if (!newsService.hasAllRequiredContent(news)) {
            responseMsg.setStatus(400);
            return responseMsg;
        }
        int result = newsService.addNews(news);

        if (result == 1) {
            responseMsg.setStatus(200);
        } else {
            responseMsg.setStatus(500);
        }
        responseMsg.getResponseMap().put("result", news);
        return responseMsg;
    }

    @ApiOperation(value = "更改现有的新闻", notes = "需要在json中填写newsId")
    @PutMapping("/news")
    ResponseMsg updateNews(@RequestBody News news) {
        int result = newsService.updateNews(news);
        ResponseMsg responseMsg = new ResponseMsg();
        if (result == 1)
            responseMsg.setStatus(200);
        else
            responseMsg.setStatus(500);
        responseMsg.getResponseMap().put("result", news);
        return responseMsg;
    }

    @ApiOperation("删除一个新闻")
    @DeleteMapping("/news/{newsId}")
    ResponseMsg deleteNews(@PathVariable("newsId") Integer id) {
        News news = newsService.getNewsByNewsId(id);
        int result = newsService.deleteNews(id);
        ResponseMsg responseMsg = new ResponseMsg();
        if (result == 1) {
            responseMsg.setStatus(200);
        } else {
            responseMsg.setStatus(404);
        }
        responseMsg.getResponseMap().put("result", news);
        return responseMsg;
    }

    @ResponseBody
    @ApiOperation("通过newsId批量删除新闻")
    @DeleteMapping("/news/deleteByBatch")
    ResponseMsg deleteNewsByBatch(@RequestBody Map<String,List<Integer>> data) {
        ResponseMsg msg = new ResponseMsg();
        List<Integer> ids = data.get("ids");
        msg.setStatus(200);
        ResponseMsg tempMsg;
        for (Integer id : ids) {
            tempMsg = deleteNews(id);
            if (tempMsg.getStatus() == 404 && msg.getStatus() != 404){
                msg.setStatus(404);
            }
        }
        return msg;
    }

    @ApiOperation("根据newsId获取一个新闻")
    @GetMapping("/news/{newsId}")
    ResponseMsg getNewsByNewsId(@PathVariable("newsId") Integer id) {
        News news = newsService.getNewsByNewsId(id);
        ResponseMsg responseMsg = new ResponseMsg();
        if (news != null) {
            responseMsg.setStatus(200);
            responseMsg.getResponseMap().put("result", news);
        } else {
            responseMsg.setStatus(404);
        }
        return responseMsg;
    }
}
