package ru.toster.uggu;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.toster.uggu.url.ObjectPara;
import ru.toster.uggu.url.ReadUrl;



//101 - работает ли нотификатион
//102 - выбор группы
//103 - Тип уведомлений 0-Без звука и вибраций 1-С вибрацией без звука 2-Со звуком и вибрацией
//111 - Первый запуск
public class SQlite extends SQLiteOpenHelper {//view-source:https://ursmu.eduschedule.ru/
    private static String DB_NAME = "cityinfo.db";
    private static final int VERSION = 1; // версия базы данных
    public static final String TABLE = "raspisanie"; // название таблицы в бд
    private HashMap<Integer, List<ObjectPara>> raspWeek = new HashMap<>();

    public static final String COLUMN_DAY_WEEK= "_id";
    public static final String RASP_DAY= "rasp";

    public SQlite(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public boolean isFirstStart(){
        String text = getTable(111);
        if (text==null) return false;
        return Boolean.parseBoolean(text);
    }

    public void setDB(String text, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();;
        values.put(COLUMN_DAY_WEEK,id);
        values.put(RASP_DAY, text);
//        db.insert(TABLE,null, values);
        db.update(TABLE,values,COLUMN_DAY_WEEK+" = ?",
                new String[] { String.valueOf(id)  });
        db.close();
    }

    public boolean isNotif(){
        String text = getTable(101);
        if (text==null)
            return false;
        return Boolean.parseBoolean(text);
    }


    public String getGroup(){
        return getTable(102);
    }

    public String getNotifPlasn(){
        return getTable(103);
    }

    public void saveRasp(Map<Integer, List<ObjectPara>> map){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = null;
        for (int i=0;i<6;i++){
            values = new ContentValues();
            values.put(COLUMN_DAY_WEEK,i);
            values.put(RASP_DAY, map.get(i).toString());
            db.insert(TABLE,null, values);
        }
        db.close();
    }
    public void save(Map<Integer, String> map){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = null;
        for (Integer key : map.keySet()){
            values = new ContentValues();
            values.put(COLUMN_DAY_WEEK,key);
            values.put(RASP_DAY, map.get(key));
            db.insert(TABLE,null, values);
        }
        db.close();
    }

    public void saveRasp(String bodyJS){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY_WEEK, 100);
        values.put(RASP_DAY,bodyJS);
        if (!isRasp())
            db.insert(TABLE, null, values);
        else
            db.update(TABLE,values,COLUMN_DAY_WEEK+" = ?",
                    new String[] { "100" });
        db.close();
    }

    public boolean isRasp(){
        try {
            if (getAllRasp()!=null)
                return true;
        }catch (Exception e){
            return false;
        }
        return false;
    }

    private String getTable(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(TABLE, null, null, null, null, null, null);

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("_id");
            int nameColIndex = c.getColumnIndex("rasp");

            do {
                if (c.getInt(idColIndex)==id) {
                    String text =c.getString(nameColIndex);
//                    db.close();
                    return text;
                }
            } while (c.moveToNext());
        }
        return null;
    }

    public Map<Integer, List<ObjectPara>> getAllRasp(){
        Map<Integer, List<ObjectPara>> raspWeek=ReadUrl.pars(getTable(100));
        return raspWeek;
    }

    public List<ObjectPara> getSQL(int dayOfWeek){
        Map<Integer, List<ObjectPara>> raspWeek = getAllRasp();
        if (getAllRasp()==null) return new ArrayList<>();
        switch (dayOfWeek){//dayOfWeek
            case Calendar.MONDAY:
                return raspWeek.get(0);
            case Calendar.TUESDAY:
                return raspWeek.get(1);
            case Calendar.WEDNESDAY:
                return raspWeek.get(2);
            case Calendar.THURSDAY:
                return raspWeek.get(3);
            case Calendar.FRIDAY:
                return raspWeek.get(4);
            case Calendar.SATURDAY:
                return raspWeek.get(5);
            case Calendar.SUNDAY:
                return raspWeek.get(6);
        }
        return new ArrayList<>();
    }
    public List<ObjectPara> getRasp(int dayOfWeek){
        Map<Integer, List<ObjectPara>> raspWeek = getAllRasp();
        if (getAllRasp()==null||raspWeek.get(dayOfWeek)==null) return new ArrayList<>();

        return raspWeek.get(dayOfWeek);
    }


    @Override
    public void onCreate(SQLiteDatabase sq) {
        sq.execSQL("create table " + TABLE + "("+COLUMN_DAY_WEEK+" INTEGER PRIMARY KEY," +
                RASP_DAY + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sq, int i, int i1) {
        sq.execSQL("DROP TABLE IF IT EXISTS " + TABLE);

        onCreate(sq);
    }
}