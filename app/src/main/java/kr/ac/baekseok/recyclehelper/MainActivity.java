package kr.ac.baekseok.recyclehelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

import java.util.ArrayList;

import kr.ac.baekseok.recyclehelper.Data.DatabaseUtil;
import kr.ac.baekseok.recyclehelper.Data.SaleItem;
import kr.ac.baekseok.recyclehelper.Data.User;
import kr.ac.baekseok.recyclehelper.Fregment.CameraFragment;
import kr.ac.baekseok.recyclehelper.Fregment.CommunityFragment;
import kr.ac.baekseok.recyclehelper.Fregment.HomeFragment;
import kr.ac.baekseok.recyclehelper.Fregment.ProfileFragment;
import kr.ac.baekseok.recyclehelper.Fregment.ShopFragment;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            DatabaseUtil.readUserInfo(db, user.getEmail(), callback -> {
                User player = (User)callback;
                User newUser = User.getInstance();
                newUser.init(player.getEmail(), player.getNickname(), player.getPoint(), player.getRate(), player.getInventory() == null ? new ArrayList<SaleItem>() : player.getInventory());

                Log.d("Main", "사용자 초기화 완료 ("+newUser.getNickname()+")");

            });
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


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
            } else if (item.getItemId() == R.id.nav_shop) {
                selectedFragment = new ShopFragment();
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