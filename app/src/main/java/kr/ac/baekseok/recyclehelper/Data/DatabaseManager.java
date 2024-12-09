package kr.ac.baekseok.recyclehelper.Data;

import com.google.firebase.firestore.FirebaseFirestore;
// 누수 생각 해야할듯 ?
// 사용법은 DatabaseManager.getInstance().getDatabase(). ~ 입니다

public class DatabaseManager {
    private static DatabaseManager instance;
    private FirebaseFirestore db;

    private DatabaseManager() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public FirebaseFirestore getDatabase() {
        return db;
    }
}
