package edu.neu.coe.info6205.mcts.connectfourtest;

import edu.neu.coe.info6205.mcts.connectfour.ConnectFourNode;
import edu.neu.coe.info6205.mcts.connectfour.ConnectFourState;
import edu.neu.coe.info6205.mcts.connectfour.MCTS;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectFourTest {
    ConnectFourState state;
    ConnectFourNode node;

    @Before
    public void setUp() {
        state = new ConnectFourState();
        node = new ConnectFourNode(state, null);
    }

    @Test
    public void testInitialState() {
        for (int[] row : state.getBoard()) {
            for (int cell : row) {
                assertEquals(-1, cell);
            }
        }
        assertEquals(0, state.getCurrentPlayer());
    }


    @Test
    public void testWinConditions() {
        state.makeMove(0); // Player 1
        state.makeMove(1); // Player 2
        state.makeMove(0); // Player 1
        state.makeMove(1); // Player 2
        state.makeMove(0); // Player 1
        state.makeMove(1); // Player 2
        state.makeMove(0); // Player 1 wins
        assertTrue(state.isWin());
    }

    @Test
    public void testIsFull() {
        for (int i = 0; i < 6; i++) { // Fill the board
            for (int j = 0; j < 7; j++) {
                state.makeMove(j);
            }
        }
        assertTrue(state.isFull());
    }

    @Test
    public void testInitialNodeSetup() {
        assertNull(node.parent);
        assertEquals(0, node.getChildren().size());
    }

    @Test
    public void testExpand() {
        node.expand();
        assertEquals(7, node.getChildren().size()); // Assuming all columns are empty and valid for a move
    }

    @Test
    public void testSelect() {
        node.expand();
        ConnectFourNode selectedNode = node.select();
        assertNotNull(selectedNode);
        assertTrue(node.getChildren().contains(selectedNode));
    }

    @Test
    public void testSimulateAndBackPropagate() {
        node.expand();
        ConnectFourNode childNode = node.getChildren().get(0);
        double result = childNode.simulate();
        childNode.backPropagate(result);
        assertEquals(1, childNode.getVisits());
        assertTrue(childNode.getWins() >= 0);
    }

    @Test
    public void testDiagonalWinCondition() {
        ConnectFourState state = new ConnectFourState();
        state.makeMove(0); // Player 1
        state.makeMove(1); // Player 2
        state.makeMove(1); // Player 1
        state.makeMove(2); // Player 2
        state.makeMove(2); // Player 1
        state.makeMove(3); // Player 2
        state.makeMove(2); // Player 1
        state.makeMove(3); // Player 2
        state.makeMove(3); // Player 1
        state.makeMove(4); // Player 2
        state.makeMove(3); // Player 1 wins diagonally
        assertTrue("Should win with a diagonal", state.isWin());
    }

    
}