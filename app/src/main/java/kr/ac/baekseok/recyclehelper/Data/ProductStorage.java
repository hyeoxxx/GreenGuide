package kr.ac.baekseok.recyclehelper.Data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class ProductStorage {

    public static void SaveProduct(FirebaseFirestore db, Product item, FirestoreCallback callback) {
        db.collection("Products").document(item.getNumber())
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error On Saving : " + e);
                   callback.onCallback(false);
                });
    }
    public static void ReadProduct(FirebaseFirestore db, String itemNumber, FirestoreCallbackReading callback) {
        db.collection("Products").document(itemNumber)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Product product = documentSnapshot.toObject(Product.class);
                    callback.onCallback(product);
                } else {
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
