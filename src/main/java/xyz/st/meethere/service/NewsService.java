package xyz.st.meethere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.st.meethere.entity.News;
import xyz.st.meethere.mapper.NewsMapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    NewsMapper newsMapper;

    public List<News> getAllNews() {
        return newsMapper.getAllNews();
    }

    public int addNews(News news) {
        setNewsDate(news);
        return newsMapper.addNews(news);
    }

    public int updateNews(News news) {
        setNewsDate(news);
        return newsMapper.updateNews(news);
    }

    public int deleteNews(Integer id) {
        return newsMapper.deleteNews(id);
    }

    private void setNewsDate(News news) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        news.setDate(Timestamp.valueOf(simpleDateFormat.format(new Date())));
    }

    public News getNewsByNewsId(Integer id) {
        return newsMapper.getNewsByNewsId(id);
    }

    public boolean hasAllRequiredContent(News news) {
        return news.getContent() != null && news.getTitle() != null && !news.getContent().equals("") && !news.getTitle().equals("");
    }
}
