package cf.parks.codingcontests.dbdb;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cf.parks.codingcontests.customui.CustomList;

/**
 * Created by RKS.
 */

public class ContestDAO extends SQLiteOpenHelper {
    private static final String TABLE_NAME1 = "ongoing";
    private static final String TABLE_NAME2 = "upcoming";
    private static final String NAME = "name";
    private static final String SITE = "site";
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";
    private static final String DURATION = "duration";
    private static final String PLATFORM = "platform";

    public ContestDAO(Context context) {
        super(context, "contestsdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME1 + " (" +
                NAME + " VARCHAR(50), " +
                SITE + " VARCHAR(150), " +
                END_TIME + " VARCHAR(50), " +
                PLATFORM + " VARCHAR(15) " +
                ");");
    db.execSQL("CREATE TABLE "+ TABLE_NAME2 + " (" +
                NAME + " VARCHAR(50), " +
                SITE + " VARCHAR(150), " +
                START_TIME + " VARCHAR(50), " +
                END_TIME + " VARCHAR(50), " +
                DURATION + " VARCHAR(50), " +
                PLATFORM + " VARCHAR(15) " +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public CustomList getContestOngoingData(Activity activity) {
        SQLiteDatabase database = getReadableDatabase();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>(); // null
        List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>(); // null
        List<String> list6 = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME1, null);
        while(cursor.moveToNext()) {
            list1.add(cursor.getString(0));
            list2.add(cursor.getString(1));
            list4.add(cursor.getString(2));
            list6.add(cursor.getString(3));
        }
        cursor.close();
        return new CustomList(activity,list1, list2, list3, list4, list5, list6);
    }

    public CustomList getContestUpcomingData(Activity activity) {
        SQLiteDatabase database = getReadableDatabase();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();
        List<String> list6 = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME2, null);
        while(cursor.moveToNext()) {
            list1.add(cursor.getString(0));
            list2.add(cursor.getString(1));
            list3.add(cursor.getString(2));
            list4.add(cursor.getString(3));
            list5.add(cursor.getString(4));
            list6.add(cursor.getString(5));
        }
        cursor.close();
        return new CustomList(activity,list1, list2, list3, list4, list5, list6);
    }

    public void insert(JSONObject jsonObject, String type) throws JSONException {
        ContentValues cv = getContentValues(jsonObject, type);
        SQLiteDatabase database = getWritableDatabase();
        if(type.equals(TABLE_NAME1))
            database.insert(TABLE_NAME1, null, cv);
        else
            database.insert(TABLE_NAME2, null, cv);
    }

    private ContentValues getContentValues(JSONObject jsonObject, String type) throws JSONException {
        ContentValues cv = new ContentValues();
        cv.put(NAME , jsonObject.getString("Name"));
        cv.put(SITE , jsonObject.getString("url"));
        cv.put(END_TIME , jsonObject.getString("EndTime"));
        cv.put(PLATFORM , jsonObject.getString("Platform"));

        if(type.equals(TABLE_NAME2)) {
            cv.put(START_TIME , jsonObject.getString("StartTime"));
            cv.put(DURATION , jsonObject.getString("Duration"));
        }

        return cv;
    }

    public void clearDB() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME1, null, null);
        database.delete(TABLE_NAME2, null, null);
    }
}
