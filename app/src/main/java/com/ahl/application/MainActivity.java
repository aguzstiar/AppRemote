package com.ahl.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ahl.appremote.AppRemote;
import com.ahl.appremote.NewData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      /*
      AppRemote.init(this, "https://barisanintelek.blogspot.com/2023/04/aplikasi-android.html", new NewData() {
         @Override
         public void onSuccess(String jsonData) {
            JSONObject jsonObj = null;
            try {
               jsonObj = new JSONObject(jsonData);
               JSONArray contacts = jsonObj.getJSONArray("Ads");
               for (int i = 0; i < contacts.length(); i++) {
                  JSONObject c = contacts.getJSONObject(i);
                  Log.e("CHECK LOG", c.getString("select_main_ads"));
               }
            } catch (JSONException e) {
               e.printStackTrace();
            }
         }
      });
       */
   }
}