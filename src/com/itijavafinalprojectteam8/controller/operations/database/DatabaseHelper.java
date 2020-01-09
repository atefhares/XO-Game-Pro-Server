
package com.itijavafinalprojectteam8.controller.operations.database;


import com.itijavafinalprojectteam8.model.Player;
import com.itijavafinalprojectteam8.others.Constants;
import com.sun.istack.internal.NotNull;

import java.sql.*;
import java.util.Vector;

public class DatabaseHelper {

    private static final String USERNAME = "test";
    private static final String PASSWORD = "!Pass12345678";

    private static final String SERVER = "localhost";
    private static final String PORT = "3306";
    private static final String PARAMS = "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://" + SERVER + ":" + PORT;
    /*======================================================================================================*/

    private static final String DATABASE_NAME = "tictactoe";
    /*======================================================================================================*/

    private static final String PLAYERS_TABLE_NAME = "players";
    private static final String PLAYERS_TABLE_COLUMN_ID = "id";
    private static final String PLAYERS_TABLE_COLUMN_NAME = "name";
    private static final String PLAYERS_TABLE_COLUMN_POINTS = "points";
    private static final String PLAYERS_TABLE_COLUMN_EMAIL = "email";
    private static final String PLAYERS_TABLE_COLUMN_PASSWORD = "password";
    private static final String PLAYERS_TABLE_COLUMN_STATUS = "status";
    /*======================================================================================================*/

    private static final String GAMES_TABLE_NAME = "games";
    private static final String GAMES_TABLE_COLUMN_ID = "id";
    private static final String GAMES_TABLE_COLUMN_PLAYER1_ID = "player1_id";
    private static final String GAMES_TABLE_COLUMN_PLAYER2_ID = "player2_id";

    private static final String GAMES_TABLE_COLUMN_WINNER_ID = "winner_id";
    private static final String GAMES_TABLE_COLUMN_START_DATE = "start_date";
    private static final String GAMES_TABLE_COLUMN_END_DATE = "end_date";
    private static final String GAMES_TABLE_COLUMN_STATUS = "game_status";
    /*======================================================================================================*/

    private static Connection mConnection;

    /*======================================================================================================*/

    public static void setupDatabaseConnection() throws SQLException {
        try {
            //create connection to mysql server

            //this line to ensure driver lib added loaded
            Class.forName("com.mysql.jdbc.Driver");

            mConnection = DriverManager.getConnection(URL + PARAMS, USERNAME, PASSWORD);
            System.out.println("Server connection established successfully...");

            System.out.println("Attempt to create database IF NOT EXISTS: " + DATABASE_NAME);
            Statement statement = mConnection.createStatement();
            int Result = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
            System.out.println("Database creation result: " + Result);

            // closing this connection and open a connection to the database itself
            mConnection.close();
            System.out.println("Attempt to select database: " + DATABASE_NAME);
            mConnection = DriverManager.getConnection(URL + "/" + DATABASE_NAME + PARAMS, USERNAME, PASSWORD);
            System.out.println("Database selected successfully...");

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void createTables() throws SQLException {
        System.out.println("Attempt to create table: " + PLAYERS_TABLE_NAME);

        Statement statement = mConnection.createStatement();

        createPlayersTable(statement);
        createGamesTable(statement);
    }

    private static void createGamesTable(@NotNull final Statement statement) throws SQLException {
        int result = statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + GAMES_TABLE_NAME
                        + "("
                        + GAMES_TABLE_COLUMN_ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                        + GAMES_TABLE_COLUMN_PLAYER1_ID + " INT NOT NULL, "
                        + GAMES_TABLE_COLUMN_PLAYER2_ID + " INT NOT NULL, "
                        + GAMES_TABLE_COLUMN_WINNER_ID + " INT  NOT NULL, "
                        + GAMES_TABLE_COLUMN_START_DATE + " DATE NOT NULL, "
                        + GAMES_TABLE_COLUMN_END_DATE + " DATE NOT NULL, "
                        + GAMES_TABLE_COLUMN_STATUS + " VARCHAR(255) NOT NULL, "
                        + "FOREIGN KEY (" + GAMES_TABLE_COLUMN_PLAYER1_ID + ") REFERENCES " + PLAYERS_TABLE_NAME + "(" + PLAYERS_TABLE_COLUMN_ID + "), "
                        + "FOREIGN KEY (" + GAMES_TABLE_COLUMN_PLAYER2_ID + ") REFERENCES " + PLAYERS_TABLE_NAME + "(" + PLAYERS_TABLE_COLUMN_ID + "), "
                        + "FOREIGN KEY (" + GAMES_TABLE_COLUMN_WINNER_ID + ") REFERENCES " + PLAYERS_TABLE_NAME + "(" + PLAYERS_TABLE_COLUMN_ID + ") "
                        + ")"
        );
        System.out.println("[createGamesTable] result: " + result);
    }

    private static void createPlayersTable(@NotNull final Statement statement) throws SQLException {
        int result = statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + PLAYERS_TABLE_NAME
                        + "("
                        + PLAYERS_TABLE_COLUMN_ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                        + PLAYERS_TABLE_COLUMN_NAME + " VARCHAR(100) NOT NULL, "
                        + PLAYERS_TABLE_COLUMN_EMAIL + " VARCHAR(100) NOT NULL, "
                        + PLAYERS_TABLE_COLUMN_PASSWORD + " VARCHAR(128) NOT NULL, "
                        + PLAYERS_TABLE_COLUMN_POINTS + " INT DEFAULT 0 NOT NULL, "
                        + PLAYERS_TABLE_COLUMN_STATUS + " INT DEFAULT " + Constants.PlayerStatus.OFFLINE + " NOT NULL"
                        + ")"
        );
        System.out.println("[createPlayersTable] result: " + result);
    }

