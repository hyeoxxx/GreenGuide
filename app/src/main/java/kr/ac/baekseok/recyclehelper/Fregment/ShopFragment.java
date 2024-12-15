package kr.ac.baekseok.recyclehelper.Fregment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import kr.ac.baekseok.recyclehelper.Data.DatabaseUtil;
import kr.ac.baekseok.recyclehelper.Data.SaleItemAdapter;
import kr.ac.baekseok.recyclehelper.Data.SaleItem;
import kr.ac.baekseok.recyclehelper.Data.User;
import kr.ac.baekseok.recyclehelper.NewPostActivity;
import kr.ac.baekseok.recyclehelper.R;

public class ShopFragment extends Fragment {
    private RecyclerView recyclerView;
    private SaleItemAdapter adapter;
    private FirebaseFirestore db;
    private ArrayList<SaleItem> saleItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        recyclerView = view.findViewById(R.id.recycler_sale_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        db = FirebaseFirestore.getInstance();
        saleItems.add(new SaleItem("바나나우유", "바나나우유애요", 1500, "0000-0000-0000"));
        saleItems.add(new SaleItem("츄파츕스", "사탕이에요", 200, "0000-0000-0000"));
        saleItems.add(new SaleItem("새우깡", "손이가요 손이가", 1700, "0000-0000-0000"));

        adapter = new SaleItemAdapter(saleItems);
        adapter.setOnItemClickListener(item -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("구매");
            builder.setMessage("정말 "+item.getName()+"을(를) 구매하시겠어요 ?");

            builder.setPositiveButton("예", (dialog, which) -> {
                User user = User.getInstance();
                if (user.getPoint() < item.getPrice()) {
                    Toast.makeText(getContext(), "포인트가 부족합니다 !", Toast.LENGTH_SHORT).show();
                    return;
                }
                user.gainPoint(-item.getPrice());
                user.addInventory(item);
                DatabaseUtil.saveUserInfo(db, user, callback -> {
                    if (callback) {
                        Toast.makeText(getContext(), "성공적으로 구매하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            builder.setNegativeButton("아니오", (dialog, which) -> {
                dialog.dismiss();
            });

            builder.show();
        });

        recyclerView.setAdapter(adapter);
        return view;
    }

}