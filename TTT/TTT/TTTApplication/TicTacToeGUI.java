import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class TicTacToeGUI extends JFrame{
	
	private JButton[][] buttons;
	private int currentPlayer;
	private JLabel ptLabel;
	
	private UserInputCells userInputCells;
	
	private boolean isFirstMove;
	
    private JComboBox<String> player1;
    private JComboBox<String> player2;

    private JLabel player1WLabel;
    private JLabel player1LLabel;
    private JLabel drawLabel;
    private JLabel player2WLabel;
    private JLabel player2LLabel;

    private int player1Wins = 0;
    private int player1Losses = 0;
    private int draws = 0;
    private int player2Wins = 0;
    private int player2Losses = 0;
    
	private UserManagement userManagement;
	
	private JLabel twLabel;
	private JLabel tlLabel;
	private JLabel tdLabel;
	
	
	public TicTacToeGUI() {
		setTitle("Tic-Tac-Toe");
		setSize(500, 450);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		currentPlayer = 1;
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton one = new JButton("");
		one.setBounds(10, 103, 75, 75);
		panel.add(one);
		
		JButton two = new JButton("");
		two.setBounds(95, 103, 75, 75);
		panel.add(two);
		
		JButton three = new JButton("");
		three.setBounds(180, 103, 75, 75);
		panel.add(three);
		
		JButton four = new JButton("");
		four.setBounds(10, 189, 75, 75);
		panel.add(four);
		
		JButton five = new JButton("");
		five.setBounds(95, 189, 75, 75);
		panel.add(five);
		
		JButton six = new JButton("");
		six.setBounds(180, 189, 75, 75);
		panel.add(six);
		
		JButton seven = new JButton("");
		seven.setBounds(10, 275, 75, 75);
		panel.add(seven);
		
		JButton eight = new JButton("");
		eight.setBounds(95, 275, 75, 75);
		panel.add(eight);
		
		JButton nine = new JButton("");
		nine.setBounds(180, 275, 75, 75);
		panel.add(nine);
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu tmenuBar = new JMenu("File");
		menuBar.add(tmenuBar);
		
		JMenu umanagementMenu = new JMenu("User Management");
		tmenuBar.add(umanagementMenu);
		
		JMenuItem addItem = new JMenuItem("Add User");
		umanagementMenu.add(addItem);
		
		
		JMenuItem deleteItem = new JMenuItem("Delete User");
		umanagementMenu.add(deleteItem);
		
		ptLabel = new JLabel("Player 1's turn");
		ptLabel.setFont(new Font("Dialog", Font.BOLD, 15));
		ptLabel.setBounds(273, 200, 174, 25);
		panel.add(ptLabel);
		
        player1 = new JComboBox<>();
        player1.addItem("Player 1");
        player1.setToolTipText("");
        player1.setBounds(10, 40, 100, 22);
        panel.add(player1);
		
        player1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePlayer1Selection(player1.getSelectedItem().toString());
                clearBoard();
            }
        });
        
        player2 = new JComboBox<>();
        player2.setModel(new DefaultComboBoxModel<>(new String[]{"Player 2", "Easy AI", "Hard AI"}));
        player2.setToolTipText("");
        player2.setBounds(180, 40, 100, 22);
        panel.add(player2);
        
        userManagement = new UserManagement(addItem, deleteItem, player1, player2);
        userManagement.refreshComboBoxes(player1, player2);
        
        player2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePlayer2Selection(player2.getSelectedItem().toString());
                clearBoard();
            }
        });
		
		buttons = new JButton[][] {
			{one, two, three},
			{four, five, six},
			{seven, eight,nine}
		};
		
		AI aiPlayer = null;
		String selectedPlayer2 = player2.getSelectedItem().toString();
		
		if ("Easy AI".equals(selectedPlayer2)) {
			aiPlayer = new BadAI(buttons,this);
		} else if ("HardAI".equals(selectedPlayer2)) {
			aiPlayer = new GoodAI(buttons, this);
		}
		
		userInputCells = new UserInputCells(buttons, ptLabel, TicTacToeGUI.this, aiPlayer,
		        player1, player2, draws, player1Losses, player1Wins, player2Losses, player2Wins);
		
		
		JButton clearBoard = new JButton("Clear");
		clearBoard.setToolTipText("Clears the board");
		clearBoard.setBounds(347, 351, 125, 25);
		panel.add(clearBoard);
		
		clearBoard.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        clearBoard();
		        userInputCells.resetTurn();
		    }
		});

		
        player1WLabel = new JLabel("W: ");
        player1WLabel.setBounds(10, 65, 50, 14);
        panel.add(player1WLabel);

        player1LLabel = new JLabel("L: ");
        player1LLabel.setBounds(10, 78, 50, 14);
        panel.add(player1LLabel);

        drawLabel = new JLabel("D: ");
        drawLabel.setBounds(105, 65, 50, 14);
        panel.add(drawLabel);

        player2WLabel = new JLabel("W: ");
        player2WLabel.setBounds(180, 65, 50, 14);
        panel.add(player2WLabel);

        player2LLabel = new JLabel("L: ");
        player2LLabel.setBounds(180, 78, 50, 14);
        panel.add(player2LLabel);

		
		JButton statsButton = new JButton("Stats");
		statsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		statsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openStats();
			}
		});
		statsButton.setBounds(347, 12, 125, 23);
		panel.add(statsButton);
		
		twLabel = new JLabel("Total Wins: ");
		tlLabel = new JLabel("Total Losses: ");
		tdLabel = new JLabel("Total Draws: ");

	}

	public void updateStatsLabels(int p1Wins, int p1Losses, int p2Wins, int p2Losses, int draws) {
	    player1Wins = p1Wins;
	    player1Losses = p1Losses;
	    player2Wins = p2Wins;
	    player2Losses = p2Losses;
	    this.draws = draws;
	    
	    if (player1WLabel != null && player1LLabel != null && drawLabel != null &&
	        player2WLabel != null && player2LLabel != null) {
	        player1WLabel.setText("W: " + player1Wins);
	        player1LLabel.setText("L: " + player1Losses);
	        drawLabel.setText("D: " + this.draws);
	        player2WLabel.setText("W: " + player2Wins);
	        player2LLabel.setText("L: " + player2Losses);

	    }
	}
	
	
	
	private void handlePlayer1Selection(String selectedPlayer1) {
	    updateTurnLabelWithPlayerName(currentPlayer, selectedPlayer1);
	}
	

	private void handlePlayer2Selection(String selectedPlayer2) {
		AI aiPlayer = null;

		if ("Easy AI".equals(selectedPlayer2)) {
			aiPlayer = new BadAI(buttons, this);
		} else if ("Hard AI".equals(selectedPlayer2)) {
			aiPlayer = new GoodAI(buttons, this);
		}

		if (userInputCells != null) {
			userInputCells.setAIPlayer(aiPlayer);
		}

		updateTurnLabelWithPlayerName(currentPlayer, selectedPlayer2);
	}
	
	public void updateTurnLabelWithPlayerName(int player, String playerName) {
	    if (player == 1) {
	        ptLabel.setText(playerName + "'s turn");
	    } else {
	        ptLabel.setText(playerName + "'s turn");
	    }
	}
	
	
	
	private void clearBoard() {
	    if (buttons == null) {
	        return;
	    }
	    for (int i = 0; i < buttons.length; i++) {
	        for (int j = 0; j < buttons[i].length; j++) {
	            buttons[i][j].setText("");
	        }
	    }
	    
	    userInputCells.resetTurn();
	    player1Wins = 0;
	    player1Losses = 0;
	    player2Wins = 0;
	    player2Losses = 0;
	    draws = 0;
	    updateStatsLabels(player1Wins, player1Losses, player2Wins, player2Losses, draws);
	}


    public int getCurrentPlayer() {
    	return currentPlayer;
    }
    
    
    public void setCurrentPlayer(int player) {
    	currentPlayer = player;
    }
    
    public void updateTurnLabelAfterAI() {
        String currentPlayerName = getCurrentPlayerName();
        ptLabel.setText(currentPlayerName + "'s turn");
    }
    
    private String getCurrentPlayerName() {
        if (currentPlayer == 1) {
            return player1.getSelectedItem().toString();
        } else {
            return player2.getSelectedItem().toString();
        }
    }
    
	private void openStats() {
		JFrame statsWindow = new JFrame("Player Stats");
		statsWindow.setSize(350,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel statspanel = new JPanel();
		statsWindow.getContentPane().add(statspanel, BorderLayout.CENTER);
		statspanel.setLayout(null);
		
		JComboBox<String> spSelect = new JComboBox<>();
		
	    String[] users = userManagement.getUserArray();

	    if (users.length > 0) {
	        spSelect.addItem("Add a new user...");
	        for (String user : users) {
	            if (!user.equals("Player 1")) {
	                spSelect.addItem(user);
	            }
	        }
	    } else {
	        spSelect.addItem("Add a new user...");
	    }

	    spSelect.setSelectedItem("Add a new user...");
		spSelect.setBounds(10, 11, 150, 22);
		statspanel.add(spSelect);
		
		JLabel twLabel = new JLabel("Total Wins: ");
		twLabel.setBounds(10, 44, 100, 30);
		statspanel.add(twLabel);
		
		JLabel tlLabel = new JLabel("Total Losses: ");
		tlLabel.setBounds(10, 85, 100, 30);
		statspanel.add(tlLabel);
		
		JLabel tdLabel = new JLabel("Total Draws: ");
		tdLabel.setBounds(10, 126, 100, 30);
		statspanel.add(tdLabel);
		
	    spSelect.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            String selectedPlayer = spSelect.getSelectedItem().toString();
	            updateStatsGUI(selectedPlayer, twLabel, tlLabel, tdLabel);
	        }
	    });
		
		statsWindow.setVisible(true);
	}
	
	private static void updateStatsGUI(String selectedPlayer, JLabel twLabel, JLabel tlLabel, JLabel tdLabel) {
	    Players player = Players.readFromFile(selectedPlayer);

	    if (player != null) {
	        twLabel.setText("Total Wins: " + player.totalW);
	        tlLabel.setText("Total Losses: " + player.totalL);
	        tdLabel.setText("Total Draws: " + player.totalD);
	    } else {
	        twLabel.setText("Total Wins: N/A");
	        tlLabel.setText("Total Losses: N/A");
	        tdLabel.setText("Total Draws: N/A");
	    }
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TicTacToeGUI().setVisible(true);
                System.out.println("If no text file is found, create a new user to create a new text file");
            }
        });
    }
}
