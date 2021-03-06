package com.kiyokiyo.proteincalenderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiyokiyo.proteincalenderapp.constants.ProteinType;
import com.kiyokiyo.proteincalenderapp.dto.ProteinEntity;
import com.kiyokiyo.proteincalenderapp.manager.CalenderDateManager;
import com.kiyokiyo.proteincalenderapp.util.DateUtil;
import com.kiyokiyo.proteincalenderapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kiyota on 2019/07/11.
 */

public class CalendarGridAdapter extends BaseAdapter {

    //カレンダーの日付を格納しているリスト
    private List<Date> mDateArray = new ArrayList();
    //当月のみを格納しているリスト
    private ArrayList<Integer> mCurrentMonthDayList;
    //飲んだ日付とプロテインの種類を突き合わせるためのhashMap
    public HashMap<Date, String> hashmap = new HashMap<Date, String>();
    //日付計算クラスのインスタンス
    private CalenderDateManager mDateManager;
    //プロテインを飲んだ日付を格納しているリスト
    public List<Date> mDrinkingDateList = new ArrayList<Date>();

    //新規登録したあとにMainでよびだしている初期化処理
    public void initList(List<ProteinEntity> mEntityList) {
        this.mEntityList = mEntityList;
        hashmap = new HashMap<Date, String>();
    }

    //DB内の全データを保持するリスト
    private List<ProteinEntity> mEntityList = new ArrayList<ProteinEntity>();
    //カレンダーの日付フォーマット型
    private static final String CALENDAR_DATE_FORMAT = "d";
    //Viewをnewするためのもの
    LayoutInflater mLayoutInflater;
    //Context
    public Context mContext;

    //コンストラクタ
    public CalendarGridAdapter(List<ProteinEntity> entityList, Context context, Calendar calendar) {
        this.mContext = context;
        this.mEntityList = entityList;
        this.mDateManager = new CalenderDateManager(calendar);
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mDateArray = this.mDateManager.getDays();
        this.mCurrentMonthDayList = new ArrayList<Integer>();
    }

    //カレンダーのセルの総数を取得するメソッド
    @Override
    public int getCount() {
        return this.mDateArray.size();
    }

    //View取得メソッド
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //List<Entity>から、List<Date>を作成する
        for (ProteinEntity entity : mEntityList) {
            mDrinkingDateList.add(entity.getDrinkingDay());
            hashmap.put(entity.getDrinkingDay(), Integer.toString(entity.getProteinType()));
        }

        // View保持クラス取得
        ViewHolder holder;

        //View.inflateを使わずに、mLayoutInflaterを使う場合は、contextが必要
        convertView = View.inflate(mContext, R.layout.calender_cell_layout, null);

        //日付を取得する
        holder = new ViewHolder();
        holder.dateText = convertView.findViewById(R.id.dateText);
        convertView.setTag(holder);

        //年とか抜きで、日付のみを表示させる整形処理
        SimpleDateFormat dateFormat = new SimpleDateFormat(CALENDAR_DATE_FORMAT);
        holder.dateText.setText(dateFormat.format(getDate(position)));

        //日付カラーのIDを取得
        int colorId;

        //当月判定
        if (this.mDateManager.isCurrentMonth(getDate(position))) {

            //当月の場合、当月リストに追加する
            this.mCurrentMonthDayList.add(position);

            //日付の文字色を、曜日に応じた色を取得して適用する処理
            switch (this.mDateManager.getDayOfWeek(getDate(position))) {
                case Calendar.SUNDAY:
                    colorId = R.color.colorCalenderSunday;
                    break;
                case Calendar.SATURDAY:
                    colorId = R.color.colorCalenderSaturday;
                    break;
                default:
                    colorId = R.color.color_black;
                    break;
            }

        } else {

            //当月じゃない日付の文字色はグレーにする
            colorId = R.color.color_calendar_not_current_month_date;

        }
        holder.dateText.setTextColor(mContext.getResources().getColor(colorId));

        //当月かつ、当日の場合は日付の背景色を透過きみどり色に変更する
        if (isCurrentMonth(position) && DateUtil.isSameDate(getDate(position), new java.util.Date())) {
            RelativeLayout todayBackGround = convertView.findViewById(R.id.today_bg);
            todayBackGround.setBackgroundColor(convertView.getResources().getColor(R.color.colorCalenderTodayBG));
        }

        //当日かつ、プロテインを飲んだ履歴がある場合、適当なプロテイン画像を表示
        if (isCurrentMonth(position) && DateUtil.isDrinkingDate(mDrinkingDateList, getDate(position))) {

            //飲んだ日付のリストの中での順番と、カレンダー上の日付の突き合わせ処理
            Date dateForType = DateUtil.compareForGetType(mDrinkingDateList, getDate(position));

            switch (hashmap.get(dateForType)) {

                case ProteinType.stCOCOA:
                    holder.proteinIcon = convertView.findViewById(R.id.protein_icon);
                    holder.proteinIcon.setBackgroundResource(R.drawable.ic_cocoa);
                    break;
                case ProteinType.stLEMON:
                    holder.proteinIcon = convertView.findViewById(R.id.protein_icon);
                    holder.proteinIcon.setBackgroundResource(R.drawable.ic_lemon);
                    break;
                case ProteinType.stNORMAL:
                    holder.proteinIcon = convertView.findViewById(R.id.protein_icon);
                    holder.proteinIcon.setBackgroundResource(R.drawable.ic_nomal);
                    break;
                case ProteinType.stYOGURT:
                    holder.proteinIcon = convertView.findViewById(R.id.protein_icon);
                    holder.proteinIcon.setBackgroundResource(R.drawable.ic_yogurt);
                    break;
                default:
                    break;
            }
        }

        return convertView;
    }

    //カレンダー上の何日目かを取得する
    @Override
    public Object getItem(int position) {
        return mDateArray.get(position);
    }

    //使わない
    @Override
    public long getItemId(int position) {
        return 0;
    }

    //カレンダーの日付の当月判定をするメソッド
    public boolean isCurrentMonth(int position) {
        return this.mCurrentMonthDayList.contains(position);
    }

    //日付リストから日付を取得するメソッド
    public java.util.Date getDate(int position) {
        return this.mDateArray.get(position);
    }

    //Viewに渡す日付と画像
    public class ViewHolder {
        public TextView dateText;
        public ImageView proteinIcon;
    }

    //「前月」「次月」ボタンが押下されたら、DateManagerを初期化して更新する処理
    public void updateMonth(int difMonth) {

        mDateManager.lastMonth(difMonth);
        this.mDateArray = this.mDateManager.getDays();
        this.mCurrentMonthDayList.clear();

    }

    //TitleMonthのsetTextに文字列を渡す用のメソッド
    public String titleMonth() {
        return mDateManager.getCalendarMonth();
    }

}