    /*======================================================================================================*/

    public static void insertPlayer(@NotNull final Vector<String> signUpData) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");


        Statement statement = mConnection.createStatement();
        int result = statement.executeUpdate("INSERT INTO " + PLAYERS_TABLE_NAME
                + " VALUES ("
                + "null, "
                + "\"" + signUpData.get(0) + "\", "
                + "\"" + signUpData.get(1) + "\", "
                + "\"" + signUpData.get(2) + "\", "
                + 0 + ", "
                + Constants.PlayerStatus.OFFLINE
                + ")"
        );

        System.out.println("[insertPlayer] result: " + result);
    }

    public static boolean isUserCredentialsCorrect(@NotNull final String email, @NotNull final String pass) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

        Statement statement = mConnection.createStatement();

        ResultSet resultSet = statement.executeQuery(
                "select * from " + PLAYERS_TABLE_NAME
                        + " WHERE " + PLAYERS_TABLE_COLUMN_EMAIL + "=" + "\"" + email + "\""
                        + " AND " + PLAYERS_TABLE_COLUMN_PASSWORD + "=" + "\"" + pass + "\""
                        + " LIMIT 1"
        );

        if (resultSet == null || !resultSet.first())
            return false;

        return resultSet.getString(PLAYERS_TABLE_COLUMN_PASSWORD).equals(pass);
    }

    public static void updatePlayerStatus(int status, int playerId) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

        Statement statement = mConnection.createStatement();
        int result = statement.executeUpdate("UPDATE " + PLAYERS_TABLE_NAME
                + " SET "
                + PLAYERS_TABLE_COLUMN_STATUS + "=" + status
                + " WHERE " + PLAYERS_TABLE_COLUMN_ID + "=" + playerId
        );

        System.out.println("[updatePlayerStatus] result: " + result);
    }

    public static Player getPlayerByEmail(String email) throws SQLException {
        if (mConnection == null)
            throw new NullPointerException("No database connection found");

        Statement statement = mConnection.createStatement();

        ResultSet resultSet = statement.executeQuery(
                "select * from " + PLAYERS_TABLE_NAME
                        + " WHERE " + PLAYERS_TABLE_COLUMN_EMAIL + "=" + "\"" + email + "\""
                        + " LIMIT 1"
        );

        Player player = null;
        if (resultSet != null && resultSet.first()) {
            player = new Player();
            player.id = resultSet.getInt(PLAYERS_TABLE_COLUMN_ID);
            player.name = resultSet.getString(PLAYERS_TABLE_COLUMN_NAME);
            player.email = resultSet.getString(PLAYERS_TABLE_COLUMN_EMAIL);
            player.status = resultSet.getInt(PLAYERS_TABLE_COLUMN_STATUS);
        }

        return player;
    }

    public static boolean playerAlreadyRegistered(String playerEmail) throws SQLException {
        return getPlayerByEmail(playerEmail) != null;
    }

    /*======================================================================================================*/


