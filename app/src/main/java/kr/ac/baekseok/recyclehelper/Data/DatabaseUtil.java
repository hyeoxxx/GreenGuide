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
                        callback.onCallback(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error getting document", e);
                    callback.onCallback(null);
                });
    }
}
