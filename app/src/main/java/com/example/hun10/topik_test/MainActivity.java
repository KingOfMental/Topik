package com.example.hun10.topik_test;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private EditText E1;
    private TextView T1;
    private Button B1;
    private Mylistner li;

    public static final String PACKAGE_DIR = "/data/data/com.example.hun10.topik_test/";
    public static final String DATABASE_NAME="test.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        li = new Mylistner();
        B1= (Button)findViewById(R.id.BT);
        E1 = (EditText)findViewById(R.id.Search);
        T1 = (TextView)findViewById(R.id.Result);
        initialize(getApplicationContext());

        B1.setOnClickListener(li);

    }
    public static void initialize(Context ctx)
    {
        File folder = new File(PACKAGE_DIR + "databases");
        folder.mkdirs();
        Log.i("aa",""+folder.exists());
        File outfile = new File(PACKAGE_DIR + "databases/test.db");
        Log.i("aa", PACKAGE_DIR + "databases/test.db");

        if(outfile.length()<=0)
        {
            AssetManager assetManager = ctx.getResources().getAssets();
            try{
                InputStream is = assetManager.open(DATABASE_NAME,AssetManager.ACCESS_BUFFER);
                long filesize = is.available();
                byte [] tempdata = new byte[(int)filesize];
                Log.i("aa", ""+filesize);

                is.read(tempdata);
                is.close();

                Log.i("aa", ""+outfile.exists());

                if(!outfile.exists())
                outfile.createNewFile();

                FileOutputStream fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    class Mylistner implements View.OnClickListener {
        public void onClick(View v)
        {

            String str = E1.getText().toString();
            if(str==null)
                Toast.makeText(getApplicationContext(),"키워드를 입력하세요.", Toast.LENGTH_SHORT).show();
            else {
                try {
                    SQLiteDatabase db = openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
                    Cursor cur = db.rawQuery("SELECT Grade FROM testdb where \"Index\"=\"" + str + "\";", null);
                    Log.i("topik", "" + cur.getCount());
                    cur.moveToFirst();
                    Log.i("topik", "" + cur.getString(0));
                    T1.setText(cur.getString(0));
                } catch (Exception e) {
                    Log.i("topik", "" + e.toString());
                }
            }
        }
    }
}
