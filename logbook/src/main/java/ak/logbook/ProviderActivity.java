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
import android.widget.*;



public class ProviderActivity extends ActionBarActivity implements TextWatcher {

    EditText eT_providerName, eT_providerAddress, eT_providerPhone;
    String providerName, providerAddress, providerPhone = null;
    int id;
    boolean isDirty, add = false;
    ActionBar bar;
    MenuItem btnSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_provider);
        bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayHomeAsUpEnabled(true);
        eT_providerName = (EditText) findViewById(R.id.ProviderName);
        eT_providerAddress = (EditText) findViewById(R.id.ProviderAddress);
        eT_providerPhone = (EditText) findViewById(R.id.ProviderPhone);
        Intent intent = getIntent();
        if (Main.enableLog) Log.w("ak.Log", "ProviderActivity received the Intent with action " +
                intent.getAction());
        if (intent.getAction().equals(Main.ACTION_EDIT)){
            id = intent.getIntExtra(Table_Part.ID,0);
            providerName = intent.getStringExtra(Table_Provider.NAME);
            providerAddress = intent.getStringExtra(Table_Provider.ADDRESS);
            providerPhone = intent.getStringExtra(Table_Provider.PHONE);
            eT_providerName.setText(providerName);
            eT_providerAddress.setText(providerAddress);
            eT_providerPhone.setText(providerPhone);
            bar.setTitle(providerName);
        }
        if (intent.getAction().equals(Main.ACTION_ADD)){
            add =true;
            bar.setTitle(R.string.newprovider);
        }
        eT_providerName.addTextChangedListener(this);
        eT_providerAddress.addTextChangedListener(this);
        eT_providerPhone.addTextChangedListener(this);
        if (Main.enableLog) Log.w("ak.Log", "onCreate ProviderActivity");

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
            if ((eT_providerName.getText().toString().length()==0)) {
                Toast.makeText(this, "Некорректные данные", Toast.LENGTH_LONG).show();
                if (eT_providerName.getText().toString().length()==0) { eT_providerName.setBackgroundResource(R.color.redColor);}
            }
            else {
                if (Main.enableLog)
                    Log.w("ak.Log", "ProviderActivity c изменениями");
                Intent answerIntent = new Intent ();
                answerIntent.putExtra (Table_Provider.ID, id);
                answerIntent.putExtra (Table_Provider.NAME, eT_providerName.getText().toString());
                answerIntent.putExtra (Table_Provider.ADDRESS, eT_providerAddress.getText().toString());
                answerIntent.putExtra(Table_Provider.PHONE, eT_providerPhone.getText().toString());
                if (add){
                    answerIntent.setAction(Main.ACTION_ADD);
                    if (Main.enableLog)
                        Log.w("ak.Log", "ProviderActivity ACTION_ADD");
                } else {
                    answerIntent.setAction(Main.ACTION_EDIT);
                }
                setResult(RESULT_OK, answerIntent);
                finish();
            }
        } else {
            if (Main.enableLog)
                Log.w("ak.Log", "ProviderActivity без изменений");
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        id=savedInstanceState.getInt("id");
        providerName=savedInstanceState.getString("providerName");
        providerAddress =savedInstanceState.getString("providerAddress");
        providerPhone=savedInstanceState.getString("providerPhone");
        isDirty=savedInstanceState.getBoolean("isDirty");
        add=savedInstanceState.getBoolean("add");
        if (Main.enableLog)
            Log.d("ak.Log", "onRestoreInstanceState ProviderActivity");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", id);
        outState.putString("providerName", providerName);
        outState.putString("providerAddress", providerAddress);
        outState.putString("providerPhone", providerPhone);
        outState.putBoolean("isDirty", isDirty);
        outState.putBoolean("add", add);
        if (Main.enableLog)
            Log.d("ak.Log", "onSaveInstanceState PartActivity");
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
