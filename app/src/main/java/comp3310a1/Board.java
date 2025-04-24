package comp3310a1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Board {

    Grid grid;
    SQLiteConnectionManager movesDatabaseConnection; // to import, store, and print moves.
    Prompt keyboardInputDisplay; // the black display a the bottom of the gui
    int move = 0;

    public Board(){
        
        keyboardInputDisplay = new Prompt(10,750);

        movesDatabaseConnection = new SQLiteConnectionManager("chessGames.db");
        int setupStage = 0;

        movesDatabaseConnection.createNewDatabase("chessGames.db");
        if (movesDatabaseConnection.checkIfConnectionDefined())
        {
            System.out.println("Game created and connected.");
            if(movesDatabaseConnection.createTables()) 
            {
                System.out.println("Game structures in place.");
                setupStage = 1;
            }
        }

        grid = new Grid(8,8, movesDatabaseConnection);
        setNewGame();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/data.txt"))) {
            String line;
            
            System.out.println("Processing resources/data.txt");
            boolean validMovesSoFar = true;
            while ((line = br.readLine()) != null && validMovesSoFar) {
                System.out.println(line);
                if(move == 0){
                    if(!grid.processMoveRequest(Color.WHITE,line)){ 
                        validMovesSoFar = false;
                    }else{
                        move = 1 - move;
                    }
                }else{
                    if(!grid.processMoveRequest(Color.BLACK,line)) 
                    {
                        validMovesSoFar = false;
                    }else{
                        move = 1 - move;
                    }
                }
            
            }
            
            setupStage = 2;
        }catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void resetBoard(){
        grid.reset();

    }

    void paint(Graphics g){
        grid.paint(g);
        keyboardInputDisplay.paint(g);
    }    

    public void setNewGame(){
        grid.reset();
        keyboardInputDisplay.reset();


        //pawns
        int row = 6;
        for(int column = 0; column < 8; column ++){
            grid.addPiece(row,column,Color.WHITE, Pieces.PAWN);
            //System.out.println("Adding Pawn");
        }

        // other pieces        
        row = 7;
        grid.addPiece(row,0,Color.WHITE, Pieces.ROOK);
        grid.addPiece(row,1,Color.WHITE, Pieces.KNIGHT);
        grid.addPiece(row,2,Color.WHITE, Pieces.BISHOP);
        grid.addPiece(row,3,Color.WHITE, Pieces.QUEEN);
        grid.addPiece(row,4,Color.WHITE, Pieces.KING);
        grid.addPiece(row,5,Color.WHITE, Pieces.BISHOP);
        grid.addPiece(row,6,Color.WHITE, Pieces.KNIGHT);
        grid.addPiece(row,7,Color.WHITE, Pieces.ROOK);

        //pawns
        row = 1;
        for(int column = 0; column < 8; column ++){
            grid.addPiece(row,column,Color.BLACK, Pieces.PAWN);
        }
        //other pieces
        row = 0;
        grid.addPiece(row,0,Color.BLACK, Pieces.ROOK);
        grid.addPiece(row,1,Color.BLACK, Pieces.KNIGHT);
        grid.addPiece(row,2,Color.BLACK, Pieces.BISHOP);
        grid.addPiece(row,3,Color.BLACK, Pieces.QUEEN);
        grid.addPiece(row,4,Color.BLACK, Pieces.KING);
        grid.addPiece(row,5,Color.BLACK, Pieces.BISHOP);
        grid.addPiece(row,6,Color.BLACK, Pieces.KNIGHT);
        grid.addPiece(row,7,Color.BLACK, Pieces.ROOK);

    }

    public boolean addMoveToGrid(Color colorToMove, String instruction){
        return grid.processMoveRequest(colorToMove, instruction);
    }

    public void keyPressed(KeyEvent e){
        System.out.println("Key Pressed! " + e.getKeyCode());

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            //grid.keyPressedEnter();
            boolean validInstruction = false;
            if(move == 0){
                validInstruction = grid.processMoveRequest(Color.WHITE, keyboardInputDisplay.getPromptText());
            }else{
                validInstruction = grid.processMoveRequest(Color.BLACK, keyboardInputDisplay.getPromptText());
            }
            keyboardInputDisplay.reset();
            if(validInstruction){
                move = 1 - move;
            }
            System.out.println("Enter Key Pressed");
            

            if(grid.isCheckMate()){
                System.out.println("Winner! Now exiting ...");
                System.exit(0);
            }

        }
        
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            //grid.keyPressedBackspace();
            keyboardInputDisplay.removeLastLetter();
            System.out.println("Backspace Key");
        }
        
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            grid.keyPressedEscape();
            System.out.println("Escape Key");
        }

        if( (e.getKeyCode()>= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z) || 
            (e.getKeyCode()>= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) || 
            (e.getKeyCode()== KeyEvent.VK_SPACE)){
            grid.keyPressedLetter(e.getKeyChar());
            keyboardInputDisplay.addLetter((char) e.getKeyCode() );
            System.out.println("Character Key sent to prompt");
        }

    }

}
