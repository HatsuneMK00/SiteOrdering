package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import xyz.st.meethere.entity.News;

import java.util.List;

@Repository
public interface NewsMapper {
    @Options(useGeneratedKeys = true,keyProperty = "newsId")
    @Insert("insert into news(title,date,content) values(#{title},#{date},#{content})")
    int addNews(News news);

    @Update("update news set title=#{title},date=#{date},content=#{content} where newsId=#{newsId}")
    int updateNews(News news);

    @Delete("delete from news where newsId=#{newsId}")
    int deleteNews(Integer newsId);

    @Select("select * from news")
    List<News> getAllNews();

    @Select("select * from news where newsId=#{newsId}")
    News getNewsByNewsId(Integer newsId);
}
