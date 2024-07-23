package edu.neu.coe.info6205.mcts.connectfour;

import java.util.Scanner;
public class MCTS {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConnectFourState state = new ConnectFourState();  // Initialize the game state

        while (!state.isWin() && !state.isFull()) {
            System.out.println("Current board:");
            printBoard(state.getBoard());
            if (state.getCurrentPlayer() == 0) {  // Human player's turn
                System.out.println("Player " + (state.getCurrentPlayer() + 1) + "'s turn:");
                int column = scanner.nextInt();
                while (column < 0 || column >= 7 || !state.makeMove(column)) {
                    System.out.println("Invalid move. Column " + column + " is full or out of range. Try again.");
                    column = scanner.nextInt();
                }
            } else if (state.getCurrentPlayer() == 1) {  // AI player's turn
                System.out.println("Computer's turn:");
                ConnectFourNode root = new ConnectFourNode(state, null);  // Initialize root node for AI decision-making
                int aiMove = root.MCTSMove();  // Use MCTS to determine the AI move
                if (aiMove != -1) {
                    state.makeMove(aiMove);  // Perform the AI move
                    System.out.println("AI moves at column " + aiMove);
                } else {
                    System.out.println("No valid moves available for AI.");
                }
            } else {
                System.out.println("Something went wrong.");
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