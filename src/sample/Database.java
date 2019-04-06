package sample;

import javafx.application.Application;
import org.sqlite.SQLiteDataSource;

import java.sql.*;


public class Database {
    // Used for to store totalamount of coins, used in updateTotalCoins(), to store the SUM of
    // coins in this variable to be called after deleting all entries before inserting back to db.
    public int totalAmount;


    // The connection method to get the connection to and from our database using the sql import and jdbc connection.
    public static void getConnection(){
        // Setting connection to null initially for a close clause
        Connection conn = null;
        try {
            // The URL address for our database location
            String url = "jdbc:sqlite:ShaggyVsSquidward.db";
            // Driver connects through taking the url parameter.
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to database is established.");
            // Will catch any exception/errors and print it off in console.
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally{
            // Closes database connection when problem is done to prevent memory leak.
            try{
                // Close clause having connection not equal to null
                if (conn != null){
                    conn.close();
                }
                // Catch for try&catch, will print any error message on console
            }catch(SQLException a){
                System.out.println(a.getMessage());
            }
        }
    }

    // This method will connect to our database and create a table using a sql query line that will create the two tables
    // as long as it does not exists
    public static void createTable(){
        // SQL query code will function through the statement command
        String url = "jdbc:sqlite:ShaggyVsSquidward.db";
        String tblSql = "CREATE TABLE IF NOT EXISTS HighScores(dmgDealt integer)";
        String tblcoins = "CREATE TABLE IF NOT EXISTS Currency(coins integer)";

        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()){
            // executes the statement from the sql query string's
            statement.execute(tblSql);
            statement.execute(tblcoins);
        }catch (SQLException e){
            // This will spit back an error if the method catches.
            System.out.println(e.getMessage());
        }
    }

    // Connection that will connect to the database, this will be used in other methods to be able to reassure connection to the databases with this.connect();
    private Connection connect(){
        String url = "jdbc:sqlite:ShaggyVsSquidward.db";
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(url);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }return connection;
    }

    // This method will display the top five ranked highscores in the table 'HighScores' under 'dmgDealt' entries.
    // By using a sql query that will select all the entries in dmgDealt and printing them off
    public void displayTopFive(){
        String sql = "SELECT * FROM HighScores ORDER BY dmgDealt DESC, null";
        // Instantiation of an integer, this will be represented as the index for the dmgDealt entries.
        int ranks = 1;
        // Try & catch
        try(Connection conn = this.connect();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql)){
            System.out.println("[HIGHSCORES]");
            // This while loop will go until the end of results.
            while (result.next()){
                // This if statement will print off all the largest entries up to 5 entries.
                if (ranks<=5) {
                    System.out.println("Rank " + ranks + ": " + result.getInt("dmgDealt"));
                    ranks++;
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // This method will display the top five ranked highscores in the table 'HighScores' under 'dmgDealt' entries.
    // By using a sql query that will select all the entries in dmgDealt and printing them off
    public String displayRank1(){
        String sql = "SELECT * FROM HighScores ORDER BY dmgDealt DESC, null";
        // Instantiation of an integer, this will be represented as the index for the dmgDealt entries.
        int ranks = 1;
        // Try & catch
        try(Connection conn = this.connect();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql)){
            System.out.println("[HIGHSCORES]");
            // This while loop will go until the end of results.
            while (result.next()){
                // This if statement will print off all the largest entries up to 5 entries.
                if (ranks<=5) {
                       return "Rank " + ranks + ": " + result.getInt("dmgDealt");
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        } return "done.";
    }


    // This method will display the total amount of 'coins' in the table 'Currency'. It will take all the entries and sum it, once the sum is taken it will store it into
    // totalAmount and print off the sum.
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

    // This method will update all the total coins by taking the sum of all coin entries in 'Currency' and store it into the variable 'totalAmount'. Once that's done it will call
    // another sql method 'deleteAllCoins()' where it will delete all the multiple entries. Once the database is clear it will call one last method 'insertCoins()' using the parameter
    // 'totalAmount' as the sum and insert that data into one entry in the database. (This will help reduce clutter in the database)
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

    // deleteAllCoins is a method that's called in 'updateTotalCoins()' where it will server to delete all the entries in Currency.
    public void deleteAllCoins(){
        String sqlDel = "DELETE FROM Currency";
        try(Connection connection = this.connect();
            PreparedStatement statement = connection.prepareStatement(sqlDel)){
            statement.executeUpdate();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    // This method will insert the newScore that the winning player achieves through one of the game modes and store it into the table 'HighScores' as 'dmgDealt'.
    public void insertScore(int newScore){
        // SQL Query code that will insert the parameter into HighScores as dmgDealt.
        String insertSQL = "INSERT INTO HighScores(dmgDealt) VALUES(?)";

        try(Connection connection = this.connect();
            PreparedStatement pStatement = connection.prepareStatement(insertSQL)){
            // Puts in the parameter newScore and executes the update.
            pStatement.setInt(1, newScore);
            pStatement.executeUpdate();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // This method will insert the parameter value into 'coins' in the table 'Currency'. This method is called during the end of the game and adds an entry to the database.
    public void insertCoins(int coins){
        // SQL Query code to insert the parameter value into Currency as coins.
        String insertSQL = "INSERT INTO Currency(coins) VALUES(?)";

        try(Connection connection = this.connect();
            PreparedStatement pStatement = connection.prepareStatement(insertSQL)){
            pStatement.setInt(1, coins);
            pStatement.executeUpdate();
        } catch (Exception e){
            System.out.println(e.getMessage());
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