package utfpr.edu.br.sos_gp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;


public class ChatFragment extends Fragment {

    List<Chat> lista = null;
    ListView ltwChats = null;

    AppCompatActivity app = new AppCompatActivity();

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        final Intent it = getActivity().getIntent();

//        app.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        app.getSupportActionBar().setTitle(it.getStringExtra("name_contact"));

        ltwChats = getView().findViewById(R.id.ltwChats);

        refreshListView();

        Button btnSend = (Button) getView().findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);

                final String id_user = String.valueOf(preferences.getInt("id_user", 0));

                //String URL = "http://192.168.0.6/sosgp/send_message.php";
                String URL = Constants.IP_ADDRESS + "send_message.php";

                final EditText txtMensagem = (EditText) getView().findViewById(R.id.txtMensagem);
                if(txtMensagem.getText().length() > 0) {

                    final SQLiteHelper db = new SQLiteHelper(getContext());
                    final long id_reg = db.insert_message(Integer.parseInt(id_user), it.getIntExtra("contact_user", 0), txtMensagem.getText().toString(), "");

                    String txtMsg = txtMensagem.getText().toString();
                    txtMensagem.setText("");
                    refreshListView();

                    Ion.with(getContext())
                            .load(URL)
                            .setMultipartParameter("contact_user", String.valueOf(it.getIntExtra("contact_user", 0)))
                            .setMultipartParameter("id_user", id_user)
                            .setMultipartParameter("message", txtMsg)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (result.get("retorno").getAsString().equals("YES")) {
                                        db.update_message(id_reg, result.get("data_message").getAsString() );
                                        refreshListView();
                                    } else if (result.get("retorno").getAsString().equals("NO")) {
                                    } else if (result.get("retorno").getAsString().equals("CONTACT_NOT_EXIST")) {
                                    }
                                }
                            });
                }
            }
        });
    }


    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("REFRESH"));
    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshListView();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == android.R.id.home){
            getActivity().finish();
            return true;
        }else {
            return false;
        }
    }

    private void refreshListView(){
        Intent itt = getActivity().getIntent();

        SharedPreferences preferences = getActivity().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);

        SQLiteHelper db = new SQLiteHelper(getContext());
        this.lista =  db.getMessages(itt.getIntExtra("contact_user", 0), preferences.getInt("id_user", 0));

        ltwChats.setAdapter(new ChatAdapter(getContext(), lista));
        ltwChats.setSelection(ltwChats.getCount() -1);
    }
}
