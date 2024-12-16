package kr.ac.baekseok.recyclehelper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import kr.ac.baekseok.recyclehelper.Community.Comment;
import kr.ac.baekseok.recyclehelper.Community.CommentAdapter;
import kr.ac.baekseok.recyclehelper.Community.Post;
import kr.ac.baekseok.recyclehelper.Community.CommunityUtils;
import kr.ac.baekseok.recyclehelper.Data.User;
import kr.ac.baekseok.recyclehelper.R;

public class PostDetailActivity extends AppCompatActivity {

    private TextView txtTitle, txtAuthor, txtContent;
    private RecyclerView recyclerViewComments;
    private EditText edtComment;
    private Button btnAddComment, btnEditPost, btnDeletePost;

    private String postId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Comment> commentList = new ArrayList<>();
    private CommentAdapter adapter;
    private Post currentPost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        txtTitle = findViewById(R.id.txt_post_title);
        txtAuthor = findViewById(R.id.txt_post_author);
        txtContent = findViewById(R.id.txt_post_content);
        recyclerViewComments = findViewById(R.id.recycler_view_comments);
        edtComment = findViewById(R.id.edt_comment);
        btnAddComment = findViewById(R.id.btn_add_comment);
        postId = getIntent().getStringExtra("postId");
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(commentList);
        recyclerViewComments.setAdapter(adapter);
        loadPost();
        loadComments();
        btnAddComment.setOnClickListener(v -> addComment());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void loadPost() {
        CommunityUtils.getPost(postId, new CommunityUtils.FirestoreCallback<Post>() {
            @Override
            public void onSuccess(Post post) {
                currentPost = post;
                txtTitle.setText(post.getTitle());
                txtAuthor.setText(post.getAuthorName());
                txtContent.setText(post.getContent());
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("PostDetailActivity", "게시물 로드 실패", e);
            }
        });
    }

    private void loadComments() {
        CommunityUtils.getComments(postId, new CommunityUtils.FirestoreCallback<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> comments) {
                commentList.clear();
                commentList.addAll(comments);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("PostDetailActivity", "댓글 로드 실패", e);
            }
        });
    }

    private void addComment() {
        String content = edtComment.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "댓글 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String commentId = db.collection("Posts").document(postId).collection("Comments").document().getId();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Comment newComment = new Comment(
                commentId,
                postId,
                user.getUid(),
                User.getInstance().getNickname(),
                content,
                System.currentTimeMillis(),
                null
        );

        CommunityUtils.addComment(postId, newComment, new CommunityUtils.FirestoreCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(PostDetailActivity.this, "댓글이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                edtComment.setText("");
                loadComments();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("PostDetailActivity", "댓글 추가 실패", e);
            }
        });
    }
}