package ak.logbook;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Drew on 24.05.13.
 */
public abstract class Table {

    public static String Name;
    public static final String ID = "_id";
    public static String[] COLUMNS;
    public static String TABLECREATESTRING;
    public static int menuLevel;

    public static String getTableName(){
        return Name;
    }

    public static String[] getColumns (){
        return COLUMNS;
    }

    abstract public String getCSVline (Cursor cursor);
    abstract public String getCSVheader ();
    abstract public ContentValues getCV (String[] line);

}
