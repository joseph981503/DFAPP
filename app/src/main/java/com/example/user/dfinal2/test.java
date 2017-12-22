package com.example.user.dfinal2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = this.getIntent();
        ArrayList<String>a=intent.getStringArrayListExtra("User");
        Map<String,String> map=new HashMap<>();

        for (String str: a) {
            System.out.println("DATA=\t"+str);
            String []items=str.split(":");
            map.put(items[0],items[1]);
        }

        DBsqlite db=new DBsqlite(this);

        DBsqlite dBsqlite= new DBsqlite(this);
        Map<String,String>map1=new HashMap<>();
        ArrayList a1=dBsqlite.getAll(dBsqlite,map);
        if(a1.size()==0){
            db.insert(db,map);
        }
//        db.delete(db,3);

    }

}
