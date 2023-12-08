import javax.swing.*;
//This class is to create the MineTile object which is used often both to create each individual tile on the board
//and assign it values
public class MineTile extends JButton {
    int r;
    int c;

    public MineTile(int r, int c) {
        this.r = r;
        this.c = c;
    }
}