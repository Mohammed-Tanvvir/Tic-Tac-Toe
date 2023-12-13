import java.util.Random;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class BadAI extends AI {
	private JButton[][] buttons;
	private TicTacToeGUI ticTacToeGUI;
	
    public BadAI(JButton[][] buttons,TicTacToeGUI ticTacToeGUI) {
        super(buttons);
        this.buttons = buttons;
        this.ticTacToeGUI = ticTacToeGUI;
    }

    @Override
    public void makeMove() {

        Random rand = new Random();
        int row, col;

        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (!buttons[row][col].getText().equals(""));

        buttons[row][col].setText("O");

        if (super.checkWin(row, col, "O")) {
            declareWinner("2");
        } else if (super.checkDraw()) {
            declareDraw();
        } else {
            ticTacToeGUI.setCurrentPlayer(1);
            ticTacToeGUI.updateTurnLabelAfterAI();
        }
    }



    private void declareWinner(String winner) {
        JOptionPane.showMessageDialog(null, "Player " + winner + " wins!");
        super.clearBoard();
        ticTacToeGUI.updateTurnLabelAfterAI();
    }


    private void declareDraw() {
        JOptionPane.showMessageDialog(null, "It's a draw!");
        super.clearBoard();
        
    }
}