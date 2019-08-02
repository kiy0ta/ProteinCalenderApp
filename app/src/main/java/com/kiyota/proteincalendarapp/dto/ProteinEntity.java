package com.kiyota.proteincalendarapp.dto;

/**
 * Created by kiyota on 2019/07/12.
 */

public class ProteinEntity {

    //ID
    private int id;
    //プロテインを飲んだ日付
    private java.util.Date drinkingDay;
    //プロテインを飲んだ日付(String)
    private String drinkingDayString;
    //飲んだプロテインの種類
    private int proteinType;
    //飲んだプロテインの価格
    private int price;
    //飲んだプロテインの本数
    private int bottle;
    //飲んだプロテインに含まれるタンパク質の量
    private int protein;

    public ProteinEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.util.Date getDrinkingDay() {
        return drinkingDay;
    }

    public void setDrinkingDay(java.util.Date drinkingDay) {
        this.drinkingDay = drinkingDay;
    }

    public String getDrinkingDayString() {
        return drinkingDayString;
    }

    public void setDrinkingDayString(String drinkingDayString) {
        this.drinkingDayString = drinkingDayString;
    }

    public int getProteinType() {
        return proteinType;
    }

    public void setProteinType(int proteinType) {
        this.proteinType = proteinType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBottle() {
        return bottle;
    }

    public void setBottle(int bottle) {
        this.bottle = bottle;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    @Override
    public String toString() {
        return "ProteinEntity{" +
                "id=" + id +
                ", drinkingDay=" + drinkingDay +
                ", drinkingDayString='" + drinkingDayString + '\'' +
                ", proteinType=" + proteinType +
                ", price=" + price +
                ", bottle=" + bottle +
                ", protein=" + protein +
                '}';
    }
}

