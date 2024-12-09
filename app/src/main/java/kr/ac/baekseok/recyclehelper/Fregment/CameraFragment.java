package kr.ac.baekseok.recyclehelper.Fregment;

import static kr.ac.baekseok.recyclehelper.Data.ProductStorage.SaveProduct;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import kr.ac.baekseok.recyclehelper.Data.DatabaseManager;
import kr.ac.baekseok.recyclehelper.Data.Product;
import kr.ac.baekseok.recyclehelper.Data.ProductStorage;
import kr.ac.baekseok.recyclehelper.R;

public class CameraFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_FILE = 1;

    private ImageView imgPreview;
    private TextView txtResult;
    private Button btnShow;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        imgPreview = view.findViewById(R.id.img_preview);
        txtResult = view.findViewById(R.id.txt_result);
        btnShow = view.findViewById(R.id.btn_how);
        db = DatabaseManager.getInstance().getDatabase();

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "재활용 정보 출력 예정", Toast.LENGTH_SHORT).show();
            }
        });

        showOptionDialog();

        return view;
    }

    private void showOptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("사진 선택")
                .setMessage("사진을 가져올 방법을 선택하세요.")
                .setPositiveButton("카메라로 촬영하기", (dialog, which) -> openCamera())
                .setNegativeButton("갤러리에서 가져오기", (dialog, which) -> openGallery())
                .setNeutralButton("취소", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(getContext(), "카메라를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // 이미지 파일만 선택 가능
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == requireActivity().RESULT_OK && data != null) {
            if (requestCode == REQUEST_FILE) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {

                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().getContentResolver(),
                                selectedImageUri
                        );
                        // 미리보기 이미지 설정 (수정필요)
                        imgPreview.setImageBitmap(imageBitmap);

                        // 바코드 스캔 (정확도 대박)
                        scanBarcodeFromBitmap(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "이미지를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "파일 URI를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getContext(), "작업이 취소되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanBarcodeFromBitmap(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        BarcodeScanner scanner = BarcodeScanning.getClient();

        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    if (barcodes.isEmpty()) {
                        txtResult.setText("바코드를 찾지 못했습니다.");
                        return;
                    }
                    StringBuilder result = new StringBuilder();
                    AtomicInteger remainingTasks = new AtomicInteger(barcodes.size());
                    for (Barcode barcode : barcodes) {
                        Log.d("BarcodeProcessing", "처리 중인 바코드: " + barcode.getDisplayValue());
                        ProductStorage.ReadProduct(db, barcode.getDisplayValue(), product -> {
                            if (product == null) {
                                Log.d("BarcodeProcessing", "제품 정보 없음: " + barcode.getDisplayValue());
                                showAddProductDialog(barcode.getDisplayValue(), db);
                                return;
                            } else {
                                Log.d("FirebaseQuery", "제품 정보: " + ((Product) product).getName());
                            }
                            result.append("바코드 값: ").append(barcode.getDisplayValue()).append("\n");
                            result.append("제품 이름: ").append(((Product)product).getName()).append("\n");
                            result.append("주요 재질: ").append(((Product)product).getMaterial()).append("\n");
                            result.append("재활용 가능 여부 : ").append(((Product)product).getRecyclable() ? "예" : "아니오").append("\n");
                            result.append("카테고리 : ").append(((Product)product).getCategory()).append("\n");
                            requireActivity().runOnUiThread(() -> txtResult.setText(result.toString()));
                            Log.d("ResultContent", "결과 내용: " + result.toString());
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "바코드 스캔에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("BarcodeScan", "Error scanning barcode", e);
                });
    }

    private void showAddProductDialog(String barcodeValue, FirebaseFirestore db) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("제품 정보 추가");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText edtProductName = dialogView.findViewById(R.id.edt_product_name);
        EditText edtProductCategory = dialogView.findViewById(R.id.edt_product_category);
        EditText edtMaterial = dialogView.findViewById(R.id.edt_product_material);
        CheckBox chkRecyclable = dialogView.findViewById(R.id.chk_recyclable);
        builder.setPositiveButton("등록", (dialog, which) -> {
            String productName = edtProductName.getText().toString().trim();
            String productCategory = edtProductCategory.getText().toString().trim();
            boolean isRecyclable = chkRecyclable.isChecked();
            String productMaterial = edtMaterial.getText().toString().trim();
            if (!productName.isEmpty() && !productCategory.isEmpty()) {
                // Firebase ㅇㅅㅇ
                Product newProduct = new Product(barcodeValue, productName, productMaterial, isRecyclable, productCategory);
                SaveProduct(db, newProduct, isSuccess -> {
                    Toast.makeText(getContext(), "제품 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(getContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}