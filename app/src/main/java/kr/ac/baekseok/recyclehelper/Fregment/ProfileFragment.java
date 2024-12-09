package kr.ac.baekseok.recyclehelper.Fregment;

import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.ac.baekseok.recyclehelper.Data.DatabaseManager;
import kr.ac.baekseok.recyclehelper.Data.DatabaseUtil;
import kr.ac.baekseok.recyclehelper.Data.User;
import kr.ac.baekseok.recyclehelper.R;

public class ProfileFragment extends Fragment {
    private TextView tv_nickname, tv_point;
    private FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_nickname = view.findViewById(R.id.tv_user_nickname);
        tv_point = view.findViewById(R.id.tv_user_point);
        db = DatabaseManager.getInstance().getDatabase();
        if (db == null) {
            // 예외처리 꼭 해야할듯
            return;
        }
        DatabaseUtil.readUserInfo(db, user.getEmail(), back -> {
            if (back == null) {
                DatabaseUtil.saveUserInfo(db, new User(user.getEmail(), "빵빵이", 10000), isSuccess -> {
                    if (isSuccess) {
                        Toast.makeText(getContext(), "임시데이터 입력 성공", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            User player = ((User) back);
            tv_nickname.setText(player.getNickname() + "님 환영합니다 !");
            tv_point.setText(player.getPoint() + "P");
        });


    }

}