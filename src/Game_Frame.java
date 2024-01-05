import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game_Frame extends JFrame implements ActionListener {
    private static Language language = new Language(Main.lang);
    static Scores scores = new Scores();
    //Map of game
    static JButton[][] cells = new JButton[10][10];
    private JButton backMenu;
    private static JLabel scoreLabel;
    static Elements elements = new Elements();
    public static int[][] map = new int[10][10];
    //Score starts from 0 unless import a CSV File.
    private static int score = 0;
    //variants for check elements
    static boolean isSelected = false;
    static int firstSelectRow = -1, firstSelectCol = -1, firstValue = -1;
    static int secondSelectRow = -1, secondSelectCol = -1, secondValue = -1;

    public Game_Frame() {
        setCells();
        design();
        scanMap();
    }

    public Game_Frame(File file) throws FileNotFoundException {
        setCells(file);
        design();
        scanMap();
    }

    private void setCells() {
        //set from 0 to 3.
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Random random = new Random();
                map[i][j] = random.nextInt(4);
            }
        }
    }

    private void setCells(File file) throws FileNotFoundException {
        //First line contains score
        Scanner fileReader = new Scanner(file);
        String num = fileReader.nextLine();
        score += new Integer(num.substring(0,num.indexOf(',')));
        int i = 0;
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            for (int j = 0; j < 10; j++) {
                map[j][i] = elements.checkFile(line.substring(4 * j, 4 * j + 3));
            }
            i++;
        }
        fileReader.close();
    }

    private void design() {
        //declare JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(language.Title());
        this.setSize(1000, 1000);
        this.setResizable(false);
        //declare Container
        Container container = this.getContentPane();
        if (Menu_Frame.isDarkMode)
            container.setBackground(new Color(0x3535A2));
        else
            container.setBackground(new Color(0x8F8FFF));
        container.setLayout(null);
        //Back to Menu
        backMenu = new JButton(language.backToMenu());
        backMenu.setBackground(new Color(0xEE8FF1));
        backMenu.setFont(language.font());
        backMenu.setFocusable(false);
        backMenu.addActionListener(this);
        backMenu.setBounds(770, 850, 200, 80);
        //Score's Label
        scoreLabel = new JLabel();

        setLabel();
        scoreLabel.setBounds(790, 20, 190, 80);
        //Game's cells
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cells[i][j] = new JButton();
                cells[i][j].setBounds((i * 75) + 10, (j * 75) + 10, 75, 75);
                setIcons(i, j);
                cells[i][j].addActionListener(new MyListener(i, j));
                container.add(cells[i][j]);

            }
        }
        container.add(backMenu);
        container.add(scoreLabel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backMenu) {
            int exit = JOptionPane.showOptionDialog(null, language.are_you_sure(), language.Exit(), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (exit == 0) {
                String name = JOptionPane.showInputDialog(null, language.submit_name());
                scores.newScore(name, score);
                new Menu_Frame();
                this.dispose();
            }
        }


    }


    public static void setIcons(int i, int j) {
        cells[i][j].setIcon(elements.icon(map[i][j]));
    }

    public static void setLabel() {
        scoreLabel.setText(language.scoreLabel() + String.valueOf(score));
        scoreLabel.setFont(language.font());
    }

    //move elements
    public static void swap(int row1, int col1, int row2, int col2) {
        int temp = map[row1][col1];
        map[row1][col1] = map[row2][col2];
        map[row2][col2] = temp;
        setIcons(row1, col1);
        setIcons(row2, col2);
        if (row1!=-1 && row2!=-1 && col1!=-1 && col2!=-1 && map[row1][col1]!=-1 && map[row2][col2]!=-1&&
                !horCheck3(row1, col1) && !horCheck3(row2, col2) && !verCheck3(row1, col1) && !verCheck3(row2, col2)) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                temp = map[row1][col1];
                map[row1][col1] = map[row2][col2];
                map[row2][col2] = temp;
                setIcons(row1, col1);
                setIcons(row2, col2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        } else {
            scanMap();
        }

    }

    //check if 3 elements in row are same.
    private static boolean horCheck3(int row, int col) {
        //2 on left
        if (col >= 2 && map[row][col - 1] % 4 == map[row][col] % 4 && map[row][col - 2] % 4 == map[row][col] % 4)
            return true;
            //2 on right
        else if (col <= 7 && map[row][col + 1] % 4 == map[row][col] % 4 && map[row][col + 2] % 4 == map[row][col] % 4)
            return true;
            //1 each on left and right
        else if (col >= 1 && col <= 8 && map[row][col + 1] % 4 == map[row][col] % 4 && map[row][col - 1] % 4 == map[row][col] % 4)
            return true;
        else
            return false;
    }

    private static boolean verCheck3(int row, int col) {
        //2 on top
        if (row >= 2 && map[row - 1][col] % 4 == map[row][col] % 4 && map[row - 2][col] % 4 == map[row][col] % 4)
            return true;
            //2 on bottom
        else if (row <= 7 && map[row + 1][col] % 4 == map[row][col] % 4 && map[row + 2][col] % 4 == map[row][col] % 4)
            return true;
            //1 each on top and bottom
        else if (row >= 1 && row <= 8 && map[row + 1][col] % 4 == map[row][col] % 4 && map[row - 1][col] % 4 == map[row][col] % 4)
            return true;
        else
            return false;
    }

    //check if 4 elements in row are same.
    private static boolean horCheck4(int row, int col) {
        //3 on left
        if (col >= 3 && map[row][col - 1] % 4 == map[row][col] % 4 && map[row][col - 2] % 4 == map[row][col] % 4 && map[row][col - 3] % 4 == map[row][col] % 4)
            return true;
            //2 on left, 1 on right
        else if (col >= 2 && col < 9 && map[row][col - 1] % 4 == map[row][col] % 4 && map[row][col - 2] % 4 == map[row][col] % 4 && map[row][col + 1] % 4 == map[row][col] % 4)
            return true;
            //1 on left, 2 on right
        else if (col >= 1 && col < 8 && map[row][col - 1] % 4 == map[row][col] % 4 && map[row][col + 1] % 4 == map[row][col] % 4 && map[row][col + 2] % 4 == map[row][col] % 4)
            return true;
            //3 on right
        else if (col < 7 && map[row][col + 1] % 4 == map[row][col] % 4 && map[row][col + 2] % 4 == map[row][col] % 4 && map[row][col + 3] % 4 == map[row][col] % 4)
            return true;
        else
            return false;
    }

    private static boolean verCheck4(int row, int col) {
        //3 on top
        if (row >= 3 && map[row - 1][col] % 4 == map[row][col] % 4 && map[row - 2][col] % 4 == map[row][col] % 4 && map[row - 3][col] % 4 == map[row][col] % 4)
            return true;
            //2 on top, 1 on below
        else if (row >= 2 && row < 9 && map[row - 1][col] % 4 == map[row][col] % 4 && map[row - 2][col] % 4 == map[row][col] % 4 && map[row + 1][col] % 4 == map[row][col] % 4)
            return true;
            //1 on top, 2 on below
        else if (row >= 1 && row < 8 && map[row - 1][col] % 4 == map[row][col] % 4 && map[row + 1][col] % 4 == map[row][col] % 4 && map[row + 2][col] % 4 == map[row][col] % 4)
            return true;
            //3 on below
        else if (row < 7 && map[row + 1][col] == map[row][col] && map[row + 2][col] == map[row][col] && map[row + 3][col] == map[row][col])
            return true;
        else
            return false;
    }

    //check if 5 elements in row are same.
    private static boolean horCheck5(int row, int col) {
        //4 on left
        if (col >= 4 && map[row][col - 1] == map[row][col] && map[row][col - 2] == map[row][col] && map[row][col - 3] == map[row][col] && map[row][col - 4] == map[row][col])
            return true;
            //3 on left, 1 on right
        else if (col >= 3 && col < 9 && map[row][col - 3] == map[row][col] && map[row][col - 1] == map[row][col] && map[row][col - 2] == map[row][col] && map[row][col + 1] == map[row][col])
            return true;
            //2 on left, 2 on right
        else if (col >= 2 && col < 8 && map[row][col - 2] == map[row][col] && map[row][col - 1] == map[row][col] && map[row][col + 1] == map[row][col] && map[row][col + 2] == map[row][col])
            return true;
            //1 on left, 3 on right
        else if (col >= 1 && col < 7 && map[row][col - 1] == map[row][col] && map[row][col + 1] == map[row][col] && map[row][col + 2] == map[row][col] && map[row][col + 3] == map[row][col])
            return true;
            //5 on right
        else if (col < 5 && map[row][col + 1] == map[row][col] && map[row][col + 2] == map[row][col] && map[row][col + 3] == map[row][col] && map[row][col + 4] == map[row][col])
            return true;
        else
            return false;
    }

    private static boolean verCheck5(int row, int col) {
        //4 on top
        if (row >= 4 && map[row - 1][col] == map[row][col] && map[row - 2][col] == map[row][col] && map[row - 3][col] == map[row][col] && map[row - 4][col] == map[row][col])
            return true;
            //3 on top, 1 on below
        else if (row >= 3 && row < 9 && map[row - 1][col] == map[row][col] && map[row - 2][col] == map[row][col] && map[row - 3][col] == map[row][col] && map[row + 1][col] == map[row][col])
            return true;
            //2 on top, 2 on below
        else if (row >= 2 && row < 8 && map[row - 2][col] == map[row][col] && map[row - 1][col] == map[row][col] && map[row + 1][col] == map[row][col] && map[row + 2][col] == map[row][col])
            return true;
            //1 on top, 3 on below
        else if (row >= 1 && row < 7 && map[row - 1][col] == map[row][col] && map[row + 1][col] == map[row][col] && map[row + 2][col] == map[row][col] && map[row + 3][col] == map[row][col])
            return true;
            //4 on below
        else if (row < 6 && map[row + 1][col] == map[row][col] && map[row + 2][col] == map[row][col] && map[row + 3][col] == map[row][col] && map[row + 4][col] == map[row][col])
            return true;
        else
            return false;
    }

    public static void scanMap() {
        boolean exit = true;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (map[i][j] != -1) {
                    if (horCheck3(i, j) || verCheck3(i, j)) {
                        if (horCheck4(i, j) || verCheck4(i, j)) {
                            if (horCheck5(i, j) || verCheck5(i, j)) {
                                if (horCheck5(i, j)) {
                                    horMark5(i,j);
                                } else if (verCheck5(i, j)) {
                                    verMark5(i,j);
                                }
                            } else {
                                if (horCheck4(i, j)) {
                                    horMark4(i, j);
                                } else if (verCheck4(i, j)) {
                                    verMark4(i, j);
                                }
                            }
                        } else {
                            if (horCheck3(i, j)) {
                                horMark3(i, j);
                            } else {
                                verMark3(i, j);
                            }

                        }
                        if (score > 1500) {
                            scores.winner();
                            new Menu_Frame();
                            return;
                        }
                        exit = false;
                    }
                }
            }
        }
        //fill -1s
        for (int i = 1; i <10; i++)
            for (int j = 0; j <10; j++)
                if (map[i][j] == -1)
                    moveRow(i, j);
        for (int i = 0; i <10; i++)
            for (int j = 0; j <10; j++)
                if (map[i][j]==-1)
                    map[i][j]=new Random().nextInt(4);
        for (int i = 0; i <10; i++)
            for (int j = 0; j <10; j++)
                setIcons(i, j);
        if (exit) return;
        else scanMap();
    }
    private static boolean isHorLinear(int i,int j){
        return map[i][j]>3 && map[i][j]<8;
    }
    private static boolean isVerLinear(int i,int j){
        return map[i][j]>7 && map[i][j]<12;
    }
    private static boolean isRadial(int i,int j){
        return map[i][j]>12 && map[i][j]<16;
    }
    private static void horMark3(int row, int col) {
        //2 on left
        if (col >= 2 && map[row][col - 1] == map[row][col] && map[row][col - 2] == map[row][col]) {
            if (isHorLinear(row,col) || isHorLinear(row,col-1) || isHorLinear(row, col-2))
                horLinearBomb(row);
            if (isVerLinear(row,col))
                verLinearBomb(col);
            if (isVerLinear(row, col-1))
                verLinearBomb(col-1);
            if (isVerLinear(row, col-2))
                verLinearBomb(col-2);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row,col-1))
                radialBomb(row,col-1);
            if (isRadial(row,col-2))
                radialBomb(row,col-2);
            map[row][col - 1] = -1;
            map[row][col] = -1;
            map[row][col - 2] = -1;
            //2 on right
        } else {
            if (col <= 7 && map[row][col + 1] == map[row][col] && map[row][col + 2] == map[row][col]) {
                if (isHorLinear(row,col) || isHorLinear(row,col+1) || isHorLinear(row, col+2))
                    horLinearBomb(row);
                if (isVerLinear(row,col))
                    verLinearBomb(col);
                if (isVerLinear(row, col+1))
                    verLinearBomb(col+1);
                if (isVerLinear(row, col+2))
                    verLinearBomb(col+2);
                if (isRadial(row,col))
                    radialBomb(row,col);
                if (isRadial(row,col+1))
                    radialBomb(row,col+1);
                if (isRadial(row,col+2))
                    radialBomb(row,col+2);
                map[row][col + 2] = -1;
                map[row][col] = -1;
                map[row][col + 1] = -1;
                //1 each on left and right
            } else {
                if (col >= 1 && col <= 8 && map[row][col + 1] == map[row][col] && map[row][col - 1] == map[row][col]) {
                    if (isHorLinear(row,col) || isHorLinear(row,col-1) || isHorLinear(row, col+1))
                        horLinearBomb(row);
                    if (isVerLinear(row,col))
                        verLinearBomb(col);
                    if (isVerLinear(row, col-1))
                        verLinearBomb(col-1);
                    if (isVerLinear(row, col+1))
                        verLinearBomb(col+1);
                    if (isRadial(row,col))
                        radialBomb(row,col);
                    if (isRadial(row,col-1))
                        radialBomb(row,col-1);
                    if (isRadial(row,col+1))
                        radialBomb(row,col+1);
                    map[row][col - 1] = -1;
                    map[row][col] = -1;
                    map[row][col + 1] = -1;
                }

            }
        }
        score += 15;
        setLabel();
    }

    private static void verMark3(int row, int col) {
        //2 on top
        if (row >= 2 && map[row - 1][col] == map[row - 2][col]) {
            if (isVerLinear(row,col) || isVerLinear(row-1,col) || isVerLinear(row-2, col))
                verLinearBomb(col);
            if (isHorLinear(row,col))
                horLinearBomb(row);
            if (isHorLinear(row-1, col))
                horLinearBomb(row-1);
            if (isHorLinear(row-2, col))
                horLinearBomb(row-2);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row-1,col))
                radialBomb(row-1,col);
            if (isRadial(row-2,col))
                radialBomb(row-2,col);
            map[row - 2][col] = -1;
            map[row - 1][col] = -1;
            map[row][col] = -1;
            //2 on below
        } else {
            if (row <= 7 && map[row + 1][col] == map[row + 2][col]) {
                if (isVerLinear(row,col) || isVerLinear(row+1,col) || isVerLinear(row+2, col))
                    verLinearBomb(col);
                if (isHorLinear(row,col))
                    horLinearBomb(row);
                if (isHorLinear(row+1, col))
                    horLinearBomb(row+1);
                if (isHorLinear(row+2, col))
                    horLinearBomb(row+2);
                if (isRadial(row,col))
                    radialBomb(row,col);
                if (isRadial(row+1,col))
                    radialBomb(row+1,col);
                if (isRadial(row+2,col))
                    radialBomb(row+2,col);
                map[row + 1][col] = -1;
                map[row][col] = -1;
                map[row + 2][col] = -1;
                //1 each on left and right
            } else {
                if (row >= 1 && row <= 8 && map[row + 1][col] == map[row - 1][col]) {
                    if (isVerLinear(row,col) || isVerLinear(row-1,col) || isVerLinear(row+1, col))
                        verLinearBomb(col);
                    if (isHorLinear(row,col))
                        horLinearBomb(row);
                    if (isHorLinear(row-1, col))
                        horLinearBomb(row-1);
                    if (isHorLinear(row+1, col))
                        horLinearBomb(row+1);
                    if (isRadial(row,col))
                        radialBomb(row,col);
                    if (isRadial(row-1,col))
                        radialBomb(row-1,col);
                    if (isRadial(row+1,col))
                        radialBomb(row+1,col);
                    map[row - 1][col] = -1;
                    map[row][col] = -1;
                    map[row + 1][col] = -1;
                }

            }
        }
        score += 15;
        setLabel();
    }

    private static void horMark4(int row, int col) {
        if (col >= 3 && map[row][col - 1] == map[row][col - 2] && map[row][col - 3] == map[row][col]) {
            if (isHorLinear(row,col) || isHorLinear(row,col-1) || isHorLinear(row, col-2) || isHorLinear(row, col-3))
                horLinearBomb(row);
            if (isVerLinear(row,col))
                verLinearBomb(col);
            if (isVerLinear(row, col-1))
                verLinearBomb(col-1);
            if (isVerLinear(row, col-2))
                verLinearBomb(col-2);
            if (isVerLinear(row,col-3))
                verLinearBomb(col-3);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row,col-1))
                radialBomb(row,col-1);
            if (isRadial(row,col-2))
                radialBomb(row,col-2);
            if (isRadial(row,col-3))
                radialBomb(row,col-3);
            if (map[row][col-3]>=0 && map[row][col-3]<4)
                map[row][col - 3] += 4;
            map[row][col - 2] = -1;
            map[row][col - 1] = -1;
            map[row][col] = -1;
            //2 on left, 1 on right
        } else if (col >= 2 && col < 9 && map[row][col - 1] == map[row][col - 2] && map[row][col + 1] == map[row][col]) {
            if (isHorLinear(row,col) || isHorLinear(row,col-1) || isHorLinear(row, col-2) || isHorLinear(row, col+1))
                horLinearBomb(row);
            if (isVerLinear(row,col))
                verLinearBomb(col);
            if (isVerLinear(row, col-1))
                verLinearBomb(col-1);
            if (isVerLinear(row, col-2))
                verLinearBomb(col-2);
            if (isVerLinear(row,col+1))
                verLinearBomb(col+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row,col-1))
                radialBomb(row,col-1);
            if (isRadial(row,col-2))
                radialBomb(row,col-2);
            if (isRadial(row,col+1))
                radialBomb(row,col+1);
            if (map[row][col-2]>=0 && map[row][col-2]<4)
                map[row][col - 2] += 4;
            map[row][col - 1] = -1;
            map[row][col] = -1;
            map[row][col + 1] = -1;
            //1 on left, 2 on right
        } else if (col >= 1 && col < 8 && map[row][col - 1] == map[row][col + 1] && map[row][col + 2] == map[row][col]) {
            if (isHorLinear(row,col) || isHorLinear(row,col-1) || isHorLinear(row, col+1) || isHorLinear(row, col+2))
                horLinearBomb(row);
            if (isVerLinear(row,col))
                verLinearBomb(col);
            if (isVerLinear(row, col-1))
                verLinearBomb(col-1);
            if (isVerLinear(row, col+1))
                verLinearBomb(col+1);
            if (isVerLinear(row,col+2))
                verLinearBomb(col+2);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row,col-1))
                radialBomb(row,col-1);
            if (isRadial(row,col+1))
                radialBomb(row,col+1);
            if (isRadial(row,col+2))
                radialBomb(row,col+2);
            if (map[row][col-1]>=0 && map[row][col-1]<4)
                map[row][col - 1] += 4;
            map[row][col] = -1;
            map[row][col + 1] = -1;
            map[row][col + 2] = -1;
            //3 on right
        } else if (col < 7 && map[row][col + 1] == map[row][col] && map[row][col + 2] == map[row][col] && map[row][col + 3] == map[row][col]) {
            if (isHorLinear(row,col) || isHorLinear(row,col+1) || isHorLinear(row, col+2) || isHorLinear(row, col+3))
                horLinearBomb(row);
            if (isVerLinear(row,col))
                verLinearBomb(col);
            if (isVerLinear(row, col+1))
                verLinearBomb(col+1);
            if (isVerLinear(row, col+2))
                verLinearBomb(col+2);
            if (isVerLinear(row,col+3))
                verLinearBomb(col+3);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row,col+1))
                radialBomb(row,col+1);
            if (isRadial(row,col+2))
                radialBomb(row,col+2);
            if (isRadial(row,col+3))
                radialBomb(row,col+3);
            if (map[row][col]>=0 && map[row][col]<4)
                map[row][col] += 4;
            map[row][col + 1] = -1;
            map[row][col + 2] = -1;
            map[row][col + 3] = -1;
        }
        score += 20;
        setLabel();
    }

    private static void verMark4(int row, int col) {
        if (row >= 3 && map[row - 1][col] == map[row - 2][col] && map[row - 3][col] == map[row][col]) {
            if (isVerLinear(row,col) || isVerLinear(row-1,col) || isVerLinear(row-2, col) || isVerLinear(row-3,col))
                verLinearBomb(col);
            if (isHorLinear(row,col))
                horLinearBomb(row);
            if (isHorLinear(row-1, col))
                horLinearBomb(row-1);
            if (isHorLinear(row-2, col))
                horLinearBomb(row-2);
            if (isHorLinear(row-3, col))
                horLinearBomb(row-3);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row-1,col))
                radialBomb(row-1,col);
            if (isRadial(row-2,col))
                radialBomb(row-2,col);
            if (isRadial(row-3, col))
                radialBomb(row-3, col);
            if (map[row][col]>=0 && map[row][col]<4)
                map[row][col] += 8;
            map[row - 1][col] = -1;
            map[row - 2][col] = -1;
            map[row - 3][col] = -1;
            //2 on left, 1 on right
        } else if (row >= 2 && row < 9 && map[row - 1][col] == map[row - 2][col] && map[row + 1][col] == map[row][col]) {
            if (isVerLinear(row,col) || isVerLinear(row-1,col) || isVerLinear(row-2, col) || isVerLinear(row+1,col))
                verLinearBomb(col);
            if (isHorLinear(row,col))
                horLinearBomb(row);
            if (isHorLinear(row-1, col))
                horLinearBomb(row-1);
            if (isHorLinear(row-2, col))
                horLinearBomb(row-2);
            if (isHorLinear(row+1, col))
                horLinearBomb(row+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row-1,col))
                radialBomb(row-1,col);
            if (isRadial(row-2,col))
                radialBomb(row-2,col);
            if (isRadial(row+1, col))
                radialBomb(row+1, col);
            if (map[row+1][col]>=0 && map[row+1][col]<4)
                map[row + 1][col] += 8;
            map[row][col] = -1;
            map[row - 1][col] = -1;
            map[row - 2][col] = -1;
            //1 on left, 2 on right
        } else if (row >= 1 && row < 8 && map[row - 1][col] == map[row + 1][col] && map[row + 2][col] == map[row][col]) {
            if (isVerLinear(row,col) || isVerLinear(row-1,col) || isVerLinear(row+2, col) || isVerLinear(row+1,col))
                verLinearBomb(col);
            if (isHorLinear(row,col))
                horLinearBomb(row);
            if (isHorLinear(row-1, col))
                horLinearBomb(row-1);
            if (isHorLinear(row+2, col))
                horLinearBomb(row+2);
            if (isHorLinear(row+1, col))
                horLinearBomb(row+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row-1,col))
                radialBomb(row-1,col);
            if (isRadial(row+2,col))
                radialBomb(row+2,col);
            if (isRadial(row+1, col))
                radialBomb(row+1, col);
            if (map[row+2][col]>=0 && map[row+2][col]<4)
                map[row + 2][col] += 8;
            map[row + 1][col] = -1;
            map[row][col] = -1;
            map[row - 1][col] = -1;
            //3 on right
        } else if (row < 7 && map[row + 1][col] == map[row + 2][col] && map[row + 3][col] == map[row][col]) {
            if (isVerLinear(row,col) || isVerLinear(row+1,col) || isVerLinear(row+2, col) || isVerLinear(row+3,col))
                verLinearBomb(col);
            if (isHorLinear(row,col))
                horLinearBomb(row);
            if (isHorLinear(row+3, col))
                horLinearBomb(row+3);
            if (isHorLinear(row+2, col))
                horLinearBomb(row+2);
            if (isHorLinear(row+1, col))
                horLinearBomb(row+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row+3,col))
                radialBomb(row+3,col);
            if (isRadial(row+2,col))
                radialBomb(row+2,col);
            if (isRadial(row+1, col))
                radialBomb(row+1, col);
            if (map[row+3][col]>=0 && map[row+3][col]<4)
                map[row + 3][col] += 8;
            map[row + 2][col] = -1;
            map[row + 1][col] = -1;
            map[row][col] = 3;
        }
        score += 20;
        setLabel();
    }
    private static void horMark5(int row, int col) {
        //4 on left
        if (col >= 4 && map[row][col - 1] == map[row][col] && map[row][col - 2] == map[row][col] && map[row][col - 3] == map[row][col] && map[row][col - 4] == map[row][col]) {
            if (isHorLinear(row,col) || isHorLinear(row,col-1) || isHorLinear(row, col-2) || isHorLinear(row, col-3) || isHorLinear(row, col-4))
                horLinearBomb(row);
            if (isVerLinear(row,col))
                verLinearBomb(col);
            if (isVerLinear(row, col-1))
                verLinearBomb(col-1);
            if (isVerLinear(row, col-2))
                verLinearBomb(col-2);
            if (isVerLinear(row,col-3))
                verLinearBomb(col-3);
            if (isVerLinear(row, col-4))
                verLinearBomb(col-4);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row,col-1))
                radialBomb(row,col-1);
            if (isRadial(row,col-2))
                radialBomb(row,col-2);
            if (isRadial(row,col-3))
                radialBomb(row,col-3);
            if (isRadial(row,col-4))
                radialBomb(row, col-4);
            if (map[row][col-4]>=0 && map[row][col-4]<4)
                map[row][col-4] += 12;
            map[row][col-3] = -1;
            map[row][col-2] =-1;
            map[row][col-1] =-1;
            map[row][col] =-1;
        }
            //3 on left, 1 on right
        else if (col >= 3 && col < 9 && map[row][col - 3] == map[row][col] && map[row][col - 1] == map[row][col] && map[row][col - 2] == map[row][col] && map[row][col + 1] == map[row][col]) {
            if (isHorLinear(row,col) || isHorLinear(row,col-1) || isHorLinear(row, col-2) || isHorLinear(row, col-3) || isHorLinear(row, col+1))
                horLinearBomb(row);
            if (isVerLinear(row,col))
                verLinearBomb(col);
            if (isVerLinear(row, col-1))
                verLinearBomb(col-1);
            if (isVerLinear(row, col-2))
                verLinearBomb(col-2);
            if (isVerLinear(row,col-3))
                verLinearBomb(col-3);
            if (isVerLinear(row, col+1))
                verLinearBomb(col+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row,col-1))
                radialBomb(row,col-1);
            if (isRadial(row,col-2))
                radialBomb(row,col-2);
            if (isRadial(row,col-3))
                radialBomb(row,col-3);
            if (isRadial(row,col+1))
                radialBomb(row, col+1);
            if (map[row][col-3]>=0 && map[row][col-3]<4)
                map[row][col-3] += 12;
            map[row][col-2] = -1;
            map[row][col-1] =-1;
            map[row][col] =-1;
            map[row][col+1] =-1;
        }
            //2 on left, 2 on right
        else if (col >= 2 && col < 8 && map[row][col - 2] == map[row][col] && map[row][col - 1] == map[row][col] && map[row][col + 1] == map[row][col] && map[row][col + 2] == map[row][col]) {
            if (isHorLinear(row,col) || isHorLinear(row,col-1) || isHorLinear(row, col-2) || isHorLinear(row, col+2) || isHorLinear(row, col+1))
                horLinearBomb(row);
            if (isVerLinear(row,col))
                verLinearBomb(col);
            if (isVerLinear(row, col-1))
                verLinearBomb(col-1);
            if (isVerLinear(row, col-2))
                verLinearBomb(col-2);
            if (isVerLinear(row,col+2))
                verLinearBomb(col+2);
            if (isVerLinear(row, col+1))
                verLinearBomb(col+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row,col-1))
                radialBomb(row,col-1);
            if (isRadial(row,col-2))
                radialBomb(row,col-2);
            if (isRadial(row,col+2))
                radialBomb(row,col+2);
            if (isRadial(row,col+1))
                radialBomb(row, col+1);
            if (map[row][col-2]>=0 && map[row][col-2]<4)
                map[row][col-2] += 12;
            map[row][col-1] = -1;
            map[row][col] =-1;
            map[row][col+1] =-1;
            map[row][col+2] =-1;
        }
            //1 on left, 3 on right
        else if (col >= 1 && col < 7 && map[row][col - 1] == map[row][col] && map[row][col + 1] == map[row][col] && map[row][col + 2] == map[row][col] && map[row][col + 3] == map[row][col]) {
            if (isHorLinear(row,col) || isHorLinear(row,col-1) || isHorLinear(row, col+2) || isHorLinear(row, col+3) || isHorLinear(row, col+1))
                horLinearBomb(row);
            if (isVerLinear(row,col))
                verLinearBomb(col);
            if (isVerLinear(row, col-1))
                verLinearBomb(col-1);
            if (isVerLinear(row, col+2))
                verLinearBomb(col+2);
            if (isVerLinear(row,col+3))
                verLinearBomb(col+3);
            if (isVerLinear(row, col+1))
                verLinearBomb(col+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row,col-1))
                radialBomb(row,col-1);
            if (isRadial(row,col+2))
                radialBomb(row,col+2);
            if (isRadial(row,col+3))
                radialBomb(row,col+3);
            if (isRadial(row,col+1))
                radialBomb(row, col+1);
            if (map[row][col-1]>=0 && map[row][col-1]<4)
                map[row][col-1] += 12;
            map[row][col] = -1;
            map[row][col+1] =-1;
            map[row][col+2] =-1;
            map[row][col+3] =-1;
        }
            //5 on right
        else if (col < 5 && map[row][col + 1] == map[row][col] && map[row][col + 2] == map[row][col] && map[row][col + 3] == map[row][col] && map[row][col + 4] == map[row][col]) {
            if (isHorLinear(row,col) || isHorLinear(row,col+1) || isHorLinear(row, col+2) || isHorLinear(row, col+3) || isHorLinear(row, col+4))
                horLinearBomb(row);
            if (isVerLinear(row,col))
                verLinearBomb(col);
            if (isVerLinear(row, col+1))
                verLinearBomb(col+1);
            if (isVerLinear(row, col+2))
                verLinearBomb(col+2);
            if (isVerLinear(row,col+3))
                verLinearBomb(col+3);
            if (isVerLinear(row, col+4))
                verLinearBomb(col+4);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row,col+1))
                radialBomb(row,col+1);
            if (isRadial(row,col+2))
                radialBomb(row,col+2);
            if (isRadial(row,col+3))
                radialBomb(row,col+3);
            if (isRadial(row,col+4))
                radialBomb(row, col+4);
            if (map[row][col]>=0 && map[row][col]<4)
                map[row][col] += 12;
            map[row][col+1] = -1;
            map[row][col+2] =-1;
            map[row][col+3] =-1;
            map[row][col+4] =-1;
        }
    }

    private static void verMark5(int row, int col) {
        //4 on top
        if (row >= 4 && map[row - 1][col] == map[row][col] && map[row - 2][col] == map[row][col] && map[row - 3][col] == map[row][col] && map[row - 4][col] == map[row][col]) {
            if (isVerLinear(row,col) || isVerLinear(row-1,col) || isVerLinear(row-2, col) || isVerLinear(row-3,col) || isVerLinear(row-4, col))
                verLinearBomb(col);
            if (isHorLinear(row,col))
                horLinearBomb(row);
            if (isHorLinear(row-3, col))
                horLinearBomb(row-3);
            if (isHorLinear(row-2, col))
                horLinearBomb(row-2);
            if (isHorLinear(row-1, col))
                horLinearBomb(row-1);
            if (isHorLinear(row-4, col))
                horLinearBomb(row-4);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row-3,col))
                radialBomb(row-3,col);
            if (isRadial(row-2,col))
                radialBomb(row-2,col);
            if (isRadial(row-1, col))
                radialBomb(row-1, col);
            if (isRadial(row-4, col))
                radialBomb(row-4, col);
            if (map[row][col]>=0 && map[row][col]<4)
                map[row][col] += 12;
            map[row-1][col] =-1;
            map[row-2][col] =-1;
            map[row-3][col] =-1;
            map[row-4][col] =-1;
        }
            //3 on top, 1 on below
        else if (row >= 3 && row < 9 && map[row - 1][col] == map[row][col] && map[row - 2][col] == map[row][col] && map[row - 3][col] == map[row][col] && map[row + 1][col] == map[row][col]) {
            if (isVerLinear(row,col) || isVerLinear(row-1,col) || isVerLinear(row-2, col) || isVerLinear(row-3,col) || isVerLinear(row+1, col))
                verLinearBomb(col);
            if (isHorLinear(row,col))
                horLinearBomb(row);
            if (isHorLinear(row-3, col))
                horLinearBomb(row-3);
            if (isHorLinear(row-2, col))
                horLinearBomb(row-2);
            if (isHorLinear(row-1, col))
                horLinearBomb(row-1);
            if (isHorLinear(row+1, col))
                horLinearBomb(row+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row-3,col))
                radialBomb(row-3,col);
            if (isRadial(row-2,col))
                radialBomb(row-2,col);
            if (isRadial(row-1, col))
                radialBomb(row-1, col);
            if (isRadial(row+1, col))
                radialBomb(row+1, col);
            if (map[row+1][col]>=0 && map[row+1][col]<4)
                map[row+1][col] += 12;
            map[row][col] =-1;
            map[row-1][col] =-1;
            map[row-2][col] =-1;
            map[row-3][col] =-1;
        }
            //2 on top, 2 on below
        else if (row >= 2 && row < 8 && map[row - 2][col] == map[row][col] && map[row - 1][col] == map[row][col] && map[row + 1][col] == map[row][col] && map[row + 2][col] == map[row][col]) {
            if (isVerLinear(row,col) || isVerLinear(row-1,col) || isVerLinear(row-2, col) || isVerLinear(row+2,col) || isVerLinear(row+1, col))
                verLinearBomb(col);
            if (isHorLinear(row,col))
                horLinearBomb(row);
            if (isHorLinear(row+2, col))
                horLinearBomb(row-3);
            if (isHorLinear(row-2, col))
                horLinearBomb(row-2);
            if (isHorLinear(row-1, col))
                horLinearBomb(row-1);
            if (isHorLinear(row+1, col))
                horLinearBomb(row+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row+2,col))
                radialBomb(row+2,col);
            if (isRadial(row-2,col))
                radialBomb(row-2,col);
            if (isRadial(row-1, col))
                radialBomb(row-1, col);
            if (isRadial(row+1, col))
                radialBomb(row+1, col);
            if (map[row+2][col]>=0 && map[row+2][col]<4)
                map[row+2][col] += 12;
            map[row+1][col] =-1;
            map[row][col] =-1;
            map[row-1][col] =-1;
            map[row-2][col] =-1;
        }
            //1 on top, 3 on below
        else if (row >= 1 && row < 7 && map[row - 1][col] == map[row][col] && map[row + 1][col] == map[row][col] && map[row + 2][col] == map[row][col] && map[row + 3][col] == map[row][col]) {
            if (isVerLinear(row,col) || isVerLinear(row-1,col) || isVerLinear(row+3, col) || isVerLinear(row+2,col) || isVerLinear(row+1, col))
                verLinearBomb(col);
            if (isHorLinear(row,col))
                horLinearBomb(row);
            if (isHorLinear(row+2, col))
                horLinearBomb(row-3);
            if (isHorLinear(row+3, col))
                horLinearBomb(row+3);
            if (isHorLinear(row-1, col))
                horLinearBomb(row-1);
            if (isHorLinear(row+1, col))
                horLinearBomb(row+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row+2,col))
                radialBomb(row+2,col);
            if (isRadial(row+3,col))
                radialBomb(row+3,col);
            if (isRadial(row-1, col))
                radialBomb(row-1, col);
            if (isRadial(row+1, col))
                radialBomb(row+1, col);
            if (map[row+3][col]>=0 && map[row+3][col]<4)
                map[row+3][col] += 12;
            map[row+2][col] =-1;
            map[row+1][col] =-1;
            map[row][col] =-1;
            map[row-1][col] =-1;
        }
            //4 on below
        else if (row < 6 && map[row + 1][col] == map[row][col] && map[row + 2][col] == map[row][col] && map[row + 3][col] == map[row][col] && map[row + 4][col] == map[row][col]) {
            if (isVerLinear(row,col) || isVerLinear(row+4,col) || isVerLinear(row+3, col) || isVerLinear(row+2,col) || isVerLinear(row+1, col))
                verLinearBomb(col);
            if (isHorLinear(row,col))
                horLinearBomb(row);
            if (isHorLinear(row+2, col))
                horLinearBomb(row-3);
            if (isHorLinear(row+3, col))
                horLinearBomb(row+3);
            if (isHorLinear(row+4, col))
                horLinearBomb(row+4);
            if (isHorLinear(row+1, col))
                horLinearBomb(row+1);
            if (isRadial(row,col))
                radialBomb(row,col);
            if (isRadial(row+2,col))
                radialBomb(row+2,col);
            if (isRadial(row+3,col))
                radialBomb(row+3,col);
            if (isRadial(row+4, col))
                radialBomb(row+4, col);
            if (isRadial(row+1, col))
                radialBomb(row+1, col);
            if (map[row+4][col]>=0 && map[row+4][col]<4)
                map[row+4][col] += 12;
            map[row+3][col] =-1;
            map[row+2][col] =-1;
            map[row+1][col] =-1;
            map[row][col] =-1;
        }
    }

    private static void moveRow(int row, int col) {
        for (int i = row; i > 0; i--) {
            map[i][col] = map[i-1][col];
        }
        map[0][col]=-1;
    }

    private static void horLinearBomb(int row) {
        for (int i=0;i<10;i++)
            map[row][i] =-1;
        //9 simple element, and a linear: 9*5 + 10
        score += 55;
    }
    private static void verLinearBomb(int col) {
        for (int i=0;i<10;i++)
            map[i][col] = -1;
        //9 simple elements, and a linear: 9*5 + 10
        score += 55;
    }
    private static void radialBomb(int row,int col) {
        int cornerRow , cornerCol;
        cornerRow = row-2;
        cornerCol = col-2;
        if (row>1 && row<8 && col>1 && col<8)
            for (int i=0;i<5;i++)
                for (int j=0;j<5;j++)
                    map[cornerRow+i][cornerCol+j] = -1;
        //24 simple elements, and a radial: 24*5 +15
        score += 135;
    }
}




