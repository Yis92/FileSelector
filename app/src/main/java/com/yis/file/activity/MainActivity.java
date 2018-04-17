package com.yis.file.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yis.file.R;

public class MainActivity extends AppCompatActivity {

    private Button btnFolder;
    private Button btnMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFolder = findViewById(R.id.btn_folder);
        btnMedia = findViewById(R.id.btn_media);

        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FolderActivity.class);
                startActivity(intent);
            }
        });

        btnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MediaStoreActivity.class);
                startActivity(intent);
            }
        });
    }

}
