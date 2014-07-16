package ak.logbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;


public class CarActivity extends ActionBarActivity implements TextWatcher{
	
	EditText eT_firm, eT_model, eT_fueltank, eT_VIN, eT_sinceodo;
	Date dNow;
	String firm, model, vin = null;
	int id, fueltank, sinceodo = 0;
	long since;
	boolean isDirty, addCar = false;
    ActionBar bar;
    MenuItem btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_car);
        bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayHomeAsUpEnabled(true);
        eT_firm = (EditText) findViewById(R.id.CarFirm);
		eT_model = (EditText) findViewById(R.id.CarModel);
		eT_fueltank = (EditText) findViewById(R.id.CarTank);
		eT_VIN = (EditText) findViewById(R.id.CarVIN);
		eT_sinceodo = (EditText) findViewById(R.id.CarOdometr);
		dNow = new Date( );
		Intent intent = getIntent();
		if (Main.enableLog) Log.w("ak.Log", "received Intent with action "+
				intent.getAction());
		if (intent.getAction().equals(Main.ACTION_EDIT)){
            id = intent.getIntExtra(Table_Car.ID,0);
			firm = intent.getStringExtra(Table_Car.FIRM);
			model = intent.getStringExtra(Table_Car.MODEL);
			fueltank = intent.getIntExtra(Table_Car.FUELTANK, 0);
			vin = intent.getStringExtra(Table_Car.VIN);
			since = intent.getLongExtra(Table_Car.SINCE, 0);
			sinceodo = intent.getIntExtra(Table_Car.SINCEODO, 0);
			eT_firm.setText(firm);
			eT_model.setText(model);
			eT_fueltank.setText(fueltank+"");
			eT_VIN.setText(vin);
			eT_sinceodo.setText(sinceodo+"");
            bar.setTitle(firm);
            bar.setSubtitle(model);
		}
        if (intent.getAction().equals(Main.ACTION_ADD)){
		    addCar=true;
            bar.setTitle(R.string.newcar);
        }
		eT_firm.addTextChangedListener(this);
		eT_model.addTextChangedListener(this);
		eT_fueltank.addTextChangedListener(this);
		eT_VIN.addTextChangedListener(this);
		eT_sinceodo.addTextChangedListener(this);
		if (Main.enableLog) Log.w("ak.Log", "onCreate CarActivity");

	}
	@Override
	public boolean onOptionsItemSelected (MenuItem menuItem) {
		switch (menuItem.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_save:
            if (isDirty) {
	    		if (Main.enableLog)
		    		Log.w("ak.Log", "isDirty="+isDirty);
			    if (eT_firm.getText().toString().length()==0 | eT_model.getText().toString().length()==0 |
					eT_fueltank.getText().toString().length()==0 |
					eT_sinceodo.getText().toString().length()==0) {
				    Toast.makeText(this, "Некорректные данные",	Toast.LENGTH_LONG).show();
				    if (eT_firm.getText().toString().length()==0) { eT_firm.setBackgroundResource(R.color.redColor);}
				    if (eT_model.getText().toString().length()==0) { eT_model.setBackgroundResource(R.color.redColor);}
    				if (eT_fueltank.getText().toString().length()==0) { eT_fueltank.setBackgroundResource(R.color.redColor);}
	    			if (eT_sinceodo.getText().toString().length()==0) { eT_sinceodo.setBackgroundResource(R.color.redColor);}
			    }
			    else {
			    	if (Main.enableLog)
				    	Log.w("ak.Log", "CarActivity c изменениями");
				    Intent answerIntent = new Intent ();
		    		answerIntent.putExtra (Table_Car.ID, id);
		    		answerIntent.putExtra (Table_Car.FIRM, eT_firm.getText().toString());
		    		answerIntent.putExtra (Table_Car.MODEL, eT_model.getText().toString());
		    		answerIntent.putExtra (Table_Car.FUELTANK, Integer.parseInt(eT_fueltank.getText().toString()));
		    		answerIntent.putExtra (Table_Car.VIN, eT_VIN.getText().toString());
			    	if (addCar){
                      answerIntent.putExtra (Table_Car.SINCE, dNow.getTime());
                      answerIntent.setAction(Main.ACTION_ADD);
                      if (Main.enableLog)
                        Log.w("ak.Log", "CarActivity ACTION_ADD");
                    } else {
                        answerIntent.setAction(Main.ACTION_EDIT);
                }
				answerIntent.putExtra (Table_Car.SINCEODO, Integer.parseInt(eT_sinceodo.getText().toString()));
				setResult(RESULT_OK, answerIntent);
				finish();
				}
	    	} else {
			    if (Main.enableLog)
				    Log.w("ak.Log", "CarActivity без изменений");
			setResult(RESULT_CANCELED);
			finish();
            }
            return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

	@Override
	public void afterTextChanged(Editable s) {
        if (!isDirty) {
            isDirty=true;
            supportInvalidateOptionsMenu();
        }
		if (Main.enableLog)
			Log.w("ak.Log", "afterTextChanged "+s);

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		if (Main.enableLog)
			Log.w("ak.Log", "beforeTextChanged "+s);

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (Main.enableLog)
			Log.w("ak.Log", "onTextChanged "+s);

	}

    @Override
    protected void onPause() {
        super.onPause();
        if (Main.enableLog)
            Log.w("ak.Log", "onPause CarActivity");
        eT_firm.removeTextChangedListener(this);
        eT_model.removeTextChangedListener(this);
        eT_fueltank.removeTextChangedListener(this);
        eT_VIN.removeTextChangedListener(this);
        eT_sinceodo.removeTextChangedListener(this);

    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    id=savedInstanceState.getInt("id");
	    fueltank=savedInstanceState.getInt("fueltank");
	    sinceodo=savedInstanceState.getInt("sinceodo");
	    firm=savedInstanceState.getString("firm");
	    model=savedInstanceState.getString("model");
	    vin=savedInstanceState.getString("vin");
	    isDirty=savedInstanceState.getBoolean("isDirty");
        addCar=savedInstanceState.getBoolean("addCar");
	    since=savedInstanceState.getLong("since");
		if (Main.enableLog)
	    Log.d("ak.Log", "onRestoreInstanceState CarActivity");
	}

	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putInt("id", id);
	    outState.putInt("fueltank", fueltank);
	    outState.putInt("sinceodo", sinceodo);
	    outState.putLong("since", since);
	    outState.putString("firm", firm);
	    outState.putString("model", model);
	    outState.putString("vin", vin);
	    outState.putBoolean("isDirty", isDirty);
        outState.putBoolean("addCar", addCar);
        if (Main.enableLog)
		    Log.d("ak.Log", "onSaveInstanceState CarActivity");
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.itemmenu, menu);
        btnSave = menu.findItem(R.id.menu_save);
        if (!isDirty) btnSave.setVisible(false);
        return true;
    }


}

