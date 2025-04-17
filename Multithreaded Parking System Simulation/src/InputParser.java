import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class InputParser {
    public static ArrayList<String[]> parseFile(String fileName) {
        ArrayList<String[]> parsedData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                parsedData.add(line.split(", "));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return parsedData;
    }
}
