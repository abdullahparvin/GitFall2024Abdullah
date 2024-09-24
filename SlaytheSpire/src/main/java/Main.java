import javax.swing.*;
import java.io.*;

public class Main extends JFrame {

    //Main method which runs the system. Does not take any input.
    public static void main(String[] args) {
        //Simple text telling the user what this system does.
        System.out.println("Welcome to the Slay the Spire Deck Cost Tally System!");
        System.out.println("The system will tally the cost of the cards in your deck and give you a visual report.");
        System.out.println("Please ensure that your files are properly formatted and placed in the system folder for processing.");

        //Getting the list of text files that need be processed. They are placed in a local folder named "Decks".
        File directory = new File("Decks");
        File[] decksList = directory.listFiles();

        //Initializing a FileProcessor object. This object will be used to process the text files in "Decks".
        FileProcessor fileProcessor = new FileProcessor();

        //Checking that there is a valid number of decks available for processing within the folder.
        assert decksList != null;
        if (decksList.length < 1){
            System.out.println("No deck files have been added for processing.");
        }
        //Processing each deck that is available in the directory list.
        else {
            for (File file : decksList) {
                fileProcessor.generateReport(file);
            }
        }
    }

}