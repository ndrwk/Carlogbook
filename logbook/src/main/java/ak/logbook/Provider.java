package ak.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class Provider extends Item {

    private String name, address, phone = null;

    public Provider(Context cntxt) {
		RowLayout = R.layout.l_provider;
        menuLevel=1;
        Table_Provider table_provider = new Table_Provider();
        columns = table_provider.COLUMNS;
        tableName = table_provider.TableName;
        context = cntxt;

    }

	public ContentValues getCV() {
		ContentValues cv = new ContentValues();
		cv.put(Table_Provider.NAME, name);
		cv.put(Table_Provider.PHONE, phone);
		cv.put(Table_Provider.ADDRESS, address);
		return cv;
	}

	public View inflateView(View v) {
		TextView tt = (TextView) v.findViewById(R.id.providername);
		TextView mt = (TextView) v.findViewById(R.id.providerphone);
		TextView bt = (TextView) v.findViewById(R.id.provideraddress);
		if (tt != null) {
			tt.setText(name);
		}
		if (mt != null) {
			mt.setText(phone);
		}
		if (bt != null) {
			bt.setText(address);
		}
		return v;
	}

	public String getAddress() {
		return address;
	}


	public String getPhone() {
		return phone;
	}

	@Override
	public void setItem(Cursor cursor) {
		id = cursor.getInt(cursor.getColumnIndex(Table_Provider.ID));
		name = cursor.getString(cursor.getColumnIndex(Table_Provider.NAME));
		phone = cursor.getString(cursor.getColumnIndex(Table_Provider.PHONE));
		address = cursor.getString(cursor.getColumnIndex(Table_Provider.ADDRESS));
	}

	@Override
	public void setItem(Intent intent) {
        id = intent.getIntExtra(Table_Provider.ID, 0);
		name = intent.getStringExtra(Table_Provider.NAME);
		phone = intent.getStringExtra(Table_Provider.PHONE);
		address = intent.getStringExtra(Table_Provider.ADDRESS);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Intent toIntent(Intent intent) {
        intent.putExtra (Table_Provider.ID, id);
        intent.putExtra (Table_Provider.NAME, name);
        intent.putExtra (Table_Provider.ADDRESS, address);
        intent.putExtra (Table_Provider.PHONE, phone);
        return intent;


	}

}
