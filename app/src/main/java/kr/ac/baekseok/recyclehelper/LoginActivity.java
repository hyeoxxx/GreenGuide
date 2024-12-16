package kr.ac.baekseok.recyclehelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import kr.ac.baekseok.recyclehelper.Data.DatabaseUtil;
import kr.ac.baekseok.recyclehelper.Data.SaleItem;
import kr.ac.baekseok.recyclehelper.Data.User;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    } else {
                        Toast.makeText(this, "다음과 같은 이유로 로그인에 실패하였습니다 : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "빈 입력란이 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_register, null);
        builder.setView(dialogView);

        EditText edtRegEmail = dialogView.findViewById(R.id.edt_reg_email);
        EditText edtRegPassword = dialogView.findViewById(R.id.edt_reg_password);
        edtRegPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtRegEmail.setText(edtEmail.getText());
        edtRegPassword.setText(edtPassword.getText());
        EditText edtRegNick = dialogView.findViewById(R.id.edt_reg_nickname);


        builder.setPositiveButton("회원가입", (dialog, which) -> {
            String nickName = edtRegNick.getText().toString().trim();
            if (!nickName.isEmpty()) {
                checkNickNameExists(nickName, exist -> {
                            if (exist) {
                                Toast.makeText(this, "닉네임이 중복되었습니다. 다른 닉네임을 사용해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        List<SaleItem> list = new ArrayList<SaleItem>();
                                                        User user = new User(email, nickName, 0, 0, list);
                                                        DatabaseUtil.saveUserInfo(db, user, isSuccess -> {
                                                            Toast.makeText(this, "회원가입 성공 !", Toast.LENGTH_SHORT).show();
                                                        });
                                                    } else {
                                                        Toast.makeText(this, "다음과 같은 사유로 회원가입에 실패하였습니다 : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                        );
                            }
                        }
                );
            } else {
                Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
    interface Callback {
        void onResult(boolean exists);
    }

    private void checkNickNameExists(String nick, Callback callback) {
        db.collection("Users")
                .whereEqualTo("nickname", nick)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        boolean exists = !task.getResult().isEmpty();
                        callback.onResult(exists);
                    } else {
                        callback.onResult(false);
                    }
                });
    }
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}