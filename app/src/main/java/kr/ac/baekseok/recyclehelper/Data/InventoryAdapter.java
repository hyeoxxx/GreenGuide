package kr.ac.baekseok.recyclehelper.Data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import kr.ac.baekseok.recyclehelper.R;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private List<SaleItem> inventory;

    public InventoryAdapter(List<SaleItem> inventory) {
        this.inventory = inventory;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        SaleItem item = inventory.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.code.setText("바코드 : " + item.getCode());
    }

    @Override
    public int getItemCount() {
        return inventory.size();
    }

    public static class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, code;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_inventory_name);
            description = itemView.findViewById(R.id.text_inventory_description);
            code = itemView.findViewById(R.id.text_inventory_code);
        }
    }
}