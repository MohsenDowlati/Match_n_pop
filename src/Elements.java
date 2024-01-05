import javax.swing.*;

public class Elements {
        private final ImageIcon simpleRed = new ImageIcon("icons/simple_red.png"),
        simpleGreen = new ImageIcon("icons/simple_green.png"),
        simpleBlue = new ImageIcon("icons/simple_blue.png"),
        simpleYellow = new ImageIcon("icons/simple_yellow.png"),
        linerRowRed = new ImageIcon("icons/horizontal_red.png"),
        linerRowGreen = new ImageIcon("icons/horizontal_green.png") ,
        linerRowBlue = new ImageIcon("icons/horizontal_blue.png"),
        linerRowYellow = new ImageIcon("icons/horizontal_yellow.png"),
        linerColRed = new ImageIcon("icons/vertical_red.png"),
        linerColGreen = new ImageIcon("icons/vertical_green.png"),
        linerColBlue = new ImageIcon("icons/vertical_blue.png"),
        linerColYellow = new ImageIcon("icons/vertical_yellow.png"),
        radiusRed = new ImageIcon("icons/radial_red.png"),
        radiusGreen = new ImageIcon("icons/radial_green.png"),
        radiusBlue = new ImageIcon("icons/radial_blue.png"),
        radiusYellow = new ImageIcon("icons/radial_yellow.png");

    public int checkFile(String elementType) {
        if (elementType.equalsIgnoreCase("SCR"))
            return 0;
        else if (elementType.equalsIgnoreCase("SCG"))
            return 1;
        else if (elementType.equalsIgnoreCase("SCB"))
            return 2;
        else if (elementType.equalsIgnoreCase("SCY"))
            return 3;
        else if (elementType.equalsIgnoreCase("LRR"))
            return 4;
        else if (elementType.equalsIgnoreCase("LRG"))
            return 5;
        else if (elementType.equalsIgnoreCase("LRB"))
            return 6;
        else if (elementType.equalsIgnoreCase("LRY"))
            return 7;
        else if (elementType.equalsIgnoreCase("LCR"))
            return 8;
        else if (elementType.equalsIgnoreCase("LCG"))
            return 9;
        else if (elementType.equalsIgnoreCase("LCB"))
            return 10;
        else if (elementType.equalsIgnoreCase("LCY"))
            return 11;
        else if (elementType.equalsIgnoreCase("RCR"))
            return 12;
        else if (elementType.equalsIgnoreCase("RCG"))
            return 13;
        else if (elementType.equalsIgnoreCase("RCB"))
            return 14;
        else if (elementType.equalsIgnoreCase("RCY"))
            return 15;
        return -1;
    }

    public ImageIcon icon(int t){
        switch (t){
            case 0 :return simpleRed;
            case 1 :return simpleGreen;
            case 2 :return simpleBlue;
            case 3 :return simpleYellow;
            case 4 :return linerRowRed;
            case 5 :return linerRowGreen;
            case 6 :return linerRowBlue;
            case 7 :return linerRowYellow;
            case 8 :return linerColRed;
            case 9 :return linerColGreen;
            case 10:return linerColBlue;
            case 11:return linerColYellow;
            case 12:return radiusRed;
            case 13:return radiusGreen;
            case 14:return radiusBlue;
            case 15:return radiusYellow;
            default:return null;
        }
    }

}
