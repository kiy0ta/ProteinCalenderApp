package com.example.kiyota.proteincalendarapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kiyota.proteincalendarapp.constants.ProteinType;
import com.example.kiyota.proteincalendarapp.manager.CalenderDateManager;
import com.example.kiyota.proteincalendarapp.util.DateUtil;
import com.example.kiyota.proteincalenderapp.R;
import com.example.kiyota.proteincalendarapp.dto.ProteinEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kiyota on 2019/07/11.
 */

public class CalendarGridAdapter extends BaseAdapter {

    //カレンダーの日付を格納しているリスト
    private List<Date> mDateArray = new ArrayList();
    //当月のみを格納しているリスト
    private ArrayList<Integer> mCurrentMonthDayList;
    //日付計算クラスのインスタンス
    private CalenderDateManager mDateManager;
    //プロテインを飲んだ日付を格納しているリスト
    //プロテインを飲んだ日付を格納しているEntity
    private List<ProteinEntity> mDrinkingDateList;
    //カレンダーの月フォーマット型
    private static final String CALENDAR_MONTH_FORMAT = "yyyy/MM";
    //カレンダーの日付フォーマット型
    private static final String CALENDAR_DATE_FORMAT = "d";
    //Context
    public Context mContext;

    //コンストラクタ
    public CalendarGridAdapter(Context context, Calendar calendar, List<ProteinEntity> drinkingDateList) {
        this.mDateManager = new CalenderDateManager(calendar);
        this.mDateArray = this.mDateManager.getDays();
        this.mDrinkingDateList = drinkingDateList;
        this.mContext = context;
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

        // View保持クラス取得
        ViewHolder holder;

        if (convertView == null) {

            //View.inflateを使わずに、mLayoutInflaterを使う場合は、contextが必要
            convertView = View.inflate(mContext, R.layout.calender_cell_layout, null);
            holder = new ViewHolder();

            //日付を取得する
            holder.dateText = convertView.findViewById(R.id.dateText);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

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
                    //TODO "文字色をグレーに修正する(かも)"
                    colorId = R.color.color_black;
                    break;
            }

        } else {

            //当月じゃない日付の文字色はグレーにする
            colorId = R.color.color_calendar_not_current_month_date;

        }

        //当月かつ、当日の場合は日付の背景色を透過きみどり色に変更する
        if (isCurrentMonth(position) && DateUtil.isSameDate(getDate(position), new Date())) {
            LinearLayout todayBackGround = convertView.findViewById(R.id.today_bg);
            todayBackGround.setBackgroundColor(convertView.getResources().getColor(R.color.colorCalenderTodayBG));
        }

        //当日かつ、プロテインを飲んだ履歴がある場合、適当なプロテイン画像を表示
        if (isCurrentMonth(position) && DateUtil.isDrinkingDate(this.mDrinkingDateList, getDate(position))) {

            switch (this.mDrinkingDateList.get(position).getProteinType()) {
                case ProteinType.COCOA:
                    holder.proteinIcon = convertView.findViewById(R.id.protein_icon);
                    holder.proteinIcon.setBackgroundResource(R.drawable.ic_cocoa);
                    break;
                case ProteinType.LEMON:
                    holder.proteinIcon = convertView.findViewById(R.id.protein_icon);
                    holder.proteinIcon.setBackgroundResource(R.drawable.ic_lemon);
                    break;
                case ProteinType.NORMAL:
                    holder.proteinIcon = convertView.findViewById(R.id.protein_icon);
                    holder.proteinIcon.setBackgroundResource(R.drawable.ic_nomal);
                    break;
                case ProteinType.YOGURT:
                    holder.proteinIcon = convertView.findViewById(R.id.protein_icon);
                    holder.proteinIcon.setBackgroundResource(R.drawable.ic_yogurt);
                    break;
                default:
                    holder.proteinIcon = convertView.findViewById(R.id.protein_icon);
                    holder.proteinIcon.setBackgroundResource(R.drawable.ic_yogurt);
                    break;
            }
        }

        return convertView;
    }

    //使わない
    @Override
    public Object getItem(int position) {
        return null;
    }

    //使わない
    @Override
    public long getItemId(int position) {
        return 0;
    }

    //カレンダーの表の上の、表示月を取得するメソッド
    public String getMonthTitle() {
        SimpleDateFormat format = new SimpleDateFormat(CALENDAR_MONTH_FORMAT);
        return format.format(mDateManager.mCalendar.getTime());
    }

    //カレンダーの日付の当月判定をするメソッド
    public boolean isCurrentMonth(int position) {
        return this.mCurrentMonthDayList.contains(position);
    }

    //日付リストから日付を取得するメソッド
    public Date getDate(int position) {
        return this.mDateArray.get(position);
    }

    //Viewに渡す日付と画像
    public class ViewHolder {
        public TextView dateText;
        public ImageView proteinIcon;
    }
}
