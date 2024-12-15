package kr.ac.baekseok.recyclehelper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.ac.baekseok.recyclehelper.Community.CommunityUtils;
import kr.ac.baekseok.recyclehelper.Community.Post;
import kr.ac.baekseok.recyclehelper.Data.Product;
import kr.ac.baekseok.recyclehelper.Data.ProductStorage;
import kr.ac.baekseok.recyclehelper.Data.User;
import kr.ac.baekseok.recyclehelper.R;

public class NewPostActivity extends AppCompatActivity {

    private EditText edtTitle, edtContent;
    private Button btnSubmit;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String boardId;
    private String barNum, barName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        edtTitle = findViewById(R.id.edt_post_title);
        edtContent = findViewById(R.id.edt_post_content);
        btnSubmit = findViewById(R.id.btn_submit_post);
        barNum = getIntent().getStringExtra("barcode");
        barName = getIntent().getStringExtra("barName");

        // 게시판 ID 가져오기
        boardId = getIntent().getStringExtra("boardId");
        if (barNum != null) {
            edtTitle.setText(barName + "(" + barNum + ")");
            edtTitle.setEnabled(false);
        }
        btnSubmit.setOnClickListener(v -> submitPost());

    }

    private void submitPost() {
        String title = edtTitle.getText().toString().trim();
        String content = edtContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String postId = db.collection("Posts").document().getId();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        User UserInstance = User.getInstance();
        Post newPost = new Post(
                postId,
                user.getUid(),
                UserInstance.getNickname(),
                title,
                content,
                System.currentTimeMillis(),
                boardId
        );

        CommunityUtils.addPost(newPost, new CommunityUtils.FirestoreCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                if (barNum != null) {
                    ProductStorage.ReadProduct(db, barNum, callback -> {
                        if (callback == null) {
                            return;
                        }
                        Product product = (Product)callback;
                        product.setPostId(postId);
                        ProductStorage.SaveProduct(db, product, call -> {
                            if (!call) {
                                Toast.makeText(NewPostActivity.this, "문제 발생", Toast.LENGTH_SHORT).show();
                            } else {
                                User user = User.getInstance();
                                user.gainPoint(100);
                                user.gainRate(100);
                                user = null;
                            }
                        });
                    });
                }
                Toast.makeText(NewPostActivity.this, "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("NewPostActivity", "게시물 등록 실패", e);
            }
        });
    }
}