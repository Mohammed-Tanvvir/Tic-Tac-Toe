import javax.swing.JButton;

public abstract class AI {
    protected JButton[][] buttons;
    protected int currentPlayer;

    public AI(JButton[][] buttons) {
        this.buttons = buttons;
        this.currentPlayer = 2;
    }

    public abstract void makeMove();

    protected boolean checkWin(int row, int col, String symbol) {
        // Check row
        if (buttons[row][(col + 1) % 3].getText().equals(symbol) &&
                buttons[row][(col + 2) % 3].getText().equals(symbol)) {
            return true;
        }
        // Check column
        if (buttons[(row + 1) % 3][col].getText().equals(symbol) &&
                buttons[(row + 2) % 3][col].getText().equals(symbol)) {
            return true;
        }
        // Check diagonals
        if (row == col && buttons[(row + 1) % 3][(col + 1) % 3].getText().equals(symbol) &&
                buttons[(row + 2) % 3][(col + 2) % 3].getText().equals(symbol)) {
            return true;
        }
        if (row + col == 2 && buttons[(row + 1) % 3][(col + 2) % 3].getText().equals(symbol) &&
                buttons[(row + 2) % 3][(col + 1) % 3].getText().equals(symbol)) {
            return true;
        }
        return false;
    }

    protected boolean checkDraw() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void clearBoard() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText("");
            }
        }
    }
}

