import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Paragraph;

public class FileProcessor {

    static Random idGenerator = new Random();
    static HistogramMaker histogramMaker = new HistogramMaker();

    //Method which processes the current deck text file.
    //Input: currentDeck; the deck text file to be processed.
    public void generateReport(File currentDeck){
        //Creating unique id.
        int reportId = 100000000 + idGenerator.nextInt(900000000);

        //Initializing data variables.
        List<Double> costs = new ArrayList<>();
        List<String> invalid = new ArrayList<>();
        boolean voided = false;
        double sum = 0;

        //Reading the current deck line by line.
        try (BufferedReader reader = new BufferedReader(new FileReader(currentDeck))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                //If file meets conditions to be voided the system stops reading.
                if (invalid.size() > 10 | costs.size() > 1000){
                    voided = true;
                    break;
                }
                else {
                    //If line is valid we extract the cost written adding it to the costs list and the sum.
                    //Else we add the current line to the valid2 cards list.
                    if (checkValidity(currentLine)) {
                        String writtenCost = currentLine.replaceAll("[^0-9]", "");
                        double cost = Double.parseDouble(writtenCost);
                        costs.add(cost);
                        sum += cost;
                    } else {
                        invalid.add(currentLine);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //If deck is valid right a regular report, otherwise write a voided one.
        if (!voided) {
            //Create the file path names for the pdf and histogram we want to create.
            String pdfPath = "Reports/SpireDeck_" + reportId + ".pdf";
            String histogramPath = "HistogramPNGs/" + reportId + "histogram.png";

            //Using the costs list data and the histogramMaker class, create the histogram.
            histogramMaker.generateHistogram(costs, histogramPath);

            //Take the histogram and all the extracted data and write it to a pdf using the saveDataToPdf method.
            saveToPDF(reportId, voided, sum, invalid, histogramPath, pdfPath);
        }
        else {
            //Create the file path names for the pdf we want to create.
            String pdfPath = "Reports/SpireDeck_" + reportId + "(VOID).pdf";
            voidedPDF(reportId, pdfPath);
        }
        System.out.println(sum);
        System.out.println(costs);
        System.out.println(invalid);

    }

    //Checks the validity of the lines within an input file.
    //Input: String Line; A string which corresponds to the current line being processed;
    //Output: Boolean variable which tells whether the current line is valid or not.
    public boolean checkValidity(String line){
        //Regular expression which matches lines to the <card name>:<cost> format. No negative costs and no numeric names.
        String format = "^[A-Za-z]+:[1-9][0-9]*(\\\\.[0-9]+)?$";

        //If line is null it is immediately valid2.
        if (line == null){
            return false;
        }

        return line.matches(format);
    }

    //Method which uses iText to write data to a PDF file and save it on the project.
    //Input: All the data needed to write the PDF
    //int id; 9-digit deck id, boolean voided; Validity of the deck, double sum; Sum of the energy costs
    //List<String> valid2; List of valid2 cards, String imagePath; Pathname of histogram png file, String pdfPath; pathname of pdf file.
    private void saveToPDF(int id, boolean voided, double sum, List<String> invalid, String imagePath, String pdfPath) {
        try {
            //Initializing the required objects to create and write to a pdf.
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfPath));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            //Writing deck id.
            document.add(new Paragraph("ID: " + id));
            //Writing total cost.
            document.add(new Paragraph("Total Cost: " + sum));

            // Creating, resizing, and adding the histogram image to the PDF
            Image image = new Image(ImageDataFactory.create(imagePath));
            image.scaleToFit(PageSize.A4.getWidth() - 100, PageSize.A4.getHeight() - 100);
            document.add(image);

            //Writing the list of valid2 cards.
            document.add(new Paragraph("Invalid cards:"));
            for (String card : invalid) {
                document.add(new Paragraph(card));
            }
            document.close();

            System.out.println("PDF saved to " + pdfPath);
        } catch (IOException e) {
            System.err.println("Error saving PDF: " + e.getMessage());
        }
    }

    //Method which uses iText to write data to a voided PDF file and save it on the project.
    //Input: int id; 9-digit deck id, boString pdfPath: pathname of pdf file.
    private void voidedPDF(int id, String pdfPath) {
        try {
            //Initializing the required objects to create and write to a pdf.
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfPath));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            //Writing deck id.
            document.add(new Paragraph("ID: " + id));

            //Report needs to be voided write VOID instead of information.
            document.add(new Paragraph("VOIDED"));
            document.close();

            System.out.println("PDF saved to " + pdfPath);
        } catch (IOException e) {
            System.err.println("Error saving PDF: " + e.getMessage());
        }
    }
}
