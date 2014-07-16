package ak.logbook;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Drew on 28.07.13.
 */
public class Table_Event extends Table{

    public static final String CAR = "car";
    public static final String DATE = "date";
    public static final String ODOMETR = "odometr";
    public static final String PROVIDER = "provider";
    public static final String TableName = "events";
    public static final String TABLECREATESTRING = "CREATE TABLE "+TableName+
            " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+CAR+
            " INTEGER NOT NULL, "+DATE+" LONG NOT NULL, "+
            ODOMETR+" INTEGER NOT NULL, "+PROVIDER+" INTEGER NOT NULL);";
    public static final String [] COLUMNS = {ID, CAR, DATE, ODOMETR, PROVIDER};



    public Table_Event (){
        Name = TableName;
        super.COLUMNS = COLUMNS;
    }

    @Override
    public String getCSVline (Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(CAR))+","+
                cursor.getLong(cursor.getColumnIndex(DATE))+","+
                cursor.getInt(cursor.getColumnIndex(ODOMETR))+","+
                cursor.getInt(cursor.getColumnIndex(PROVIDER))+"\n";
    }

    @Override
    public String getCSVheader() {
        return CAR+","+DATE+","+ODOMETR+","+PROVIDER+"\n";
    }

    @Override
    public ContentValues getCV(String[] line) {
        if (line.length!=COLUMNS.length-1){
            return null;
        }
        ContentValues cv = new ContentValues();
        cv.put(CAR, line[0]);
        cv.put(DATE, line[1]);
        cv.put(ODOMETR, line[2]);
        cv.put(PROVIDER, line[3]);
        return cv;
    }
}
