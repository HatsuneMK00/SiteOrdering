package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import xyz.st.meethere.entity.Comment;
import xyz.st.meethere.entity.Ground;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.service.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
//        FIXME: 返回内容里需要有userName，groundName
        comments = commentService.getAllCheckComments();
        // FIXME: 当结果为null时返回404
        if (comments == null)
            responseMsg.setStatus(404);
        else
            responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", comments);
        return responseMsg;
    }

    /* TODO: 代码有些冗余
    * 应该把这两个函数实现成返回通过审核的留言？
    * 现在这两个接口和getCheckedCommentByGroundId有点重复了
    * */
    @GetMapping("/comment/user/{userId}")
    ResponseMsg getCommentsByUserId(@PathVariable("userId") Integer id) {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        //        FIXME: 返回内容里需要有userName，groundName
        comments = commentService.getCommentsByUserId(id);
        // FIXME: 当结果为null时返回404 其他情况还是返回200吧
        if (comments == null)
            responseMsg.setStatus(404);
        else
//            其实这里不管怎么样都会返回200 因为查不到内容的时候都是返回长度为0的list 不会返回null的
//        如果为空就不显示吧 不管是用户不存在还是该用户没有comment了
            responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", comments);
        return responseMsg;
    }

    @GetMapping("/comment/ground/{groundId}")
    ResponseMsg getCommentsByGroundId(@PathVariable("groundId") Integer id) {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        //        FIXME: 返回内容里需要有userName，groundName
        //        FIXME: 给用户用接口，返回已审核过的留言
        comments = commentService.getCommentsByGroundId(id);
        // FIXME: 当场馆不存在时返回404 场馆存在不论有没有留言都返回200
        if (comments == null)
            responseMsg.setStatus(404);
        else
//            情况同上
            responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", comments);
        return responseMsg;
    }

    @GetMapping("/checkedcomment/ground/{groundId}")
    ResponseMsg getcheckedCommentsByGroundId(@PathVariable("groundId") Integer id) {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        //        FIXME: 返回内容里需要有userName，groundName
        //        FIXME: 给用户用接口，返回已审核过的留言
        comments = commentService.getCheckedCommentsByGroundId(id);
        // FIXME: 当场馆不存在时返回404 场馆存在不论有没有留言都返回200

//          情况同上
        if (comments == null)
            responseMsg.setStatus(404);
        else
            responseMsg.setStatus(200);
        responseMsg.getResponseMap().put("result", comments);
        return responseMsg;
    }

    /*
     * 支持4种搜索方式：
     * 1. 纯文字: 内容匹配
     * 2. "gid:"开头: 返回对应gid的Ground的留言
     *   例如: gid: 1,2,3,4,5
     * 3. "uid:"开头: 返回对应uid的user的留言
     * 4. 搜索内容为空: 返回全部
     *
     * */
    @ResponseBody
    @ApiOperation("通过搜索返回对应comment")
    @PostMapping("/comment/match")