class MyListener implements ActionListener {
    private int buttonNum_Row;
    private int buttonNum_Col;

    MyListener(int buttonNum_Row, int buttonNum_Col) {
        this.buttonNum_Row = buttonNum_Row;
        this.buttonNum_Col = buttonNum_Col;
    }

    private boolean check() {
        return ((Game_Frame.firstSelectRow == buttonNum_Row - 1 || Game_Frame.firstSelectRow == buttonNum_Row + 1)
                && (Game_Frame.firstSelectCol == buttonNum_Col)) || (Game_Frame.firstSelectRow == buttonNum_Row && (
                Game_Frame.firstSelectCol == buttonNum_Col - 1 || Game_Frame.firstSelectCol == buttonNum_Col + 1
        ));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!Game_Frame.isSelected) {
            Game_Frame.firstSelectRow = buttonNum_Row;
            Game_Frame.firstSelectCol = buttonNum_Col;
            Game_Frame.firstValue = Game_Frame.map[buttonNum_Row][buttonNum_Col];
            Game_Frame.isSelected = true;
            Game_Frame.secondSelectRow = -1;
            Game_Frame.secondSelectCol = -1;
            Game_Frame.secondValue = -1;
        } else {
            if (check()) {
                Game_Frame.secondSelectRow = buttonNum_Row;
                Game_Frame.secondSelectCol = buttonNum_Col;
                Game_Frame.secondValue = Game_Frame.map[buttonNum_Row][buttonNum_Col];
                Game_Frame.isSelected = false;
            } else {
                Game_Frame.firstSelectRow = buttonNum_Row;
                Game_Frame.firstSelectCol = buttonNum_Col;
                Game_Frame.firstValue = Game_Frame.map[buttonNum_Row][buttonNum_Col];
                Game_Frame.secondSelectRow = -1;
                Game_Frame.secondSelectCol = -1;
                Game_Frame.secondValue = -1;
            }
        }
        if (Game_Frame.secondValue != -1)
            Game_Frame.swap(Game_Frame.firstSelectRow, Game_Frame.firstSelectCol
                    , Game_Frame.secondSelectRow, Game_Frame.secondSelectCol);

    }
}
