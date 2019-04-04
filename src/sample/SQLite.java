package sample;

import javafx.application.Application;

import java.sql.*;


public class SQLite {


    // The connection method to get the connection to and from our database
    // Status: working
    public static void getConnection(){
        Connection conn = null;
        try {
            //String url = "jdbc:sqlite:/Users/rain/Desktop/MASTER-final/ShaggyVsSquidward.db";
            String url = "jdbc:sqlite:ShaggyVsSquidward.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to database is established.");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally{
            // Closes database connection when problem is done to prevent memory leak.
            try{
                if (conn != null){
                    conn.close();
                }
            }catch(SQLException a){
                System.out.println(a.getMessage());
            }
        }
    }

    // Database: Create a table method
    // This method will create a table into our database for our highscores.
    // Status: working
    public static void createTable(){
        String url = "jdbc:sqlite:ShaggyVsSquidward.db";
        String tblSql = "CREATE TABLE IF NOT EXISTS HighScores(dmgDealt integer PRIMARY KEY)";

        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()){
            statement.execute(tblSql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // Connection connect method
    // Status: working
    private Connection connect(){
        String url = "jdbc:sqlite:ShaggyVsSquidward.db";
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }return connection;
    }


    // Method for displaying the entries in the table HighScores in our database
    // Status: working
    public void displayDB(){
        String sql = "SELECT dmgDealt FROM HighScores";

        try(Connection conn = this.connect();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql)){

            while (result.next()){

                System.out.println("Current Highscore: "+result.getInt("dmgDealt"));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // Use to insert a new highscore.
    // Status: working
    public void insert(int newScore){
        String insertSQL = "INSERT INTO HighScores(dmgDealt) VALUES(?)";

        try(Connection connection = this.connect();
        PreparedStatement pStatement = connection.prepareStatement(insertSQL)){
            pStatement.setInt(1, newScore);
            pStatement.executeUpdate();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // This method will update the highscore by updating the entry when a new highscore is called
    // status: working
    public void update (int newScore){
        String sql = "UPDATE HighScores SET dmgDealt = ?";
        try(Connection connection = this.connect();
        PreparedStatement pstatement = connection.prepareStatement(sql)){
            pstatement.setInt(1, newScore);
            pstatement.executeUpdate();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}