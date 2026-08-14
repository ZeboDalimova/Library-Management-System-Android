package com.example.kutubxona;

public class Person {
    public int id;
    public String username;
    public String email;
    public String phone;
    public String password;
    public String status;
    public String image;
    public Person(int i, String u, String e, String p,String pas,String s,String im){
        this.id = i;
        this.username = u;
        this.email = e;
        this.phone = p;
        this.password = pas;
        this.status = s;
        this.image = im;
    }
    public Person(String u, String e, String p,String pas,String s,String im){
        this.username = u;
        this.email = e;
        this.phone = p;
        this.password = pas;
        this.status = s;
        this.image = im;
    }
}
