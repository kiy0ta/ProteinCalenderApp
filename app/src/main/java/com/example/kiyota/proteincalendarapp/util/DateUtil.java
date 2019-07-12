package com.example.kiyota.proteincalendarapp.util;


import com.example.kiyota.proteincalendarapp.dto.ProteinEntity;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kiyota on 2019/07/12.
 */

public class DateUtil {

    //日付の同日比較メソッド
    public static boolean isSameDate(Date date1,Date date2) {
        int result = DateUtils.truncatedCompareTo(date1,date2, Calendar.DATE);
        return result == 0;
    }

    //プロテインを飲んだ日かどうか確認するメソッド
    public static boolean isDrinkingDate(List<ProteinEntity> drinkingDateList, Date targetDate) {
        for(ProteinEntity date : drinkingDateList) {
            if (isSameDate(date.getDrinkingDay(),targetDate)) {
                return true;
            }
        }
        return false;
    }

}
