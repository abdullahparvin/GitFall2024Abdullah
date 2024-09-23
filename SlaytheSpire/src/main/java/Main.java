import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Random;

public class Main {

    static Random idGenerator = new Random();
    public static void main(String[] args) {
        System.out.println("Welcome to the Slay the Spire Deck Cost Tally System!");
        System.out.println("Please ensure that your files are properly formatted and placed in the system folder for processing.");

        File directory = new File("/Users/abdullah/GitFall2024Abdullah/SlaytheSpire/Decks");
        File[] decksList = directory.listFiles();

        assert decksList != null;
        if (decksList.length < 1){
            System.out.println("No deck files have been added for processing.");
        }
        else {
            for (File file : decksList) {
                generateReport(file);
            }
        }
    }

    public static void generateReport(File currentDeck){
        int deckId = 100000000 + idGenerator.nextInt(900000000);
        List<Integer> costs = new ArrayList<>();
        List<String> invalid = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(currentDeck))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (checkValidity(currentLine)) {
                    String writtenCost = currentLine.replaceAll("[^0-9]", "");
                    int cost = Integer.parseInt(writtenCost);
                    costs.add(cost);
                }
                else {
                    invalid.add(currentLine);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(costs);
        System.out.println(invalid);
    }

    public static boolean checkValidity(String line){
        String format = "^[A-Za-z]+:[1-9][0-9]*$";

        if (line == null){
            return false;
        }

        return line.matches(format);
    }
}