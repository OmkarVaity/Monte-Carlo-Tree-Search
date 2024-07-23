package edu.neu.coe.info6205.mcts.connectfour;

public class ConnectFourMove {
    private final int column;

    public ConnectFourMove(int column) {
        this.column = column;
    }

    // Getter for column
    public int getColumn() {
        return column;
    }
}