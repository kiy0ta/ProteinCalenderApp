package com.kiyokiyo.proteincalenderapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.kiyokiyo.proteincalenderapp.R;
import com.kiyokiyo.proteincalenderapp.constants.DefaultData;
import com.kiyokiyo.proteincalenderapp.constants.ProteinType;
import com.kiyokiyo.proteincalenderapp.dao.ProteinCalendarDao;
import com.kiyokiyo.proteincalenderapp.dto.ProteinEntity;
import com.kiyokiyo.proteincalenderapp.adapter.CalendarGridAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;


public class TopActivity extends AppCompatActivity {

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
    Button mButtonOverCalendar;
    LinearLayout mLinearLayout;
    private static final int MY_REQUEST_CODE = 1111;
    private AppUpdateManager appUpdateManager;

    //カレンダーの月フォーマット型
    private static final String CLICKED_CALENDAR_FORMAT = "yyyy/MM/dd";
    TextView mTitleText;

    public static Intent getStartIntent(SplashActivity splashActivity) {
        return new Intent(splashActivity, TopActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        /**
         * log
         */
        Log.d("loglog", "バージョンチェックをします");
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
        Log.d("loglog", "appUpdateInfo.updateAvailability():"+appUpdateInfo.updateAvailability());
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                   ) {
                /**
                 * log
                 */
                Log.d("loglog", "開始します");
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            MY_REQUEST_CODE);
                    /**
                     * log
                     */
                    Log.d("loglog", "アップデートが完了しました");
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

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

                List<ProteinEntity> entityList = new ArrayList<ProteinEntity>();
                Date clickedDate = (Date) mCalendarGridView.getItem(position);

                //Date型に変換する
                SimpleDateFormat dateFormat = new SimpleDateFormat(CLICKED_CALENDAR_FORMAT, Locale.JAPAN);
                String dateToString = (dateFormat.format(clickedDate));

                try {
                    entityList = mDao.selectTypeDate(dateToString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (ProteinEntity entity : entityList) {
                    if (entity.getProteinType() >= 0) {
                        Resources res = getResources();
                        //Dialogを呼び出す処理
                        new AlertDialog.Builder(TopActivity.this, R.style.MyAlertDialogStyle)
                                .setMessage(R.string.dialog_message)
                                //閉じるボタン押下でダイアログを閉じる処理
                                .setNegativeButton(res.getString(R.string.dialog_close_button), null)
                                //Dialogを閉じる処理
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                    }
                                })
                                .show();
                        return;
                    }
                }


                //「プロテイン選択バー」のVisibleをTrueにする処理
                mLinearLayout = findViewById(R.id.appear_select_protein);
                mLinearLayout.setVisibility(View.VISIBLE);

                //カレンダーを覆うボタンのVisibleをTrueにする処理
                mButtonOverCalendar = findViewById(R.id.button_over_calendar);
                mButtonOverCalendar.setVisibility(View.VISIBLE);

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

                //グレー部分を押下したらキャンセルする処理
                mButtonOverCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLinearLayout.setVisibility(View.GONE);
                        mButtonOverCalendar.setVisibility(View.GONE);
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
                    mLinearLayout.setVisibility(View.GONE);
                    mButtonOverCalendar.setVisibility(View.GONE);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.i("update", "Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            Log.d("loglog", "アップデートが必要です");
                            Log.d("loglog", "" + appUpdateInfo.updateAvailability());
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    Log.d("loglog", "アップデートを開始します");
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            this,
                                            MY_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }
}







