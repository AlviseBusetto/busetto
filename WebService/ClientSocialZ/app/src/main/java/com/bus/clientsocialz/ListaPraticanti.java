package com.bus.clientsocialz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by BUS on 16/05/2018.
 */

public class ListaPraticanti extends AppCompatActivity {
    private static final String SOAP_ACTION = "";
    private static final String METHOD_NAME = "getHobbiesConPraticanti";
    private static final String NAMESPACE = "http://ws/";
    private static final String URL = "http://10.0.2.2:8080/WebServiceSocialZ/WebServiceSocialZ?WSDL";

    private ScrollView scrollView;
    private Context context;
    private Button btn;
    static String hobby;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praticanti);
        hobby = getIntent().getExtras().getString("Hobby");
        btn = findViewById(R.id.tornaRicerca);
        btn.setOnClickListener(View -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        });
        new AsyncTaskRunner(this).execute(hobby);
        scrollView = (ScrollView) findViewById(R.id.scrollUtenti);
        context=this;
        new AsyncTaskRunner(this).execute(hobby);

    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String> {

        private Activity activity;

        public AsyncTaskRunner(Activity context) {
            activity = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("hobby", strings[0]);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);

                HttpTransportSE ht = new HttpTransportSE(URL);
                try {
                    ht.call(SOAP_ACTION, envelope);
                    SoapObject response = (SoapObject) envelope.bodyIn;

                    return response.getProperty("return").toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            ArrayList<String> praticanti = new ArrayList<>();
            scrollView.removeAllViews();
            String costruzione = "";
            for (int i = 0; i < result.length(); i++){
                if(result.charAt(i) != '{' && result.charAt(i) != '}'){
                    costruzione += result.charAt(i);
                } else if(result.charAt(i) == '}'){
                    praticanti.add(costruzione);
                    costruzione = "";
                }
            }
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            params.setMargins(40, 10, 10, 10);
            for (String t: praticanti){
                System.out.println(t);
                TextView tv = new TextView(context);
                tv.setLayoutParams(params);
                tv.setText(t);
                linearLayout.addView(tv);
            }
            scrollView.addView(linearLayout);
        }
    }
}
