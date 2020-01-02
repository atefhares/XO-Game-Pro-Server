
package database;


import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConfig {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
//    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private static final String SERVER = "localhost";
    private static final String PORT = "3306";
    private static final String PARAMS = "?useSSL=false&serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://" + SERVER + ":" + PORT;
    /*======================================================================================================*/

    private static final String DATABASE_NAME = "tic-tac-toe";
    private static final String USERS_TABLE_NAME = "users";
    private static final String USERS_TABLE_COLUMN_ID = "ID";
    private static final String USERS_TABLE_COLUMN_FULL_NAME = "Name";
    private static final String USERS_TABLE_COLUMN_POINTS = "Points";
    
    private static final String USERS_TABLE_COLUMN_EMAIL = "Email";
    private static final String USERS_TABLE_COLUMN_PASSWORD = "Password";
    private static final String USERS_TABLE_COLUMN_STATUS = "Status";
    /*======================================================================================================*/

    private static final String GAMES_TABLE_NAME = "Games";
    private static final String GAMES_TABLE_COLUMN_ID = "ID";
    private static final String GAMES_TABLE_COLUMN_PLAYER1 = "Player1";
    private static final String GAMES_TABLE_COLUMN_PLAYER2 = "Player2";
    
    private static final String GAMES_TABLE_COLUMN_WINNER = "Winner";
    private static final String GAMES_TABLE_COLUMN_START_DATE = "Start_Date";
    private static final String GAMES_TABLE_COLUMN_END_DATE = "End_Date";
    private static final String GAMES_TABLE_COLUMN_STATUS = "Status";
    /*======================================================================================================*/

    private static Connection mConnection;

    /*======================================================================================================*/
    private ResultSet UserSet;
    
    private ResultSet GameSet;
/*======================================================================================================*/
    public  ResultSet getUserSet() {
        return UserSet;
    }

    public void setUserSet(ResultSet UserSet) {
        this.UserSet = UserSet;
    }

    public ResultSet getGameSet() {
        return GameSet;
    }

    public void setGameSet(ResultSet GameSet) {
        this.GameSet = GameSet;
    }
/*======================================================================================================*/
    public static void setupDatabaseConnection() throws SQLException {
        try {
            //create connection to mysql server
            Class.forName("com.mysql.jdbc.Driver");
            mConnection = DriverManager.getConnection(URL + PARAMS, USERNAME, PASSWORD);
            System.out.println("Server connection established successfully...");
            
            System.out.println("Attempt to create database IF NOT EXISTS: " + DATABASE_NAME);
            Statement statement = mConnection.createStatement();
            //int Result = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
            //System.out.println("Database creation result: " + Result);
            
            // closing this connection and open a connection to the database itself
            mConnection.close();
            System.out.println("Attempt to select database: " + DATABASE_NAME);
            mConnection = DriverManager.getConnection(URL + "/" + DATABASE_NAME + PARAMS, USERNAME, PASSWORD);
            System.out.println("Database selected successfully...");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createTable() throws SQLException {
        System.out.println("Attempt to create table: " + USERS_TABLE_NAME);
        Statement statement = mConnection.createStatement();

        int Users_Creation = statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + USERS_TABLE_NAME
                        + "("
                        + USERS_TABLE_COLUMN_ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                        + USERS_TABLE_COLUMN_FULL_NAME + " VARCHAR(100) NOT NULL, "
                        + USERS_TABLE_COLUMN_POINTS + " INT DEFAULT 0 NOT NULL, "
                        + USERS_TABLE_COLUMN_EMAIL + " VARCHAR(100) NOT NULL, "
                        + USERS_TABLE_COLUMN_PASSWORD + " VARCHAR(100) NOT NULL, "
                        + USERS_TABLE_COLUMN_STATUS + " BOOLEAN DEFAULT false NOT NULL"

                        + ")"
        );
        int Games_Creation = statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + GAMES_TABLE_NAME
                        + "("
                        + GAMES_TABLE_COLUMN_ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                        + GAMES_TABLE_COLUMN_PLAYER1 + " INT NOT NULL, "
                        + GAMES_TABLE_COLUMN_PLAYER2 + " INT NOT NULL, "
                        + GAMES_TABLE_COLUMN_WINNER + " INT  NOT NULL, "
                        + GAMES_TABLE_COLUMN_START_DATE + " DATE NOT NULL, "
                        + GAMES_TABLE_COLUMN_END_DATE + " DATE NOT NULL, "
                        + GAMES_TABLE_COLUMN_STATUS + " VARCHAR(255) NOT NULL, "
                        +"FOREIGN KEY (Player1) REFERENCES Users(ID), FOREIGN KEY (Player2) REFERENCES Users(ID), FOREIGN KEY (Winner) REFERENCES Users(ID) "
                        + ")"
                
        );
       
        
        System.out.println("Users Table creation result: " + Users_Creation + "Games Table creation result: " + Games_Creation );
    }

    public static void insertUsersRow(Vector Attribute) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

        

        Statement statement = mConnection.createStatement();
        int result = statement.executeUpdate("INSERT INTO " + USERS_TABLE_NAME
                + " VALUES ("
                + "null, "
                + "\"" + Attribute.get(0) + "\", "
                + "\"" + Attribute.get(1) + "\", "
                + "\"" + Attribute.get(2) + "\", "
                + "\"" + Attribute.get(3) + "\", "
                + "\"" + Attribute.get(4) + "\""
                + ")"
        );

        System.out.println("Insertion result: " + result);

    }
    public static void insertGamesRow(Vector Attribute) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

        

        Statement statement = mConnection.createStatement();
                System.out.println(Attribute.get(0));

        int result = statement.executeUpdate("INSERT INTO " + GAMES_TABLE_NAME
                + " VALUES ("
                + "null, "
                + "\"" + Attribute.get(0) + "\", "
                + "\"" + Attribute.get(1) + "\", "
                + "\"" + Attribute.get(2) + "\", "
                + "\"" + Attribute.get(3) + "\", "
                + "\"" + Attribute.get(4) + "\", "
                + "\"" + Attribute.get(5) + "\""
                + ")"
        );
        
        System.out.println("Insertion result: " + result);

    }

    public static void updateUserRow(Vector Attribute) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

       

        Statement statement = mConnection.createStatement();
        int result = statement.executeUpdate("UPDATE " + USERS_TABLE_NAME
                + " SET "
                + USERS_TABLE_COLUMN_FULL_NAME + "=\"" + Attribute.get(1)  + "\", "
                + USERS_TABLE_COLUMN_POINTS + "=\"" + Attribute.get(2)  + "\", "
                + USERS_TABLE_COLUMN_EMAIL + "=\"" + Attribute.get(3)  + "\", "
                + USERS_TABLE_COLUMN_PASSWORD + "=\"" + Attribute.get(4)  + "\", "
                + USERS_TABLE_COLUMN_STATUS + "=\"" + Attribute.get(5)  + "\" "
                + " WHERE " + USERS_TABLE_COLUMN_ID + "=" + Attribute.get(0) 
        );

        System.out.println("Update result: " + result);
    }
    public static void updateGameRow(Vector Attribute) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

       

        Statement statement = mConnection.createStatement();
        int result = statement.executeUpdate("UPDATE " + GAMES_TABLE_NAME
                + " SET "
                + GAMES_TABLE_COLUMN_PLAYER1 + "=\"" + Attribute.get(1)  + "\", "
                + GAMES_TABLE_COLUMN_PLAYER1 + "=\"" + Attribute.get(2)  + "\", "
                + GAMES_TABLE_COLUMN_WINNER + "=\"" + Attribute.get(3)  + "\", "
                + GAMES_TABLE_COLUMN_START_DATE + "=\"" + Attribute.get(4)  + "\", "
                + GAMES_TABLE_COLUMN_END_DATE + "=\"" + Attribute.get(5)  + "\", "
                + GAMES_TABLE_COLUMN_STATUS + "=\"" + Attribute.get(6)  + "\""
                + " WHERE " + GAMES_TABLE_COLUMN_ID + "=" + Attribute.get(0) 
        );

        System.out.println("Update result: " + result);
    }


    public static void deleteUserRow(int UserID) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

        

        Statement statement = mConnection.createStatement();
        int result = statement.executeUpdate("DELETE FROM " + USERS_TABLE_NAME
                + " WHERE " + USERS_TABLE_COLUMN_ID + "=" + UserID
        );
        System.out.println("Deletion result: " + result);
    }
    
    public static void deleteGameRow(int GameID) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

        

        Statement statement = mConnection.createStatement();
        int result = statement.executeUpdate("DELETE FROM " + GAMES_TABLE_NAME
                + " WHERE " + GAMES_TABLE_COLUMN_ID + "=" + GameID
        );
        System.out.println("Deletion result: " + result);
    }
    public void SelectUserAll() throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

       
        Statement statement = mConnection.createStatement();
       ResultSet TmpUsers =statement.executeQuery("SELECT * FROM " + USERS_TABLE_NAME);
        setUserSet(TmpUsers);
                
        System.out.println("Selected Query :"+TmpUsers);
    }
    public void SelectGameAll() throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

        

        Statement statement = mConnection.createStatement();
       ResultSet TmpGames =statement.executeQuery("SELECT * FROM " + GAMES_TABLE_NAME);
        setGameSet(TmpGames);
                
        System.out.println("Selected Query :"+TmpGames);
    }
    public boolean Signin(Vector UserObj) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");
        Statement statement = mConnection.createStatement();
       ResultSet TmpUser = statement.executeQuery("SELECT * FROM `" + USERS_TABLE_NAME + "` WHERE `Email` LIKE '"+ UserObj.get(0)+"'");
       
       
       //System.out.println(TmpUser.getString(2));
      if(TmpUser.first()&&TmpUser.getString(5).equals(UserObj.get(1)))
       {
        return true;
       }
       else
       {
       return false;
       }
      
                
       
    }

    /*======================================================================================================*/
            @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            setupDatabaseConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            createTable();
        } catch (SQLException ex) {
            Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        Vector employee;
        employee = new Vector();
        employee.add(2);
        employee.add("mohamed mostafa");
                employee.add(0);
                employee.add("medoaboserii@gmail.com");
                employee.add("testtest");
                employee.add(1);
                Vector Game;
        Game = new Vector();
       // Game.add(1);
        Game.add(2);
        Game.add(3);
        Game.add(2);
                Game.add("2019-12-04");
                Game.add("2019-12-05");
                Game.add("test");
Vector sign;
sign=new Vector();
sign.add("medoaboserii@gmail.com");
sign.add("testtest");





        
        try {
            //insertUsersRow(employee);
            //insertGamesRow(Game);
            //updateUserRow(employee);
            //updateGameRow(Game);
            //deleteGameRow(1);
            //deleteUserRow(1);
            /*Javaproject ob=new Javaproject();
            ob.SelectUserAll();
            
            ResultSet o=ob.getUserSet();
            o.next();
            if(o.next())
            System.err.println(o.getString(2));
            //game select
            ob.SelectGameAll();
            ResultSet s=ob.getGameSet(); 
            if(s.next())
            System.err.println(s.getInt(1));*/
            DbConfig s=new DbConfig();
            boolean x=s.Signin(sign);
            System.out.println(x);
            
        } catch (SQLException ex) {
            Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
