package kr.ac.baekseok.recyclehelper.Fregment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.baekseok.recyclehelper.Community.Board;
import kr.ac.baekseok.recyclehelper.Community.BoardAdapter;
import kr.ac.baekseok.recyclehelper.PostListActivity;
import kr.ac.baekseok.recyclehelper.R;

public class CommunityFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Board> boardList = new ArrayList<>();
    private BoardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_boards);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 게시판 데이터 초기화
        initializeBoardData();

        // 어댑터 설정
        adapter = new BoardAdapter(boardList, board -> {
            // 게시판 클릭 시 해당 게시물 목록으로 이동
            Intent intent = new Intent(getActivity(), PostListActivity.class);
            intent.putExtra("boardId", board.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initializeBoardData() {
        boardList.add(new Board("3", "공지사항", "중요한 공지사항이 올라오는 곳입니다."));
        boardList.add(new Board("1", "자유게시판", "일반적인 이야기를 나누는 곳입니다."));
        boardList.add(new Board("2", "질문게시판", "질문과 답변을 주고받는 곳입니다."));
    }
}