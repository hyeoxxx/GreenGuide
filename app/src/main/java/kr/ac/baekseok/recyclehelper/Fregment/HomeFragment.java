package kr.ac.baekseok.recyclehelper.Fregment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.ac.baekseok.recyclehelper.Community.Post;
import kr.ac.baekseok.recyclehelper.Data.Product;
import kr.ac.baekseok.recyclehelper.Data.User;
import kr.ac.baekseok.recyclehelper.R;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductPostAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private FirebaseFirestore db;
    private List<Post> postListNotice = new ArrayList<>();
    private List<Post> postListQuestion = new ArrayList<>();
    private List<User> rankingList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRecentProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new ProductPostAdapter(productList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchRecentProducts();
        fetchRecentPosts("3");
        fetchRecentPosts("2");
        fetchRanking();
        return view;
    }
    private void updateNoticeSection(List<Post> posts) {
        TextView noticeTextView = getView().findViewById(R.id.tv_noticeList);
        StringBuilder noticeContent = new StringBuilder();
        for (Post post : posts) {
            noticeContent.append("• ").append(post.getTitle()).append("\n");
        }
        noticeTextView.setText(noticeContent.toString());
    }

    private void updateQuestionSection(List<Post> posts) {
        TextView questionTextView = getView().findViewById(R.id.tv_questionList);
        StringBuilder questionContent = new StringBuilder();
        for (Post post : posts) {
            questionContent.append("• ").append(post.getTitle()).append("\n");
        }
        questionTextView.setText(questionContent.toString());
    }

    private void updateRankingSection(List<User> users) {
        TextView noticeTextView = getView().findViewById(R.id.tv_ranking);
        StringBuilder noticeContent = new StringBuilder();
        int ranked = 0;
        for (User user : users) {
            ranked++;
            noticeContent.append("• ").append(ranked).append(". ").append(user.getNickname()).append(" ").append(user.getRate()).append("회").append("\n");
        }
        noticeTextView.setText(noticeContent.toString());
    }
    private void fetchRecentProducts() {
        db.collection("Products")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Product product = doc.toObject(Product.class);
                        productList.add(product);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Failed to fetch products", e));
    }

    private void fetchRecentPosts(String boardId) {
        db.collection("Posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("category", boardId)
                .limit(3)
                .get()
                .addOnSuccessListener(query -> {

                    if (boardId.equals("3")) {
                        postListNotice.clear();
                    } else if (boardId.equals("2")) {
                        postListQuestion.clear();
                    }

                    for (QueryDocumentSnapshot doc : query) {
                        Post post = doc.toObject(Post.class);
                        if (boardId.equals("3")) {
                            postListNotice.add(post);
                        } else if (boardId.equals("2")) {
                            postListQuestion.add(post);
                        }
                    }

                    if (boardId.equals("3")) {
                        updateNoticeSection(postListNotice);
                    } else if (boardId.equals("2")) {
                        updateQuestionSection(postListQuestion);
                    }
                });
    }


    private void fetchRanking() {
        db.collection("Users")
                .orderBy("rate", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(query -> {
                    for (QueryDocumentSnapshot doc : query) {
                        User user = doc.toObject(User.class);
                        rankingList.add(user);
                    }

                    updateRankingSection(rankingList);
                });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public class ProductPostAdapter extends RecyclerView.Adapter<ProductPostAdapter.ProductViewHolder> {
        private List<Product> productList;

        public ProductPostAdapter(List<Product> productList) {
            this.productList = productList;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_post, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            Product product = productList.get(position);

            holder.tvProductName.setText(product.getName());
            holder.tvProductMaterial.setText("재질: " + product.getMaterial());
            holder.tvProductNumber.setText("바코드 번호 : "+product.getNumber());

            if (product.getTimestamp() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                holder.tvProductTimestamp.setText("등록일: " + dateFormat.format(product.getTimestamp()));
            } else {
                holder.tvProductTimestamp.setText("등록일: 없음");
            }
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {
            TextView tvProductName, tvProductMaterial, tvProductTimestamp, tvProductNumber;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvProductMaterial = itemView.findViewById(R.id.tvProductMaterial);
                tvProductTimestamp = itemView.findViewById(R.id.tvProductTimestamp);
                tvProductNumber = itemView.findViewById(R.id.tvProductNumber);
            }
        }
    }
}
