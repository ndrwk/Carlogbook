package ak.logbook;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Drew on 26.05.13.
 */
public class Table_Part extends Table {
    public static final String TableName = "parts";
    public static final String PART = "part";
    public static final String PARTNUMBER = "partnumber";
    public static final String TABLECREATESTRING = "CREATE TABLE "+TableName+
            " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+PART+
            " TEXT NOT NULL, "+PARTNUMBER+" TEXT);";
    public static final String[]COLUMNS = {ID, PART, PARTNUMBER,};


    public Table_Part (){
        Name = TableName;
        super.COLUMNS=COLUMNS;

    }

    @Override
    public String getCSVline (Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(PART))+","+
                cursor.getString(cursor.getColumnIndex(PARTNUMBER))+"\n";
    }

    @Override
    public String getCSVheader() {
        return PART+","+PARTNUMBER+"\n";
    }

    @Override
    public ContentValues getCV(String[] line) {
        if (line.length!=COLUMNS.length-1){
            return null;
        }
        ContentValues cv = new ContentValues();
        cv.put(PART, line[0]);
        cv.put(PARTNUMBER, line[1]);
        return cv;
    }
}
