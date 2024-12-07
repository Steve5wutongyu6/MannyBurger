package com.wutongyu.mannyburger;

//评论数据封装类
public class Comment {
    private int CommentId;
    private String UserName;
    private String CommentContent;
    private String CommentTime;
    private String video;

    public Comment() {
        super();
    }

    public Comment(int commentId, String userName, String commentContent, String commentTime, String video) {
        CommentId = commentId;
        UserName = userName;
        CommentContent = commentContent;
        CommentTime = commentTime;
        this.video = video;
    }


    public int getCommentId() {
        return CommentId;
    }

    public void setCommentId(int commentId) {
        CommentId = commentId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String commentContent) {
        CommentContent = commentContent;
    }

    public String getCommentTime() {
        return CommentTime;
    }

    public void setCommentTime(String commentTime) {
        CommentTime = commentTime;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "CommentId=" + CommentId +
                ", UserName='" + UserName + '\'' +
                ", CommentContent='" + CommentContent + '\'' +
                ", CommentTime='" + CommentTime + '\'' +
                ", video='" + video + '\'' +
                '}';
    }
}
