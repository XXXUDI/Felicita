package com.socompany.felicitashop;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

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

import java.util.HashMap;

import io.paperdb.Paper;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.socompany.felicitashop", appContext.getPackageName());
    }

    @Test
    public void addPicture() {
        Uri imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/felicitashop-57377.appspot.com/o/%D0%97%D0%BD%D1%96%D0%BC%D0%BE%D0%BA%20%D0%B5%D0%BA%D1%80%D0%B0%D0%BD%D0%B0%202023-06-05%20205148.png?alt=media&token=19711713-c73a-4d04-b6ff-219261e4501f");
        StorageTask uploadTask;
        StorageReference storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        if(imageUri != null) {
            StorageReference fileRef = storageProfilePictureRef.child(Paper.book().read(Prevalent.userPhoneKey));
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
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            });
        }
    }
}