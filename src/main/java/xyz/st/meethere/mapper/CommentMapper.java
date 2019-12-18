package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import xyz.st.meethere.entity.Comment;

import java.util.List;

@Repository
public interface CommentMapper {
    @Select("select * from comment where groundId=#{groundId}")
    List<Comment> getCommentsByGroundId(Integer groundId);

    @Select("select * from comment where userId=#{userId}")
    List<Comment> getCommentsByUserId(Integer userId);

    @Delete("delete from comment where groundId=#{groundId} and userId=#{userId}")
    int deleteCommentOfUserIdOnGroundId(Integer userId, Integer groundId);

    @Update("update comment set date=#{date},content=#{content} where groundId=#{groundId} and userId=#{userId}")
    int updateCommentOfUserIdOnGroundId(Integer userId, Integer groundId);

    @Insert("insert into comment(userId,groundId,date,content) values(#{userId},#{groundId},#{date},#{content})")
    int addCommentOfUserIdOnGroundId(Comment comment);
}
