package ak.logbook;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Drew on 24.05.13.
 */
public class Table_Category extends Table {

    public static final String TableName = "categories";
    public static final String ID = "_id";
    public static final String CATEGORY = "category";
    public static final String ICON = "icon";
    public static final String COLOR = "color";

    public static final String TABLECREATESTRING = "CREATE TABLE "+TableName+
            " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+CATEGORY+
            " TEXT NOT NULL, "+ICON+" INTEGER, "+COLOR+" INTEGER);";
    public static final String [] COLUMNS = {ID, CATEGORY, ICON, COLOR};


    public Table_Category (){
        Name = TableName;
        super.COLUMNS=COLUMNS;
    }

    @Override
    public String getCSVline (Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(CATEGORY))+","+
                cursor.getInt(cursor.getColumnIndex(ICON))+","+
                cursor.getInt(cursor.getColumnIndex(COLOR))+"\n";
    }

    @Override
    public String getCSVheader() {
        return CATEGORY+","+ICON+","+COLOR+"\n";
    }

    @Override
    public ContentValues getCV(String[] line) {
        if (line.length!=COLUMNS.length-1){
            return null;
        }
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY, line[0]);
        cv.put(ICON, line[1]);
        cv.put(COLOR, line[2]);
        return cv;
    }
}
