package ak.logbook;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Drew on 24.05.13.
 */
public class Table_Car extends Table {

    public static final String FIRM = "firm";
    public static final String MODEL = "model";
    public static final String FUELTANK = "fueltank";
    public static final String VIN = "VIN";
    public static final String SINCE = "since";
    public static final String SINCEODO = "sinceodo";
    public static final String TableName = "cars";
    public static final String TABLECREATESTRING = "CREATE TABLE "+TableName+
            " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+FIRM+
            " TEXT NOT NULL, "+MODEL+" TEXT NOT NULL, "+FUELTANK+" INTEGER NOT NULL, "+
            VIN+" TEXT, "+SINCE+" LONG, "+SINCEODO+" INTEGER);";
    public static final String [] COLUMNS = {ID,FIRM,MODEL,FUELTANK,VIN,SINCE,SINCEODO};

    public Table_Car (){
        Name = TableName;
        super.COLUMNS = COLUMNS;
    }

    @Override
    public String getCSVline (Cursor cursor) {
//        return cursor.getInt(cursor.getColumnIndex(Table_Car.ID))+","+
          return cursor.getString(cursor.getColumnIndex(Table_Car.FIRM))+","+
                cursor.getString(cursor.getColumnIndex(Table_Car.MODEL))+","+
                cursor.getInt(cursor.getColumnIndex(Table_Car.FUELTANK))+","+
                cursor.getString(cursor.getColumnIndex(Table_Car.VIN))+","+
                cursor.getLong(cursor.getColumnIndex(Table_Car.SINCE))+","+
                cursor.getInt(cursor.getColumnIndex(Table_Car.SINCEODO))+"\n";
    }

    @Override
    public String getCSVheader() {
        return FIRM+","+MODEL+","+FUELTANK+","+VIN+","+SINCE+","+SINCEODO+"\n";
    }

    @Override
    public ContentValues getCV(String[] line) {
        if (line.length!=COLUMNS.length-1){
            return null;
        }
        ContentValues cv = new ContentValues();
        cv.put(Table_Car.FIRM, line[0]);
        cv.put(Table_Car.MODEL, line[1]);
        cv.put(Table_Car.FUELTANK, line[2]);
        cv.put(Table_Car.VIN, line[3]);
        cv.put(Table_Car.SINCE, line[4]);
        cv.put(Table_Car.SINCEODO, line[5]);
        return cv;
    }
}
