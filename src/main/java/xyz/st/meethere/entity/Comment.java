package xyz.st.meethere.entity;

import java.sql.Timestamp;

public class Comment {
    private int commentId;
    private int userId;
    private String userName;
    private int groundId;
    private String groundName;
    private Timestamp date;
    private String content;
    private int checked;

    public Comment(int commentId, int userId, int groundId, Timestamp date, String content, int checked) {
        this.commentId = commentId;
        this.userId = userId;
        this.groundId = groundId;
        this.date = date;
        this.content = content;
        this.checked = checked;
    }

    public Comment() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroundName() {
        return groundName;
    }

    public void setGroundName(String groundName) {
        this.groundName = groundName;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroundId() {
        return groundId;
    }

    public void setGroundId(int groundId) {
        this.groundId = groundId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
