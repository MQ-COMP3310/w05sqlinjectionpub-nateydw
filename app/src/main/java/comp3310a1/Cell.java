package comp3310a1;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;



public class Cell extends Rectangle{
    private static int size = 70;
    protected int col;
    protected int row;

    protected Color backbgroundColor;
    protected Color textColor; //should be opposite of piece colour later on
    protected char displayCharacter; //" " or "K" or "A-H" or "1-8"
    
    protected int cellType; // 0 = board, 1 = label
    protected boolean hasPiece;
    protected Color pieceColour;
    protected Pieces piece;
    protected ImageIcon pieceGraphic;
    protected BufferedImage pieceImage;
    


    public Cell(){
        super(0,0,0,0);
        col = -1;
        row = -1;
        displayCharacter = ' ';
        backbgroundColor = Color.DARK_GRAY;
        textColor = Color.WHITE;
        cellType = 0; // 0 = board, 1 = label
        hasPiece = false;
        pieceColour = Color.white;
        piece = Pieces.NOTHING;
    }

    public Cell(int columnIndex, int rowIndex, int inX, int inY, int cellRole, Color cellColour, String label, Pieces thePiece){
        super(inX,inY,size,size);
        col = columnIndex;
        row = rowIndex;
        displayCharacter = label.charAt(0);
        backbgroundColor = cellColour;
        textColor = Color.WHITE;
        cellType = cellRole; // 0 = board, 1 = label
        hasPiece = false;
        pieceColour = Color.WHITE;
        piece = thePiece; // default as a pawn, but common with hasPiece
    }



    protected void drawPiece(Graphics g){
        if(hasPiece){
            // draw the piece on it
            
            if(piece == Pieces.PAWN){
                if(pieceColour == Color.WHITE){
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_plt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    
                }else{
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_pdt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }else if(piece == Pieces.KING){
                if(pieceColour == Color.WHITE){
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_klt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    
                }else{
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_kdt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }else if(piece == Pieces.QUEEN){
                if(pieceColour == Color.WHITE){
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_qlt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    
                }else{
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_qdt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }else if(piece == Pieces.BISHOP){
                if(pieceColour == Color.WHITE){
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_blt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    
                }else{
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_bdt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }else if(piece == Pieces.KNIGHT){
                if(pieceColour == Color.WHITE){
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_nlt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    
                }else{
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_ndt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

            }else if(piece == Pieces.ROOK){
                if(pieceColour == Color.WHITE){
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_rlt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    
                }else{
                    try {
                        pieceImage = ImageIO.read(new File("resources/Chess_rdt60.png"));
                        g.drawImage(pieceImage, x + 5, y + 5, null);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

        }
    }

    void paint(Graphics g){
        if(cellType == 0) {
            // this is a board cell
            
            g.setColor(backbgroundColor);
            g.fillRect(x, y, size, size);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, size, size);

            drawPiece(g);
            
        } else {
            // this is a label cell
            g.setColor(Color.BLACK);
            Font f = new Font("Arial", Font.PLAIN, 40);
            FontMetrics metrics = g.getFontMetrics(f);
            int drawXPos = x + ((size - metrics.stringWidth(""+displayCharacter))/2);
            int drawYPos = y + ((size + metrics.getHeight())/2 - 10);

            g.setFont(f); 
            g.drawString(""+displayCharacter, drawXPos, drawYPos);
        }

    }

    void reset(){
        // board cell neads to be cleared.
        // label piece does not need to be changed (risky if overwritten?)
        if(cellType == 0){
            hasPiece = false;
            piece = Pieces.NOTHING;
        }
    }

    public boolean hasPiece(){
        return hasPiece;
    }

    public Color getPieceColor(){
        if(hasPiece){
            return pieceColour;
        }else{
            return null;
        }
    }

    public boolean isKingPiece(){
        if(hasPiece){
            return piece == Pieces.KING;
        }else{
            return false;
        }
    }

    public void setPiece(Color newPieceColour, Pieces pieceToSet){
        pieceColour = newPieceColour;
        hasPiece = true;
        piece = pieceToSet;
    }

    public void removePiece(){
        reset();
    }

    public Pieces getPieceStatus(){
        return piece;
    }

    public void makeNewPiece(Pieces setToPiece){
        piece= setToPiece;
    }

    
    public String getStoredCharacter(){
        return "" + displayCharacter;
    }

    @Override
    public String toString(){
        return "";
    }
    
}
