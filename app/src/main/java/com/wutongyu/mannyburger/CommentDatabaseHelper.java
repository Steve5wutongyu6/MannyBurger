package com.wutongyu.mannyburger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CommentDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "comments.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_COMMENTS = "comments";
    private static final String COLUMN_COMMENT_ID = "comment_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_COMMENT_CONTENT = "comment_content";
    private static final String COLUMN_COMMENT_TIME = "comment_time";
    private static final String COLUMN_VIDEO = "video";

    private final Context context;
    private List<Comment> commentList;

    public CommentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String CREATE_TABLE = "CREATE TABLE " + TABLE_COMMENTS + "("
                + COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_COMMENT_CONTENT + " TEXT,"
                + COLUMN_COMMENT_TIME + " TEXT,"
                + COLUMN_VIDEO + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }

    // 添加评论
    public void addComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, comment.getUserName());
        values.put(COLUMN_COMMENT_CONTENT, comment.getCommentContent());
        values.put(COLUMN_COMMENT_TIME, comment.getCommentTime());
        values.put(COLUMN_VIDEO, comment.getVideo());
        db.insert(TABLE_COMMENTS, null, values);
        db.close();
    }

    // 获取评论列表
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Comment comment = commentList.get(position);
        // 绑定控件
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText(comment.getUserName());
        text2.setText(comment.getCommentContent());

        return convertView;
    }


    public List<Comment> getAllComments() {
        List<Comment> commentList = new ArrayList<>(); // 初始化评论列表
        SQLiteDatabase db = this.getReadableDatabase(); // 获取评论数据库
        String query = "SELECT * FROM " + TABLE_COMMENTS; // 查询
        Cursor cursor = db.rawQuery(query, null);

        // 检查游标是否成功移动到第一条记录
        if (cursor.moveToFirst()) {
            // 获取各列的索引
            int idIndex = cursor.getColumnIndex(COLUMN_COMMENT_ID);
            int userNameIndex = cursor.getColumnIndex(COLUMN_USER_NAME);
            int commentContentIndex = cursor.getColumnIndex(COLUMN_COMMENT_CONTENT);
            int commentTimeIndex = cursor.getColumnIndex(COLUMN_COMMENT_TIME);
            int videoIndex = cursor.getColumnIndex(COLUMN_VIDEO);

            // 确保所有需要的列都存在
            if (idIndex != -1 && userNameIndex != -1 && commentContentIndex != -1 && commentTimeIndex != -1 && videoIndex != -1) {
                // 遍历游标中的每一项记录
                do {
                    // 根据列索引获取数据
                    int id = cursor.getInt(idIndex);
                    String userName = cursor.getString(userNameIndex);
                    String commentContent = cursor.getString(commentContentIndex);
                    String commentTime = cursor.getString(commentTimeIndex);
                    String video = cursor.getString(videoIndex);

                    // 创建Comment对象并添加到评论列表中
                    Comment comment = new Comment(id, userName, commentContent, commentTime, video);
                    commentList.add(comment);
                } while (cursor.moveToNext());
            } else {
                // 处理列不存在的情况，可以记录日志或抛出异常
                throw new IllegalArgumentException("One or more columns do not exist in the database.");
            }
        }

        cursor.close();
        db.close();
        return commentList;
    }


    // 清空Comment方法
    public void emptyComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        // 清空评论的具体实现
        comment.setCommentId(0);
        comment.setUserName("");
        comment.setCommentContent("");
        comment.setCommentTime("");
        comment.setVideo("");
    }

    // 更新评论到数据库
    public void updateComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, comment.getUserName());
        values.put(COLUMN_COMMENT_CONTENT, comment.getCommentContent());
        values.put(COLUMN_COMMENT_TIME, comment.getCommentTime());
        values.put(COLUMN_VIDEO, comment.getVideo());
        db.update(TABLE_COMMENTS, values, COLUMN_COMMENT_ID + " = ?", new String[]{String.valueOf(comment.getCommentId())});
        db.close();
    }

    // 删除所有评论
    public void deleteAllComments() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMMENTS, null, null);
        db.close();
    }
}