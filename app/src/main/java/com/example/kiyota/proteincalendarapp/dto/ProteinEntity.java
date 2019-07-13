package com.example.kiyota.proteincalendarapp.dto;

import java.util.Date;

/**
 * Created by kiyota on 2019/07/12.
 */

public class ProteinEntity {

    //プロテインを飲んだ日付
    private Date drinkingDay;
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

    public ProteinEntity(Date drinkingDay, int proteinType, int price, int bottle, int protein) {
        this.drinkingDay = drinkingDay;
        this.proteinType = proteinType;
        this.price = price;
        this.bottle = bottle;
        this.protein = protein;
    }

    public ProteinEntity() {

    }

    public Date getDrinkingDay() {
        return drinkingDay;
    }

    public void setDrinkingDay(Date drinkingDay) {
        this.drinkingDay = drinkingDay;
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

    public String getDrinkingDayString() {
        return drinkingDayString;
    }

    public void setDrinkingDayString(String drinkingDayString) {
        this.drinkingDayString = drinkingDayString;
    }
}
