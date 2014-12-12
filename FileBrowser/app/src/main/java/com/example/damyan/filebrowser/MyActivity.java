package com.example.damyan.filebrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MyActivity extends Activity {

    ListView listView;
    MyAdapter adapter;
    List<String> filesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        listView = (ListView)findViewById(R.id.listView);

        adapter = new MyAdapter(this, Environment.getExternalStorageDirectory());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = (File)listView.getItemAtPosition(position);



                if(file.isDirectory()) {
                    adapter.setCurrentDirectory(file);
                } else if(file.isFile()){

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    MimeTypeMap map = MimeTypeMap.getSingleton();
                    String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
                    String type = map.getMimeTypeFromExtension(ext);

                    if (type == null) {
                        type = "*/*";
                    }

                    Uri data = Uri.fromFile(file);

                    intent.setDataAndType(data, type);

                    if(intent.resolveActivity(getPackageManager()) != null){
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                        builder.setMessage("There is no appropriate app ! You can download one (in case this didn`t cross your mind) !")
                                .setIcon(getResources().getDrawable(R.drawable.ic_launcher))
                                .setNeutralButton("Back to file manager", null)
                                .create().show();

                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed() {

        if(adapter.getCurrentDirectory().getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            finish();
            return;
        }

        adapter.setCurrentDirectory(adapter.getCurrentDirectory().getParentFile());
        adapter.notifyDataSetInvalidated();
    }

    public class MyAdapter extends ArrayAdapter<File>{

        private File currentDirectory;
        private List<File> files;

        public MyAdapter(Context context, File currentDirectory) {
            super(context, android.R.layout.simple_list_item_1);

            this.currentDirectory = currentDirectory;
            files = new ArrayList<File>(Arrays.asList(currentDirectory.listFiles()));
            addAll(currentDirectory.listFiles());
            notifyDataSetInvalidated();


        }

        public void setCurrentDirectory(File file){


            if(!file.isDirectory()) throw new IllegalArgumentException("Argument should be a directory!");

            currentDirectory = file;
            files = new ArrayList<File>(Arrays.asList(currentDirectory.listFiles()));

            clear();
            addAll(currentDirectory.listFiles());

        }

        public File getCurrentDirectory(){
            return currentDirectory;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            MyAdapterHolder holder = null;

            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item, null);

                holder = new MyAdapterHolder();

                holder.textView = (TextView)convertView.findViewById(R.id.text);

                convertView.setTag(holder);

            } else {
                holder = (MyAdapterHolder)convertView.getTag();
            }

            holder.textView.setText(files.get(position).getName());
            int color = files.get(position).isDirectory() ? Color.CYAN : Color.BLACK;
            holder.textView.setTextColor(color);

            return convertView;
        }

        private class MyAdapterHolder{
            private TextView textView;
        }
    }
}
