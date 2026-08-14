package com.example.kutubxona;

public class Zakladka {
    public int id;
    public int book_id;
    public int user_id;
    public String status;

    public Zakladka(int i, int b, int u, String s){
        this.id = i;
        this.book_id = b;
        this.user_id = u;
        this.status = s;
    }
    public Zakladka(int b, int u, String s){
        this.book_id = b;
        this.user_id = u;
        this.status = s;
    }
}
