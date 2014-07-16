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



public class PartActivity extends ActionBarActivity implements TextWatcher {

    EditText eT_partName;
    EditText eT_partNumber;
    String partName = null;
    String partNumber = null;
    int id;
    boolean isDirty, add = false;
    ActionBar bar;
    MenuItem btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_part);
        bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayHomeAsUpEnabled(true);
        eT_partName = (EditText) findViewById(R.id.PartName);
        eT_partNumber = (EditText) findViewById(R.id.PartNumber);
        Intent intent = getIntent();
        if (Main.enableLog) Log.w("ak.Log", "PartActivity received the Intent with action " +
                intent.getAction());
        if (intent.getAction().equals(Main.ACTION_EDIT)){
            id = intent.getIntExtra(Table_Part.ID,0);
            partName = intent.getStringExtra(Table_Part.PART);
            partNumber = intent.getStringExtra(Table_Part.PARTNUMBER);
            eT_partName.setText(partName);
            eT_partNumber.setText(partNumber);
            bar.setTitle(partName);
        }
        if (intent.getAction().equals(Main.ACTION_ADD)){
            add =true;
            bar.setTitle(R.string.newpart);
        }
        eT_partName.addTextChangedListener(this);
        eT_partNumber.addTextChangedListener(this);
        if (Main.enableLog) Log.w("ak.Log", "onCreate PartActivity");

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
            if ((eT_partName.getText().toString().length()==0)) {
                Toast.makeText(this, "Некорректные данные", Toast.LENGTH_LONG).show();
                if (eT_partName.getText().toString().length()==0) { eT_partName.setBackgroundResource(R.color.redColor);}
//                if (eT_partNumber.getText().toString().length()==0) { eT_partNumber.setBackgroundResource(R.color.redColor);}
            }
            else {
                if (Main.enableLog)
                    Log.w("ak.Log", "PartActivity c изменениями");
                Intent answerIntent = new Intent ();
                answerIntent.putExtra (Table_Part.ID, id);
                answerIntent.putExtra (Table_Part.PART, eT_partName.getText().toString());
                answerIntent.putExtra (Table_Part.PARTNUMBER, eT_partNumber.getText().toString());
//                answerIntent.putExtra (Table_Part.CATEGORY, category);
                if (add){
                    answerIntent.setAction(Main.ACTION_ADD);
                    if (Main.enableLog)
                        Log.w("ak.Log", "PartActivity ACTION_ADD");
                } else {
                    answerIntent.setAction(Main.ACTION_EDIT);
                }
                setResult(RESULT_OK, answerIntent);
                finish();
            }
        } else {
            if (Main.enableLog)
                Log.w("ak.Log", "PartActivity без изменений");
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
        partName=savedInstanceState.getString("partName");
        partNumber=savedInstanceState.getString("partNuber");
//        category=savedInstanceState.getInt("category");
        isDirty=savedInstanceState.getBoolean("isDirty");
        add=savedInstanceState.getBoolean("add");
        if (Main.enableLog)
            Log.d("ak.Log", "onRestoreInstanceState PartActivity");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", id);
        outState.putString("partName", partName);
        outState.putString("partNumber", partNumber);
//        outState.putInt("category", category);
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
