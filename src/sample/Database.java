package sample;

import javafx.application.Application;
import org.sqlite.SQLiteDataSource;

import java.sql.*;


public class Database {
    // Used for to store totalamount of coins, used in updateTotalCoins(), to store the SUM of
    // coins in this variable to be called after deleting all entries before inserting back to db.
    private int totalAmount;


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
        String tblSql = "CREATE TABLE IF NOT EXISTS HighScores(dmgDealt integer)";
        String tblcoins = "CREATE TABLE IF NOT EXISTS Currency(coins integer)";

        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()){
            statement.execute(tblSql);
            statement.execute(tblcoins);
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

//    // Method for displaying the entries in the table HighScores in our database
//    // Status: probably will not need
//    public void displayScore(){
//        String sql = "SELECT dmgDealt FROM HighScores";
//
//        try(Connection conn = this.connect();
//            Statement statement = conn.createStatement();
//            ResultSet result = statement.executeQuery(sql)){
//
//            while (result.next()){
//                System.out.println("Current Highscore: "+result.getInt("dmgDealt"));
//            }
//        } catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//    }

    // Use to display the top five highscores in our database, and prints to console.
    // status: needs to be able to print in gui
    public void displayTopFive(){
        String sql = "SELECT * FROM HighScores ORDER BY dmgDealt DESC, null";
        int i = 1;
        try(Connection conn = this.connect();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql)){
            System.out.println("[HIGHSCORES]");
            while (result.next()){
                if (i<=5) {
                    System.out.println("Rank " + i + ": " + result.getInt("dmgDealt"));
                    i++;
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    ////////// METHODS I MIGHT NEED FOR LATER? IDK YET.
//    // This will display the coins from the table Currency, used as the database to store the coins.
//    public void displayCoins(){
//        String sql = "SELECT coins FROM Currency";
//        try(Connection conn = this.connect();
//            Statement statement = conn.createStatement();
//            ResultSet result = statement.executeQuery(sql)){
//
//            while (result.next()){
//                System.out.println("Wallet: "+result.getInt("coins")+" coins.");
//            }
//        } catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//    }

//    // Status: working
//    // A method that sums the total amount of coins earned from the game
//    // edit: I MIGHT JUST REMOVE 'TOTAL' AND JUST SUM FROM COINS IN CURRENCY
//    public void displayTotalCoins(){
//        String sql = "SELECT SUM(total) FROM TotalAmount";
//        try(Connection conn = this.connect();
//            Statement statement = conn.createStatement();
//            ResultSet result = statement.executeQuery(sql)){
//            int index = 1;
//            while (result.next()){
//                int totalAmount = result.getInt(index);
//                System.out.println("Wallet Total: "+totalAmount+" coins.");
//            }
//        } catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//    }


    // Status: working
    // A method that sums the total amount of coins earned from the game
    public void displayTotalCoins(){
        String sql = "SELECT SUM(coins) FROM Currency";
        try(Connection conn = this.connect();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql)){
            int index = 1;
            while (result.next()){
                int totalAmount = result.getInt(index);
                System.out.println("Wallet Total: "+totalAmount+" coins.");
            }
            while (result.next()){
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // ***(alogrithm idea for total coins in 1 entry): for total coins, summing total amount, delete all entries then add back
    // total amount.

    public void updateTotalCoins(){
        String sqlSum = "SELECT SUM(coins) FROM Currency";
        try(Connection conn = this.connect();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sqlSum)){
            int index = 1;
            int totalAmount;
            while (result.next()){
                totalAmount = result.getInt(index);
                //System.out.println("Wallet Total: "+totalAmount+" coins.");
                this.totalAmount = totalAmount;
            }
            deleteAllCoins();
            insertCoins(this.totalAmount);


        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteAllCoins(){
        String sqlDel = "DELETE FROM Currency";
        try(Connection connection = this.connect();
            PreparedStatement statement = connection.prepareStatement(sqlDel)){
            statement.executeUpdate();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    // Use to insert a new highscore.
    // Status: working
    public void insertScore(int newScore){
        String insertSQL = "INSERT INTO HighScores(dmgDealt) VALUES(?)";

        try(Connection connection = this.connect();
        PreparedStatement pStatement = connection.prepareStatement(insertSQL)){
            pStatement.setInt(1, newScore);
            pStatement.executeUpdate();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // Insert coins into our data (table: Currency).
    public void insertCoins(int coins){
        String insertSQL = "INSERT INTO Currency(coins) VALUES(?)";

        try(Connection connection = this.connect();
            PreparedStatement pStatement = connection.prepareStatement(insertSQL)){
            pStatement.setInt(1, coins);
            pStatement.executeUpdate();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // This method will update the highscore by updating the entry when a new highscore is called
    // status: working
    public void updateScore (int newScore){
        String sql = "UPDATE HighScores SET dmgDealt = ?";
        try(Connection connection = this.connect();
        PreparedStatement pstatement = connection.prepareStatement(sql)){
            pstatement.setInt(1, newScore);
            pstatement.executeUpdate();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // Deletes entries equal to the parameter score
    public void deleteScore(int score){
        String delScore = "DELETE FROM HighScores WHERE dmgDealt = ?";
        try(Connection connection = this.connect();
            PreparedStatement statement = connection.prepareStatement(delScore)){
                statement.setInt(1, score);
                statement.executeUpdate();
        } catch (Exception e){
                System.out.println(e.getMessage());
        }
    }

    // Use to decrease the amount that will cost for upgrading damage.
    // Status: Need to take the SUM of all values in coins first
    public void decreaseCoins(int coins){
        String purchaseSQL = "DELETE FROM Currency WHERE dmgDealt = ?";
        try(Connection connection = this.connect();
            PreparedStatement statement = connection.prepareStatement(purchaseSQL)){
                statement.setInt(1,coins);
                statement.executeUpdate();
        } catch (Exception e){
                System.out.print(e.getMessage());
        }
    }
}