package ak.logbook;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Drew on 26.05.13.
 */
public class Table_Currency extends Table {
    public static final String TableName = "currencies";
    public static final String ID = "_id";
    public static final String CURRENCY = "currency";
    public static final String PRICE = "price";
    public static final String ICON = "icon";
    public static final String TABLECREATESTRING = "CREATE TABLE "+TableName+
            " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+CURRENCY+
            " TEXT NOT NULL, "+PRICE+" DOUBLE NOT NULL, "+ICON+" INTEGER NOT NULL);";
    public static final String[] COLUMNS = {ID, CURRENCY, PRICE, ICON};


    public Table_Currency (){
        Name = TableName;
        super.COLUMNS=COLUMNS;
    }

    @Override
    public String getCSVline (Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(CURRENCY))+","+
                cursor.getDouble(cursor.getColumnIndex(PRICE))+","+
                cursor.getInt(cursor.getColumnIndex(ICON))+"\n";
    }

    @Override
    public String getCSVheader() {
        return CURRENCY+","+PRICE+","+ICON+"\n";
    }

    @Override
    public ContentValues getCV(String[] line) {
        if (line.length!=COLUMNS.length-1){
            return null;
        }
        ContentValues cv = new ContentValues();
        cv.put(CURRENCY, line[0]);
        cv.put(PRICE, line[1]);
        cv.put(ICON, line[2]);
        return cv;
    }
}
