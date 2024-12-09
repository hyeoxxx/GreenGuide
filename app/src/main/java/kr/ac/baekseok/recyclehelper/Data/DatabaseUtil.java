package kr.ac.baekseok.recyclehelper.Data;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseUtil {

    public static void saveUserInfo(FirebaseFirestore db, User user, ProductStorage.FirestoreCallback callback) {
        db.collection("Users").document(user.getEmail())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error On Saving : " + e);
                    callback.onCallback(false);
                });
    }
    public static void readUserInfo(FirebaseFirestore db, String email, ProductStorage.FirestoreCallbackReading callback) {
        db.collection("Users").document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        callback.onCallback(user);
                    } else {
                        // 문서가 존재하지 않을 경우 null 전달
                        callback.onCallback(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error getting document", e);
                    callback.onCallback(null);
                });
    }
    public static interface FirestoreCallback {
        void onCallback(boolean isSuccess);
    }
    public interface FirestoreCallbackReading<T> {
        void onCallback(T data);
    }
}
