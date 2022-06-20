package com.example.myapp.gameplay;

import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.example.myapp.Coord;

import com.example.myapp.effect.FullColorLed.EmitLedOnArrivedPiece;

import com.example.myapp.effect.LED.LEDAgent;

class FullLedEmitRunnableOnArrived implements Runnable {
    private int pieceColor = 0;

    public void setPieceColor(int color) {
        if (pieceColor >= 0 || pieceColor <= 255)
            this.pieceColor = color;
    }

    @Override
    public void run() {
        EmitLedOnArrivedPiece effector = new EmitLedOnArrivedPiece(pieceColor);
        effector.emit();
    }
}

public class Board {
    public interface OnNextPieceListener {
        void onNewNextPiece(PieceType nextPiece);
    }

    public interface OnLineClearListener {
        void onLineClear(int score);
    }

    public interface OnGameOverListener {
        void onGameOver();
    }

    static final PieceType[] pieceTypes = PieceType.values();

    final Random rng = new Random();

    BoardTimer timer;

    int rows, columns;
    int lineClearScore = 800;
    int[][] pile;

    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            tick();
        }
    };

    public OnNextPieceListener nextPieceListener;
    public OnLineClearListener lineClearListener;
    public OnGameOverListener gameOverListener;

    final Queue<Piece> queue = new LinkedList<>();
    Piece currentPiece;
    Piece holdPiece;

    LEDAgent ledAgent = new LEDAgent();

    public Board(int rows, int columns, List<ArrayList<Integer>> levels) {
        if (columns < 4)
            throw new IllegalArgumentException("Board needs at least 4 columns");

        this.rows = rows;
        this.columns = columns;

        LinkedList<Pair<Integer, Integer>> levelsQueue = new LinkedList<>();
        for (ArrayList<Integer> level : levels)
            levelsQueue.add(new Pair<>(level.get(0), level.get(1)));

        timer = new BoardTimer(handler, levelsQueue);

        pile = new int[rows][columns];
        generateNewPiece();
        generateNewPiece();
        currentPiece = queue.poll();
        if (nextPieceListener != null && queue.peek() != null) {
            nextPieceListener.onNewNextPiece(queue.peek().getType());
        }
    }

    public void startGame() {
        timer.start();
    }

    public void pause() {
        timer.pause();
    }

    void generateNewPiece() {
        PieceType type = pieceTypes[rng.nextInt(pieceTypes.length)];
        queue.add(new Piece(type, rng.nextInt(columns / 2)));
    }

    void tick() {
        Piece piece = getCurrentPiece();
        if (piece == null) {
            generateNewPiece();
            return;
        }

        drop();

    }

    /**
     * @return PieceType of holding piece
     */
    public PieceType hold() {
        Piece oldHold = holdPiece;
        holdPiece = getCurrentPiece();
        if (oldHold == null)
            pushNextPiece();
        else
            currentPiece = oldHold;

        return holdPiece.getType();
    }

    void drop() {
        Piece piece = getCurrentPiece();
        Coord[] colliders = piece.getDownColliders();
        for (Coord coll : colliders)
            // arrived point
            if (coll.x >= rows || pile[coll.x][coll.y] != 0) {
                addToPile(piece);

                FullLedEmitRunnableOnArrived runnable = new FullLedEmitRunnableOnArrived();
                runnable.setPieceColor(piece.color);
                Thread th = new Thread(runnable);
                th.start();

                pushNextPiece();
                return;
            }
        piece.move(1, 0);
    }

    void pushNextPiece() {
        generateNewPiece();
        currentPiece = queue.poll();
        if (nextPieceListener != null && queue.peek() != null) {
            nextPieceListener.onNewNextPiece(queue.peek().getType());
        }
    }

    void addToPile(Piece piece) {
        Log.d("AddToPile", piece.getCoord().toString());
        if (piece.getCoord().x <= 0) {
            gameOverListener.onGameOver();
            timer.pause();
            return;
        }

        for (Coord coord : piece.getBlockPositions())
            pile[coord.x][coord.y] = piece.color;

        int diff = 0;
        for (int i = rows - 1; i >= 0; --i) {
            int cnt = 0;
            for (int j = 0; j < columns; ++j)
                if (pile[i][j] != 0)
                    ++cnt;
            if (cnt >= columns) {
                ++diff;
                if (lineClearListener != null)
                    lineClearListener.onLineClear(lineClearScore);
            }
            else
                ledAgent.shutdownAll();

            if (i - diff >= 0)
                pile[i] = pile[i - diff];
            else
                pile[i] = new int[columns];
        }
    }

    public void rotate(int dir) {
        Piece p = getCurrentPiece();
        p.rotate(dir);
        // Check if the rotation is valid
        int dx = 0, dy = 0;
        for (Coord cr : p.getBlockPositions()) {
            dx = Math.max(dx, cr.x - rows + 1);
            dy = Math.max(dy, cr.y - columns + 1);
        }
        Log.d("Rotate move", dx + "," + dy);
        p.move(-dx, -dy);
        for (Coord cr : p.getBlockPositions()) {
            Log.d("After rotate", cr.x + "," + cr.y);
        }
    }

    public void steer(int dir) {
        Piece piece = getCurrentPiece();
        if (piece == null)
            return;

        Coord[] colliders = (dir < 0) ? piece.getLeftColliders() : piece.getRightColliders();
        for (Coord coll : colliders)
            if (coll.y >= columns || coll.y < 0 || pile[coll.x][coll.y] != 0)
                return;
        piece.move(0, dir);
    }

    public void hardDrop() {
        Piece piece = queue.peek();
        while (piece == queue.peek())
            drop();
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public int[][] getPile() {
        return pile;
    }

    public void setLineClearScore(int score) {
        lineClearScore = score;
    }
}