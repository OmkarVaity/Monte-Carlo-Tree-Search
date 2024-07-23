package edu.neu.coe.info6205.mcts.connectfour;

import java.util.Scanner;

public class ConnectFour {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConnectFourState state = new ConnectFourState();
        while (!state.isWin() && !state.isFull()) {
            System.out.println("Current board:");
            printBoard(state.getBoard());
            System.out.println("Player " + (state.getCurrentPlayer() + 1) + "'s turn:");
            int column = scanner.nextInt();
            if (!state.makeMove(column)) {
                System.out.println("Column " + column + " is full. Try again.");
            }
        }
        scanner.close();
        if (state.isWin()) {
            System.out.println("Player " + (2 - state.getCurrentPlayer()) + " wins!");
        } else {
            System.out.println("It's a draw!");
        }
    }

    private static void printBoard(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                System.out.print((cell == -1 ? "." : (cell == 0 ? "X" : "O")) + " ");
            }
            System.out.println();
        }
        System.out.println("0 1 2 3 4 5 6");
    }
}