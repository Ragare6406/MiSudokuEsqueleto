package com.example.sudokuesqueleto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class SudokuGenerator {
    private static final int BOARD_SIZE = 9;
    private int[][] board;
    public int getBoardSize() {
        return BOARD_SIZE;
    }
    public int[][] generate() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        if (fillBoard(0, 0)) {
            return board;
        }
        return null;
    }

    private boolean fillBoard(int row, int col) {
        return solveSudoku(board, row, col);
    }

    protected boolean isSafe(int row, int col, int num) {
        return isRowSafe(row, num) && isColSafe(col, num) && isBoxSafe(row - row % 3, col - col % 3, num);
    }

    private boolean isRowSafe(int row, int num) {
        for (int col = 0; col < BOARD_SIZE; col++) {
            if (board[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean isColSafe(int col, int num) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (board[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean isBoxSafe(int rowStart, int colStart, int num) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[rowStart + row][colStart + col] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<RemovedNumber> removeNumbers(int  n) {
        Random random = new Random();
        List<RemovedNumber> removedNumbers = new ArrayList<>();

        Set<Integer> removedPositions = new HashSet<>();
        while (removedNumbers.size() < n) {
            int position = random.nextInt(BOARD_SIZE * BOARD_SIZE);
            if (!removedPositions.contains(position)) {
                removedPositions.add(position);
                int row = position / BOARD_SIZE;
                int col = position % BOARD_SIZE;
                int value = board[row][col];
                if (value != 0) {
                    removedNumbers.add(new RemovedNumber(row, col, value));
                    board[row][col] = 0;
                }
            }
        }

        return removedNumbers;
    }

    private boolean solveSudoku(int[][] board, int row, int col) {
        if (row == BOARD_SIZE) {
            return true;
        }

        int nextRow = (col == BOARD_SIZE - 1) ? row + 1 : row;
        int nextCol = (col == BOARD_SIZE - 1) ? 0 : col + 1;

        if (board[row][col] != 0) {
            return solveSudoku(board, nextRow, nextCol);
        }

        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums);

        for (Integer num : nums) {
            if (isSafe(row, col, num)) {
                board[row][col] = num;
                if (solveSudoku(board, nextRow, nextCol)) {
                    return true;
                }
            }
        }

        board[row][col] = 0;
        return false;
    }


}
