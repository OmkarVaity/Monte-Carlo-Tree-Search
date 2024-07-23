package edu.neu.coe.info6205.mcts.connectfour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConnectFourNode {
    private ConnectFourState state;
    public ConnectFourNode parent;
    public List<ConnectFourNode> children = new ArrayList<>();
    private int visits = 0;
    private double wins = 0.0; // Wins are typically the number of times a win occurred in simulations

    private static final Random random = new Random();

    public ConnectFourNode(ConnectFourState state, ConnectFourNode parent) {
        this.state = state;
        this.parent = parent;
    }

    public int MCTSMove() {
        ConnectFourNode root = new ConnectFourNode(state, null);
        root.expand(); // Initial expansion to create children based on possible moves
        int iterations = 1000;
        for (int i = 0; i < iterations; i++) {
            ConnectFourNode node = root;
            while (!node.getChildren().isEmpty() && !node.getState().isWin()) {
                node = node.select();
                if (node == null) {
                    break; // Safe guard against null node
                }
            }
            if (node != null && !node.getChildren().isEmpty() && !node.getState().isWin()) {
                node.expand();
                node = node.select(); // This should now safely select a non-null node
            }
            if (node != null) {
                double result = node.simulate();
                node.backPropagate(result);
            }
        }

        // Select the best move from the root
        ConnectFourNode bestMove = root.select();
        if (bestMove != null) {
            int bestMoveColumn = extractColumnFromState(bestMove.getState(), state);
            return bestMoveColumn; // Return the best move column
        }
        return -1; // Indicate no valid move found
    }

    private int extractColumnFromState(ConnectFourState newState, ConnectFourState oldState) {
        // Compare newState and oldState to find the column where the move was made
        // This assumes the newState differs from the oldState by exactly one move
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                if (newState.getBoard()[row][col] != oldState.getBoard()[row][col]) {
                    return col;
                }
            }
        }
        return -1; // default case, though should never happen
    }


    // Expands node by creating all possible moves from the current state
    public void expand() {
        for (int col = 0; col < 7; col++) {
            ConnectFourState newState = new ConnectFourState(state); // Copy state
            if (newState.makeMove(col)) {
                children.add(new ConnectFourNode(newState, this));
//                System.out.println("Expanded node at column " + col);
            }
        }
        if (children.isEmpty()) {
            System.out.println("No children were expanded, possibly all columns are full");
        }
    }


    // Simulation: Randomly play until the game ends
    public double simulate() {
        ConnectFourState simulationState = new ConnectFourState(state); // Copy the state
        int player = simulationState.getCurrentPlayer();
        while (!simulationState.isWin() && !simulationState.isFull()) {
            List<Integer> possibleMoves = new ArrayList<>();
            for (int col = 0; col < 7; col++) {
                if (simulationState.canMakeMove(col)) {
                    possibleMoves.add(col);
                }
            }
//            System.out.println("Possible moves: " + possibleMoves);
            if (possibleMoves.isEmpty()) break;
            int col = possibleMoves.get(random.nextInt(possibleMoves.size()));
            simulationState.makeMove(col);
        }
        if (simulationState.isWin()) {
            return (simulationState.getCurrentPlayer() != player) ? 1.0 : 0.0;
        }
        return 0.5; // Draw
    }

    // Backpropagation: Update current node and all parent nodes
    public void backPropagate(double result) {
        ConnectFourNode node = this;
        while (node != null) {
            node.visits++;
            node.wins += result;
            node = node.parent;
        }
    }

    // Selects the best child using the UCB1 formula
    public ConnectFourNode select() {
        ConnectFourNode selected = null;
        double bestValue = Double.MAX_VALUE;
        double ucbValue = 0;
        for (ConnectFourNode child : children) {
            if (child.visits > 0) {
                ucbValue = (child.wins / child.visits) + Math.sqrt(2 * Math.log(visits) / child.visits);
            } else {
                ucbValue = Double.MIN_VALUE; // Provide a large value to ensure unvisited nodes are selected
            }
            if (ucbValue < bestValue) {
                bestValue = ucbValue;
//                System.out.println("Best value: " + bestValue);
                selected = child;
//                System.out.println("Selected child: " + selected);
            }
        }
        if (selected == null && !children.isEmpty()) {
//            System.out.println("Select method error corrected: Returning first child as fallback.");
            return children.get(0); // Fallback to the first child if none are selected
        }

        return selected;
    }


    public ConnectFourState getState() {
        return state;
    }

    public int getVisits() {
        return visits;
    }

    public double getWins() {
        return wins;
    }

    public List<ConnectFourNode> getChildren() {
        return children;
    }
}