package com.example.kutubxona;

public class Book {

    public int id;
    public String kitobname;
    public int kateg_id;
    public String rasm_id;
    public String opesan;
    public String fayl_id;
    public String audio_id;
    public String komment;

    public Book(int i, String n, int k, String r, String o,String f,String aud, String kom){
        this.id = i;
        this.kitobname = n;
        this.kateg_id = k;
        this.rasm_id = r;
        this.opesan = o;
        this.fayl_id = f;
        this.audio_id = aud;
        this.komment= kom;
    }
    public Book(String n, int k, String r, String o,String f,String aud,String kom){
        this.kitobname = n;
        this.kateg_id = k;
        this.rasm_id = r;
        this.opesan = o;
        this.fayl_id = f;
        this.audio_id = aud;
        this.komment= kom;
    }
}
