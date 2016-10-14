package com.carepuppy.pirtu.caremypuppy.Models;

/**
 * Created by pirtu on 20/09/2016.
 */
public class Carer {

    private String adress;
    private String city;
    private int cp;
    private String name;
    private double priceD;
    private double priceN;
    private float stars;
    private String surname;
    private String urlImg;
    private String description;
    private String id_carer;
    private int phone;

    public Carer() {
    }

    public Carer(String description, String adress, String city, int cp, String name, double priceD, double priceN, float stars, String surname, String urlImg, String id_carer, int phone) {
        this.description = description;
        this.adress = adress;
        this.city = city;
        this.cp = cp;
        this.name = name;
        this.priceD = priceD;
        this.priceN = priceN;
        this.stars = stars;
        this.surname = surname;
        this.urlImg = urlImg;
        this.id_carer = id_carer;
        this.phone = phone;
    }

    public double getPriceN() {
        return priceN;
    }

    public void setPriceN(double priceN) {
        this.priceN = priceN;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceD() {
        return priceD;
    }

    public void setPriceD(double priceD) {
        this.priceD = priceD;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId_carer() {
        return id_carer;
    }

    public void setId_carer(String id_carer) {
        this.id_carer = id_carer;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Carer{" +
                "adress='" + adress + '\'' +
                ", city='" + city + '\'' +
                ", cp=" + cp +
                ", name='" + name + '\'' +
                ", priceD=" + priceD +
                ", priceN=" + priceN +
                ", stars=" + stars +
                ", surname='" + surname + '\'' +
                ", urlImg='" + urlImg + '\'' +
                ", description='" + description + '\'' +
                ", id_carer='" + id_carer + '\'' +
                ", phone=" + phone +
                '}';
    }
}