//        TODO: 感觉这些逻辑都应该在Service层里面，而不是在Controller里
    ResponseMsg getCommentByMatch(@RequestBody Map<String, String> params) {
        String searchParam = params.get("match");
        int groundId = 0;
        if (params.containsKey("groundId")) {
            groundId = Integer.parseInt(params.get("groundId"));
        }
        if (searchParam.equals("")) {
            ResponseMsg responseMsg = getAllComments();
            List<Comment> comments = (List<Comment>) responseMsg.getResponseMap().get("result");
            List<Comment> tempComment = new ArrayList<>();
            for (Comment comment :
                    comments) {
                if (groundId == 0 || comment.getGroundId() == groundId)
                    tempComment.add(comment);
            }
            if (tempComment.size() == 0)
                responseMsg.setStatus(404);
            responseMsg.getResponseMap().put("result", tempComment);
            return responseMsg;

        } else if (searchParam.startsWith("gid:")) {
            String param = searchParam.split(":")[1];
            String[] ids = param.split(",");
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(200);
            ArrayList<Comment> retComment = new ArrayList<>();
            for (String id : ids) {
//                FIXME: 返回内容应该是一层的数组
                List<Comment> comments = commentService.getCommentsByGroundId(Integer.valueOf(id.trim()));
                for (Comment comment :
                        comments) {
                    if (groundId == 0 || comment.getGroundId() == groundId){
                        retComment.add(comment);
                    }
                }
            }
            if (retComment.size() == 0)
                responseMsg.setStatus(404);
            responseMsg.getResponseMap().put("result", retComment);
            return responseMsg;
        } else if (searchParam.startsWith("uid:")) {
            String param = searchParam.split(":")[1];
            String[] ids = param.split(",");
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(200);
            ArrayList<Comment> retComment = new ArrayList<>();
            for (String id : ids) {
//                FIXME: 这边的函数调错了
                List<Comment> comments = commentService.getCommentsByUserId(Integer.valueOf(id.trim()));
                for (Comment comment :
                        comments) {
                    if (groundId == 0 || comment.getGroundId() == groundId){
                        retComment.add(comment);
                    }
                }
            }
            if (retComment.size() == 0)
                responseMsg.setStatus(404);
            responseMsg.getResponseMap().put("result", retComment);
            return responseMsg;
        } else {
            List<Comment> comments = commentService.getCommentsByMatch(searchParam);
            List<Comment> retComment = new ArrayList<>();
            for (Comment comment :
                    comments) {
                if (groundId == 0 || comment.getGroundId() == groundId){
                    retComment.add(comment);
                }
            }
            ResponseMsg responseMsg = new ResponseMsg();
            responseMsg.setStatus(200);
            if (comments.size() == 0)
                responseMsg.setStatus(404);
            responseMsg.getResponseMap().put("result", retComment);
            return responseMsg;
        }
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
                responseMsg.setStatus(404);
        }
        return responseMsg;
    }

    @ResponseBody
    @ApiOperation("通过commentId批量删除新闻")
    @DeleteMapping("/comment/deleteByBatch")
    ResponseMsg deleteCommentByBatch(@RequestBody Map<String, List<Integer>> data) {
        ResponseMsg msg = new ResponseMsg();
        List<Integer> ids = data.get("ids");
        msg.setStatus(200);
        ResponseMsg tempMsg;
        for (Integer id : ids) {
            tempMsg = deleteComment(id);
            if (tempMsg.getStatus() == 404 && msg.getStatus() != 404) {
                msg.setStatus(404);
            }
        }
//        FIXME: 这边的赋值是个什么鬼msg.setStatus(200);
        return msg;
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

    @PostMapping("/comment")
    ResponseMsg addComment(@RequestBody Comment comment) {
        int result = commentService.addComment(comment);
        ResponseMsg responseMsg = new ResponseMsg();
        if (result == 1)
            responseMsg.setStatus(200);
        else
            responseMsg.setStatus(500);
        responseMsg.getResponseMap().put("result", comment);
        return responseMsg;
    }

    /*
     * 管理员用
     * */
    @GetMapping("/comment/uncheckedComment")
    ResponseMsg getAllUncheckedComment() {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
//        fixme:返回的内容中要有userName,groundName
//        fixme:不通过的留言也要返回
        comments = commentService.getAllUncheckComments();
        responseMsg.getResponseMap().put("result", comments);
        responseMsg.setStatus(200);
        return responseMsg;
    }

    @GetMapping("/comment/allComment")
    ResponseMsg getAllComment() {
        ResponseMsg responseMsg = new ResponseMsg();
        List<Comment> comments = null;
        comments = commentService.getAllComments();
        responseMsg.getResponseMap().put("result", comments);
        responseMsg.setStatus(200);
        if (comments.size() == 0)
            responseMsg.setStatus(404);
        return responseMsg;
    }

    @PutMapping("/comment/check/{commentId}")
    ResponseMsg checkComment(@PathVariable("commentId") Integer commentId) {
        int retVal = commentService.checkComment(commentId);
        return getResponseMsg(commentId, retVal);
    }

    @PutMapping("/comment/uncheck/{commentId}")
    ResponseMsg uncheckComment(@PathVariable("commentId") Integer commentId) {
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
            responseMsg.getResponseMap().put("result", comment);
        } else {
            responseMsg.setStatus(404);
        }
        return responseMsg;
    }

}
