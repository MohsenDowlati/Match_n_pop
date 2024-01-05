import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Scores {
    Scanner scanner;
    private ArrayList<Integer> top5 = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    Language language = new Language(Main.lang);
    {
        try {
            scanner = new Scanner(new File("SCORES"));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,language.not_found_exception(),language.not_found_exception_title(),JOptionPane.ERROR_MESSAGE);
        }
    }
    public Scores() {
        fromFile();
    }

    public void fromFile() {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            names.add(line.substring(0,line.indexOf(' ')));
            top5.add(new Integer(line.substring(line.indexOf(' ')+1)));
        }
        scanner.close();
    }

    private String highs() {
        String highs = "";
        for (int i=0;i<5;i++) {
            highs += (i+1)+". "+names.get(i)+"....."+top5.get(i)+"\n";
        }
        return highs;
    }

    public void showHighScores() {
        String highScores = highs();
        JOptionPane.showMessageDialog(null,highScores,"",JOptionPane.INFORMATION_MESSAGE);
    }

    private int search(int key){
        for (int i=0;i<5;i++) {
            if (top5.get(i)<key)
                return i;
        }
        return -1;
    }

    public void newScore (String name,int score) {
        int index = search(score);
        if (index!=-1){
            top5.add(index,score);
            names.add(index,name);
            top5.remove(5);
            names.remove(5);
        }
        showHighScores();
        try {
            putInFile();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,e+language.not_found_exception(),language.not_found_exception_title(),JOptionPane.ERROR_MESSAGE);
        }
    }

    private void putInFile() throws FileNotFoundException {
        PrintStream ps = new PrintStream(new File("SCORES"));
        for (int i=0;i<5;i++){
            ps.println(names.get(i)+" "+top5.get(i));
        }
    }

    public void winner() {
        String name= JOptionPane.showInputDialog(null,language.submit_name());
        newScore(name,1500);
    }
}
