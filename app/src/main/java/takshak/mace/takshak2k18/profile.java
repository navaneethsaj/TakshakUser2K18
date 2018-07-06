package takshak.mace.takshak2k18;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class profile extends AppCompatActivity {
    TextView name,phone,rank,notificationBox,readmore;
    LinearLayout notificationlayout;
    boolean ismessageexpanded = false;
    String url = "https://us-central1-takshakapp18.cloudfunctions.net/getnotification";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        rank=(TextView)findViewById(R.id.rank);
        name=(TextView)findViewById(R.id.name);
        phone=(TextView)findViewById(R.id.number);
        rank.setText("unknown");
        name.setText("unknown");
        phone.setText("unknown");

        notificationBox = findViewById(R.id.notificationBox);
        notificationlayout = findViewById(R.id.notificationLayout);
        readmore = findViewById(R.id.readmore);

        //Execute only when internet connection is LIVE
        new MyAsyncTask().execute(url);

        notificationBox.setMaxLines(2);
        notificationlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ismessageexpanded == false){
                    notificationBox.setMaxLines(100);
                    readmore.setText("...Show less");
                    ismessageexpanded = true;
                }else {
                    notificationBox.setMaxLines(2);
                    readmore.setText("...Show more");
                    ismessageexpanded = false;
                }
            }
        });
    }
    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                notificationlayout.setVisibility(View.VISIBLE);
                notificationBox.setVisibility(View.VISIBLE);
                notificationBox.setText(s);
            }else {
                notificationlayout.setVisibility(View.GONE);
                notificationBox.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notificationlayout.setVisibility(View.GONE);
        }
    }
}
