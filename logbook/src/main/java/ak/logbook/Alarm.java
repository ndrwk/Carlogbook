package ak.logbook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;

public class Alarm extends Item {
/*"CREATE TABLE alarm (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
		    "odometr INTEGER, date LONG, car INTEGER NOT NULL, note TEXT, "+
		    "category INTEGER, provider INTEGER);",
*/		    
	public static final String TableName = "alarms";
	public static final String ID = "_id";
	public static final String ODOMETR = "odometr";
	public static final String DATE = "date";
	public static final String CAR = "car";
	public static final String CATEGORY = "category";
	public static final String PROVIDER = "provider";
	public static final String NOTE = "note";
	public static final String TABLECREATESTRING = "CREATE TABLE "+TableName+
			" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+ODOMETR+
		    " INTEGER NOT NULL, "+DATE+" LONG, "+CAR+" INTEGER NOT NULL, "+
			CATEGORY+" INTEGER, "+PROVIDER+" INTEGER, "+NOTE+" TEXT);";
	public static final String[] COLUMNS = {ID, ODOMETR, DATE, CAR, CATEGORY, PROVIDER,
					NOTE};
	private int id, odometr, car, category, provider = 0;
	private long date = 0;
	private String note = null;
	
	public Alarm (){
	}
	
	@Override
	public void setItem(Cursor cursor) {
		id=cursor.getInt(cursor.getColumnIndex(ID));
		odometr=cursor.getInt(cursor.getColumnIndex(ODOMETR));
		date=cursor.getLong(cursor.getColumnIndex(DATE));
		car=cursor.getInt(cursor.getColumnIndex(CAR));
		category=cursor.getInt(cursor.getColumnIndex(CATEGORY));
		provider=cursor.getInt(cursor.getColumnIndex(PROVIDER));
		note=cursor.getString(cursor.getColumnIndex(NOTE));
	}

	@Override
	public void setItem(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContentValues getCV(){
		ContentValues cv = new ContentValues();
		cv.put(ODOMETR, odometr);
		cv.put(DATE, date);
		cv.put(CAR, car);
		cv.put(CATEGORY, category);
		cv.put(PROVIDER, provider);
		cv.put(NOTE, note);
		return cv;
	}

	@Override
	public View inflateView(View v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent toIntent(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}

