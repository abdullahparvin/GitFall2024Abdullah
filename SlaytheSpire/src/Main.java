import java.io.*;
import java.util.*;
import java.util.Random;

public class Main {
    
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
        System.out.println("ok");
    }
}