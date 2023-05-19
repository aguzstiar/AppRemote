package com.ahl.appremote;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.util.Base64;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AppRemote {

   private static String cAppStatus;
   private static String cJson;

   public static void init(Activity activity, String linkBlog, NewData newData) {
      new Thread(new Runnable() {
         @Override
         public void run() {
            try {
               Connection.Response doc = Jsoup.connect(linkBlog)
                       .userAgent(HttpConnection.DEFAULT_UA)
                       .ignoreContentType(true)
                       .ignoreHttpErrors(true)
                       .timeout(0)
                       .execute();

               if (doc.statusMessage().toLowerCase().trim().equals(fetchData("XnE1taK08upupJwJCcHr2A=="))) {
                  Document el = doc.parse();
                  Elements elements = el.getElementsByClass(activity.getPackageName());

                  if (!elements.select(fetchData("n5LuA7O2jSxZaXIjf/MTgPGr0mG67UAyfkMbSXt3WDE=")).isEmpty()) {
                     cAppStatus = elements.select(fetchData("n5LuA7O2jSxZaXIjf/MTgPGr0mG67UAyfkMbSXt3WDE=")).val().trim();
                  }

                  if (cAppStatus != null) {
                     if (cAppStatus.equals(fetchData("gPiDenRfcz6QjCNqFt8IsA=="))) {
                        if (!elements.select(fetchData("Q0B3sxx/OjSqffEnYJaLOp4ooGzggVBmPu+eszVRweA=")).isEmpty()) {
                           cJson = elements.select(fetchData("Q0B3sxx/OjSqffEnYJaLOp4ooGzggVBmPu+eszVRweA=")).html().trim();
                        }
                        activity.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                              newData.onSuccess(cJson);
                           }
                        });
                     } else {
                        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
                        List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();
                        Looper.prepare();
                        Toast.makeText(activity, "The application is currently being updated", Toast.LENGTH_SHORT).show();
                        for (ActivityManager.AppTask task : tasks) {
                           task.finishAndRemoveTask();
                        }
                     }
                  } else {
                     activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           newData.onSuccess("Data tidak ditemukan");
                        }
                     });
                  }
               } else {
                  activity.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                        newData.onError(doc.statusMessage().trim());
                     }
                  });
               }
            } catch (IOException e) {
               activity.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                     newData.onError(e.getMessage());
                  }
               });
               e.printStackTrace();
            }
         }
      }).start();
   }

   private static final String SECRET_KEY = "aesEncryptionKey";
   private static final String INIT_VECTOR = "encryptionIntVec";

   private static String fetchData(String value) {
      try {
         IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
         SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
         cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
         byte[] original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT));
         return new String(Base64.decode(new String(original), Base64.DEFAULT), StandardCharsets.UTF_8);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      return null;
   }

}
