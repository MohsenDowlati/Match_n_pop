import javax.swing.*;

public class Main {
    public static int lang;
    public static void main(String[] args) {
        //declare options
        String[] langs = new String[]{"فارسی","English"};
        //option pane
        lang = JOptionPane.showOptionDialog(null,"Choose your language.","Language",
                JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,new ImageIcon("icons/language.png"),
                langs,null);//0==farsi,1==english
        new Menu_Frame();
    }
}
