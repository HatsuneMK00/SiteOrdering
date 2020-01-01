package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import xyz.st.meethere.entity.Comment;

import java.util.List;

@Repository
public interface CommentMapper {
    @Select("select comment.userId, comment.groundId, groundName, userName, commentId, date, content, checked from " +
            "(comment join user on comment.userId=user.userId) join ground on ground.groundId=comment.groundId where " +
            "commentId=#{commentId}")
    Comment getCommentsByCommentId(Integer commentId);

    @Select("select comment.userId, comment.groundId, groundName, userName, commentId, date, content, checked from " +
            "(comment join user on comment.userId=user.userId) JOIN ground on ground.groundId=comment.groundId where " +
            "comment.groundId=#{groundId}")
    List<Comment> getCommentsByGroundId(Integer groundId);

    @Select("select comment.userId, comment.groundId, groundName, userName, commentId, date, content, checked from " +
            "(comment join user on comment.userId=user.userId) JOIN ground on ground.groundId=comment.groundId where " +
            "comment.groundId=#{groundId} and checked=1")
    List<Comment> getCheckedCommentsByGroundId(Integer groundId);

    @Select("select comment.userId, comment.groundId, groundName, userName, commentId, date, content, checked from " +
            "(comment join user on comment.userId=user.userId) JOIN ground on ground.groundId=comment.groundId where " +
            "comment.userId=#{userId}")
    List<Comment> getCommentsByUserId(Integer userId);

    @Select("select comment.userId, comment.groundId, groundName, userName, commentId, date, content, checked from " +
            "(comment join user on comment.userId=user.userId) JOIN ground on ground.groundId=comment.groundId where " +
            "content like CONCAT('%',#{matchParam},'%')")
    List<Comment> getCommentByContentMatch(String matchParam);

    @Delete("delete from comment where commentId=#{commentId}")
    int deleteComment(Integer commentId);

    @Update("update comment set date=#{date},content=#{content},checked=#{checked} where commentId=#{commentId}")
    int updateComment(Comment comment);

    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    @Insert("insert into comment(userId,groundId,date,content,checked) values(#{userId},#{groundId},#{date}," +
            "#{content},#{checked})")
    int addCommentOfUserIdOnGroundId(Comment comment);

    /*
     * 管理员操作
     * -1表示不通过的订单
     * 0表示未审核的订单
     * 1表示通过的订单
     * */
    @Update("update comment set checked=1 where commentId=#{id}")
    int updateCommentSetChecked(Integer id);

    @Update("update comment set checked=-1 where commentId=#{id}")
    int updateCommentSetUnchecked(Integer id);

    @Select("select comment.userId, comment.groundId, groundName, userName, commentId, date, content, checked from " +
            "(comment join user on comment.userId=user.userId) join ground on ground.groundId=comment.groundId " +
            "where checked=0 or checked=-1")
    List<Comment> getAllUncheckedComments();

    @Select("select * from comment where checked=1")
    List<Comment> getAllCheckedComments();

    @Select("select comment.userId, comment.groundId, groundName, userName, commentId, date, content, checked from " +
            "(comment join user on comment.userId=user.userId) join ground on ground.groundId=comment.groundId")
    List<Comment> getAllComments();
}
