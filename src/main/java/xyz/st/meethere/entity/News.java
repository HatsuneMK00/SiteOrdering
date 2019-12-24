package xyz.st.meethere.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

@ApiModel("News")
public class News {
    @ApiModelProperty("新闻标题")
    private String title;
    @ApiModelProperty("新闻正文")
    private String content;
    @ApiModelProperty("新闻时间，不需要填写")
    private Timestamp date;
    @ApiModelProperty("新闻id，数据库中的主键，POST时不需要填写，UPDATE时需要填写")
    private int newsId;

    public News(String title, String content, Timestamp date, int newsId) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.newsId = newsId;
    }

    public News() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }
}
