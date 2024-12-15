package kr.ac.baekseok.recyclehelper.Data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import kr.ac.baekseok.recyclehelper.R;

public class SaleItemAdapter extends RecyclerView.Adapter<SaleItemAdapter.SaleItemViewHolder> {
    private List<SaleItem> saleItems;
    private OnItemClickListener listener;

    public SaleItemAdapter(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }
    public interface OnItemClickListener {
        void onItemClick(SaleItem item);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SaleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
        return new SaleItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleItemViewHolder holder, int position) {
        SaleItem item = saleItems.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.price.setText(item.getPrice() + "원");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return saleItems.size();
    }

    public static class SaleItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price;

        public SaleItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_sale_name);
            description = itemView.findViewById(R.id.text_sale_description);
            price = itemView.findViewById(R.id.text_sale_price);
        }
    }
}