package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import xyz.st.meethere.entity.News;

import java.util.List;

@Repository
public interface NewsMapper {
    @Insert("insert into news(title,date,content) values(#{title},#{date},#{content})")
    News addNews(News news);

    @Update("update news set title=#{title},date=#{date},content=#{content} where newsId=#{newsId}")
    int updateNews(News news);

    @Delete("delete from news where newsId=#{newsId}")
    int deleteNews(Integer newsId);

    @Select("select * from news")
    List<News> getAllNews();

    @Select("select * from news where newsId=#{newsId}")
    News getNewsByNewsId(Integer newsId);
}
