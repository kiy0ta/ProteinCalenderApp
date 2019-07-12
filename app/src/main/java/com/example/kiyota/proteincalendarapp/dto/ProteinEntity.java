package com.example.kiyota.proteincalendarapp.dto;

import java.util.Date;

/**
 * Created by kiyota on 2019/07/12.
 */

public class ProteinEntity {

    private int proteinType;
    private Date drinkingDay;

    public ProteinEntity(int proteinType, Date drinkingDay) {
        this.proteinType = proteinType;
        this.drinkingDay = drinkingDay;
    }

    public int getProteinType() {
        return proteinType;
    }

    public void setProteinType(int proteinType) {
        this.proteinType = proteinType;
    }

    public Date getDrinkingDay() {
        return drinkingDay;
    }

    public void setDrinkingDay(Date drinkingDay) {
        this.drinkingDay = drinkingDay;
    }
}
