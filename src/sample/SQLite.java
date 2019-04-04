package sample;

import javafx.application.Application;

import java.sql.*;


public class SQLite {

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
    public static void createTable(){
        String url = "jdbc:sqlite:ShaggyVsSquidward.db";
        String tblSql = "CREATE TABLE IF NOT EXISTS HighScores(dmgDealt integer)";

        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()){
            statement.execute(tblSql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private Connection connect(){
        String url = "jdbc:sqlite:ShaggyVsSquidward.db";
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }return connection;
    }



    public void displayDB(){
        String sql = "SELECT dmgDealt FROM HighScores";

        try(Connection conn = this.connect();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql)){

            while (result.next()){
                System.out.println(result.getInt("dmgDealt"));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
