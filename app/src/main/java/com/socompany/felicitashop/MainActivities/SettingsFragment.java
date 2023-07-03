package com.socompany.felicitashop.MainActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.socompany.felicitashop.Prevalent.Prevalent;
import com.socompany.felicitashop.R;
import com.socompany.felicitashop.Tools.Parser;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class SettingsFragment extends Fragment {

    private CircleImageView userImage;
    private TextView userName;
    private TextView userPhone;
    private Button changeProfileButton;
    private FrameLayout writeUs, rules, question;

    private Uri imageUri, newImageUri;
    private StorageReference storageProfilePictureRef;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        initialize(view);

        displayUserData();
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCropAppAvailable()) {
                    CropImage.activity().start(getContext(), SettingsFragment.this);
                } else {
                    loadImageFromDownloads();
                }
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                imageUri = result.getUri();
                userImage.setImageURI(imageUri);
                uploadImage(imageUri);
            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCropAppAvailable() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getContext().getPackageManager().queryIntentActivities(intent, 0);
        return list.size() > 0;
    }

    private void uploadImage(Uri imageUri) {
        if(imageUri != null) {
            StorageReference fileRef = storageProfilePictureRef.child(Paper.book().read(Prevalent.userPhoneKey) + ".WebP");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()) {

                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {
                        Uri dowloadUrl = task.getResult();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Paper.book().read(Prevalent.userPhoneKey));
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, Object> updateImage = new HashMap<>();
                                updateImage.put("Image", dowloadUrl.toString());
                                reference.updateChildren(updateImage);
                                Toast.makeText(getActivity(), "Фото збережено", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Помилка: " + error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }

    private void displayUserData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Paper.book().read(Prevalent.userPhoneKey));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.child("Image").exists()) {
                        String image = snapshot.child("Image").getValue().toString();
                        String name = snapshot.child("Name").getValue().toString();
                        String phone = snapshot.child("Phone").getValue().toString();

                        Picasso.get().load(image).into(userImage);
                        userName.setText(name);
                        userPhone.setText("+38 " + Parser.phoneParser(phone));

                    }
                    else {
                        String name = snapshot.child("Name").getValue().toString();
                        String phone = snapshot.child("Phone").getValue().toString();

                        userName.setText(name);
                        userPhone.setText("+38 " + Parser.phoneParser(phone));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void initialize(View view) {
        FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        userImage = view.findViewById(R.id.settings_profileImage);
        userName = view.findViewById(R.id.settings_name);
        userPhone = view.findViewById(R.id.settings_number);
        changeProfileButton = view.findViewById(R.id.settings_changeProfile);
        writeUs = view.findViewById(R.id.settings_write);
        rules = view.findViewById(R.id.settings_rules);
        question = view.findViewById(R.id.settings_questions);

    }
    private void loadImageFromDownloads() {
        String downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        String fileName = "ok.jpg"; // Замените "your_image.jpg" на имя вашего файла
        String imagePath = downloadsDirectory + "/" + fileName;
        imageUri = Uri.parse("file://" + imagePath);
        userImage.setImageURI(imageUri);
    }
}