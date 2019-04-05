package sample;

import javafx.application.Application;
import org.sqlite.SQLiteDataSource;

import java.sql.*;


public class Database {
    // Used for to store totalamount of coins, used in updateTotalCoins(), to store the SUM of
    // coins in this variable to be called after deleting all entries before inserting back to db.
    public int totalAmount;


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
                this.totalAmount = totalAmount;
            }
            deleteAllCoins();
            insertCoins(this.totalAmount);


        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // This method will act as a purchasing upgrade method from the application to database,
    // by checking if there's enough coins to allow for an upgrade to take place. If valid, this method will
    // take the sum in coins and load it into one entry while deleting all other entries. Then it will decrease
    // the coin amount in the database by 20, by calling deleteAllCoins, insertCoins (the total amount - 20 ) and update
    // it to the database to save.
    public void purchaseUpgrade(){
        String sqlSum = "SELECT SUM(coins) FROM Currency";
        try(Connection conn = this.connect();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sqlSum)){
            int index = 1;
            int totalAmount;
            while (result.next()){
                totalAmount = result.getInt(index);
                this.totalAmount = totalAmount;
            }
            System.out.println(this.totalAmount);
            if (this.totalAmount >= 20){
                deleteAllCoins();
                insertCoins(this.totalAmount-20);
                updateTotalCoins();
            }
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

    // Gives this.totalAmount the coin value of the sum of coins in Currency table in database.
    public int getCoins(){
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
        return totalAmount;
    }
}