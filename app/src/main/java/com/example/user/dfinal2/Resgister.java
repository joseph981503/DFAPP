package com.example.user.dfinal2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Resgister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister);



        final Button button = findViewById(R.id.R_submit);
//        Register-----------------------------------------------------------------------------
        button.setOnClickListener((View v)-> {
            // Code here executes on main thread after user presses button
            RadioGroup rg3 = (RadioGroup) findViewById(R.id.test);
            try {
                final String check =
                        ((RadioButton)findViewById(rg3.getCheckedRadioButtonId()))
                                .getText().toString();
            }catch (Exception e){
                Toast toast = Toast.makeText(Resgister.this, "請閱讀隱私條款", Toast.LENGTH_LONG);
                toast.show();
            }


            EditText U_Email=(EditText)findViewById(R.id.U_Email);
            EditText U_Pwd=(EditText)findViewById(R.id.U_Pwd);
            EditText U_Pwd1=(EditText)findViewById(R.id.U_Pwd1);
            EditText U_Name=(EditText)findViewById(R.id.U_Name);
            EditText U_Phone  =(EditText)findViewById(R.id.U_Phone  );
            RadioGroup rg = (RadioGroup) findViewById(R.id.U_Gender);
            final String U_Gender = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
            EditText U_Birth=(EditText)findViewById(R.id.U_Birth);
            RadioGroup rg1 = (RadioGroup) findViewById(R.id.U_Info);
            final String U_Info = ((RadioButton)findViewById(rg1.getCheckedRadioButtonId())).getText().toString();
            if(U_Pwd.getText().toString().equals(U_Pwd1.getText().toString())){
                Map<String,String> data=new HashMap<>();
                data.put("U_Email",U_Email.getText().toString());
                data.put("U_Pwd",U_Pwd.getText().toString());
                data.put("U_Name",U_Name.getText().toString());
                data.put("U_Phone",U_Phone.getText().toString());
                data.put("U_Gender",U_Gender);
                data.put("U_Birth",U_Birth.getText().toString());
                data.put("U_Info",U_Info);
                String url="http://140.119.163.195/DFapp/Register.php";
                try {
                    post(url,data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Toast toast = Toast.makeText(Resgister.this, "密碼不一致！", Toast.LENGTH_LONG);
                toast.show();
            }

        });

    }
    private String post(String url1,Map<String,String> data) throws Exception{
        String sData="";
        String ID="";
        String PWD="";
        URL url=new URL(url1);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        for(Map.Entry<String,String > m :data.entrySet()){
            sData+= URLEncoder.encode(m.getKey(), "UTF-8")+"="+URLEncoder.encode(m.getValue(), "UTF-8")+"&";
            System.out.println("SData Res=\t"+sData);
        }
        String finalSData = sData;
        ExecutorService pool = Executors.newFixedThreadPool(10);

        pool.execute(()->{
            try
            {
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( finalSData );
                wr.flush();
                // Get the server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }
                String rlt=sb.toString();
                System.out.println("rlt=\t"+rlt);
                if(rlt.equals("false")){
                    runOnUiThread(()-> {

                        Toast.makeText(Resgister.this,"帳號密碼錯誤",Toast.LENGTH_LONG).show();

                    });
                }else{
                    runOnUiThread(()-> {
                        Toast.makeText(Resgister.this,"成功",Toast.LENGTH_LONG).show();
                    });
                }
                System.out.println("Test=\t"+sb.toString());
                reader.close();
            }
            catch(Exception ex)
            {   }
        });
        return "ok";
    }

}
