package ak.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;


public class Currency extends Item {

	public Currency(Context cntxt){
		RowLayout=R.layout.l_currency;
        menuLevel=1;
        columns = Table_Currency.COLUMNS;
        tableName = Table_Currency.TableName;
        context = cntxt;
    }


	private String currency = null;
	double price = 1;
    int icon = 0;
	
	public ContentValues getCV(){
		ContentValues cv = new ContentValues();
		cv.put(Table_Currency.CURRENCY, currency);
		cv.put(Table_Currency.PRICE, price);
        cv.put(Table_Currency.ICON, icon);
		return cv;
	}
	

	public String getCurrency (){
		return currency;
	}
	
	public double getPrice (){
		return price;
	}

 	public View inflateView (View v){
		TextView tt = (TextView) v.findViewById(R.id.currencytoptext);
		TextView bt = (TextView) v.findViewById(R.id.currencybottomtext);
        View icn = v.findViewById(R.id.view_curr_icon);
        if (icn != null){
            IconicFontDrawable icon_draw = new IconicFontDrawable(context);
            icon_draw.setIcon(Icon.getIcon(icon));
            icn.setBackgroundDrawable(icon_draw);

        }
		if (tt != null) {
			tt.setText(getCurrency());
		}
		if(bt != null){
			bt.setText("Курс "+getPrice()+" руб.");
		}
		return v;
	}

	@Override
	public void setItem(Cursor cursor) {
		id=cursor.getInt(cursor.getColumnIndex(Table_Currency.ID));
		currency=cursor.getString(cursor.getColumnIndex(Table_Currency.CURRENCY));
		price=cursor.getDouble(cursor.getColumnIndex(Table_Currency.PRICE));
        icon=cursor.getInt(cursor.getColumnIndex(Table_Currency.ICON));
    }

	@Override
	public void setItem(Intent intent) {
        id=intent.getIntExtra(Table_Currency.ID, 0);
        currency=intent.getStringExtra(Table_Currency.CURRENCY);
        price=intent.getDoubleExtra(Table_Currency.PRICE,0);
        icon=intent.getIntExtra(Table_Currency.ICON, 0);
    }

	@Override
	public String toString() {
		return getCurrency();
	}

	@Override
	public Intent toIntent(Intent intent) {
        intent.putExtra (Table_Currency.ID, id);
        intent.putExtra (Table_Currency.CURRENCY, currency);
        intent.putExtra (Table_Currency.PRICE, price);
        intent.putExtra (Table_Currency.ICON, icon);
        return intent;
    }
}
