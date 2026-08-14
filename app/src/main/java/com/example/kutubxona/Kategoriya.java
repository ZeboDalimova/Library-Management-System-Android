package com.example.kutubxona;

public class Kategoriya {

    public int id;
    public String kat_name;
    public String image;

    public Kategoriya(int i, String n,String im){
        this.id = i;
        this.kat_name = n;
        this.image = im;
    }
    public Kategoriya(String n,String im){
        this.kat_name = n;
        this.image = im;
    }
}
