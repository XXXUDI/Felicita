package com.socompany.felicitashop;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        FirebaseDatabase ref = FirebaseDatabase.getInstance();
        DatabaseReference reference = ref.getReference().child("Users");
        assertEquals(4, 2 + 2);
    }
}