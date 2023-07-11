package com.socompany.felicitashop.MainActivities.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.socompany.felicitashop.R;
import com.soundcloud.android.crop.Crop;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    private String category;

    private Uri imageUri, newImageUri;
    private ImageView productImage;
    private EditText productName, productDescription, productPrice;
    private StorageReference productImageRef;
    private Button addButon;
    private DatabaseReference productRef;
    private String productRandomKey, saveCurrentData, saveCurrentTime;
    private String downloadedImageUrl;

    private boolean isCropAvailable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initialize();

        setupListeners();
    }

    private void setupListeners() {
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCropAppAvailable()) {
                    CropImage.activity().start(AddProductActivity.this);
                } else {
                    isCropAvailable = false;
                    Crop.pickImage(AddProductActivity.this);
                }
            }
        });

        addButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                String price = productPrice.getText().toString();
                String description = productDescription.getText().toString();
                Uri productImageUri = imageUri;

                if(!(name == null) || !name.equals("")) {
                    if(!(price == null) || !name.equals("")) {
                        if(!(description == null) || !description.equals("")) {
                            if(!(productImageUri == null)) {
                                saveProductInfo(name, price, description, productImageUri);
                            }
                        }
                    }
                }
            }
        });
    }

    private void saveProductInfo(String name, String price, String description, Uri productImageUri) {


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        saveCurrentData = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentData + saveCurrentTime;

        Uri imageToUpload = fixImageSize(imageUri);

        StorageReference filePath = productImageRef.child(imageToUpload.getLastPathSegment() + productRandomKey + ".WebM"); // Can change to webm

        final UploadTask uploadTask = filePath.putFile(imageToUpload);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddProductActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        //Here change
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadedImageUrl = task.getResult().toString();
                            Toast.makeText(AddProductActivity.this, "Фото збережено", Toast.LENGTH_SHORT).show();
                        }
                        SaveProductInfoToDatabase(name, category, description, price);
                    }
                });
            }
        });
    }

    private Uri fixImageSize(Uri imageUri) {
        try {
            // Получаем путь к файлу изображения
            String imagePath = getRealPathFromURI(imageUri);
            // Получаем размер файла в килобайтах
            long imageSize = new File(imagePath).length() / 1024;
            // Проверяем, превышает ли размер файла 300 КБ
            if (imageSize > 300) {
                // Создаем новый файл для сжатого изображения
                File compressedImageFile = new File(getCacheDir(), "compressed.jpg");
                // Сжимаем изображение и сохраняем его в новый файл
                Bitmap compressedBitmap = BitmapFactory.decodeFile(imagePath);
                FileOutputStream out = new FileOutputStream(compressedImageFile);
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                out.close();
                // Возвращаем новый Uri для сжатого изображения
                return Uri.fromFile(compressedImageFile);
            } else {
                // Если размер файла не превышает 300 КБ, возвращаем исходный Uri
                return imageUri;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return imageUri;
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void SaveProductInfoToDatabase(String name, String category, String description, String price) {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("data", saveCurrentData);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", description);
        productMap.put("image", downloadedImageUrl);
        productMap.put("category", category);
        productMap.put("price", price);
        productMap.put("pname", name);


        if(noNull(productMap)) {
            productRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(AddProductActivity.this, "Товар доданий", Toast.LENGTH_SHORT).show();

                        Intent backIntent = new Intent(AddProductActivity.this, AdminMainActivity.class);
                        startActivity(backIntent);
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(AddProductActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProductActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AddProductActivity.this, "Ooops, error", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean noNull(HashMap<String, Object> productMap) {
        for (Object value : productMap.values()) {
            if (value == null) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(isCropAvailable) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    imageUri = result.getUri();
                    productImage.setImageURI(imageUri);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(AddProductActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            if(resultCode == RESULT_OK){
                if(requestCode == Crop.REQUEST_PICK) {
                    newImageUri = data.getData();
                    Uri destination_uri = Uri.fromFile(new File(getCacheDir(), "cropped"));
                    Crop.of(newImageUri, destination_uri).asSquare().start(AddProductActivity.this);
                    imageUri = Crop.getOutput(data);
                    productImage.setImageURI(imageUri);
                } else if(requestCode == Crop.REQUEST_CROP){
                    handleCrop(resultCode, data);
                }
            }
        }
    }

    private void handleCrop(int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            imageUri = Crop.getOutput(data);
            productImage.setImageURI(imageUri);
        } else if(resultCode == Crop.RESULT_ERROR) {
            String error  = Crop.getError(data).toString();
            Toast.makeText(AddProductActivity.this, error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCropAppAvailable() {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");
            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list != null && list.size() > 0;
    }

    private void initialize() {
        category = getIntent().getExtras().get("category").toString();
        productImage = findViewById(R.id.add2_product_image);
        productName = findViewById(R.id.add2_pName);
        productDescription = findViewById(R.id.add2_pDescription);
        productPrice = findViewById(R.id.add2_pPrice);
        addButon = findViewById(R.id.add2_nextButton);
        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
    }
}