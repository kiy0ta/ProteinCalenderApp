package com.example.kiyota.proteincalendarapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kiyota.proteincalendarapp.constants.DefaultData;
import com.example.kiyota.proteincalendarapp.constants.ProteinType;
import com.example.kiyota.proteincalendarapp.dao.ProteinCalendarDao;
import com.example.kiyota.proteincalendarapp.dto.ProteinEntity;
import com.example.kiyota.proteincalenderapp.R;
import com.example.kiyota.proteincalendarapp.adapter.CalendarGridAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    CalendarGridAdapter mCalendarGridView;
    Calendar mCalendar;
    TextView mTextViewForPrice;
    TextView mTextViewForBottle;
    TextView mTextViewForProtein;
    ProteinCalendarDao mDao;
    List<ProteinEntity> mEntityList = new ArrayList<ProteinEntity>();
    ImageView mImageViewCocoa;
    ImageView mImageViewLemon;
    ImageView mImageViewNormal;
    ImageView mImageViewYogurt;

    //カレンダーの月フォーマット型
    private static final String CLICKED_CALENDAR_FORMAT = "yyyy/MM/dd";
    TextView mTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //メイン画面をセットする処理
        setContentView(R.layout.activity_main);
        mDao = new ProteinCalendarDao(this);

        //カレンダークラスのオブジェクトを生成する(= new)
        mCalendar = Calendar.getInstance();

        //GridViewにResourceIDを紐付ける
        GridView grid = findViewById(R.id.calendar_grid);

        try {
            mEntityList = mDao.selectAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mCalendarGridView = new CalendarGridAdapter(mEntityList, this, mCalendar);

        grid.setAdapter(mCalendarGridView);

        //TitleMonthに表示する文字列を
        mTitleText = findViewById(R.id.titleText);
        String calendarTitle = mCalendarGridView.titleMonth();
        mTitleText.setText(calendarTitle);

        //「合計金額」の文字列を表示する処理
        mTextViewForPrice = findViewById(R.id.total_price);
        //「合計本数」の文字列を表示する処理を
        mTextViewForBottle = findViewById(R.id.total_bottle);
        //「合計タンパク質」の文字列を表示する処理
        mTextViewForProtein = findViewById(R.id.total_protein);

        //DBのデータ数を取得する処理
        int count = mDao.getDataCount();

        if (count == 0) {
            //DBの中にデータがない場合、初期値「0」を表示する
            mTextViewForPrice.setText(DefaultData.defaultTotalPrice);
            mTextViewForBottle.setText(DefaultData.defaultTotalBottle);
            mTextViewForProtein.setText(DefaultData.defaultTotalProtein);
        } else {
            //DBの中にデータがある場合、すべてを取得してEntityに格納するDaoを呼び出す
            //拡張for
            int sumPrice = 0;
            int sumBottle = 0;
            int sumProtein = 0;
            try {
                for (ProteinEntity entity : mDao.selectCurrentMonth(mCalendarGridView.titleMonth())) {
                    sumPrice = sumPrice + entity.getPrice();
                    sumBottle = sumBottle + entity.getBottle();
                    sumProtein = sumProtein + entity.getProtein();
                }

                setTotalText(sumPrice, sumBottle, sumProtein);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //カレンダーの「前月」を押下したときのカレンダー更新(再計算)処理
        ImageView lastMonth = findViewById(R.id.calendar_last_month);
        lastMonth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCalendarGridView.updateMonth(-1);
                mCalendarGridView.notifyDataSetChanged();
                mTitleText = findViewById(R.id.titleText);
                mTitleText.setText(mCalendarGridView.titleMonth());

                int sumPrice = 0;
                int sumBottle = 0;
                int sumProtein = 0;

                try {
                    for (ProteinEntity entity : mDao.selectCurrentMonth(mCalendarGridView.titleMonth())) {
                        sumPrice = sumPrice + entity.getPrice();
                        sumBottle = sumBottle + entity.getBottle();
                        sumProtein = sumProtein + entity.getProtein();
                    }

                    setTotalText(sumPrice, sumBottle, sumProtein);

                } catch (
                        ParseException e)

                {
                    e.printStackTrace();
                }

            }
        });

        //カレンダーの「次月」を押下したときのカレンダー更新(再計算)処理
        ImageView nextMonth = findViewById(R.id.calender_next_month);
        nextMonth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCalendarGridView.updateMonth(1);
                mCalendarGridView.notifyDataSetChanged();
                mTitleText = findViewById(R.id.titleText);
                mTitleText.setText(mCalendarGridView.titleMonth());

                int sumPrice = 0;
                int sumBottle = 0;
                int sumProtein = 0;

                try {
                    for (ProteinEntity entity : mDao.selectCurrentMonth(mCalendarGridView.titleMonth())) {
                        sumPrice = sumPrice + entity.getPrice();
                        sumBottle = sumBottle + entity.getBottle();
                        sumProtein = sumProtein + entity.getProtein();
                    }

                    setTotalText(sumPrice, sumBottle, sumProtein);

                } catch (
                        ParseException e)

                {
                    e.printStackTrace();
                }
            }
        });

        //gridViewの1マスをクリックしたときの処理
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                //「プロテイン選択バー」のVisibleをTrueにする処理
                LinearLayout linearLayout = findViewById(R.id.appear_select_protein);
                linearLayout.setVisibility(View.VISIBLE);

                //「プロテイン選択バー」の画像(ココア)を押下したときの処理
                mImageViewCocoa = findViewById(R.id.calendar_protein_type_cocoa);
                mImageViewCocoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickProcess(position, ProteinType.stCOCOA);

                    }
                });

                //「プロテイン選択バー」の画像(レモン)を押下したときの処理
                mImageViewLemon = findViewById(R.id.calendar_protein_type_lemon);
                mImageViewLemon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        clickProcess(position, ProteinType.stLEMON);
                    }
                });

                //「プロテイン選択バー」の画像(ノーマル)を押下したときの処理
                mImageViewNormal = findViewById(R.id.calendar_protein_type_noemal);
                mImageViewNormal.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        clickProcess(position, ProteinType.stNORMAL);
                    }
                });

                //「プロテイン選択バー」の画像(ヨーグルト)を押下したときの処理
                mImageViewYogurt = findViewById(R.id.calendar_protein_type_yogurt);
                mImageViewYogurt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        clickProcess(position, ProteinType.stYOGURT);
                    }
                });

            }

            //クリックされたプロテインに応じて登録、更新処理を行うメソッド

            public void clickProcess(int position, String type) {

                Date clickedDate = (Date) mCalendarGridView.getItem(position);

                //Date型に変換する
                SimpleDateFormat dateFormat = new SimpleDateFormat(CLICKED_CALENDAR_FORMAT, Locale.JAPAN);
                String dateToString = (dateFormat.format(clickedDate));
                mDao.registProteinAllInfo(dateToString, type);

                //mEntityListの中身を更新するために、サイドすべてのデータを取得する
                try {
                    mEntityList = mDao.selectAll();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mCalendarGridView.initList(mEntityList);

                mCalendarGridView.notifyDataSetChanged();

                LinearLayout linearLayout = findViewById(R.id.appear_select_protein);

                int sumPrice = 0;
                int sumBottle = 0;
                int sumProtein = 0;

                try {
                    for (ProteinEntity entity : mDao.selectCurrentMonth(mCalendarGridView.titleMonth())) {
                        sumPrice = sumPrice + entity.getPrice();
                        sumBottle = sumBottle + entity.getBottle();
                        sumProtein = sumProtein + entity.getProtein();
                    }

                    setTotalText(sumPrice, sumBottle, sumProtein);
                    linearLayout.setVisibility(View.GONE);

                } catch (
                        ParseException e)

                {
                    e.printStackTrace();
                }

            }
        });
    }

    public void setTotalText(int sumPrice, int sumBottle, int sumProtein) {
        String sumPriceString = Integer.toString(sumPrice);
        String sumBottleString = Integer.toString(sumBottle);
        String sumProteinString = Integer.toString(sumProtein);

        mTextViewForPrice.setText(sumPriceString + " 円");
        mTextViewForBottle.setText(sumBottleString + " 本");
        mTextViewForProtein.setText(sumProteinString + " g");

    }

}







