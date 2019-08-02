package com.example.kiyota.proteincalendarapp.constants;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by kiyota on 2019/07/12.
 */

public class ProteinCalendarContract {

    public static final String AUTHORITY = "com.example.kiyota.proteincalendarapp.provider";
    public static final String PATH_INPUT = "spa_input";


    public interface BaseInputColumns extends BaseColumns {
    }

    public static final class Input implements BaseInputColumns {
        public static final Uri CONTENT_URI = Uri.parse(
                "content://" + AUTHORITY + "/" + PATH_INPUT);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/spa_input_v1";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/spa_input_v1";

        public static final String RECORD_DATE = "record_date";

        public static final String RECORD_TYPE = "record_type";

        public static final String RECORD_PRICE = "record_price";

        public static final String RECORD_BOTTLE = "record_bottle";

        public static final String RECORD_PROTEIN = "record_protein";

        public static final String DEFAULT_SORT_ORDER = _ID + " ASC";

        private Input() {
        }
    }
}
