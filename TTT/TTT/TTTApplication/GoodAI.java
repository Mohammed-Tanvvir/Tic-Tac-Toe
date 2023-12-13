import java.util.Random;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class GoodAI extends AI {
	private JButton[][] buttons;
	private TicTacToeGUI ticTacToeGUI;
	
    public GoodAI(JButton[][] buttons,TicTacToeGUI ticTacToeGUI) {
        super(buttons);
        this.buttons = buttons;
        this.ticTacToeGUI = ticTacToeGUI;
    }
    @Override
    public void makeMove() {
    	
        // Check for winning move
        if (winMove()) {
            return;
        }

        // Check for blocking move
        if (blockMove()) {
            return;
        }

        // Prioritize center
        if (buttons[1][1].getText().equals("")) {
            buttons[1][1].setText("O");
        } else {
            //Prioritize corner
            cornerMove();
        }

        if (super.checkWin(1, 1, "O")) {
            declareWinner("2");
        } else if (super.checkDraw()) {
            declareDraw();
        } else {
            ticTacToeGUI.setCurrentPlayer(1);
            ticTacToeGUI.updateTurnLabelAfterAI();
        }
    }

    private boolean winMove() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText("O");
                    if (super.checkWin(i, j, "O")) {
                        declareWinner("2");
                        return true;
                    } else {
                        buttons[i][j].setText("");
                    }
                }
            }
        }
        return false;
    }

    private boolean blockMove() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText("X");
                    if (super.checkWin(i, j, "X")) {
                        buttons[i][j].setText("O");
                        ticTacToeGUI.updateTurnLabelAfterAI();
                        return true;
                    } else {
                        buttons[i][j].setText("");
                    }
                }
            }
        }
        return false;
    }

    private void cornerMove() {
        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        Random rand = new Random();
        for (int[] corner : corners) {
            int row = corner[0];
            int col = corner[1];
            if (buttons[row][col].getText().equals("")) {
                buttons[row][col].setText("O");
                return;
            }
        }
        // If corners are occupied, makes random move
        int row, col;
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (!buttons[row][col].getText().equals(""));
        buttons[row][col].setText("O");
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

