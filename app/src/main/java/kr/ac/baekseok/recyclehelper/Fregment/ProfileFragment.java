package kr.ac.baekseok.recyclehelper.Fregment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.ac.baekseok.recyclehelper.Data.DatabaseUtil;
import kr.ac.baekseok.recyclehelper.Data.User;
import kr.ac.baekseok.recyclehelper.InventoryActivity;
import kr.ac.baekseok.recyclehelper.LoginActivity;
import kr.ac.baekseok.recyclehelper.R;

public class ProfileFragment extends Fragment {
    private TextView tv_nickname, tv_point;
    private FirebaseFirestore db;
    private TextView tv_setProfile, tv_inventory, tv_alram;
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
        tv_setProfile = view.findViewById(R.id.btn_setting);
        tv_inventory = view.findViewById(R.id.btn_inventory);
        db = FirebaseFirestore.getInstance();

        DatabaseUtil.readUserInfo(db, user.getEmail(), back -> {
            if (back == null) {
                return;
            }
            User player = User.getInstance();
            tv_nickname.setText(player.getNickname() + "님 환영합니다 !");
            tv_point.setText(player.getPoint() + "P");
        });
        tv_setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("닉네임 변경");

                LinearLayout layout = new LinearLayout(requireContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(16, 16, 16, 16);

                EditText editText = new EditText(requireContext());
                editText.setHint("변경할 닉네임");
                layout.addView(editText);

                builder.setView(layout);

                builder.setPositiveButton("확인", (dialog, which) -> {
                    String inputText = editText.getText().toString();
                    if (!inputText.isEmpty()) {
                        User user = User.getInstance();
                        db.collection("Users")
                                .whereEqualTo("nickname", inputText) // 닉네임 필드 쿼리
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        boolean exists = !task.getResult().isEmpty();

                                        user.setNickname(inputText);
                                        DatabaseUtil.saveUserInfo(db, user, callback -> {
                                            if (callback) {
                                                Toast.makeText(requireContext(), "변경 완료", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(requireContext(), "저장에 문제가 생겼습니다", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(requireContext(), "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(requireContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("취소", (dialog, which) -> {
                    dialog.dismiss();
                });

                builder.show();
            }
        });

        tv_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InventoryActivity.class);
                startActivity(intent);
            }
        });

    }

}