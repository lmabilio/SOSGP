package utfpr.edu.br.sos_gp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);

        sendRegistrationToServer(s);
        saveInPreference(s);
    }


    private void saveInPreference(String token){
        SharedPreferences.Editor preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE).edit();
        preferences.putString("token_user", token);
        preferences.commit();
    }

    private void sendRegistrationToServer(String token){
        SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
        final String id_user = String.valueOf(preferences.getInt("id_user", 0));

        if(preferences.getInt("id_user", 0) > 0) {
            //String URL = "http://192.168.0.6/sosgp/update_token.php";
            String URL = Constants.IP_ADDRESS + "update_token.php";
            Ion.with(getBaseContext())
                    .load(URL)
                    .setMultipartParameter("id_user", id_user)
                    .setMultipartParameter("token_user", token)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                        }
                    });
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {

            if(remoteMessage.getData().get("action").equals("NEW_MESSAGE")){
                int id_user = Integer.parseInt(remoteMessage.getData().get("id_user"));
                int contact_user = Integer.parseInt(remoteMessage.getData().get("contact_user"));
                String message = remoteMessage.getData().get("message");
                String data = remoteMessage.getData().get("data");
                String name_user = remoteMessage.getData().get("name_user");


                sendNotification(message, id_user, name_user);

                SQLiteHelper db = new SQLiteHelper(getBaseContext());
                db.insert_message(id_user, contact_user, message, data);

                Intent it = new Intent("REFRESH");
                LocalBroadcastManager.getInstance(this).sendBroadcast(it);

            } else if(remoteMessage.getData().get("action").equals("NEW_CONTACT")){
                int id_user = Integer.parseInt(remoteMessage.getData().get("id_user"));
                int contact_user = Integer.parseInt(remoteMessage.getData().get("contact_user"));
                String name_user = remoteMessage.getData().get("name_user");

                SQLiteHelper db = new SQLiteHelper(getBaseContext());
                db.saveContact(contact_user, id_user, name_user);

                Intent it = new Intent("CONTACT");
                LocalBroadcastManager.getInstance(this).sendBroadcast(it);
            }


            Log.d(TAG, "Message data payload: " + remoteMessage.getData());


        }


    }


    private void sendNotification(String messageBody, int contact_user, final String name_user) {
        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);

        Intent intent = null;

        final int id_user = preferences.getInt("id_user", 0);

        if (preferences.getString("role_user", "").equals("admin")){
            intent = new Intent(this, ChamadosActivity.class);

        }else{
            intent = new Intent(this, ChatActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String URL = Constants.IP_ADDRESS + "get_all_contacts.php";

        Ion.with(getBaseContext())
                .load(URL)
                .setBodyParameter("id_user", String.valueOf(id_user))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for(int i=0; i< result.size(); i++){

                            JsonObject obj = result.get(i).getAsJsonObject();

                            final String name_user = obj.get("name_user").getAsString();
                        }
                    }

                });


        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo_sos)
                        .setContentTitle("SOSGP - " + name_user)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Random r = new Random();
        notificationManager.notify(r.nextInt() /* ID of notification */, notificationBuilder.build());
    }
}