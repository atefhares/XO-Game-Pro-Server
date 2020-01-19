package com.itijavafinalprojectteam8.model;

public class Game {
    public int id;
    public String player1Email;
    public String player2Email;
    public String gameState;

    @Override
    public String toString() {
        return "Game{" +
                "player1Email='" + player1Email + '\'' +
                ", player2Email='" + player2Email + '\'' +
                ", gameState='" + gameState + '\'' +
                '}';
    }
}
