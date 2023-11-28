package com.example.sudokuesqueleto;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class GameBoard extends View {
    private final int boardColor;
    private int cellSize;
    private int selectedRow = -1;
    private int selectedCol = -1;
    public List<RemovedNumber> removedNumbers; //cambiado a public
    private int inputNumber = 0;
    private int[][] sudokuBoard;
    private OnGameOverListener gameOverListener; //variable creada
    private SudokuGenerator generator;
    public boolean[][] editableCells; //cambiado a public
    private final Paint cellHighlightPaint = new Paint();
    private final Paint highlightPaint = new Paint();
    private final Paint squareHighlightPaint = new Paint();
    private final Paint boardPaint = new Paint();
    private final Paint textPaint = new Paint();
    private final Paint inputTextPaint = new Paint();

    public void setOnGameOverListener(OnGameOverListener listener) {
        this.gameOverListener = listener;
    }

    interface OnGameOverListener {
        void onGameOver();
    }

    public GameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.GameBoard,
                0, 0);
        try {
            boardColor = a.getInteger(R.styleable.GameBoard_boardColor, 0);
        }
        finally {
            a.recycle();
        }

        highlightPaint.setColor(Color.parseColor("#88AAB1FF"));
        highlightPaint.setStyle(Paint.Style.FILL);

        cellHighlightPaint.setColor(Color.parseColor("#886200EE"));
        cellHighlightPaint.setStyle(Paint.Style.FILL);

        squareHighlightPaint.setColor(Color.parseColor("#88D1D7FF"));
        squareHighlightPaint.setStyle(Paint.Style.FILL);

        generator = new SudokuGenerator();
        sudokuBoard = generator.generate();
        removedNumbers = generator.removeNumbers(20);

        editableCells = new boolean[9][9];
        for(RemovedNumber removedNumber : removedNumbers) {
            editableCells[removedNumber.row][removedNumber.col] = true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasure, int heightMeasure) {
        super.onMeasure(widthMeasure, heightMeasure);

        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
        cellSize = dimension / 9;

        setMeasuredDimension(dimension, dimension);

    }

    @Override
    protected void onDraw(Canvas canvas){

        boardPaint.setStyle(Paint.Style.STROKE);
        boardPaint.setStrokeWidth(16);
        boardPaint.setColor(boardColor);
        boardPaint.setAntiAlias(true);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(cellSize * 0.7f);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setSubpixelText(true);
        textPaint.setLinearText(true);

        inputTextPaint.setStyle(Paint.Style.FILL);
        inputTextPaint.setTextSize(cellSize * 0.7f);
        inputTextPaint.setColor(Color.parseColor("#FFA500"));
        inputTextPaint.setTextAlign(Paint.Align.CENTER);
        inputTextPaint.setAntiAlias(true);
        inputTextPaint.setSubpixelText(true);
        inputTextPaint.setLinearText(true);

        canvas.drawRect(0,0,getWidth(),getHeight(),boardPaint);

        if (selectedRow != -1 && selectedCol != -1) {
            canvas.drawRect(0, selectedRow * cellSize, getWidth(), (selectedRow + 1) * cellSize, highlightPaint);
            canvas.drawRect(selectedCol * cellSize, 0, (selectedCol + 1) * cellSize, getHeight(), highlightPaint);

            int areaStartRow = selectedRow / 3 * 3;
            int areaStartCol = selectedCol / 3 * 3;
            canvas.drawRect(areaStartCol * cellSize, areaStartRow * cellSize, (areaStartCol + 3) * cellSize, (areaStartRow + 3) * cellSize, squareHighlightPaint);

            canvas.drawRect(selectedCol * cellSize, selectedRow * cellSize, (selectedCol + 1) * cellSize, (selectedRow + 1) * cellSize, cellHighlightPaint);
        }

        drawBoard(canvas);
        drawNumbers(canvas,textPaint);
        drawInputNumbers(canvas,inputTextPaint);
    }

    private void drawBoard(Canvas canvas){
        for (int i = 0; i < 9; i++){
            if (i % 3 == 0){
                drawThickLines(canvas);
            }
            else{
                drawThinLines(canvas);
            }
            canvas.drawLine(0, i * cellSize, getWidth(), i * cellSize, boardPaint);
            canvas.drawLine(i * cellSize, 0, i * cellSize, getHeight(), boardPaint);
        }
    }

    private void drawThickLines(Canvas canvas){
        boardPaint.setStyle(Paint.Style.STROKE);
        boardPaint.setStrokeWidth(8);
        boardPaint.setColor(boardColor);
    }

    private void drawThinLines(Canvas canvas){
        boardPaint.setStyle(Paint.Style.STROKE);
        boardPaint.setStrokeWidth(4);
        boardPaint.setColor(boardColor);
    }

    private void drawNumbers(Canvas canvas, Paint textPaint) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int number = sudokuBoard[i][j];
                if (number != 0) {
                    float x = j * cellSize + cellSize / 2;
                    float y = i * cellSize + cellSize / 2 + (textPaint.getTextSize() / 2) - textPaint.descent();
                    canvas.drawText(String.valueOf(number), x, y, textPaint);
                }
            }
        }
    }

    private void drawInputNumbers(Canvas canvas, Paint paint) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (editableCells[row][col]) {
                    if (sudokuBoard[row][col] != 0) {
                        if (checkNumberPlacement(row, col, sudokuBoard[row][col])) {
                            paint.setColor(Color.parseColor("#FFA500"));
                        } else {
                            paint.setColor(Color.RED);
                        }
                        canvas.drawText(Integer.toString(sudokuBoard[row][col]), col * cellSize + cellSize / 2f, row * cellSize + cellSize / 2f + cellSize * 0.2f, paint);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) (event.getX() / cellSize);
            int y = (int) (event.getY() / cellSize);

            if (x >= 0 && x < 9 && y >= 0 && y < 9) {
                selectedRow = y;
                selectedCol = x;
                if (inputNumber != 0) {
                    fillCell(inputNumber);
                    inputNumber = 0;
                }

                invalidate();

            }
        }
        return true;
    }

    public void setInputNumber(int number) {
        inputNumber = number;
        if (selectedRow != -1 && selectedCol != -1) {
            fillCell(inputNumber);
            inputNumber = 0;
        }
    }

    public void fillCell(int number) {
        if (selectedRow != -1 && selectedCol != -1 && editableCells[selectedRow][selectedCol]) {
            sudokuBoard[selectedRow][selectedCol] = number;
            invalidate();

            if (isGameOver() && gameOverListener != null) {
                gameOverListener.onGameOver();
            }
        }
    }

    public boolean isGameOver() {   //lo he cambiado a public
        if (isBoardFull()) {
            if (isBoardValid()) {
                System.out.println("Board is valid");
                return true;
            } else {
                System.out.println("Board is not valid");
                return true; //modificado de false a true
            }

        }
        return false;
    }

    public boolean isBoardFull() { //lo he cambiado a public
        for (int row = 0; row < generator.getBoardSize(); row++) {
            for (int col = 0; col < generator.getBoardSize(); col++) {
                if (getCell(row, col) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isBoardValid() {
        for (int row = 0; row < generator.getBoardSize(); row++) {
            for (int col = 0; col < generator.getBoardSize(); col++) {
                int currentValue = getCell(row, col);
                setCell(row, col, 0);
                if (!generator.isSafe(row, col, currentValue)) {
                    setCell(row, col, currentValue);
                    return false;
                }
                setCell(row, col, currentValue);
            }
        }
        return true;
    } //cambiado a public

    private int getCell(int row, int col) {
        return sudokuBoard[row][col];
    }

    private void setCell(int row, int col, int value) {
        sudokuBoard[row][col] = value;
    }

    public void resetBoard(int n) {
        selectedCol= -1;
        selectedRow= -1;//para que deseleccione las casillas selecionadas
        sudokuBoard = generator.generate();
        removedNumbers = generator.removeNumbers(n);
        updateEditableCells();
        invalidate();
    }

    public void updateEditableCells() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                editableCells[row][col] = false;
            }
        }
        for (RemovedNumber removedNumber : removedNumbers) {
            editableCells[removedNumber.row][removedNumber.col] = true;
        }
    }

    private boolean checkNumberPlacement(int row, int col, int number) {
        for (int i = 0; i < 9; i++) {
            if (sudokuBoard[row][i] == number && i != col) {
                return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (sudokuBoard[i][col] == number && i != row) {
                return false;
            }
        }

        int squareStartRow = (row / 3) * 3;
        int squareStartCol = (col / 3) * 3;
        for (int i = squareStartRow; i < squareStartRow + 3; i++) {
            for (int j = squareStartCol; j < squareStartCol + 3; j++) {
                if (sudokuBoard[i][j] == number && (i != row || j != col)) {
                    return false;
                }
            }
        }

        return true;
    }
    public void setNumber(int num) { //creamos el método setNumber que será llamado cuando hagamos click
        setInputNumber(num);
    }

}



