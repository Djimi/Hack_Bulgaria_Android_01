package com.example.damyan.expenselist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ScaleDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity implements AdapterView.OnItemLongClickListener {

    private MyAdapter adapter;

    List<LabelToPricePair> items;

    private ListView listView;

    EditText label;
    EditText price;

    ExpenseListOpenHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        adapter = new MyAdapter();

        items = new ArrayList<LabelToPricePair>();

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        label = (EditText)findViewById(R.id.labelField);
        price = (EditText)findViewById(R.id.priceField);

        db = new ExpenseListOpenHelper(this);

        AsyncTask<Void, Void, Void> asyncTask = new GetAsyncTask();
        asyncTask.execute();

    }

    public void addButtonClicked(View v){
        if(TextUtils.isEmpty(label.getText().toString()) || TextUtils.isEmpty(price.getText().toString())) {
            return;
        }

        final LabelToPricePair pair = makePairFromUserInput();

        new Thread(new Runnable() {
            @Override
            public void run() {
                db.addExpense(pair.price, pair.label);
            }
        }).start();

        items.add(pair);
        resetPriceAndLabelFields();

        adapter.notifyDataSetInvalidated();
    }

    private LabelToPricePair makePairFromUserInput() {
        String tempLabel = label.getText().toString();
        double tempPrice = Double.parseDouble(price.getText().toString());
        LabelToPricePair pair = new LabelToPricePair(tempLabel, tempPrice);

        return pair;
    }

    private void resetPriceAndLabelFields() {
        label.setText("");
        price.setText("");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if(TextUtils.isEmpty(label.getText().toString()) || TextUtils.isEmpty(price.getText().toString())){
            return true;
        }

        final LabelToPricePair pair = (LabelToPricePair) parent.getAdapter().getItem(position);


        final LabelToPricePair pairForDelete = new LabelToPricePair(pair.label, pair.price);
        final LabelToPricePair newPair = makePairFromUserInput();
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.update(pairForDelete, newPair);
            }
        }).start();

        pair.label = newPair.label;
        pair.price = newPair.price;
        resetPriceAndLabelFields();
        adapter.notifyDataSetInvalidated();

        return true;
    }

    private class MyAdapter extends BaseAdapter {


        public void addAll(List<LabelToPricePair> pairs){
            items.addAll(pairs);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            View view = null;

            if(convertView == null){
                view = getLayoutInflater().inflate(R.layout.adapter_item, null);

                viewHolder = new ViewHolder();

                viewHolder.name = (TextView)view.findViewById(R.id.nameFieldItem);
                viewHolder.price = (TextView)view.findViewById(R.id.priceFieldItem);


                view.setTag(viewHolder);

            } else {
                view = convertView;
                viewHolder = (ViewHolder)(view.getTag());
            }


            viewHolder.name.setText(items.get(position).label);
            viewHolder.price.setText("" + items.get(position).price);
            viewHolder.position = position;



            ((ImageButton)view.findViewById(R.id.removeButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(MyActivity.this)
                            .setIcon(new ScaleDrawable(getResources().getDrawable(R.drawable.jerry), 0, 0.5f, 0.5f).getDrawable())
                            .setTitle("Action chooser")
                            .setMessage("Are you sure you want to delete this thing ???")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final LabelToPricePair p = (LabelToPricePair)getItem(position);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            db.delete(p.label, p.price);
                                        }
                                    }).start();

                                    items.remove(position);
                                    adapter.notifyDataSetInvalidated();
                                }
                            })
                            .setNegativeButton("No", null)
                            .create()
                            .show();
                }
            });

            return view;
        }
    }

    private class ViewHolder {
        TextView name;
        TextView price;

        int position;
    }

    public static class LabelToPricePair{
        String label;
        double price;

        public LabelToPricePair(String label, double price) {
            this.label = label;
            this.price = price;
        }
    }

    private class GetAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            adapter.addAll(db.getCurrentItems(adapter));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetInvalidated();
        }
    }

}
