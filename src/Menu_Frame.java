import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class Menu_Frame extends JFrame implements ActionListener {
    public static boolean isDarkMode;
    private ImageIcon darkModeIcon = new ImageIcon("icons/dark_mode.png");
    private ImageIcon lightModeIcon = new ImageIcon("icons/light_mode.png");
    private Language language = new Language(Main.lang);
    private JButton randomStart , fileStart , scores;
    private JCheckBox darkMode;
    private Container container;
    JFileChooser fileChooser;
    public Menu_Frame() {
        //set JFrame
        this.setTitle(language.Title());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(420,420));
        this.setResizable(true);

        //container
        container = this.getContentPane();
        container.setBackground(new Color(0x8F8FFF));
        container.setLayout(new FlowLayout());
        //dark mode check box
        darkMode = new JCheckBox();
        darkMode.setFocusable(false);
        darkMode.setBackground(new Color(0x8F8FFF));
        darkMode.addActionListener(this);
        darkMode.setIcon(darkModeIcon);
        darkMode.setBounds(10,15,40,40);
        //randomly
        randomStart = new JButton(language.menuButtons(0));
        buttons(randomStart);
        //file start
        fileStart = new JButton(language.menuButtons(1));
        buttons(fileStart);
        //high scores
        scores = new JButton(language.menuButtons(2));
        buttons(scores);

        container.add(darkMode);
        container.add(randomStart);
        container.add(fileStart);
        container.add(scores);


        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //dark mode check box
        if (darkMode.isSelected()) {
            container.setBackground(new Color(0x3535A2));
            darkMode.setBackground(new Color(0x3535A2));
            darkMode.setIcon(lightModeIcon);
            isDarkMode = true;
        } else {
            container.setBackground(new Color(0x8F8FFF));
            darkMode.setBackground(new Color(0x8F8FFF));
            darkMode.setIcon(darkModeIcon);
            isDarkMode = false;
        }
        //random start
        if (e.getSource()==randomStart) {
            new Game_Frame();
            this.dispose();
        }
        //show High Scores
        if (e.getSource()==scores)
            new Scores().showHighScores();
        //start from file
        if (e.getSource()==fileStart){
            //select from PC
            fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                //catch Exception
                try {
                    new Game_Frame(new File(fileChooser.getSelectedFile().getAbsolutePath()));
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null,language.not_found_exception(),language.not_found_exception_title(),JOptionPane.ERROR_MESSAGE);
                }
                this.dispose();//close this frame
            }
        }
    }
    //design buttons
    private void buttons(JButton button){
        button.setBackground(new Color(0xEE8FF1));
        button.setFont(language.font());
        button.setFocusable(false);
        button.addActionListener(this);
    }
}
