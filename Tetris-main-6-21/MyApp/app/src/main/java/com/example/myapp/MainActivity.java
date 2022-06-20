package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.example.myapp.hw.HwContainer;
import com.example.myapp.ults.DAOHelper;

import com.example.myapp.hw.Piezo;

import com.example.myapp.effect.TextLCD.TextLCDAgent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnPlayNew, btnExitApp;
    private final Piezo piezo = HwContainer.piezo;
    TextLCDAgent textLCDAgent = new TextLCDAgent();

    // Used to load the 'myapp' library on application startup.
    static {
        System.loadLibrary("myapp");
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DAOHelper.createDatabaseIfNotExists(getApplicationContext());
        setContentView(R.layout.activity_main);

        btnPlayNew = findViewById(R.id.btnPlayNew);
        btnExitApp = findViewById(R.id.btnExitApp);

        btnPlayNew.setOnClickListener(this);
        btnExitApp.setOnClickListener(this);

        piezo.out(50, 100);
        piezo.out(0, 100);
        piezo.out(50, 100);
        piezo.out(0, 100);
        piezo.out(50, 100);
        piezo.out(0, 100);
        piezo.out(50, 100);
        piezo.out(0, 100);

        textLCDAgent.print("Tetris Game", "Tetris Game");
    }

    @Override
    protected void onResume() {
        super.onResume();
        textLCDAgent.print("Tetris Game", "Tetris Game");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnPlayNew): {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                break;
            }
            case (R.id.btnExitApp): {
                piezo.out(20, 100);
                piezo.out(0, 100);
                finish();
                break;
            }
        }
    }
}
