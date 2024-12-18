package com.wutongyu.mannyburger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private final Context context;
    private final List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        super(context, 0, commentList);
        this.context = context;
        this.commentList = commentList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 如果 convertView 为空，则创建一个新视图
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        // 获取当前评论
        Comment comment = commentList.get(position);

        // 绑定控件
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText(comment.getUserName());
        text2.setText(comment.getCommentContent());

        // 设置点击监听器
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoPath = comment.getVideo();
                if (videoPath != null && !videoPath.isEmpty()) {
                    // 调用 CommentActivity 中的方法来启动 VideoPlayActivity
                    ((CommentActivity) context).startVideoPlayActivity(videoPath);
                }
            }
        });

        return convertView;
    }
}
