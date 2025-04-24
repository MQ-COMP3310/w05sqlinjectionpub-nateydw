package comp3310a1;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.function.Consumer;



public class Grid {

    Cell[][] cells;
    boolean gameFinished;
    SQLiteConnectionManager movesDatabaseConnection;
    MoveProcessor moveProcessor;

    public Grid(int rows, int cols, SQLiteConnectionManager sqlConn){
        cells = new Cell[rows + 1][cols + 1];
        //board cells + row and col for lables.
        
        //create the board and label cells
        for (int i = 0; i < cells.length -1; i++) {
            int x = 0;
            int y = 0;
            for (int j = 0; j < cells[i].length -1; j++) {
                x = 10 + 80 * i;
                y = 10 + 80 * j;
                if( (i + j) % 2 == 0){
                    cells[i][j] = new Cell(i,j,y,x, 0, Color.LIGHT_GRAY, " ",Pieces.NOTHING);
                }
                else{
                    cells[i][j] = new Cell(i,j,y,x, 0, Color.DARK_GRAY, " ",Pieces.NOTHING);
                }
            }
            //add row label on the end
            y = 10 + 80 * cols;
            cells[i][cols] = new Cell(i,cols,y,x, 1, Color.WHITE, Integer.toString(8-i), Pieces.NOTHING); 
        }
        
        //now draw the row of letters for the bottom row (A thorugh H letters)
        for(int l = 0; l < cells[rows].length; l++){
            int x = 10 + 80 * rows;
            int y = 10 + 80 * l;
            if(l <8){
                cells[rows][l] = new Cell(rows,l,y,x, 1, Color.WHITE, Character.toString ((char) l + 65), Pieces.NOTHING); 
            }else{
                // no label for the bottom right corner.
                cells[rows][l] = new Cell(rows,l,y,x, 1, Color.WHITE, " ", Pieces.NOTHING); 
            }
        }

        gameFinished = false;
        movesDatabaseConnection = sqlConn;
        moveProcessor = new MoveProcessor(this);
    }

    public void addPiece(int row, int column, Color theColourToCreate, Pieces piece){
        cells[row][column].setPiece(theColourToCreate, piece);
    }


