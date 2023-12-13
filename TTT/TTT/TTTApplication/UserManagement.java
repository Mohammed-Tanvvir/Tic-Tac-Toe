import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class UserManagement {
    private JMenuItem addItem;
    private JMenuItem deleteItem;
    private List<String> userNames = new ArrayList<>();
    private JComboBox<String> player1ComboBox;
    private JComboBox<String> player2ComboBox;

    public UserManagement(JMenuItem addItem, JMenuItem deleteItem, JComboBox<String> player1ComboBox, JComboBox<String> player2ComboBox) {
        this.addItem = addItem;
        this.deleteItem = deleteItem;
        this.player1ComboBox = player1ComboBox;
        this.player2ComboBox = player2ComboBox;
        
        this.addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = JOptionPane.showInputDialog("Enter your name (Up to 10 characters): ");
                if (userName != null && !userName.isEmpty() && userName.length() <= 10) {
                    addPlayer(userName);
                    refreshComboBoxes(player1ComboBox, player2ComboBox);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid name. Please try again.");
                }
            }
        });

        this.deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteUserPopup();
                refreshComboBoxes(player1ComboBox, player2ComboBox);
            }
        });
        readNamesFromFile();
    }
    
    private void showDeleteUserPopup() {
        ArrayList<String> userList = getUserNamesList();

        if (userList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No users to delete.");
            return;
        }

        String selectedUser = (String) JOptionPane.showInputDialog(
                null,
                "Select a user to delete:",
                "Delete User",
                JOptionPane.QUESTION_MESSAGE,
                null,
                userList.toArray(),
                userList.get(0)
        );

        if (selectedUser != null) {
            boolean deleted = deleteUser(selectedUser);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "User '" + selectedUser + "' deleted.");
            } else {
                JOptionPane.showMessageDialog(null, "Error deleting user '" + selectedUser + "'.");
            }
        }
    }

    private boolean deleteUser(String userName) {
        ArrayList<String> users = getUserNamesList();
        if (users.contains(userName)) {
            users.remove(userName);
            deleteFromFile(users);
            return true;
        }
        return false;
    }

    public static void addPlayer(String playerName) {
    	FileManager.writeToFile(playerName, 0, 0, 0);
    	JOptionPane.showMessageDialog(null, "User '" + playerName + "' added.");
    }
    
    private ArrayList<String> getUserNamesList() {
        ArrayList<String> userNamesList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens.length > 0) {
                    userNamesList.add(tokens[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userNamesList;
    }

    private void deleteFromFile(ArrayList<String> users) {
        try (FileWriter writer = new FileWriter("Users.txt")) {
            for (String user : users) {
                writer.write(user);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getUserArray() {
        return userNames.toArray(new String[0]);
    }
    
    private void readNamesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens.length > 0) {
                    userNames.add(tokens[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void refreshComboBoxes(JComboBox<String> player1ComboBox, JComboBox<String> player2ComboBox) {
    	
        Object selectedPlayer1 = player1ComboBox.getSelectedItem();
        Object selectedPlayer2 = player2ComboBox.getSelectedItem();

        userNames = getUserNamesList();

        userNames.add(0, "Player 1");

        DefaultComboBoxModel<String> player1Model = new DefaultComboBoxModel<>(userNames.toArray(new String[0]));

        String[] defaultPlayer2Options = {"Player 2", "Easy AI", "Hard AI"};

        List<String> combinedPlayer2Options = new ArrayList<>();
        combinedPlayer2Options.addAll(userNames);
        combinedPlayer2Options.addAll(List.of(defaultPlayer2Options));

        DefaultComboBoxModel<String> player2Model = new DefaultComboBoxModel<>(combinedPlayer2Options.toArray(new String[0]));

        player1ComboBox.setModel(player1Model);
        player2ComboBox.setModel(player2Model);

        player1ComboBox.setSelectedItem(selectedPlayer1);
        player2ComboBox.setSelectedItem(selectedPlayer2);
    }
}