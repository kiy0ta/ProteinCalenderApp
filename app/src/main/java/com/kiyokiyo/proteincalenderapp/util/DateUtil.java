package com.kiyokiyo.proteincalenderapp.util;


import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kiyota on 2019/07/12.
 */

public class DateUtil {

    //飲んだ日付のタイプを返すメソッド
    public static Date compareForGetType(List<Date> drinkingList, Date currentDate) {

        for (Date date : drinkingList) {

            if (isSameDate(date, currentDate)) {

                return date;
            }
        }
        return null;
    }

    //日付の同日比較メソッド
    public static boolean isSameDate(java.util.Date date1, java.util.Date date2) {
        int result = DateUtils.truncatedCompareTo(date1, date2, Calendar.DATE);
        return result == 0;
    }

    //プロテインを飲んだ日が、当月かどうか確認するメソッド
    public static boolean isDrinkingDate(List<Date> drinkingDateList, java.util.Date targetDate) {
        for (Date date : drinkingDateList) {
            if (isSameDate(date, targetDate)) {
                return true;
            }
        }
        return false;
    }

}