    public boolean processMoveRequest(Color colorOfMover, String instruction){

        System.out.println("Instruction: " + instruction);

        String [] arguments = instruction.split(" ");

        if(arguments.length != 3){
            System.out.println("arg length is NOT 3");
            return false;
        }

        System.out.println("arg length is 3");
        int fromRow = getRow(arguments[1]);
        int fromCol = getCol(arguments[1]);
        int toRow = getRow(arguments[2]);
        int toCol = getCol(arguments[2]);

        //check bounds of the move
        if(fromRow<0 || fromRow > 8 || fromCol<0 || fromCol > 8 || toRow<0 || toRow > 8 || toCol<0 || toCol > 8){
            return false;
        }
        Color pieceColor = cells[fromRow][fromCol].getPieceColor();
        if( !(pieceColor != null && cells[fromRow][fromCol].hasPiece() && pieceColor == colorOfMover ) ){
            return false;
        }

        //assume: has piece
        //assume: colour of the piece is the piece to move
        // now check the move is a valid chess move
        Pieces pieceType = cells[fromRow][fromCol].getPieceStatus();

        if(arguments[0].equals("MOVE")){

            System.out.println("MOVE recognised");
            
            if( pieceType == Pieces.PAWN ){
                if(! moveProcessor.isValidPawnMove(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            if( pieceType == Pieces.ROOK ){
                if(! moveProcessor.isValidRookMove(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            if( pieceType == Pieces.KNIGHT ){
                if(! moveProcessor.isValidKnightMove(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            if( pieceType == Pieces.BISHOP ){
                if(! moveProcessor.isValidBishopMove(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            if( pieceType == Pieces.QUEEN ){
                if(! moveProcessor.isValidQueenMove(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            if( pieceType == Pieces.KING ){
                if(! moveProcessor.isValidKingMove(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            //TODO: checkAnalysis
            
            cells[toRow][toCol].setPiece(colorOfMover,cells[fromRow][fromCol].getPieceStatus());
            cells[fromRow][fromCol].reset();
            movesDatabaseConnection.addValidMove(instruction);
            return true;

        }else if(arguments[0].equals("TAKE")){
            
            System.out.println("TAKE recognised");
            
            if( pieceType == Pieces.PAWN ){
                if(! moveProcessor.isValidPawnTake(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            if( pieceType == Pieces.ROOK ){
                if(! moveProcessor.isValidRookTake(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            if( pieceType == Pieces.KNIGHT ){
                if(! moveProcessor.isValidKnightTake(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            if( pieceType == Pieces.BISHOP ){
                if(! moveProcessor.isValidBishopTake(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            if( pieceType == Pieces.QUEEN ){
                if(! moveProcessor.isValidQueenTake(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            if( pieceType == Pieces.KING ){
                if(! moveProcessor.isValidKingTake(fromRow,fromCol,toRow,toCol,pieceColor)){
                    return false;
                }
            }

            //TODO: checkAnalysis
            
            cells[toRow][toCol].setPiece(colorOfMover,cells[fromRow][fromCol].getPieceStatus());
            cells[fromRow][fromCol].reset();
            movesDatabaseConnection.addValidMove(instruction);
            return true;

        }else if(arguments[0].equals("CASTLE")){
            
            // currenlty not implemented
            System.out.println("CASTLE recognised");
            System.out.println("CASTLE feature not implemented");
            return false;
       
        }else if(arguments[0].equals("ENPASSENT")){
           
            // currenlty not implemented
            System.out.println("ENPASSENT recognised");
            System.out.println("ENPASSENT feature not implemented");
            return false;
       
        }else{
            //not a MOVE or TAKE command
            return false;
        }

        //in theory, should never get here, but just in case.
        //return false;
    }

    public boolean isCheckMate(){
        //TODO: Checkmate
        return false;
    }

    boolean isCellEmpty(int row, int col){
        return !cells[row][col].hasPiece();
    }

    Color pieceColour(int row, int col){
        return cells[row][col].getPieceColor();
    }

    boolean isKingPiece(int row, int col){
        return cells[row][col].isKingPiece();
    }

    public int getRow(String s){
        if(s.length() == 2){
            int row = -1; 
            if(Character.isDigit(s.charAt(1))){
                row = s.charAt(1) - '0';
                row = 8 - row; // as the indexes are inverted from top left.
                if(row > 7 || row < 0){
                    row = -1;
                }
            }
            return row;
        }else{
            return -1;
        }
    }

    public int getCol(String s){
        if(s.length() == 2){
            int col = -1; 
            if(Character.isAlphabetic(s.charAt(0))){
                col = s.charAt(0) - 'A';
                if(col > 7 || col < 0){
                    col = -1;
                }
            }
            return col;
        }else{
            return -1;
        }
    }


    public void paint(Graphics g) {
        doToEachCell((Cell c) -> c.paint(g));
    }

    public void reset(){
        // goes to a blank board.
        // Load a game from DB as well?
        
        gameFinished = false;
        doToEachCell((Cell c) -> c.reset());
        
    }

    /**
     * Takes a cell consumer (i.e. a function that has a single `Cell` argument and
     * returns `void`) and applies that consumer to each cell in the grid.
     *
     * @param func The `Cell` to `void` function to apply at each spot.
     */
    public void doToEachCell(Consumer<Cell> func) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                func.accept(cells[i][j]);
            }
        }
    }

	//@Override
	public Iterator<Cell> iterator() {
		return new CellIterator(cells);
	}


    void keyPressedEscape(){
        System.exit(0);
    }


    void keyPressedLetter(char letter){
        if(!gameFinished){
            System.out.println("grid keypress received letter: " + letter);
        }
        if(letter == 'P' || letter == 'p'){
            printGameMoves();
        }
    }

    void printGameMoves(){
        System.out.println("Requested to print moves");
        String sql = "SELECT moveNumber, instruction from moves ORDER BY moveNumber";
        movesDatabaseConnection.printMoves(sql);
    }

}
