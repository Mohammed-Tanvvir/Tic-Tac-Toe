import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class UserInputCells implements ActionListener {
    private JButton[][] buttons;
    private int currentPlayer;
    private JLabel turnLabel;
    
    private TicTacToeGUI ticTacToeGUI;
    private JComboBox<String> player1ComboBox;
    private JComboBox<String> player2ComboBox;
    
    private AI aiPlayer;
    
    private boolean isFirstMove;
    
    private int draws;
    private int player1Losses;
    private int player1Wins;
    private int player2Losses;
    private int player2Wins;

    public UserInputCells(JButton[][] buttons, JLabel turnLabel, TicTacToeGUI ticTacToeGUI, AI aiPlayer,
            JComboBox<String> player1ComboBox, JComboBox<String> player2ComboBox,
            int draws, int player1Losses, int player1Wins, int player2Losses, int player2Wins) {
    	
        this.buttons = buttons;
        this.currentPlayer = 1;
        this.turnLabel = turnLabel;
        
        this.ticTacToeGUI = ticTacToeGUI; 
        this.player1ComboBox = player1ComboBox;
        this.player2ComboBox = player2ComboBox;
        
        this.aiPlayer = aiPlayer;
        
        this.draws = draws;
        this.player1Losses = player1Losses;
        this.player1Wins = player1Wins;
        this.player2Losses = player2Losses;
        this.player2Wins = player2Wins;
        
        
        addActionListeners();
    }

    
    public void setAIPlayer(AI aiPlayer) {
    	this.aiPlayer = aiPlayer;
    }
    
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void updateTurnLabel() {
        String currentPlayerName = getCurrentPlayerName();
        turnLabel.setText(currentPlayerName + "'s turn");
    }
    
    
    private String getCurrentPlayerName() {
        if (currentPlayer == 1) {
            return player1ComboBox.getSelectedItem().toString();
        } else {
            return player2ComboBox.getSelectedItem().toString();
        }
    }

    private String getCurrentPlayerName(JComboBox<String> playerComboBox) { // To declareWinner() method
        return (playerComboBox.getSelectedIndex() == 0) ? "Player 1" : playerComboBox.getSelectedItem().toString();
    }
    
    public void updateTurnLabelAfterAIMove() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1; 
        updateTurnLabel();        
    }
    
    private void addActionListeners() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].addActionListener(this);
            }
        }
    }
    

    
    private void handlePlayerMove(JButton clickedButton) {
        if (currentPlayer == 1) {
            clickedButton.setText("X");
        } else {
            clickedButton.setText("O");
        }

        if (checkWin()) {
            declareWinner(Integer.toString(currentPlayer));
        } else if (checkDraw()) {
            declareDraw();
        } else {
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            updateTurnLabel();
            
            if (currentPlayer == 2) {
            }
        }
    }

    
    private void handleAIMove() {
        if (aiPlayer != null) {
            aiPlayer.makeMove();
            if (checkWin()) {
                declareWinner("2");
            } else if (checkDraw()) {
                declareDraw();
            } else {
                currentPlayer = 1;
            }
            updateStats();
        }
    }
    
    	private boolean checkWin(int row, int col, String symbol) {
        // Checks row
        if (buttons[row][(col + 1) % 3].getText().equals(symbol) &&
            buttons[row][(col + 2) % 3].getText().equals(symbol)) {
            return true;
        }
        // Checks column
        if (buttons[(row + 1) % 3][col].getText().equals(symbol) &&
            buttons[(row + 2) % 3][col].getText().equals(symbol)) {
            return true;
        }
        // Checks diagonals
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

    private boolean checkDraw() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void declareWinner(String winner) {
        String winnerName = (currentPlayer == 1) ? getCurrentPlayerName(player1ComboBox) : getCurrentPlayerName(player2ComboBox);
        JOptionPane.showMessageDialog(null, winnerName + " wins!");
        
        if ("1".equals(winner)) {
            player1Wins++;
            player2Losses++;
            updatePlayerStats(player1ComboBox.getSelectedItem().toString(), 1);
            updatePlayerStats(player2ComboBox.getSelectedItem().toString(), 2);
        } else {
            player1Losses++;
            player2Wins++;
            updatePlayerStats(player1ComboBox.getSelectedItem().toString(), 2);            
            updatePlayerStats(player2ComboBox.getSelectedItem().toString(), 1);

        }
        
        updateStats();
        resetTurn();
    }
    private void declareDraw() {
        JOptionPane.showMessageDialog(null, "It's a draw!");
        draws++;
        updatePlayerStats(player1ComboBox.getSelectedItem().toString(), 0);
        updatePlayerStats(player2ComboBox.getSelectedItem().toString(), 0);
        updateStats();
        resetTurn();
    }

    private void updatePlayerStats(String playerName, int result) {
        Players player = Players.readFromFile(playerName);
        if (player != null) {
            if (result == 1) {
                player.totalW++;
            } else if (result == 2) {
                player.totalL++;
            } else {
                player.totalD++;
            }
            FileManager.writeToFile(player.player, player.totalW, player.totalL, player.totalD);
        }
    }
    
    private void updateStats() {
        ticTacToeGUI.updateStatsLabels(player1Wins, player1Losses, player2Wins, player2Losses, draws);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();

        if (clickedButton.getText().equals("")) {
            handlePlayerMove(clickedButton);
            if (!checkWin() && !checkDraw()) {
                handleAIMove();
            }
        }
    }

    public int getPlayer1Wins() {
        return player1Wins;
    }

    public int getPlayer1Losses() {
        return player1Losses;
    }

    public int getPlayer2Wins() {
        return player2Wins;
    }

    public int getPlayer2Losses() {
        return player2Losses;
    }

    public int getDraws() {
        return draws;
    }
    
    public void resetTurn() {
        isFirstMove = true;
        currentPlayer = 1;
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText("");
            }
        }

        updateTurnLabel();
    }

    
    private void clearBoard() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText("");
            }
        }

        resetTurn();
        
        player1Wins = 0;
        player1Losses = 0;
        player2Wins = 0;
        player2Losses = 0;
        draws = 0;
        updateStats();
    }
    
    private boolean checkWin() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (!buttons[i][j].getText().equals("")) {
                    if (checkWin(i, j, buttons[i][j].getText())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

