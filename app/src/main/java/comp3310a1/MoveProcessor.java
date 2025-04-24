package comp3310a1;

import java.awt.Color;
import java.lang.Math;

public class MoveProcessor {

    private Grid gameGrid;

    public MoveProcessor(Grid theGrid){
        gameGrid = theGrid;
    }

    /**
     * check if the move being requested is a valid pawn move. Does not check if the piece is a pawn.
     */
    boolean isValidPawnMove(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){

        int moveDirection = 0;

        if(pieceColour == Color.WHITE){
            moveDirection = -1;
        }
        if(pieceColour == Color.BLACK){
            moveDirection = 1;
        }
        if(moveDirection == 0){return false;}

        if (fromCol == toCol && fromRow + moveDirection == toRow && gameGrid.isCellEmpty(toRow,toCol)){
            return true;
        }

        // now for the jump case
        if (pieceColour == Color.WHITE && fromCol == toCol && fromRow == 6 && toRow == 4 && gameGrid.isCellEmpty(5,toCol) && gameGrid.isCellEmpty(toRow,4)){
            return true;
        }

        if(pieceColour == Color.BLACK && fromCol == toCol && fromRow == 1 && toRow == 3 &&  gameGrid.isCellEmpty(2,toCol) && gameGrid.isCellEmpty(toRow,3) ){
            return true;
        }

        return false;
    }

    /**
     * check if the move being requested is a valid pawn take manoeuvre. Does not check if the piece is a pawn.
     */
    boolean isValidPawnTake(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){
        
        int moveDirection = 0;

        if(pieceColour == Color.WHITE){
            moveDirection = -1;
        }
        if(pieceColour == Color.BLACK){
            moveDirection = 1;
        }

        if( ( toCol - fromCol == -1 || toCol - fromCol == 1 ) ){
            if(pieceColour == Color.WHITE && fromRow + moveDirection == toRow && gameGrid.pieceColour(toRow,toCol) == Color.BLACK && !gameGrid.isKingPiece(toRow,toCol) ){
                return true;
            }

            if(pieceColour == Color.BLACK && fromRow + moveDirection == toRow && gameGrid.pieceColour(toRow,toCol) == Color.WHITE && !gameGrid.isKingPiece(toRow,toCol) ){
                return true;
            }
        }


        return false;
    }

    /**
     * check if the move being requested is a valid rook move. Does not check if the piece is a rool.
     */
    boolean isValidRookMove(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){
        
        if(fromRow == toRow && fromCol == toCol){
            return false;
        }

        if(fromRow == toRow){
            if(fromCol < toCol){
                for(int colCheck = fromCol + 1; colCheck <= toCol; colCheck++){
                    if( !gameGrid.isCellEmpty(fromRow,colCheck) ){
                        return false;
                    }
                }
                return true;
            }else{
  
                for(int colCheck = fromCol - 1; colCheck >= toCol; colCheck--){
                    if( !gameGrid.isCellEmpty(fromRow,colCheck) ){
                        return false;
                    }
                }
                return true;
            }
        }


        if(fromCol == toCol){
            if(fromRow < toRow){
                for(int rowCheck = fromRow + 1; rowCheck <= toRow; rowCheck++){
                    if( !gameGrid.isCellEmpty(fromRow,rowCheck) ){
                        return false;
                    }
                }
                return true;
            }else{
  
                for(int rowCheck = fromRow - 1; rowCheck >= toRow; rowCheck--){
                    if( !gameGrid.isCellEmpty(fromRow,rowCheck) ){
                        return false;
                    }
                }
                return true;
            }
        }

        return false;
    }


    /**
     * check if the move being requested is a valid rook take manoeuvre. Does not check if the piece is a rook.
     */
    boolean isValidRookTake(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){
        
        if(fromRow == toRow && fromCol == toCol){
            return false;
        }

        if(fromRow == toRow){
            if(fromCol < toCol){
                for(int colCheck = fromCol + 1; colCheck < toCol; colCheck++){
                    if( !gameGrid.isCellEmpty(fromRow,colCheck) ){
                        return false;
                    }
                }
                return ( !gameGrid.isCellEmpty(toRow,toCol) && !gameGrid.isKingPiece(toRow,toCol) && gameGrid.pieceColour(toRow,toCol) != pieceColour);
            }else{
  
                for(int colCheck = fromCol - 1; colCheck > toCol; colCheck--){
                    if( !gameGrid.isCellEmpty(fromRow,colCheck) ){
                        return false;
                    }
                }
                return ( !gameGrid.isCellEmpty(toRow,toCol) && !gameGrid.isKingPiece(toRow,toCol) && gameGrid.pieceColour(toRow,toCol) != pieceColour);
            }
        }


        if(fromCol == toCol){
            if(fromRow < toRow){
                for(int rowCheck = fromRow + 1; rowCheck < toRow; rowCheck++){
                    if( !gameGrid.isCellEmpty(fromRow,rowCheck) ){
                        return false;
                    }
                }
                return ( !gameGrid.isCellEmpty(toRow,toCol) && !gameGrid.isKingPiece(toRow,toCol) && gameGrid.pieceColour(toRow,toCol) != pieceColour);
            }else{
  
                for(int rowCheck = fromRow - 1; rowCheck > toRow; rowCheck--){
                    if( !gameGrid.isCellEmpty(fromRow,rowCheck) ){
                        return false;
                    }
                }
                return ( !gameGrid.isCellEmpty(toRow,toCol) && !gameGrid.isKingPiece(toRow,toCol) && gameGrid.pieceColour(toRow,toCol) != pieceColour);
            }
        }

        return false;
    
    }


