package kr.ac.baekseok.recyclehelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

import kr.ac.baekseok.recyclehelper.Data.DatabaseManager;
import kr.ac.baekseok.recyclehelper.Data.DatabaseUtil;
import kr.ac.baekseok.recyclehelper.Data.Product;
import kr.ac.baekseok.recyclehelper.Data.ProductStorage;
import kr.ac.baekseok.recyclehelper.Data.User;
import kr.ac.baekseok.recyclehelper.Fregment.CameraFragment;
import kr.ac.baekseok.recyclehelper.Fregment.CommunityFragment;
import kr.ac.baekseok.recyclehelper.Fregment.HomeFragment;
import kr.ac.baekseok.recyclehelper.Fregment.ProfileFragment;
import kr.ac.baekseok.recyclehelper.R;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = DatabaseManager.getInstance().getDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        FirebaseApp.initializeApp(this);

        // 커뮤니티를 위한 로그인 확인
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            DatabaseUtil.readUserInfo(db, user.getEmail(), callback -> {
                User player = (User)callback;
                User newUser = User.getInstance();
                newUser.init(player.getEmail(), player.getNickname(), player.getPoint());

                Log.d("Main", "사용자 초기화 완료 ("+newUser.getNickname()+")");

            });
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        // 초기 화면 설정
        loadFragment(new HomeFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.nav_camera) {
                selectedFragment = new CameraFragment();
            } else if (item.getItemId() == R.id.nav_community) {
                selectedFragment = new CommunityFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}