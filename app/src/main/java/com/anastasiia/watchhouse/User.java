package com.anastasiia.watchhouse;

/**
 * Created by anastasiia on 24.05.17.
 */
public class User {
    int id;
    String name;
    String email;
    String ip;


    public User(){}
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String ip) {
        this.name = name;
        this.email = email;
        this.ip = ip;
    }


    public User(int id, String name, String email, String ip) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.ip = ip;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
