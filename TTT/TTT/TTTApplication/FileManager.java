import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class FileManager {
    private static final String FILE_PATH = "Users.txt";
    
    public static void writeToFile(String playerName, int W, int L, int D) {
        try {
        	
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
            
            BufferedReader fileReader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            StringBuilder inputBuffer = new StringBuilder();
            boolean userExists = false;

            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens.length > 0 && tokens[0].equals(playerName)) {
                    line = playerName + " " + W + " " + L + " " + D;
                    userExists = true;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            fileReader.close();

            if (!userExists) {
                inputBuffer.append(playerName).append(" ").append(W).append(" ").append(L).append(" ").append(D).append('\n');
            }

            try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
                fileOut.write(inputBuffer.toString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            
        }
    }
}
