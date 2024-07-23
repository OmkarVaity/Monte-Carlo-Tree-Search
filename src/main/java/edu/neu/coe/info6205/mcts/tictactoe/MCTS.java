package edu.neu.coe.info6205.mcts.tictactoe;

import edu.neu.coe.info6205.mcts.core.Move;
import edu.neu.coe.info6205.mcts.core.Node;
import edu.neu.coe.info6205.mcts.core.State;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {
    public static TicTacToeNode root;
    public void run(int maxiterations) {
        for (int i = 0; i < maxiterations; i++) {
            Node<TicTacToe> node = select(root);
            if (node.state().isTerminal()) {
                break;
            }
            int result = simulate(node);
            backPropagate(node, result);
        }
    }

    Node<TicTacToe> select(Node<TicTacToe> node) {
        while (!node.isLeaf() && !node.state().isTerminal()) {
            if (!node.children().isEmpty()) {
                node = bestChild(node);
            } else {
                expandNode(node);
                return node;
            }
        }
        return node;
    }

    public void expandNode(Node<TicTacToe> node){
        node.explore();
    }

    private double ucb1(Node<TicTacToe> node) {
        double explorationCoefficient  = Math.sqrt(2);
        double exploitation = node.wins() / (double) node.playouts();
        double exploration = Math.sqrt(Math.log(node.getParent().playouts()) / (double) node.playouts());
        return exploitation + explorationCoefficient  * exploration ;
    }

    int simulate(Node<TicTacToe> node) {
        State<TicTacToe> currentstate = node.state();
        Random random = new Random();
        while (!currentstate.isTerminal()) {
            currentstate = simulateRandomMove(currentstate, random);
        }
        return currentstate.winner().orElse(-1);
    }

    private State<TicTacToe> simulateRandomMove(State<TicTacToe> state, Random random) {
        List<Move<TicTacToe>> possibleMoves = new ArrayList<>(state.moves(state.player()));
        if (possibleMoves.isEmpty()) {
            throw new IllegalStateException("No moves available but the game is not terminal.");
        }
        Move<TicTacToe> randomMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
        return state.next(randomMove);
    }

    void backPropagate(Node<TicTacToe> node, int result) {
        while (node != null) {
            node.setPlayouts(node.playouts() + 1);
            if (isWinForCurrentPlayer(node, result)) {
                node.setWins(node.wins() + 1);
            }
            node = node.getParent();
        }
    }

    private boolean isWinForCurrentPlayer(Node<TicTacToe> node, int result) {
        int player = node.state().player();
        return (player == TicTacToe.X && result == 1) || (player == TicTacToe.O && result == -1) || result == 0;
    }

    Node<TicTacToe> bestChild(Node<TicTacToe> node) {
        if (node.children().isEmpty()) {
            return null;
        }
        return node.children().stream()
                .max(Comparator.comparingDouble(this::ucb1))
                .orElseThrow(() -> new IllegalStateException("Best child not found when expected"));
    }

    public MCTS(TicTacToeNode root) {
        MCTS.root = root;
    }

}