    /**
     * check if the move being requested is a valid knight move. Does not check if the piece is a knight.
     */
    boolean isValidKnightMove(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){

        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);

        if( (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2) ){
            return gameGrid.isCellEmpty( toRow, toCol );
        }else{
            return false;
        }

    }

    /**
     * check if the move being requested is a valid knight take manoeuvre. Does not check if the piece is a knight.
     */
    boolean isValidKnightTake(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){
        
        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);

        if( (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2) ){
            return !gameGrid.isCellEmpty( toRow, toCol ) && gameGrid.pieceColour(toRow,toCol) != pieceColour && !gameGrid.isKingPiece(toRow,toCol);
        }else{
            return false;
        }

    }

    /**
     * check if the move being requested is a valid bishop move. Does not check if the piece is a bishop.
     */
    boolean isValidBishopMove(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){

        if(fromRow == toRow && fromCol == toCol){
            return false;
        }

        int rowDiff = fromRow - toRow;
        int colDiff = fromCol - toCol;

        if( Math.abs(rowDiff) != Math.abs(colDiff) ){
            return false;
        }

        int spacesToMove = Math.abs(rowDiff);
        if(rowDiff < 0){rowDiff = 1;}else{rowDiff = -1;}
        if(colDiff < 0){colDiff = 1;}else{colDiff = -1;}
        //now check the space is clear
        
        for(int i = 1; i <= spacesToMove; i++){
            if( !gameGrid.isCellEmpty( fromRow + (i*rowDiff), fromCol + (i*colDiff)) ){
                return false;
            }
        }
        
        return true;
    }

    /**
     * check if the move being requested is a valid bishop take manoeuvre. Does not check if the piece is a bishop.
     */
    boolean isValidBishopTake(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){

        if(fromRow == toRow && fromCol == toCol){
            return false;
        }

        int rowDiff = fromRow - toRow;
        int colDiff = fromCol - toCol;

        if( Math.abs(rowDiff) != Math.abs(colDiff) ){
            return false;
        }

        int spacesToMove = Math.abs(rowDiff);
        if(rowDiff < 0){rowDiff = 1;}else{rowDiff = -1;}
        if(colDiff < 0){colDiff = 1;}else{colDiff = -1;}
        //now check the space is clear
        
        for(int i = 1; i < spacesToMove; i++){
            if( !gameGrid.isCellEmpty( fromRow + (i*rowDiff), fromCol + (i*colDiff)) ){
                return false;
            }
        }

        return ( !gameGrid.isCellEmpty(toRow,toCol) && !gameGrid.isKingPiece(toRow,toCol) && gameGrid.pieceColour(toRow,toCol) != pieceColour);
    }    

    /**
     * check if the move being requested is a valid queen move. Does not check if the piece is a queen. Queens can move like bishops or rooks right?
     */
    boolean isValidQueenMove(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){

        return ( isValidRookMove(fromRow,fromCol,toRow,toCol,pieceColour) || isValidBishopMove(fromRow,fromCol,toRow,toCol,pieceColour) );
    }

    /**
     * check if the move being requested is a valid queen move. Does not check if the piece is a queen. Queens can take like bishops or rooks right?
     */
    boolean isValidQueenTake(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){

        return ( isValidRookTake(fromRow,fromCol,toRow,toCol,pieceColour) || isValidBishopTake(fromRow,fromCol,toRow,toCol,pieceColour) );
    }   

    /**
     * check if the move being requested is a valid king move. Does not check if the piece is a king.
     */
    boolean isValidKingMove(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){
        
        if(fromRow == toRow && fromCol == toCol){
            return false;
        }

        int rowDiff = fromRow - toRow;
        int colDiff = fromCol - toCol;

        if (Math.abs(rowDiff) > 1 || Math.abs(colDiff) > 1){
            return false;
        }

        //at this point, the move is not to its own square
        //and the row and col diff are not > 1
        //so just check if the cell is empty
        //handle check elsewhere
        
        return gameGrid.isCellEmpty( toRow, toCol );
    }

    /**
     * check if the move being requested is a valid king move. Does not check if the piece is a king.
     */
    boolean isValidKingTake(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){

        if(fromRow == toRow && fromCol == toCol){
            return false;
        }

        int rowDiff = fromRow - toRow;
        int colDiff = fromCol - toCol;

        if (Math.abs(rowDiff) > 1 || Math.abs(colDiff) > 1){
            return false;
        }

        //at this point, the move is not to its own square
        //and the row and col diff are not > 1
        //so just check if the cell is occupied by a piece of the opposite colour
        //handle check elsewhere
        
        return !gameGrid.isCellEmpty( toRow, toCol ) && gameGrid.pieceColour(toRow,toCol) != pieceColour && !gameGrid.isKingPiece(toRow,toCol);

    }   

    boolean isValidCastle(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){
        //not implemented
        return false;
    }

    boolean isValidEnPassant(int fromRow, int fromCol, int toRow, int toCol, Color pieceColour){
        //not implemented
        return false;
    }



}
