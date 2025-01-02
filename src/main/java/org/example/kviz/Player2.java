package org.example.kviz;

public class Player2 {
    private String name;
    private int score;

    public Player2(String name, int score) {
        this.setName(name);
        this.setScore(score);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    @Override
    public String toString() {
        return "Player2{" +
                "name= " + this.getName() +
                ", score= " + this.getScore() +
                "}";
    }
}
