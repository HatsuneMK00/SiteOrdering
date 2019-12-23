package xyz.st.meethere.controller;

import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.st.meethere.entity.Comment;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.service.CommentService;

import java.util.List;

@RestController
@ResponseBody
public class CommentController {
    @Autowired
    CommentService commentService;

    Logger logger = LoggerFactory.getLogger(getClass());

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
            logger.error(e.getMessage(),e);
        }
        return responseMsg;
    }

    @GetMapping("/comment/user/{userId}")
    ResponseMsg getCommentsByUserId(@PathVariable("userId") Integer id) {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        try {
            comments = commentService.getCommentsByUserId(id);
            responseMsg.getResponseMap().put("result",comments);
            responseMsg.setStatus(200);
        } catch (Exception e){
            responseMsg.setStatus(500);
            logger.error(e.getMessage(),e);
        }
        return responseMsg;
    }

    @GetMapping("/comment/ground/{groundId}")
    ResponseMsg getCommentsByGroundId(@PathVariable("groundId") Integer id){
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        try {
            comments = commentService.getCommentsByGroundId(id);
            responseMsg.getResponseMap().put("result",comments);
            responseMsg.setStatus(200);
        } catch (Exception e){
            responseMsg.setStatus(500);
            logger.error(e.getMessage(),e);
        }
        return responseMsg;
    }
}
