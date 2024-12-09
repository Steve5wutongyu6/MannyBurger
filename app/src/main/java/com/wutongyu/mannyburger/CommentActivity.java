package com.wutongyu.mannyburger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private ListView listView;
    private CommentAdapter adapter;
    private List<Comment> commentList;
    private CommentDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        dbHelper = new CommentDatabaseHelper(this);
        listView = findViewById(R.id.listViewComments);
        commentList = new ArrayList<>();
        adapter = new CommentAdapter(this, commentList);
        listView.setAdapter(adapter);

        CommentDatabaseHelper dbHelper = new CommentDatabaseHelper(this);

        // 无论是否存在数据，先清空评论数据，可以调用emptyComment方法
        //emptyAllComments();// 这个方法有问题，不生效
        // 刷新列表视图
        refreshListView();

        // 删除评论
        dbHelper.deleteAllComments();

        // 添加好评
        dbHelper.addComment(new Comment(0, "Alice", "The burger was amazing! Highly recommend.", new Date().toString(), null));
        dbHelper.addComment(new Comment(0, "Bob", "Fantastic service and food quality.", new Date().toString(), null));
        dbHelper.addComment(new Comment(0, "Charlie", "Best burger I've ever had.", new Date().toString(), null));
        dbHelper.addComment(new Comment(0, "David", "Great value for money.", new Date().toString(), null));

        // 添加差评
        dbHelper.addComment(new Comment(0, "Eve", "The burger was cold and dry.", new Date().toString(), null));
        dbHelper.addComment(new Comment(0, "Frank", "Service was very slow.", new Date().toString(), null));

        // 添加一条包含视频的评论
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.example);
        dbHelper.addComment(new Comment(0, "Grace", "Check out my experience!", new Date().toString(), videoUri.toString()));

        // 从数据库中读取评论数据，并展示在 CommentActivity 中
        loadCommentsFromDatabase(dbHelper);
    }

/*    // 清空所有评论,此方法不生效，已废弃
    private void emptyAllComments() {
        List<Comment> comments = dbHelper.getAllComments();
        for (Comment comment : comments) {
            dbHelper.emptyComment(comment);
            dbHelper.updateComment(comment);
        }
    }*/

    //刷新评论列表
    private void refreshListView() {
        List<Comment> comments = dbHelper.getAllComments();
        CommentAdapter adapter = new CommentAdapter(this, comments);
        listView.setAdapter(adapter);
    }

    // 加载评论数据库
    private void loadCommentsFromDatabase(CommentDatabaseHelper dbHelper) {
        commentList.clear();
        commentList.addAll(dbHelper.getAllComments());
        adapter.notifyDataSetChanged();
    }

    // 在 CommentActivity 中添加方法来启动 VideoPlayActivity
    void startVideoPlayActivity(String videoPath) {
        Intent intent = new Intent(this, VideoPlayActivity.class);
        intent.putExtra("videoPath", videoPath);
        startActivity(intent);
    }
}
