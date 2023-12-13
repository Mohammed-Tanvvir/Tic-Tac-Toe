import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Players {
    public String player;
    public int totalW;
    public int totalL;
    public int totalD;

    public Players(String player, int totalW, int totalL, int totalD) {
        this.player = player;
        this.totalW = totalW;
        this.totalL = totalL;
        this.totalD = totalD;
    }

    public static Players readFromFile(String playerName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens.length > 0 && tokens[0].equals(playerName)) {
                    int wins = Integer.parseInt(tokens[1]);
                    int losses = Integer.parseInt(tokens[2]);
                    int draws = Integer.parseInt(tokens[3]);
                    return new Players(playerName, wins, losses, draws);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
