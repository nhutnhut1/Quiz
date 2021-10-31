package com.example.englishpractice.model;

public class RankDB {

//    public RankDB(String name, long score, int rank) {
//        this.name = name;
//        this.score = score;
//        this.rank = rank;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public long getScore() {
//        return score;
//    }
//
//    public void setScore(long score) {
//        this.score = score;
//    }
//
//    public int getRank() {
//        return rank;
//    }
//
//    public void setRank(int rank) {
//        this.rank = rank;
//    }

    private String name;
    private int score;
    private int rank;


    public RankDB(String name, int score, int rank) {
        this.score = score;
        this.rank = rank;
        this.name = name;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
