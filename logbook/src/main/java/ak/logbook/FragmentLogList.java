package ak.logbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.*;


public class FragmentLogList extends ListFragment {


    public interface ClickListenerCallback {
        public void ClickCallback(int pos);
        public void LongClickCallback(int pos);
    }

    ClickListenerCallback clickListenerCallback;

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
                clickListenerCallback.LongClickCallback(position);
                getListView().setItemChecked(position, true);
                return true;
            }
        });
    }



        @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            clickListenerCallback = (ClickListenerCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ClickListenerCallback");
        }
    }


    public void dispList (ArrayAdapter <?> arrayadapter) {
//        arrayadapter = new ArrayAdapter<String>(actvty, android.R.layout.simple_list_item_activated_1, arraylist);
        setListAdapter(arrayadapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        clickListenerCallback.ClickCallback(position);
        getListView().setItemChecked(position, true);
    }

}
