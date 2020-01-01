package xyz.st.meethere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.st.meethere.entity.Comment;
import xyz.st.meethere.mapper.CommentMapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    public List<Comment> getAllUncheckComments(){
        return commentMapper.getAllUncheckedComments();
    }

    public List<Comment> getAllCheckComments(){
        return commentMapper.getAllCheckedComments();
    }

    public List<Comment> getAllComments() {
        return commentMapper.getAllComments();
    }
    
    public List<Comment> getCommentsByMatch(String matchParam){
        return commentMapper.getCommentByContentMatch(matchParam);
    }

    public Comment getCommentByCommentId(Integer id){
        return commentMapper.getCommentsByCommentId(id);
    }

    public List<Comment> getCommentsByGroundId(Integer id){
        return commentMapper.getCommentsByGroundId(id);
    }

    public List<Comment> getCheckedCommentsByGroundId(Integer id){
        return commentMapper.getCheckedCommentsByGroundId(id);
    }

    public List<Comment> getCommentsByUserId(Integer id){
        return commentMapper.getCommentsByUserId(id);
    }

    public int addComment(Comment comment){
        setCommentDate(comment);
//        所有留言都需要经过审核
        comment.setChecked(0);
        return commentMapper.addCommentOfUserIdOnGroundId(comment);
    }

    public int updateComment(Comment comment){
        setCommentDate(comment);
        comment.setChecked(0);
        return commentMapper.updateComment(comment);
    }

    public int deleteComment(Integer commentId){
        return commentMapper.deleteComment(commentId);
    }

    public int checkComment(Integer commentId){
        return commentMapper.updateCommentSetChecked(commentId);
    }

    public int uncheckComment(Integer commentId){
        return commentMapper.updateCommentSetUnchecked(commentId);
    }

    private void setCommentDate(Comment comment) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        comment.setDate(Timestamp.valueOf(simpleDateFormat.format(new Date())));
    }

}
