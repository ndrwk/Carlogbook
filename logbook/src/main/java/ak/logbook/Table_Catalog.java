package ak.logbook;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Drew on 24.05.13.
 */
public class Table_Catalog extends Table{

    public static final int menuLevel=0;
    public static final String TableName = "catalog";
    public static String ITEMNAME = "itemname";
    public static String ICON = "icon";
    public static String CHILDNAME = "childname";
    public static final String TABLECREATESTRING = "CREATE TABLE "+TableName+
            " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+ITEMNAME+
            " TEXT NOT NULL, "+ICON+" INTEGER NOT NULL, "+CHILDNAME+" TEXT NOT NULL);";
    public static final String [] COLUMNS = new String []{ID, ITEMNAME, ICON, CHILDNAME};


    public Table_Catalog (){
        Name = TableName;
    }

    @Override
    public String getCSVline (Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(Table_Catalog.ITEMNAME))+","+
                cursor.getString(cursor.getColumnIndex(Table_Catalog.ICON))+","+
                cursor.getInt(cursor.getColumnIndex(Table_Catalog.CHILDNAME))+"\n";
    }

    @Override
    public String getCSVheader() {
        return ITEMNAME+","+ICON+","+CHILDNAME+"\n";
    }

    @Override
    public ContentValues getCV(String[] line) {
        if (line.length!=COLUMNS.length-1){
            return null;
        }
        ContentValues cv = new ContentValues();
        cv.put(Table_Catalog.ITEMNAME, line[0]);
        cv.put(Table_Catalog.ICON, line[1]);
        cv.put(Table_Catalog.CHILDNAME, line[2]);
        return cv;
    }


}
