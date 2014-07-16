package ak.logbook;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Drew on 26.05.13.
 */
public class Table_Provider extends Table {
    public static final String TableName = "providers";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String TABLECREATESTRING = "CREATE TABLE " + TableName
            + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + NAME + " TEXT NOT NULL, " + PHONE + " TEXT, " + ADDRESS
            + " TEXT);";
    public static final String[] COLUMNS ={ ID, NAME, PHONE, ADDRESS };


    public Table_Provider (){
        Name = TableName;
        super.COLUMNS=COLUMNS;

    }

    @Override
    public String getCSVline (Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(NAME))+","+
                cursor.getString(cursor.getColumnIndex(PHONE))+","+
                cursor.getString(cursor.getColumnIndex(ADDRESS))+"\n";
    }

    @Override
    public String getCSVheader() {
        return NAME+","+PHONE+","+ADDRESS+"\n";
    }

    @Override
    public ContentValues getCV(String[] line) {
        if (line.length!=COLUMNS.length-1){
            return null;
        }
        ContentValues cv = new ContentValues();
        cv.put(NAME, line[0]);
        cv.put(PHONE, line[1]);
        cv.put(ADDRESS, line[2]);
        return cv;
    }
}
