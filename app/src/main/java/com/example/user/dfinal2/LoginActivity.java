package com.example.user.dfinal2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button log = findViewById(R.id.Login);
//        LOGIN-----------------------------------------------------------------------------

        EditText ID=(EditText)findViewById(R.id.ID);
        EditText PWD=(EditText)findViewById(R.id.PWD);
        Map<String,String>map=new HashMap<>();
//        map.put("Email","example@example.com");
////        map.put("PWD","12345");

        try{
            DBsqlite dBsqlite= new DBsqlite(this);
            ArrayList a=dBsqlite.getAll(dBsqlite,map);
            if(a.size()>0){
                String ID_keep="";
                String PWD_keep="";
                String Keep="";
                String str=a.toArray()[0].toString();
                System.out.println("str=\t"+str);
                String []str1=str.split(",");

                for(int i=0;i<str1.length;i++){
                    String str3=str1[i];
                    if (str3.contains("Email")){
                        String []str4=str3.split("=");
                        ID_keep=str4[1];
                        System.out.println("ID_keep=\t"+ID_keep);
                    }
                    if (str3.contains("PWD")){
                        String []str4=str3.split("=");
                        PWD_keep=str4[1];
                        System.out.println("PWD=\t"+PWD_keep);
                    }
                    if (str3.contains("Keep")){
                        String []str4=str3.split("=");
                        Keep=str4[1];
                        System.out.println("Keep=\t"+str4[1]);
                    }
                }
                if(Keep.equals("yes")){
                    Map<String, String> x=new HashMap<>();
                    x.put("ID",ID_keep);
                    x.put("PWD",PWD_keep);
                    String url="http://140.119.163.195/DFapp/login.php";
                    try {
                    post(url,x);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.print("Response!!!"+e);
                    }
                }

            }
        }catch (Exception e){
            System.out.println("Error=\t"+e);
        }


        log.setOnClickListener((View v)-> {
            // Code here executes on main thread after user presses button'
            Map<String, String> x=new HashMap<>();
            x.put("ID",ID.getText().toString());
            x.put("PWD",PWD.getText().toString());

            String url="http://140.119.163.195/DFapp/login.php";
            try {
                post(url,x);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("Response!!!"+e);
            }

        });
        final Button button = findViewById(R.id.Register_bt);
//        Register-----------------------------------------------------------------------------
        button.setOnClickListener((View v)-> {
                // Code here executes on main thread after user presses button
                Intent intent=new Intent(LoginActivity.this,Resgister.class);
                startActivity(intent);

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
            if(m.getKey().equals("ID")){
                ID=m.getValue();
            }else {
                PWD=m.getValue();
            }

        }
        String finalSData = sData;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        String finalPWD = PWD;
        String finalID = ID;
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
                rlt=rlt.trim();
                String rlt1[]=rlt.split(";");
                System.out.println("rlt=\t"+rlt);
                if(rlt.equals("false")){
                    runOnUiThread(()-> {

                        Toast.makeText(LoginActivity.this,"帳號密碼錯誤",Toast.LENGTH_LONG).show();

                    });

                }else{
                    String U_Name = rlt1[0];
                    Integer U_Num = Integer.valueOf(rlt1[1]);
                    ArrayList<String>a=new ArrayList<>();
                    a.add("U_Name:"+rlt1[0]);
                    a.add("U_Num:"+rlt1[1]);
                    a.add("Email:"+ finalID);
                    a.add("PWD:"+ finalPWD);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("User",a);
                    CheckBox checkBox=(CheckBox) findViewById(R.id.Logincheck);

                    if(checkBox.isChecked()){
                        runOnUiThread(()-> {
                            Toast toast = Toast.makeText(LoginActivity.this,
                                    "歡迎回來！！\t"+ U_Name, Toast.LENGTH_LONG);
                            toast.show();
                            a.add("Keep:"+"yes");
                            Intent intent = new Intent(LoginActivity.this, test.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            this.finish();
                        });

                    }else {
                        runOnUiThread(()-> {
                            Toast toast = Toast.makeText(LoginActivity.this,
                                    "歡迎回來！！\t"+ U_Name, Toast.LENGTH_LONG);
                            toast.show();
                            a.add("Keep:"+"no");
                            Intent intent = new Intent(LoginActivity.this, test.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            this.finish();
                        });
                    }

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
