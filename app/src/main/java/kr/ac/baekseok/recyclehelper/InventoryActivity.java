package kr.ac.baekseok.recyclehelper;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import kr.ac.baekseok.recyclehelper.Data.SaleItem;
import kr.ac.baekseok.recyclehelper.Data.InventoryAdapter;
import kr.ac.baekseok.recyclehelper.Data.User;

public class InventoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InventoryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        recyclerView = findViewById(R.id.recycler_inventory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        User user = User.getInstance();
        List<SaleItem> inventory = user.getInventory();

        adapter = new InventoryAdapter(inventory);
        recyclerView.setAdapter(adapter);
    }
}