//These are the imports needed to use some of the objects that cannot be used otherwise, such as the GUI, and the
//other more commanly known Scanner, Random and array list imports.
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
public class GameCode{
    Scanner scan = new Scanner(System.in);
    private int tileSize = 70;
    private int numRows = 10;
    private int numCols = numRows;
    private int boardWidth = numCols * tileSize;
    private int boardHeight = numRows * tileSize;
    int mineCount = 10;
    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    Random random = new Random();

    int tilesClicked = 0; //goal is to click all tiles except the ones containing mines
    boolean gameOver = false;
    public GameCode(){
        //The following is a simple statement which asks for the text that the user would like to be displayed on top
        //of the GUI, and the constructor which initiates the GUI
        System.out.print("What do you want your username to be? ");
        GamePanel panel = new GamePanel(boardWidth, boardHeight, numRows, numCols, scan.nextLine());

        //This nested iteration using 2 for loops is the main code of body itself, where it assigns and checks each square
        // using the other methods within this class and the other 2 classes.
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r,c);
                board[r][c] = tile;

                //The following code was built by searching up how to do interact with the GUI using mouse, and several other questions that followed.
                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource();

                        //left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.getText().equals("")) {
                                if (mineList.contains(tile)) {
                                    revealMines();
                                }
                                else {
                                    checkMine(tile.r, tile.c);
                                }
                            }
                        }
                        //right click
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.getText() == "" && tile.isEnabled()) {
                                tile.setText("🚩");
                            }
                            else if (tile.getText() == "🚩") {
                                tile.setText("");
                            }
                        }
                    }
                });

                panel.getBoardPanel().add(tile);
            }
        }
        panel.editFrame(true);
        setMines();
    }

    //The following method was created with the help of figuring out how to use arraylist in google(concept was not mastered at all)
    // The method sets mines all across the board/panel
    private void setMines() {
        mineList = new ArrayList<MineTile>();

        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows); //0-7
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft -= 1;
            }
        }
    }

    //The following method is relatively easy to comprehend, where it simply reveals the mines and updates the GUI as required.
    private void revealMines() {
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("💣");
        }

        gameOver = true;
        GamePanel panel = new GamePanel();
        panel.editText("Game Over!");
    }

    //The method is used to Check if there is a mine around the tile that was clicked, the top3, left & right, and bottom 3.
    private void checkMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return;
        }

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) {
            return;
        }
        tile.setEnabled(false);
        tilesClicked += 1;

        int minesFound = 0;

        //top 3
        minesFound += countMine(r-1, c-1);  //top left
        minesFound += countMine(r-1, c);    //top
        minesFound += countMine(r-1, c+1);  //top right

        //left and right
        minesFound += countMine(r, c-1);    //left
        minesFound += countMine(r, c+1);    //right

        //bottom 3
        minesFound += countMine(r+1, c-1);  //bottom left
        minesFound += countMine(r+1, c);    //bottom
        minesFound += countMine(r+1, c+1);  //bottom right

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        }
        else {
            tile.setText("");

            //top 3
            checkMine(r-1, c-1);    //top left
            checkMine(r-1, c);      //top
            checkMine(r-1, c+1);    //top right

            //left and right
            checkMine(r, c-1);      //left
            checkMine(r, c+1);      //right

            //bottom 3
            checkMine(r+1, c-1);    //bottom left
            checkMine(r+1, c);      //bottom
            checkMine(r+1, c+1);    //bottom right
        }

        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            GamePanel panel = new GamePanel();
            panel.editText("Mines Cleared! You\"ve won!");
        }
    }

    //Relatively easy method, simply determines the count of mines
    public int countMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return 0;
        }
        if (mineList.contains(board[r][c])) {
            return 1;
        }
        return 0;
    }
}
