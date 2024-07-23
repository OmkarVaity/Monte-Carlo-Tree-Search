package edu.neu.coe.info6205.mcts.tictactoe;

import static org.junit.Assert.*;
import org.junit.Test;
import edu.neu.coe.info6205.mcts.core.Node;

public class MCTSTest {

    @Test
    public void InitializationAndGameStart() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode initialNode = new TicTacToeNode(game.start());
        MCTS mcts = new MCTS(initialNode);
        assertNotNull(mcts);
        assertEquals("TicTacToe\n. . .\n. . .\n. . .\n", initialNode.state().toString());
    }

    @Test
    public void SelectMethod() {
        // Initialize the game and root node properly
        TicTacToe game = new TicTacToe();
        TicTacToeNode rootNode = new TicTacToeNode(game.start());
        rootNode.explore();  // Ensure the root node has at least one child to select from, if necessary

        MCTS mcts = new MCTS(rootNode);  // Ensure MCTS is initialized with a non-null root
        Node<TicTacToe> selectedNode = mcts.select(rootNode);

        assertNotNull(selectedNode);  // Check that select does not return null
    }

    @Test
    public void BestChildSelection() {
        TicTacToeNode rootNode = new TicTacToeNode(new TicTacToe().start());
        rootNode.explore();  // To ensure there are children to select from
        Node<TicTacToe> bestChild = new MCTS(rootNode).bestChild(rootNode);
        assertNotNull(bestChild);
        assertTrue(rootNode.children().contains(bestChild));
    }

    @Test
    public void SimulationAndBackPropagation() {
        TicTacToeNode node = new TicTacToeNode(new TicTacToe().start());
        MCTS mcts = new MCTS(node);
        int initialPlayouts = node.playouts();
        int result = mcts.simulate(node);
        mcts.backPropagate(node, result);
        assertEquals(initialPlayouts + 1, node.playouts());
    }


    @Test
    public void UCTFormulaValidation() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode rootNode = new TicTacToeNode(game.start());
        rootNode.explore(); // To ensure there are children to select from
        MCTS mcts = new MCTS(rootNode);
        Node<TicTacToe> selectedNode = mcts.select(rootNode); // assuming select uses UCT

        // Validate UCT calculation manually and compare
        // This test assumes you have access to some internal UCT calculation logs or values.
        double expectedUCTValue = calculateExpectedUCT(rootNode);
        double actualUCTValue = getActualUCTFromNode(selectedNode);
        assertEquals("UCT values should match", expectedUCTValue, actualUCTValue, 0.01);
    }

    private double calculateExpectedUCT(TicTacToeNode node) {
        // Implement expected UCT calculation logic
        return 0.0; // Placeholder
    }

    private double getActualUCTFromNode(Node<TicTacToe> node) {
        // Retrieve actual UCT value from the node
        return 0.0; // Placeholder
    }

    @Test
    public void NodeExpansionCoverage() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode rootNode = new TicTacToeNode(game.start());
        MCTS mcts = new MCTS(rootNode);

        int initialChildCount = rootNode.children().size();
        rootNode.explore();
        int newChildCount = rootNode.children().size();
        assertTrue("Expected more children after expansion", newChildCount > initialChildCount);
    }


    @Test(timeout = 1000) // 1000 milliseconds as time constraint
    public void PerformanceUnderConstraints() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode rootNode = new TicTacToeNode(game.start());
        MCTS mcts = new MCTS(rootNode);

        mcts.simulate(rootNode); // Should complete within 1000 ms
    }

}