package xyz.st.meethere.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.st.meethere.entity.Comment;
import xyz.st.meethere.service.CommentService;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class CommentControllerTest {

    private CommentService commentService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        commentService = mock(CommentService.class);
        this.mockMvc = standaloneSetup(new CommentController(commentService)).build();
    }

    @Test
    public void happy_path_when_get_all_comments() throws Exception {
        List<Comment> retComments = new ArrayList<>();
        retComments.add(new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                1
        ));
        retComments.add(new Comment(
                2,
                1,
                2,
                null,
                "this is comment 2",
                1
        ));
        when(commentService.getAllCheckComments()).thenReturn(retComments);
        mockMvc.perform(get("/comment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result[0].checked").value(1))
                .andExpect(jsonPath("$.responseMap.result[1].checked").value(1));
        verify(commentService).getAllCheckComments();
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void get_null_when_get_all_checked_comments() throws Exception {
        when(commentService.getAllCheckComments()).thenReturn(null);
        mockMvc.perform(get("/comment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(commentService).getAllCheckComments();
    }

    @Test
    public void get_nothing_when_get_all_checked_comments() throws Exception {
        when(commentService.getAllCheckComments()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/comment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result").exists());
        verify(commentService).getAllCheckComments();
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void happy_path_when_get_all_comments_by_userId() throws Exception {
        int userId = 1;
        List<Comment> retComments = new ArrayList<>();
        retComments.add(new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                1
        ));
        retComments.add(new Comment(
                2,
                1,
                2,
                null,
                "this is comment 2",
                1
        ));
        when(commentService.getCommentsByUserId(userId)).thenReturn(retComments);
        mockMvc.perform(get("/comment/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result[0].userId").value(userId))
                .andExpect(jsonPath("$.responseMap.result[1].userId").value(userId));
        verify(commentService).getCommentsByUserId(userId);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void get_null_when_get_all_comments_by_userId() throws Exception {
        int userId = 1;
        when(commentService.getCommentsByUserId(userId)).thenReturn(null);
        mockMvc.perform(get("/comment/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(commentService).getCommentsByUserId(userId);
    }

    @Test
    public void get_nothing_when_get_all_comments_by_userId() throws Exception {
        int userId = 1;
        when(commentService.getCommentsByUserId(userId)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/comment/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result").exists());
        verify(commentService).getCommentsByUserId(userId);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void happy_path_when_get_all_comments_by_groundId() throws Exception {
        int groundId = 1;
        List<Comment> retComments = new ArrayList<>();
        retComments.add(new Comment(
                1,
                1,
                groundId,
                null,
                "this is comment 1",
                1
        ));
        retComments.add(new Comment(
                2,
                2,
                groundId,
                null,
                "this is comment 2",
                1
        ));
        when(commentService.getCommentsByGroundId(groundId)).thenReturn(retComments);
        mockMvc.perform(get("/comment/ground/" + groundId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result[0].groundId").value(groundId))
                .andExpect(jsonPath("$.responseMap.result[1].groundId").value(groundId));
        verify(commentService).getCommentsByGroundId(groundId);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void get_null_when_get_all_comments_by_groundId() throws Exception {
        int groundId = 1;
        when(commentService.getCommentsByGroundId(groundId)).thenReturn(null);
        mockMvc.perform(get("/comment/ground/" + groundId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(commentService).getCommentsByGroundId(groundId);
    }

    @Test
    public void get_nothing_when_get_all_comments_by_groundId() throws Exception {
        int groundId = 1;
        when(commentService.getCommentsByGroundId(groundId)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/comment/ground/" + groundId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result").exists());
        verify(commentService).getCommentsByGroundId(groundId);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void happy_path_when_delete_a_comment() throws Exception {
        int commentId = 1;
        Comment retComment = new Comment(
                commentId,
                1,
                1,
                null,
                "this is comment 1",
                1
        );
        when(commentService.getCommentByCommentId(commentId)).thenReturn(retComment);
        when(commentService.deleteComment(commentId)).thenReturn(1);

        mockMvc.perform(delete("/comment/" + commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result.commentId").value(retComment.getCommentId()))
                .andExpect(jsonPath("$.responseMap.result.content").value(retComment.getContent()));
        InOrder order = inOrder(commentService);
        order.verify(commentService).getCommentByCommentId(commentId);
        order.verify(commentService).deleteComment(commentId);
    }

    @Test
    public void comment_doesnt_exist_when_delete_a_comment() throws Exception {
        int commentId = 1;
        when(commentService.deleteComment(commentId)).thenReturn(0);
        when(commentService.getCommentByCommentId(commentId)).thenReturn(null);
        mockMvc.perform(delete("/comment/" + commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        InOrder order = inOrder(commentService);
        order.verify(commentService).getCommentByCommentId(commentId);
        order.verify(commentService).deleteComment(commentId);
    }

    @Test
    public void happy_path_when_update_a_comment() throws Exception {
        Comment comment = new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                0
        );
        when(commentService.updateComment(any(Comment.class))).thenReturn(1);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(comment);

        mockMvc.perform(put("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result.commentId").value(1));
        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentService).updateComment(commentArgumentCaptor.capture());
        Comment actualArgComment = commentArgumentCaptor.getValue();

        assertAll(
                ()->assertEquals(comment.getCommentId(),actualArgComment.getCommentId()),
                () -> assertEquals(comment.getGroundId(),actualArgComment.getGroundId()),
                () -> assertEquals(comment.getUserId(),actualArgComment.getUserId())
        );
    }

    @Test
    public void comment_doesnt_exist_when_update_a_news() throws Exception {
        when(commentService.updateComment(any(Comment.class))).thenReturn(0);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new Comment());

        mockMvc.perform(put("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(500));
        verify(commentService).updateComment(any(Comment.class));
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void happy_path_when_add_a_comment() throws Exception {
        when(commentService.addComment(any(Comment.class))).thenReturn(1);

        Comment comment = new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                0
        );

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(comment);

        mockMvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result.commentId").value(1));
        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentService).addComment(commentArgumentCaptor.capture());
        Comment actualArgComment = commentArgumentCaptor.getValue();

        assertAll(
                ()->assertEquals(comment.getCommentId(),actualArgComment.getCommentId()),
                () -> assertEquals(comment.getGroundId(),actualArgComment.getGroundId()),
                () -> assertEquals(comment.getUserId(),actualArgComment.getUserId())
        );
    }

    @Test
    public void nothing_passed_when_add_a_comment() throws Exception {
        when(commentService.addComment(any(Comment.class))).thenReturn(0);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(new Comment());

        mockMvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(500));
        verify(commentService).addComment(any(Comment.class));
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void happy_path_when_get_all_unchecked_comment() throws Exception {
        List<Comment> retComments = new ArrayList<>();
        retComments.add(new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                0
        ));
        retComments.add(new Comment(
                2,
                2,
                1,
                null,
                "this is comment 2",
                0
        ));
        when(commentService.getAllUncheckComments()).thenReturn(retComments);

        mockMvc.perform(get("/comment/uncheckedComment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMap.result[0].checked").value(0))
                .andExpect(jsonPath("$.responseMap.result[1].checked").value(0));
        verify(commentService).getAllUncheckComments();
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void happy_path_when_admin_check_a_comment() throws Exception {
        when(commentService.checkComment(1)).thenReturn(1);
        Comment retComment = new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                1
        );
        when(commentService.getCommentByCommentId(1)).thenReturn(retComment);
        mockMvc.perform(put("/comment/check/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMap.result").exists())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result.commentId").value(1));
        InOrder order = inOrder(commentService);
        order.verify(commentService).checkComment(1);
        order.verify(commentService).getCommentByCommentId(1);
    }

    @Test
    public void comment_doesnt_exist_when_admin_check_a_comment() throws Exception {
        when(commentService.checkComment(1)).thenReturn(0);
        mockMvc.perform(put("/comment/check/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(commentService).checkComment(1);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void happy_path_when_admin_uncheck_a_comment() throws Exception {
        when(commentService.uncheckComment(1)).thenReturn(1);
        Comment retComment = new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                1
        );
        when(commentService.getCommentByCommentId(1)).thenReturn(retComment);
        mockMvc.perform(put("/comment/uncheck/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMap.result").exists())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.responseMap.result.commentId").value(1));
        InOrder order = inOrder(commentService);
        order.verify(commentService).uncheckComment(1);
        order.verify(commentService).getCommentByCommentId(1);
    }

    @Test
    public void comment_doesnt_exist_when_admin_uncheck_a_comment() throws Exception {
        when(commentService.uncheckComment(1)).thenReturn(0);
        mockMvc.perform(put("/comment/uncheck/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(commentService).uncheckComment(1);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void happy_path_when_get_checked_comment_by_ground_id() throws Exception {
        when(commentService.getCheckedCommentsByGroundId(anyInt())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/checkedcomment/ground/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(commentService).getCheckedCommentsByGroundId(1);
    }

    @Test
    public void comment_not_exist_or_ground_not_exist_when_checked_comment_by_ground_id() throws Exception {
        when(commentService.getCheckedCommentsByGroundId(anyInt())).thenReturn(null);

        mockMvc.perform(get("/checkedcomment/ground/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));

        verify(commentService).getCheckedCommentsByGroundId(1);
    }

    @ParameterizedTest
    @MethodSource("matchStringProviderHappyPath")
    public void happy_path_when_get_comment_by_match_without_ground_id(Map<String,String> requestParams) throws Exception{
        List<Comment> retComments = new ArrayList<>();
        retComments.add(new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                0
        ));
        retComments.add(new Comment(
                2,
                2,
                1,
                null,
                "this is comment 2",
                0
        ));
        Comment retComment = new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                1
        );

        when(commentService.getAllCheckComments()).thenReturn(retComments);
        when(commentService.getCommentsByGroundId(anyInt())).thenReturn(retComments);
        when(commentService.getCommentsByUserId(anyInt())).thenReturn(retComments);
        when(commentService.getCommentsByMatch(anyString())).thenReturn(retComments);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(post("/comment/match")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

    }

    static Stream<Map<String,String>> matchStringProviderHappyPath(){
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("match","gid:1");
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("match","uid:1");
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("match","hello");
        HashMap<String, String> map4 = new HashMap<>();
        map4.put("match","");
        map4.put("groundId","1");
        return Stream.of(
                map1,map2,map3,map4
        );
    }

    @ParameterizedTest
    @MethodSource("matchStringProviderCornerCase")
    public void corner_case_when_get_comment_by_match_without_ground_id(Map<String,String> requestParams) throws Exception {
        when(commentService.getAllCheckComments()).thenReturn(new ArrayList<>());
        when(commentService.getCommentsByGroundId(anyInt())).thenReturn(new ArrayList<>());
        when(commentService.getCommentsByUserId(anyInt())).thenReturn(new ArrayList<>());
        when(commentService.getCommentsByMatch(anyString())).thenReturn(new ArrayList<>());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(post("/comment/match")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
    }

    static Stream<Map<String,String>> matchStringProviderCornerCase(){
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("match","gid:452");
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("match","uid:51");
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("match","null");
        HashMap<String, String> map4 = new HashMap<>();
        map4.put("match","");
        map4.put("groundId","1");
        return Stream.of(
                map1,map2,map3,map4
        );
    }

    @Test
    public void happy_path_when_delete_comment_by_batch() throws Exception {
        when(commentService.getCommentByCommentId(anyInt())).thenReturn(null);
        when(commentService.deleteComment(anyInt())).thenReturn(1);

        HashMap<String,List<Integer>> requestParams = new HashMap<>();
        requestParams.put("ids", Arrays.asList(1, 2, 3));

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(delete("/comment/deleteByBatch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
        verify(commentService, times(3)).deleteComment(anyInt());
    }

    @Test
    public void fail_once_when_delete_comment_by_batch() throws Exception {
        when(commentService.deleteComment(anyInt())).thenReturn(1).thenReturn(0);
        HashMap<String,List<Integer>> requestParams = new HashMap<>();
        requestParams.put("ids", Arrays.asList(1, 2, 3));

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(requestParams);

        mockMvc.perform(delete("/comment/deleteByBatch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(commentService, times(3)).deleteComment(anyInt());
    }

    @Test
    public void happy_path_when_get_all_comment() throws Exception {
        List<Comment> retComments = new ArrayList<>();
        retComments.add(new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                0
        ));
        retComments.add(new Comment(
                2,
                2,
                1,
                null,
                "this is comment 2",
                0
        ));
        Comment retComment = new Comment(
                1,
                1,
                1,
                null,
                "this is comment 1",
                1
        );
        when(commentService.getAllComments()).thenReturn(retComments);

        mockMvc.perform(get("/comment/allComment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
        verify(commentService).getAllComments();
    }

    @Test
    public void get_nothing_when_get_all_comment() throws Exception {
        when(commentService.getAllComments()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/comment/allComment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(404));
        verify(commentService).getAllComments();
    }

}