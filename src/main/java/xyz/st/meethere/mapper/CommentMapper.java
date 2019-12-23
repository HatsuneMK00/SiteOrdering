package xyz.st.meethere.mapper;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;
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

    @Options(useGeneratedKeys = true,keyProperty = "commentId")
    @Insert("insert into comment(userId,groundId,date,content,checked) values(#{userId},#{groundId},#{date},#{content},#{checked})")
    int addCommentOfUserIdOnGroundId(Comment comment);

    @Update("update comment set checked=1 where commentId=#{id}")
    int updateCommentSetChecked(Integer id);

    @Update("update comment set checked=0 where commentId=#{id}")
    int updateCommentSetUnchecked(Integer id);

    /*
    * 管理员操作
    * */
    @Select("select * from comment where checked=0")
    List<Comment> getAllUncheckedComments();

    @Select("select * from comment where checked=1")
    List<Comment> getAllCheckedComments();
}