//    public static void insertUsersRow(Vector Attribute) throws SQLException {
//        if (mConnection == null)
//            throw new NullPointerException("No database connection found");
//
//
//        Statement statement = mConnection.createStatement();
//        int result = statement.executeUpdate("INSERT INTO " + PLAYERS_TABLE_NAME
//                + " VALUES ("
//                + "null, "
//                + "\"" + Attribute.get(0) + "\", "
//                + "\"" + Attribute.get(1) + "\", "
//                + "\"" + Attribute.get(2) + "\", "
//                + "\"" + Attribute.get(3) + "\", "
//                + "\"" + Attribute.get(4) + "\""
//                + ")"
//        );
//
//        System.out.println("Insertion result: " + result);
//
//    }
//
//    public static void insertGamesRow(Vector Attribute) throws SQLException {
//        if (mConnection == null)
//            throw new NullPointerException("No database connection found");
//
//
//        Statement statement = mConnection.createStatement();
//        System.out.println(Attribute.get(0));
//
//        int result = statement.executeUpdate("INSERT INTO " + GAMES_TABLE_NAME
//                + " VALUES ("
//                + "null, "
//                + "\"" + Attribute.get(0) + "\", "
//                + "\"" + Attribute.get(1) + "\", "
//                + "\"" + Attribute.get(2) + "\", "
//                + "\"" + Attribute.get(3) + "\", "
//                + "\"" + Attribute.get(4) + "\", "
//                + "\"" + Attribute.get(5) + "\""
//                + ")"
//        );
//
//        System.out.println("Insertion result: " + result);
//
//    }
//
//    public static void updateUserRow(Vector Attribute) throws SQLException {
//        if (mConnection == null)
//            throw new NullPointerException("No database connection found");
//
//
//        Statement statement = mConnection.createStatement();
//        int result = statement.executeUpdate("UPDATE " + PLAYERS_TABLE_NAME
//                + " SET "
//                + PLAYERS_TABLE_COLUMN_NAME + "=\"" + Attribute.get(1) + "\", "
//                + PLAYERS_TABLE_COLUMN_POINTS + "=\"" + Attribute.get(2) + "\", "
//                + PLAYERS_TABLE_COLUMN_EMAIL + "=\"" + Attribute.get(3) + "\", "
//                + PLAYERS_TABLE_COLUMN_PASSWORD + "=\"" + Attribute.get(4) + "\", "
//                + PLAYERS_TABLE_COLUMN_STATUS + "=\"" + Attribute.get(5) + "\" "
//                + " WHERE " + PLAYERS_TABLE_COLUMN_ID + "=" + Attribute.get(0)
//        );
//
//        System.out.println("Update result: " + result);
//    }
//
//    public static void updateGameRow(Vector Attribute) throws SQLException {
//        if (mConnection == null)
//            throw new NullPointerException("No database connection found");
//
//
//        Statement statement = mConnection.createStatement();
//        int result = statement.executeUpdate("UPDATE " + GAMES_TABLE_NAME
//                + " SET "
//                + GAMES_TABLE_COLUMN_PLAYER1 + "=\"" + Attribute.get(1) + "\", "
//                + GAMES_TABLE_COLUMN_PLAYER1 + "=\"" + Attribute.get(2) + "\", "
//                + GAMES_TABLE_COLUMN_WINNER_ID + "=\"" + Attribute.get(3) + "\", "
//                + GAMES_TABLE_COLUMN_START_DATE + "=\"" + Attribute.get(4) + "\", "
//                + GAMES_TABLE_COLUMN_END_DATE + "=\"" + Attribute.get(5) + "\", "
//                + GAMES_TABLE_COLUMN_STATUS + "=\"" + Attribute.get(6) + "\""
//                + " WHERE " + GAMES_TABLE_COLUMN_ID + "=" + Attribute.get(0)
//        );
//
//        System.out.println("Update result: " + result);
//    }
//
//    public static void deleteUserRow(int UserID) throws SQLException {
//        if (mConnection == null)
//            throw new NullPointerException("No database connection found");
//
//
//        Statement statement = mConnection.createStatement();
//        int result = statement.executeUpdate("DELETE FROM " + PLAYERS_TABLE_NAME
//                + " WHERE " + PLAYERS_TABLE_COLUMN_ID + "=" + UserID
//        );
//        System.out.println("Deletion result: " + result);
//    }
//
//    public static void deleteGameRow(int GameID) throws SQLException {
//        if (mConnection == null)
//            throw new NullPointerException("No database connection found");
//
//
//        Statement statement = mConnection.createStatement();
//        int result = statement.executeUpdate("DELETE FROM " + GAMES_TABLE_NAME
//                + " WHERE " + GAMES_TABLE_COLUMN_ID + "=" + GameID
//        );
//        System.out.println("Deletion result: " + result);
//    }
//
//    public void SelectUserAll() throws SQLException {
//        if (mConnection == null)
//            throw new NullPointerException("No database connection found");
//
//
//        Statement statement = mConnection.createStatement();
//        ResultSet TmpUsers = statement.executeQuery("SELECT * FROM " + PLAYERS_TABLE_NAME);
//        setUserSet(TmpUsers);
//
//        System.out.println("Selected Query :" + TmpUsers);
//    }
//
//    public void SelectGameAll() throws SQLException {
//        if (mConnection == null)
//            throw new NullPointerException("No database connection found");
//
//
//        Statement statement = mConnection.createStatement();
//        ResultSet TmpGames = statement.executeQuery("SELECT * FROM " + GAMES_TABLE_NAME);
//        setGameSet(TmpGames);
//
//        System.out.println("Selected Query :" + TmpGames);
//    }
//
//    public boolean Signin(Vector UserObj) throws SQLException {
//        if (mConnection == null)
//            throw new NullPointerException("No database connection found");
//        Statement statement = mConnection.createStatement();
//        ResultSet TmpUser = statement.executeQuery("SELECT * FROM `" + PLAYERS_TABLE_NAME + "` WHERE `Email` LIKE '" + UserObj.get(0) + "'");
//
//
//        //System.out.println(TmpUser.getString(2));
//        if (TmpUser.first() && TmpUser.getString(5).equals(UserObj.get(1))) {
//            return true;
//        } else {
//            return false;
//        }
//
//
//    }
}