package edu.neu.coe.info6205.mcts.connectfour;

import java.util.Arrays;

public class ConnectFourState {
    private final int[][] board;
    private int currentPlayer; // 0 for player one, 1 for player two

    // Default constructor for initial game state
    public ConnectFourState() {
        this.board = new int[6][7]; // 6 rows, 7 columns
        for (int[] row : this.board) {
            Arrays.fill(row, -1); // -1 indicates an empty spot
        }
        this.currentPlayer = 0; // Player one starts
    }

    // Copy constructor to duplicate the state
    public ConnectFourState(ConnectFourState other) {
        this.board = new int[6][7];
        for (int i = 0; i < 6; i++) {
            this.board[i] = other.board[i].clone(); // Deep copy of the board array
        }
        this.currentPlayer = other.currentPlayer;
    }

    public boolean canMakeMove(int column) {
        return board[0][column] == -1; // Check if the top of the column is empty
    }


    public boolean makeMove(int column) {
        for (int row = 5; row >= 0; row--) { // Start from the bottom of the column
            if (board[row][column] == -1) {
                board[row][column] = currentPlayer;
                currentPlayer = 1 - currentPlayer; // Switch player
                return true; // Move was successful
            }
        }
        return false; // Column is full
    }

    public boolean isWin() {
        return (checkHorizontal() || checkVertical() || checkDiagonalAscending() || checkDiagonalDescending());
    }

    private boolean checkHorizontal() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) { // Only need to check starting from first 4 columns
                int token = board[row][col];
                if (token != -1 && token == board[row][col + 1] && token == board[row][col + 2] && token == board[row][col + 3]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkVertical() {
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 3; row++) { // Only need to check starting from first 3 rows
                int token = board[row][col];
                if (token != -1 && token == board[row + 1][col] && token == board[row + 2][col] && token == board[row + 3][col]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonalAscending() {
        for (int row = 3; row < 6; row++) { // Start from row 3 to allow for ascending diagonal
            for (int col = 0; col < 4; col++) { // Only need to check first 4 columns
                int token = board[row][col];
                if (token != -1 && token == board[row - 1][col + 1] && token == board[row - 2][col + 2] && token == board[row - 3][col + 3]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonalDescending() {
        for (int row = 0; row < 3; row++) { // Only need to check first 3 rows
            for (int col = 0; col < 4; col++) { // Only need to check first 4 columns
                int token = board[row][col];
                if (token != -1 && token == board[row + 1][col + 1] && token == board[row + 2][col + 2] && token == board[row + 3][col + 3]) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFull() {
        for (int col = 0; col < 7; col++) {
            if (board[0][col] == -1) { // Check top of each column
                return false; // At least one move is still possible
            }
        }
        return true; // No moves left
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int[][] getBoard() {
        return board;
    }
}