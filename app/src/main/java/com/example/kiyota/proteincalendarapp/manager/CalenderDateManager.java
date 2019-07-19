package com.example.kiyota.proteincalendarapp.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kiyota on 2019/07/11.
 */

public class CalenderDateManager {

    //カレンダークラスの日付を取得する
    public Calendar mCalendar;
    //選択日の月と現在の月が合致するか、判定するために月までに整形する
    private static final String IS_CURRENT_MONTH_FORMAT = "yyyy.MM";
    //カレンダー(GridView)のマスの総数を計算する
    public static final int COUNT_DAYS = 6 * 7;
    //表示しているカレンダーのタイトル用フォーマット
    private static final String FORMAT_FOR_TITLE = "yyyy/MM";

    //コンストラクタ
    public CalenderDateManager(Calendar calender) {
        this.mCalendar = calender;
    }

    //当月のカレンダーの上下に、前の月の日にちを含めて表示するメソッド
    public List<Date> getDays() {

        //現在、カレンダークラスに入っている日付の状態を取得する
        Date startDate = this.mCalendar.getTime();

        //当月のカレンダーの上下に表示される、前月分と来月分の日数を計算する
        //まずは、現在の月の選択されている日付に1を入れてリセットする
        this.mCalendar.set(Calendar.DATE, 1);

        //日曜日(1)始まりなので、現在の曜日から日曜日に戻す計算をする
        int dayOfWeek = this.mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        this.mCalendar.add(Calendar.DATE, -dayOfWeek);

        //日曜日始まり(前月含む)の日数をListに入れて呼び出し元にreturnする処理
        List<Date> daysList = new ArrayList<>();
        for (int i = 0; i < COUNT_DAYS; i++) {
            daysList.add(this.mCalendar.getTime());
            //1日進める
            this.mCalendar.add(Calendar.DATE, 1);
        }

        //カレンダーを初期状態に戻す
        this.mCalendar.setTime(startDate);
        return daysList;
    }

    //現在の日にちが、当月かどうか判定するメソッド
    public boolean isCurrentMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(IS_CURRENT_MONTH_FORMAT);
        String currentMonth = format.format(this.mCalendar.getTime());
        return currentMonth.equals(format.format(date));
    }

    //曜日取得メソッド
    public int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //「前月」または「次月」のカレンダーをセットする
    public void lastMonth(int i) {
        this.mCalendar.add(Calendar.MONTH, i);
    }

    //カレンダーの日付を取得、フォーマットして返すメソッド
    //MainのTitleMonth表示に使用する
    public String getCalendarMonth() {
        Date date = mCalendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_FOR_TITLE);
        String dateTitle;
        dateTitle = format.format(date);
        return dateTitle;
    }

}