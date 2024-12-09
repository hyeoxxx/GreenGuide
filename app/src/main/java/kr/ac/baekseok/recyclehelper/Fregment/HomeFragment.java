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

import kr.ac.baekseok.recyclehelper.Data.Product;
import kr.ac.baekseok.recyclehelper.R;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductPostAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private FirebaseFirestore db;

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

        return view;
    }
    private void fetchRecentProducts() {
        db.collection("Products")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10) // 최근 10개 제품만 가져오기
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

            // 날짜 포맷 변환
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
            TextView tvProductName, tvProductMaterial, tvProductTimestamp;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvProductMaterial = itemView.findViewById(R.id.tvProductMaterial);
                tvProductTimestamp = itemView.findViewById(R.id.tvProductTimestamp);
            }
        }
    }
}
