package com.example.datastoragemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.activity_main__tv__info);
        final Button btnColor = findViewById(R.id.activity_main__btn_change_color);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             tvInfo.setTextColor(Color.BLUE);
            }
        });

        SharedPreferences preferences = getSharedPreferences("any name you want", Context.MODE_PRIVATE);
        int textColor = preferences.getInt("InfoTextColor", -1);
        if (textColor != -1) {
            tvInfo.setTextColor(textColor);
        }

        final EditText etInfo = findViewById(R.id.activity_main__et__info_internally);
        final Button btnInternalStorage = findViewById(R.id.activity_main__btn__internal_storage);
        btnInternalStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToInternalStorage(etInfo.getText().toString() + "\n");
                etInfo.setText("");

            }
        });

        final Button btnExternalStorage = findViewById(R.id.activity_main__btn__external_storage);
        btnExternalStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToExternalStorage(etInfo.getText().toString() + "\n");
                etInfo.setText("");
            }
        });

    }

    private void sendDataToExternalStorage(String data) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File downloadFolder =
                    new File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "newDirectory/newSubdirectory");

            boolean isSuccessful = downloadFolder.mkdirs();
                if (!isSuccessful) {
                    Toast.makeText(this,"external storage access error",Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void sendDataToInternalStorage(String data) {
        try {
            FileOutputStream commChannel = openFileOutput("InternalFile.txt",Context.MODE_APPEND );
            commChannel.write(data.getBytes());
            commChannel.close();
            Toast.makeText(this, "info stored", Toast.LENGTH_SHORT).show();
            Toast.makeText(this,fileList()[0], Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"IO error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences preferences = getSharedPreferences("any name you want",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("InfoTextColor", tvInfo.getCurrentTextColor());
        editor.apply();

//        SharedPreferences.Editor editor =
//                getSharedPreferences("", Context.MODE_PRIVATE).edit();
    }
}
