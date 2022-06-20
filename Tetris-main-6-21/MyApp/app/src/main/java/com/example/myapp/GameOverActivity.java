package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapp.effect.FullColorLed.EmitLedOnGameOver;

class FullLedEmitRunnableOnGameOver implements Runnable {
    @Override
    public void run() {
        EmitLedOnGameOver effector = new EmitLedOnGameOver();
        effector.emit();
    }
}

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnBackToMain;

    FullLedEmitRunnableOnGameOver fLedEmitRunnableOGOver = new FullLedEmitRunnableOnGameOver();
    Thread fLedEmitThreadOGOver = new Thread(fLedEmitRunnableOGOver);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(this);

        fLedEmitThreadOGOver.start();
    }

    @Override
    public void onClick(View view) {
        fLedEmitThreadOGOver.interrupt();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        fLedEmitThreadOGOver.interrupt();
    }
}