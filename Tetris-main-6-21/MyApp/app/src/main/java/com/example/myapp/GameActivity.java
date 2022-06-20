package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.dao.SettingDAO;
import com.example.myapp.dto.Setting;
import com.example.myapp.effect.DotMatrix.DotMatrixAgent;
import com.example.myapp.effect.FullColorLed.EmitLedOnClearLine;
import com.example.myapp.effect.FullColorLed.EmitLedOnStartGame;
import com.example.myapp.effect.LED.LEDAgent;
import com.example.myapp.effect.Segment.SegmentAgent;
import com.example.myapp.effect.TextLCD.TextLCDAgent;
import com.example.myapp.hw.HwContainer;
import com.example.myapp.hw.Piezo;

import com.example.myapp.gameplay.Board;
import com.example.myapp.gameplay.BoardView;
import com.example.myapp.gameplay.PieceView;


class TextLCDRunnable implements Runnable {
    TextLCDAgent effector = new TextLCDAgent();
    String line1 = "", line2 = "";

    public void setTextOnLine1(String line) {
        line1 = line;
    }

    public void setTextOnLine2(String line) {
        line2 = line;
    }

    public void setTextOnLines(String line1, String line2) {
        setTextOnLine1(line1);
        setTextOnLine2(line2);
    }

    public void stop() {
        effector.stop();
    }

    @Override
    public void run() {
        effector.print(line1, line2);
    }
}

class FullLedEmitRunnableOnStartGame implements Runnable {
    @Override
    public void run() {
        EmitLedOnStartGame effector = new EmitLedOnStartGame();
        effector.emit();
    }
}

class FullLedEmitRunnableOnClearLine implements Runnable {
    @Override
    public void run() {
        EmitLedOnClearLine effector = new EmitLedOnClearLine();
        effector.emit();
    }
}

public class GameActivity extends AppCompatActivity implements Board.OnLineClearListener, Board.OnGameOverListener {
    BoardView boardView;
    TextView scoreView;
    int score = 0, combo = 0; // score = combo * 100 + 1000
    Boolean isGameOver = false;

    TextLCDRunnable textLCDRunnable = new TextLCDRunnable();
    Thread textLCDThread = new Thread(textLCDRunnable);

    DotMatrixAgent dMatrix = new DotMatrixAgent();

    SegmentAgent segment = new SegmentAgent(); // start segment thread by invoking its constructor

    LEDAgent ledAgent = new LEDAgent();

    FullLedEmitRunnableOnStartGame fLedEmitRunnableOSGame = new FullLedEmitRunnableOnStartGame();
    Thread fLedEmitThread = new Thread(fLedEmitRunnableOSGame);
    FullLedEmitRunnableOnClearLine fLedEmitRunnableOCLine = new FullLedEmitRunnableOnClearLine();
    Thread fLedEmitThreadOCLine = new Thread(fLedEmitRunnableOCLine);

    private final Piezo piezo = HwContainer.piezo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        piezo.out(30, 100);
        piezo.out(0, 100);
        textLCDRunnable.setTextOnLines("THE CHALLENGER", "THE CHALLENGER");
        textLCDThread.start();

        dMatrix.print("TETRIS GAME", 10, Integer.MAX_VALUE);

        segment.print(0);

        fLedEmitThread.start();

        ledAgent.shutdownAll();

        Setting setting = SettingDAO.getInstance().getSetting(getApplicationContext());
        boardView = this.findViewById(R.id.boardView);

        PieceView holdView = this.findViewById(R.id.holdPieceView);
        holdView.setOnClickListener((View) -> holdView.setPiece(boardView.hold()));

        PieceView nextPieceView = this.findViewById(R.id.nextPieceView);
        boardView.setOnNextPieceListener(nextPieceView::setPiece);

        boardView.setOnLineClearListener(this);
        boardView.setOnGameOverListener(this);

        boardView.setLineClearScore((int) setting.getLineScore());

        scoreView = this.findViewById(R.id.scoreTextView);
        scoreView.setText("0");
    }

    @Override
    protected void onPause() {
        if (!isGameOver) {
            dMatrix.stop();
        }
        ledAgent.shutdownAll();
        textLCDRunnable.stop();
        segment.stopThread();
        boardView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGameOver = false;
    }

    @Override
    public void onLineClear(int addScore) {
        combo += 1;
        score += addScore + combo * 100;
        String scoreString = score + "";
        scoreView.setText(scoreString);

        ledAgent.increment();
        segment.print(score);
        fLedEmitThreadOCLine.start();
    }

    @Override
    public void onGameOver() {
        ledAgent.shutdownAll();

        fLedEmitThreadOCLine.interrupt();
        fLedEmitThread.interrupt();

        dMatrix.print("GAME OVER", 10, 3);
        isGameOver = true;

        Intent intent = new Intent(this, GameOverActivity.class);
        startActivity(intent);
        segment.stop();
        finish();
    }
}