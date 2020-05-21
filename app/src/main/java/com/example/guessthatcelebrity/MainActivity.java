package com.example.guessthatcelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    String result=null;
    ImageView image;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    int answer_location, link_location;
    int number=0;
    final String link="http://www.posh24.se/kandisar";
    ArrayList<String> name=new ArrayList<String>();
    ArrayList<String> linklist=new ArrayList<String>();
    ArrayList<String> pos=new ArrayList<String>();
    downloadImage newimage=null;
    Bitmap image1;


    //Pattern recognition
    public void regex(String in, String pattern, ArrayList<String>s){
        Pattern p= Pattern.compile(pattern);
        Matcher m= p.matcher(in);

        String temp=null;
        while (m.find()){
            temp=m.group(1);

            Log.i("infoo", temp);
            s.add(temp);
        }
    }

    //main layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        image = findViewById(R.id.image);

        downloadhtml task = new downloadhtml();


        try {
            result = task.execute(link).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        regex(result, "alt=\"(.*?)\"/>", name);
        regex(result, "<img src=\"(.*?)\"", linklist);

        setText();
        setImage();


    }



    //find random text
    public void setText(){
        Random rand=new Random();
        Log.i("SIZEE", Integer.toString(name.size()));
        int x=rand.nextInt(name.size());
        link_location=x;
        answer_location= rand.nextInt(4);
        pos.clear();
        for(int i=0; i<4; i++){
            if(i==answer_location){
             pos.add(name.get(x));
            }
            else{
                int y= rand.nextInt(name.size());
                if(y==x){
                    while(y!=x){
                        y=rand.nextInt(name.size());
                    }
                }
                pos.add(name.get(y));
            }
        }
          button1.setText(pos.get(0));
          button2.setText(pos.get(1));
          button3.setText(pos.get(2));
          button4.setText((pos.get(3)));
    }


  //button
  public void click(View view){
        Button b=(Button) view;

        if(view.getTag().toString().equals(Integer.toString(answer_location))){
            Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "WRONG!", Toast.LENGTH_SHORT).show();
        }
        number+=1;

      setText();
      setImage();
    }




  public  void setImage(){

        newimage=null;
        newimage=new downloadImage();
     Log.i("LINKKKKKKK","COMES HERE");
      try {
           image1 = newimage.execute(linklist.get(link_location)).get();
           Log.i("LINKKKKKK", linklist.get(link_location));
          image.setImageBitmap(image1);
      }catch (Exception e){
          e.printStackTrace();
          Log.i("LINKKKKKK", linklist.get(link_location));
      }

  }


   //donwloading
    public class downloadhtml extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... html) {

            String out=null;

            try {
                URL url = new URL(html[0]);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                InputStream in = http.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);
                int data=reader.read();

                while(data!=-1){
                    char current=(char)data;
                    out+=current;
                    data=reader.read();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
            return out;
        }
    }


    //image downloader
    public class downloadImage extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap myimage;

            try{
                URL url=new URL(strings[0]);
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in= connection.getInputStream();
                myimage= BitmapFactory.decodeStream(in);
                connection.disconnect();
                return myimage;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }


        }
    }
}
