package ak.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class Car extends Item {

    public Car(Context cntxt){
		RowLayout = R.layout.l_car;
        menuLevel=1;
        columns = Table_Car.COLUMNS;
        tableName = Table_Car.TableName;
        context = cntxt;

    }


    private String firm, model, vin = null;
    private int fueltank, sinceodo = 0;
    private long since=0;

	public ContentValues getCV(){
		ContentValues cv = new ContentValues();
		cv.put(Table_Car.FIRM, firm);
		cv.put(Table_Car.MODEL, model);
		cv.put(Table_Car.FUELTANK, fueltank);
		cv.put(Table_Car.VIN, vin);
		cv.put(Table_Car.SINCE, since);
		cv.put(Table_Car.SINCEODO, sinceodo);
		return cv;
	}

	public View inflateView (View v){
		TextView tt = (TextView) v.findViewById(R.id.toptxtCar);
		TextView bt = (TextView) v.findViewById(R.id.bottomtxtCar);
        View icon =  v.findViewById(R.id.view_car_icon);

        if (tt != null) {
            tt.setText(firm+" "+model);
        }
        if(bt != null){
            bt.setText("VIN "+vin);
        }
        if (icon!=null){
            IconicFontDrawable icon_draw = new IconicFontDrawable(context);
            icon_draw.setIcon(IconsEnum.AUTOMOBILE_CAR);
            icon.setBackgroundDrawable(icon_draw);
        }
		return v;
	}
	
	@Override
	public void setItem(Cursor cursor) {
		id=cursor.getInt(cursor.getColumnIndex(Table_Car.ID));
		firm=cursor.getString(cursor.getColumnIndex(Table_Car.FIRM));
		model=cursor.getString(cursor.getColumnIndex(Table_Car.MODEL));
		fueltank=cursor.getInt(cursor.getColumnIndex(Table_Car.FUELTANK));
		vin=cursor.getString(cursor.getColumnIndex(Table_Car.VIN));
		since=cursor.getLong(cursor.getColumnIndex(Table_Car.SINCE));
		sinceodo=cursor.getInt(cursor.getColumnIndex(Table_Car.SINCEODO));
		
	}

	@Override
	public void setItem(Intent intent) {
		id=intent.getIntExtra(Table_Car.ID, 0);
		firm=intent.getStringExtra(Table_Car.FIRM);
		model=intent.getStringExtra(Table_Car.MODEL);
		fueltank=intent.getIntExtra(Table_Car.FUELTANK, 0);
		vin=intent.getStringExtra(Table_Car.VIN);
		since=intent.getLongExtra(Table_Car.SINCE, 0);
		sinceodo=intent.getIntExtra(Table_Car.SINCEODO, 0);
	}

	public void setItem (String _firm, String _model, int _fueltank, String _vin,
			long _since, int _sinceodo){
		firm=_firm;
		model=_model;
		fueltank=_fueltank;
		vin=_vin;
		since=_since;
		sinceodo=_sinceodo;
	}

		@Override
	public String toString() {
		return firm+" "+model;
	}

	@Override
	public Intent toIntent(Intent intent) {
		intent.putExtra (Table_Car.ID, id);
		intent.putExtra (Table_Car.FIRM, firm);
		intent.putExtra (Table_Car.MODEL, model);
		intent.putExtra (Table_Car.FUELTANK, fueltank);
		intent.putExtra (Table_Car.VIN, vin);
		intent.putExtra (Table_Car.SINCE, since);
		intent.putExtra (Table_Car.SINCEODO, sinceodo);
		return intent;
	}

	
	}
