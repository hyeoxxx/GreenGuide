package kr.ac.baekseok.recyclehelper.Community;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class CommunityUtils {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static interface FirestoreCallback<T> {
        void onSuccess(T result);  // 성공 시 호출
        void onFailure(Exception e);  // 실패 시 호출
    }

    public static void addPost(Post post, FirestoreCallback callback) {
        db.collection("Posts")
                .document(post.getPostId())
                .set(post)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e));
    }
    public static void getPost(String postId, FirestoreCallback<Post> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Posts")
                .document(postId) // 게시물 ID로 특정 문서 지정
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // 문서를 Post 객체로 변환
                        Post post = documentSnapshot.toObject(Post.class);
                        callback.onSuccess(post);
                    } else {
                        // 문서가 없을 경우 null 반환
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(callback::onFailure); // 오류 발생 시 콜백
    }
    public static void getPosts(String category, FirestoreCallback<List<Post>> callback) {
        db.collection("Posts")
                .whereEqualTo("category", category)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        posts.add(post);
                    }
                    callback.onSuccess(posts);
                })
                .addOnFailureListener(e -> callback.onFailure(e));
    }
    // 댓글 추가
    public static void addComment(String postId, Comment comment, FirestoreCallback<Void> callback) {
        db.collection("Posts")
                .document(postId)
                .collection("Comments")
                .document(comment.getCommentId())
                .set(comment)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    // 댓글 목록 가져오기
    public static void getComments(String postId, FirestoreCallback<List<Comment>> callback) {
        db.collection("Posts")
                .document(postId)
                .collection("Comments")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Comment> comments = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Comment comment = document.toObject(Comment.class);
                        comments.add(comment);
                    }
                    callback.onSuccess(comments);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
