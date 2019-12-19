package xyz.st.meethere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.st.meethere.entity.News;
import xyz.st.meethere.mapper.NewsMapper;

import java.sql.Timestamp;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    NewsMapper newsMapper;

    public List<News> getAllNews(){
        return newsMapper.getAllNews();
    }

    public int addNews(News news) {
        /*
        * 这里需要加上封装时间的逻辑
        * */
        return newsMapper.addNews(news);
    }

    public int updateNews(News news){
        /*
        * 这里需要加上封装时间的逻辑
        * */
        return newsMapper.updateNews(news);
    }

    public int deleteNews(Integer id){
        return newsMapper.deleteNews(id);
    }
}
