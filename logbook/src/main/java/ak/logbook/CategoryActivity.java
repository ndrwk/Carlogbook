package ak.logbook;


/**
 * Created by Drew on 26.05.13.
 */


import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;


public class CategoryActivity extends ActionBarActivity implements TextWatcher {

    EditText eT_category;
//    TextView tVicon;
    Spinner CategorySpinner;
    String category = null;
//    int id, iconId, tmpIcon, color;
    int id, iconId, color;
    boolean isDirty, add = false;
    ActionBar bar;
    MenuItem btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_category);
        bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayHomeAsUpEnabled(true);
        eT_category = (EditText) findViewById(R.id.CategoryName);
        CategorySpinner = (Spinner) findViewById(R.id.CategorySpinner);
        Intent intent = getIntent();
        ArrayList <IconsEnum> arr = Icon.getArrayList();
        IconAdapter mIconAdapter = new IconAdapter(this,R.layout.l_icon, arr);
        CategorySpinner.setAdapter(mIconAdapter);
        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=iconId){
                    Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                    iconId = position;
                    if (!isDirty) {
                        isDirty=true;
                        supportInvalidateOptionsMenu();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (Main.enableLog) Log.w("ak.Log", "CategoryActivity. received Intent with action " +
                intent.getAction());
        if (intent.getAction().equals(Main.ACTION_EDIT)){
            id = intent.getIntExtra(Table_Category.ID,0);
            category = intent.getStringExtra(Table_Category.CATEGORY);
            iconId = intent.getIntExtra(Table_Category.ICON, 0);
            color = intent.getIntExtra(Table_Category.COLOR, 0);
            eT_category.setText(category);
            CategorySpinner.setBackgroundColor(color);
            CategorySpinner.setSelection(iconId);
            bar.setTitle(category);
        }
        if (intent.getAction().equals(Main.ACTION_ADD)){
            add =true;
            bar.setTitle(R.string.newcategory);
            color = -1000;
        }
        eT_category.addTextChangedListener(this);
        if (Main.enableLog) Log.w("ak.Log", "onCreate CategoryActivity");

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
//            iconId = tmpIcon;
            if (eT_category.getText().toString().length()==0) {
                Toast.makeText(this, "Некорректные данные", Toast.LENGTH_LONG).show();
                if (eT_category.getText().toString().length()==0) { eT_category.setBackgroundResource(R.color.redColor);}
            }
            else {
                if (Main.enableLog)
                    Log.w("ak.Log", "CategoryActivity c изменениями");
                Intent answerIntent = new Intent ();
                answerIntent.putExtra (Table_Category.ID, id);
                answerIntent.putExtra (Table_Category.CATEGORY, eT_category.getText().toString());
                answerIntent.putExtra (Table_Category.ICON, iconId);
                answerIntent.putExtra(Table_Category.COLOR, color);
                if (add){
                    answerIntent.setAction(Main.ACTION_ADD);
                    if (Main.enableLog)
                        Log.w("ak.Log", "CategoryActivity ACTION_ADD");
                } else {
                    answerIntent.setAction(Main.ACTION_EDIT);
                }
                setResult(RESULT_OK, answerIntent);
                finish();
            }
        } else {
            if (Main.enableLog)
                Log.w("ak.Log", "CategoryActivity без изменений");
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
        iconId=savedInstanceState.getInt("icon");
        category=savedInstanceState.getString("category");
        isDirty=savedInstanceState.getBoolean("isDirty");
        add=savedInstanceState.getBoolean("add");
        color=savedInstanceState.getInt("color");
        if (Main.enableLog)
            Log.d("ak.Log", "onRestoreInstanceState CategoryActivity");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", id);
        outState.putInt("icon", iconId);
        outState.putString("category", category);
        outState.putBoolean("isDirty", isDirty);
        outState.putBoolean("add", add);
        outState.putInt("color", color);
        if (Main.enableLog)
            Log.d("ak.Log", "onSaveInstanceState CategoryActivity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.itemmenu, menu);
        btnSave = menu.findItem(R.id.menu_save);
        if (!isDirty) btnSave.setVisible(false);
        return true;
    }


    public void onClickbtncolor(View v) {
        int initialValue = 0xFF000000;
        if (!add) initialValue = color;
        final ColorPickerDialog colorDialog = new ColorPickerDialog(this, initialValue);
        colorDialog.setAlphaSliderVisible(false);
        colorDialog.setTitle("Цвет категории");
        colorDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                color = colorDialog.getColor();
                isDirty = true;
                CategorySpinner.setBackgroundColor(color);
                supportInvalidateOptionsMenu();
                    Toast.makeText(CategoryActivity.this, "Selected Color: " + color, Toast.LENGTH_LONG).show();
            }
        });

        colorDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing
            }
        });

        colorDialog.show();
    }


}

