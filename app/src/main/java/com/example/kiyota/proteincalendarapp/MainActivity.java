package com.example.kiyota.proteincalendarapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kiyota.proteincalendarapp.activity.TotalizationActivity;
import com.example.kiyota.proteincalendarapp.constants.DefaultData;
import com.example.kiyota.proteincalendarapp.constants.ProteinType;
import com.example.kiyota.proteincalendarapp.dao.ProteinCalendarDao;
import com.example.kiyota.proteincalendarapp.dto.ProteinEntity;
import com.example.kiyota.proteincalenderapp.R;
import com.example.kiyota.proteincalendarapp.adapter.CalendarGridAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CalendarGridAdapter cg;
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
    ImageView mProteinIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //メイン画面をセットする処理
        setContentView(R.layout.activity_main);
        mDao = new ProteinCalendarDao(this);

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
                for (ProteinEntity entity : mEntityList = mDao.selectAll()) {
                    sumPrice = sumPrice + entity.getPrice();
                }
                for (ProteinEntity entity : mEntityList = mDao.selectAll()) {
                    sumBottle = sumBottle + entity.getBottle();
                }
                for (ProteinEntity entity : mEntityList = mDao.selectAll()) {
                    sumProtein = sumProtein + entity.getProtein();
                }

                String sumPriceString = Integer.toString(sumPrice);
                String sumBottleString = Integer.toString(sumBottle);
                String sumProteinString = Integer.toString(sumProtein);

                mTextViewForPrice.setText(sumPriceString);
                mTextViewForBottle.setText(sumBottleString);
                mTextViewForProtein.setText(sumProteinString);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        //カレンダークラスのオブジェクトを生成する(= new)
        mCalendar = Calendar.getInstance();

        List<ProteinEntity> list = new ArrayList<ProteinEntity>();

        GridView grid = findViewById(R.id.calendar_grid);

        cg = new CalendarGridAdapter(this, mCalendar, list);

        grid.setAdapter(cg);

        //gridViewの1マスをクリックしたときの処理
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //「プロテイン選択バー」のVisibleをTrueにする処理
                LinearLayout linearLayout = findViewById(R.id.appear_select_protein);
                linearLayout.setVisibility(View.VISIBLE);

                //TODO"いいやり方がないか質問する"
                //「プロテイン選択バー」の画像(ココア)を押下したときの処理
                mImageViewCocoa = findViewById(R.id.calendar_protein_type_cocoa);
                mImageViewCocoa.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        //daoの登録メソッドを呼び出す

                        String day = "2019/07/07";
                        //Date型に変換する
                        //SimpleDateFormat dateFormat = new SimpleDateFormat(CALENDAR_FORMAT);
                        //String dateToString = (dateFormat.format(date));
                        mDao.registProteinAllInfo(day, ProteinType.stCOCOA);
                        mProteinIcon.findViewById(R.id.protein_icon);
                        mProteinIcon.setImageResource(R.drawable.ic_cocoa);
                    }
                });

                //「プロテイン選択バー」の画像(レモン)を押下したときの処理
                mImageViewLemon = findViewById(R.id.calendar_protein_type_lemon);
                mImageViewLemon.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        //daoの登録メソッドを呼び出す

                        String day = "2019/07/09";
                        //Date型に変換する
                        //SimpleDateFormat dateFormat = new SimpleDateFormat(CALENDAR_FORMAT);
                        //String dateToString = (dateFormat.format(date));
                        mDao.registProteinAllInfo(day, ProteinType.stLEMON);
                        mProteinIcon.findViewById(R.id.protein_icon);
                        mProteinIcon.setImageResource(R.drawable.ic_lemon);
                    }
                });

                //「プロテイン選択バー」の画像(ノーマル)を押下したときの処理
                mImageViewNormal = findViewById(R.id.calendar_protein_type_noemal);
                mImageViewNormal.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        //daoの登録メソッドを呼び出す

                        String day = "2019/07/11";
                        //Date型に変換する
                        //SimpleDateFormat dateFormat = new SimpleDateFormat(CALENDAR_FORMAT);
                        //String dateToString = (dateFormat.format(date));
                        mDao.registProteinAllInfo(day, ProteinType.stNORMAL);
                        mProteinIcon.findViewById(R.id.protein_icon);
                        mProteinIcon.setImageResource(R.drawable.ic_nomal);
                    }
                });

                //「プロテイン選択バー」の画像(ヨーグルト)を押下したときの処理
                mImageViewYogurt = findViewById(R.id.calendar_protein_type_yogurt);
                mImageViewYogurt.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        //daoの登録メソッドを呼び出す

                        String day = "2019/07/13";
                        //Date型に変換する
                        //SimpleDateFormat dateFormat = new SimpleDateFormat(CALENDAR_FORMAT);
                        //String dateToString = (dateFormat.format(date));
                        mDao.registProteinAllInfo(day, ProteinType.stYOGURT);
                        mProteinIcon.findViewById(R.id.protein_icon);
                        mProteinIcon.setImageResource(R.drawable.ic_yogurt);
                    }
                });

            }

        });


        //「グラフ集計」を押下したときのページ遷移の処理
        Button button = findViewById(R.id.totalization_button);
        button.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TotalizationActivity.class);
                startActivity(intent);
            }
        });

        //カレンダーの「前月」を押下したときのカレンダー更新(再計)処理
        ImageView lastMonth = findViewById(R.id.calendar_last_month);
        lastMonth.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

            }
        });

    }


}

