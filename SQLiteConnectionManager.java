package comp3310a1;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class SQLiteConnectionManager {

    private String databaseURL = "";
 
    private String movesDropTableString = "DROP TABLE IF EXISTS moves;"; 
    private String movesCreateString = 
          "CREATE TABLE moves (\n" 
        + "	moveNumber INTEGER PRIMARY KEY AUTOINCREMENT,\n"
        + "	instruction TEXT NOT NULL\n"
        + ");";
    
    /**
     * Set the database file name in the sqlite project to use
     * 
     * @param fileName the database file name
     */
    public SQLiteConnectionManager(String filename)
    {
        databaseURL = "jdbc:sqlite:sqlite/" + filename;
    }

    /**
     * Connect to a sample database 
     *
     * @param fileName the database file name
     */
    public void createNewDatabase(String fileName) {

        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
                
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Check that the file has been cr3eated
     *
     * @return true if the file exists in the correct location, false otherwise. If no url defined, also false.
     */
    public boolean checkIfConnectionDefined(){
        if(databaseURL == ""){
            return false;
        }else{
            try (Connection conn = DriverManager.getConnection(databaseURL)) {
                if (conn != null) {
                    return true; 
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Create the table structures (2 tables, wordle words and valid words)
     *
     * @return true if the table structures have been created.
     */
    public boolean createTables(){
        if(databaseURL != ""){
            try (   Connection conn = DriverManager.getConnection(databaseURL);
                    Statement stmt = conn.createStatement();
                ) 
            {
                if (conn != null) {
                    stmt.execute(movesDropTableString);
                    stmt.execute(movesCreateString);
                    return true;  
                } 
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
            
        }
        return false;
    }

    /**
     * Take an id and a word and store the pair in the valid words
     * @param id the unique id for the word
     * @param word the word to store
     */
    public void addValidMove(int id, String move){

        String sql = "INSERT INTO moves(moveNumber,instruction) VALUES("+id+","+move+")";

        try (Connection conn = DriverManager.getConnection(databaseURL);
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get the last move recorded in the database to help assist with checking if 
     * en passant can be a valid move.
     */
    public String getLastMove(){

        String sql = "SELECT moveNumber, instruction FROM moves ORDER BY moveNumber Desc LIMIT 1;";
        
        try (Connection conn = DriverManager.getConnection(databaseURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);) {
            
            ResultSet resultRows  = pstmt.executeQuery();
            int moveNumber = -1;
            String result = resultRows.getString("instruction");
            moveNumber = resultRows.getInt("moveNumber");
            System.out.println("Move "+moveNumber+": " + result);
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }


    /**
     * Check if the last move corresponds to the move string in the parameter.
     * @param move an all-caps string corresponding to a move to compare to the last move in the database
     */
    public boolean checkLastMoveTypeCorrespondsTo(String move){
        
        String sql = "SELECT moveNumber FROM moves WHERE instruction LIKE \"%"+move+"%\" AND moveNumber = (SELECT max(moveNumber) FROM moves);";
        try (Connection conn = DriverManager.getConnection(databaseURL);
             Statement stmt = conn.createStatement();
             ResultSet returnStatement = stmt.executeQuery(sql)) {
            
            //If there is a row, then the last moved matched.
            return !returnStatement.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Take an id and a word and store the pair in the valid words
     * @param word the word to store
     * Assume auto increment.
     */
    public void addValidMove(String move){

        String sql = "INSERT INTO moves(instruction) VALUES(?)";

        try (Connection conn = DriverManager.getConnection(databaseURL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, move);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Possible weakness here?
     * @param sql the string to run to get all the moves in order.
     */
    public void printMoves(String sql){
        try (   Connection conn = DriverManager.getConnection(databaseURL);
                PreparedStatement stmt = conn.prepareStatement(sql)
            ) 
        {
            if (conn != null) {
                ResultSet resultRows  = stmt.executeQuery();
                int moveNumber = 1;
                while (resultRows.next())
                {
                    String result = resultRows.getString("instruction");
                    moveNumber = resultRows.getInt("moveNumber");
                    System.out.println("Move "+moveNumber+": " + result);
                    
                }
                
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
