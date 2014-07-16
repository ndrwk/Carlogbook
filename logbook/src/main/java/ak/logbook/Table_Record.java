package ak.logbook;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Drew on 28.07.13.
 */
public class Table_Record extends Table{

    public static final String TableName = "records";
    public static final String EVENT = "event";
    public static final String PART = "part";
    public static final String QTY = "qty";
    public static final String SUMM = "summ";
    public static final String CATEGORY = "category";
    public static final String CURRENCY = "currency";
    public static final String NOTE = "note";
    public static final String TABLECREATESTRING = "CREATE TABLE "+TableName+
            " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+EVENT+
            " INTEGER NOT NULL, "+CATEGORY+" INTEGER NOT NULL, "+PART+" INTEGER, "+QTY+" INTEGER, "+
            SUMM+" REAL, "+CURRENCY+" INTEGER, "+NOTE+" TEXT);";
    public static final String[] COLUMNS = {ID, EVENT, CATEGORY, PART, QTY, SUMM, CURRENCY, NOTE};



    public Table_Record (){
        Name = TableName;
        super.COLUMNS = COLUMNS;
    }

    @Override
    public String getCSVline (Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(EVENT))+","+
                cursor.getInt(cursor.getColumnIndex(CATEGORY))+","+
                cursor.getInt(cursor.getColumnIndex(PART))+","+
                cursor.getInt(cursor.getColumnIndex(QTY))+","+
                cursor.getDouble(cursor.getColumnIndex(SUMM))+","+
                cursor.getInt(cursor.getColumnIndex(CURRENCY))+","+
                cursor.getString(cursor.getColumnIndex(NOTE))+"\n";
    }

    @Override
    public String getCSVheader() {
        return EVENT+","+CATEGORY+","+PART+","+QTY+","+SUMM+","+CURRENCY+","+NOTE+"\n";
    }

    @Override
    public ContentValues getCV(String[] line) {
        if (line.length!=COLUMNS.length-1){
            return null;
        }
        ContentValues cv = new ContentValues();
        cv.put(EVENT, line[0]);
        cv.put(CATEGORY, line[1]);
        cv.put(PART, line[2]);
        cv.put(QTY, line[3]);
        cv.put(SUMM, line[4]);
        cv.put(CURRENCY, line[5]);
        cv.put(NOTE, line[6]);
        return cv;
    }
}
