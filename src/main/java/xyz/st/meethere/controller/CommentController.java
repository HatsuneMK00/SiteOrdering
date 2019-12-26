package xyz.st.meethere.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import xyz.st.meethere.entity.Comment;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.service.CommentService;

import java.util.List;

@RestController
@ResponseBody
public class CommentController {
    final
    CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    Logger logger = LoggerFactory.getLogger(getClass());

    /*
     * 获取所有审核通过的留言
     * */
    @GetMapping("/comment")
    ResponseMsg getAllComments() {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        try {
            comments = commentService.getAllCheckComments();
            responseMsg.getResponseMap().put("result", comments);
            responseMsg.setStatus(200);
        } catch (Exception e) {
            responseMsg.setStatus(500);
            logger.error(e.getMessage(), e);
        }
        return responseMsg;
    }

    @GetMapping("/comment/user/{userId}")
    ResponseMsg getCommentsByUserId(@PathVariable("userId") Integer id) {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        try {
            comments = commentService.getCommentsByUserId(id);
            responseMsg.getResponseMap().put("result", comments);
            responseMsg.setStatus(200);
        } catch (Exception e) {
            responseMsg.setStatus(500);
            logger.error(e.getMessage(), e);
        }
        return responseMsg;
    }

    @GetMapping("/comment/ground/{groundId}")
    ResponseMsg getCommentsByGroundId(@PathVariable("groundId") Integer id) {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        try {
            comments = commentService.getCommentsByGroundId(id);
            responseMsg.getResponseMap().put("result", comments);
            responseMsg.setStatus(200);
        } catch (Exception e) {
            responseMsg.setStatus(500);
            logger.error(e.getMessage(), e);
        }
        return responseMsg;
    }

    @DeleteMapping("/comment/{commentId}")
    ResponseMsg deleteComment(@PathVariable("commentId") Integer id) {
        ResponseMsg responseMsg = new ResponseMsg();
        Comment comment = commentService.getCommentByCommentId(id);
        int retVal = commentService.deleteComment(id);
        if (retVal == 1) {
            responseMsg.setStatus(200);
            responseMsg.getResponseMap().put("result", comment);
        } else {
            if (comment == null) {
                responseMsg.setStatus(404);
            } else {
                responseMsg.setStatus(500);
            }
        }
        return responseMsg;
    }

    @PutMapping("/comment")
    ResponseMsg updateComment(@RequestBody Comment comment) {
        ResponseMsg responseMsg = new ResponseMsg();
        int retVal = commentService.updateComment(comment);
        /*
         * 这个返回回去的comment里面的值应该是更新过了的
         * */
        if (retVal == 1) {
            responseMsg.setStatus(200);
            responseMsg.getResponseMap().put("result", comment);
        } else {
            responseMsg.setStatus(500);
            responseMsg.getResponseMap().put("result", comment);
        }
        return responseMsg;
    }

    /*
     * 管理员用
     * */
    @GetMapping("/comment/uncheckedComment")
    ResponseMsg getAllUncheckedComment() {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        try {
            comments = commentService.getAllUncheckComments();
            responseMsg.getResponseMap().put("result", comments);
            responseMsg.setStatus(200);
        } catch (Exception e) {
            responseMsg.setStatus(500);
            logger.error(e.getMessage(), e);
        }
        return responseMsg;
    }

    @GetMapping("/comment/allComment")
    ResponseMsg getAllComment() {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        try {
            comments = commentService.getAllComments();
            responseMsg.getResponseMap().put("result", comments);
            responseMsg.setStatus(200);
        } catch (Exception e) {
            responseMsg.setStatus(500);
            logger.error(e.getMessage(), e);
        }
        return responseMsg;
    }

    @PutMapping("/comment/check/{commentId}")
    ResponseMsg checkComment(@PathVariable("commentId") Integer commentId) {
        int retVal = commentService.checkComment(commentId);
        return getResponseMsg(commentId, retVal);
    }

    @PutMapping("/comment/uncheck/{commentId}")
    ResponseMsg uncheckComment(@PathVariable("commentId") Integer commentId){
        int retVal = commentService.uncheckComment(commentId);
        return getResponseMsg(commentId, retVal);
    }

    /*
    * IDEA自动生成的函数
    * 用于通过commentId获取对应的comment 再根据retVal设置status来封装response信息
    * */
    private ResponseMsg getResponseMsg(Integer commentId, int retVal) {
        ResponseMsg responseMsg = new ResponseMsg();
        if (retVal == 1) {
            responseMsg.setStatus(200);
            Comment comment = commentService.getCommentByCommentId(commentId);
            responseMsg.getResponseMap().put("result",comment);
        } else {
            responseMsg.setStatus(500);
        }
        return responseMsg;
    }

}
