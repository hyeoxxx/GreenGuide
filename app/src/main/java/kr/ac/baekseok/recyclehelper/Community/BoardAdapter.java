package kr.ac.baekseok.recyclehelper.Community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.baekseok.recyclehelper.R;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private final List<Board> boardList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Board board);
    }

    public BoardAdapter(List<Board> boardList, OnItemClickListener listener) {
        this.boardList = boardList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Board board = boardList.get(position);
        holder.name.setText(board.getName());
        holder.description.setText(board.getDescription());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(board));
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public static class BoardViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_board_name);
            description = itemView.findViewById(R.id.text_board_description);
        }
    }
}