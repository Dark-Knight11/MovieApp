package com.sies.movierecomendations;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FullScreenPFP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_pfp);
        ImageView full = findViewById(R.id.imageView);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("CineMania", Context.MODE_PRIVATE);
        File filepath  = directory.getAbsoluteFile();

        try {
            File f = new File(filepath, "profile.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            full.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // External Storage //
/*      File filepath = Environment.getExternalStorageDirectory();
        Bitmap bitmap = BitmapFactory.decodeFile(filepath.getAbsolutePath() + "/CineMania/ProfilePic.jpg");
        pfp.setImageBitmap(bitmap);
        long MAXBYTES = 4096*4096;
        profile.getPath().getBytes(MAXBYTES).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            pfp.setImageBitmap(bitmap);
        }).addOnFailureListener(Throwable::printStackTrace); */


/*        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsoluteFile()+"/CineMania/");
        dir.mkdir();
        File file = new File(dir, "ProfilePic.jpg");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } */
    }
}