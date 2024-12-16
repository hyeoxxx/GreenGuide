package kr.ac.baekseok.recyclehelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import kr.ac.baekseok.recyclehelper.Community.CommunityUtils;
import kr.ac.baekseok.recyclehelper.Community.Post;
import kr.ac.baekseok.recyclehelper.Community.PostAdapter;

public class PostListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String boardId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        boardId = getIntent().getStringExtra("boardId");

        recyclerView = findViewById(R.id.recycler_view_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(postList, post -> {

            Intent intent = new Intent(PostListActivity.this, PostDetailActivity.class);
            intent.putExtra("postId", post.getPostId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAddPost = findViewById(R.id.fab_add_post);
        fabAddPost.setOnClickListener(v -> {
                Intent intent = new Intent(PostListActivity.this, NewPostActivity.class);
                intent.putExtra("boardId", boardId);
            startActivity(intent);
        });

        loadPosts();
    }

    private void loadPosts() {
        CommunityUtils.getPosts(boardId, new CommunityUtils.FirestoreCallback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                postList.clear();
                postList.addAll(posts);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("PostListActivity", "게시물 로드 실패", e);
            }
        });
    }
}