package kr.ac.baekseok.recyclehelper.Data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

/*
    작성자 최혁 (griscaf@gmail.com)
    상품 정보의 처리를 보다 효율적으로 하기 위함
 */
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
