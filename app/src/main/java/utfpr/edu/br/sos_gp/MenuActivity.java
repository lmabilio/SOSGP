package utfpr.edu.br.sos_gp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //foram criados tres botoes para ajuda, bombeiro, ambulancia e policia
        //cada um tem sua funçao para consulta no banco, se clicar em um
        //ele busca no banco seu respectivo id e salva na tabela de contatos o id do
        //socorrista e o id do usuario
        ImageView btnBombeiro = findViewById(R.id.btnBombeiro);
        ImageView btnAmbulancia = findViewById(R.id.btnAmbulancia);
        ImageView btnPolicia = findViewById(R.id.btnPolicia);

        SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
        final String id_user = String.valueOf(preferences.getInt("id_user", 0));
        final Contato[] contato = {null};

        final SQLiteHelper db = new SQLiteHelper(getBaseContext());

        btnBombeiro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String URL = Constants.IP_ADDRESS + "get_bomb.php";
                Ion.with(getBaseContext())
                        .load(URL)
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {
                                for(int i=0; i< result.size(); i++){

                                    JsonObject obj = result.get(i).getAsJsonObject();
                                    if (obj.get("type_user").getAsString().equals("bombeiro")){

                                        //Intent it = new Intent(MenuActivity.this, ChatsActivity.class);
                                        //startActivity(it);
                                        //contato = db.getUser(result.get("id_user").getAsInt());
                                        contato[0] = db.getUser(obj.get("id_user").getAsInt());
                                        Intent it = new Intent(MenuActivity.this, ChatActivity.class);
                                        it.putExtra("contact_user", obj.get("id_user").getAsInt());
                                        it.putExtra("name_contact", contato[0].getName_contact());
                                        startActivity(it);

                                        String URL = Constants.IP_ADDRESS + "add_contact.php";
                                        Ion.with(getBaseContext())
                                                .load(URL)
                                                .setBodyParameter("email_user", "@bomb")
                                                .setBodyParameter("id_user", id_user)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject result) {

                                                        if (result.get("retorno").getAsString().equals("YES")) {
                                                            Toast.makeText(getBaseContext(), "Contato cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                                                            String a = result.get("name_user").getAsString();
                                                            int b = result.get("contact_user").getAsInt();

                                                            SQLiteHelper db = new SQLiteHelper(getBaseContext());
                                                            db.saveContact(Integer.parseInt(id_user), result.get("contact_user").getAsInt(), result.get("name_user").getAsString());
                                                            //refreshListView();

                                                        } else if (result.get("retorno").getAsString().equals("EMAIL_ERROR")) {
                                                            Toast.makeText(getBaseContext(), "Contato não existe!", Toast.LENGTH_LONG).show();

                                                        } else if (result.get("retorno").getAsString().equals("CONTACT_EXIST")) {
                                                            Toast.makeText(getBaseContext(), "Continuaçao do chamado!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                    } else {

                                        Toast.makeText(getBaseContext(), ":(", Toast.LENGTH_LONG).show();

                                    }

                                    }


                            }
                        });

            }
        });

        btnPolicia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String URL = Constants.IP_ADDRESS + "get_polic.php";
                Ion.with(getBaseContext())
                        .load(URL)
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {
                                for(int i=0; i< result.size(); i++){

                                    JsonObject obj = result.get(i).getAsJsonObject();
                                    if (obj.get("type_user").getAsString().equals("policia")){

                                        contato[0] = db.getUser(obj.get("id_user").getAsInt());
                                        Intent it = new Intent(MenuActivity.this, ChatActivity.class);
                                        it.putExtra("contact_user", obj.get("id_user").getAsInt());
                                        it.putExtra("name_contact", contato[0].getName_contact());
                                        startActivity(it);

                                        String URL = Constants.IP_ADDRESS + "add_contact.php";
                                        Ion.with(getBaseContext())
                                                .load(URL)
                                                .setBodyParameter("email_user", "@policia")
                                                .setBodyParameter("id_user", id_user)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject result) {

                                                        if (result.get("retorno").getAsString().equals("YES")) {
                                                            Toast.makeText(getBaseContext(), "Contato cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                                                            SQLiteHelper db = new SQLiteHelper(getBaseContext());
                                                            db.saveContact(Integer.parseInt(id_user), result.get("contact_user").getAsInt(), result.get("name_user").getAsString());
                                                            //refreshListView();

                                                        } else if (result.get("retorno").getAsString().equals("EMAIL_ERROR")) {
                                                            Toast.makeText(getBaseContext(), "Contato não existe!", Toast.LENGTH_LONG).show();

                                                        } else if (result.get("retorno").getAsString().equals("CONTACT_EXIST")) {
                                                            Toast.makeText(getBaseContext(), "Continuaçao do chamado!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                    } else {

                                        Toast.makeText(getBaseContext(), ":(", Toast.LENGTH_LONG).show();

                                    }

                                }


                            }
                        });

            }
        });

        btnAmbulancia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String URL = Constants.IP_ADDRESS + "get_amb.php";
                Ion.with(getBaseContext())
                        .load(URL)
                        .asJsonArray()
                        .setCallback(new FutureCallback<JsonArray>() {
                            @Override
                            public void onCompleted(Exception e, JsonArray result) {
                                for(int i=0; i< result.size(); i++){

                                    JsonObject obj = result.get(i).getAsJsonObject();
                                    if (obj.get("type_user").getAsString().equals("ambulancia")){

                                        contato[0] = db.getUser(obj.get("id_user").getAsInt());
                                        Intent it = new Intent(MenuActivity.this, ChatActivity.class);
                                        it.putExtra("contact_user", obj.get("id_user").getAsInt());
                                        it.putExtra("name_contact", contato[0].getName_contact());
                                        startActivity(it);

                                        String URL = Constants.IP_ADDRESS + "add_contact.php";
                                        Ion.with(getBaseContext())
                                                .load(URL)
                                                .setBodyParameter("email_user", "@amb")
                                                .setBodyParameter("id_user", id_user)
                                                .asJsonObject()
                                                .setCallback(new FutureCallback<JsonObject>() {
                                                    @Override
                                                    public void onCompleted(Exception e, JsonObject result) {

                                                        if (result.get("retorno").getAsString().equals("YES")) {
                                                            Toast.makeText(getBaseContext(), "Contato cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                                                            SQLiteHelper db = new SQLiteHelper(getBaseContext());
                                                            db.saveContact(Integer.parseInt(id_user), result.get("contact_user").getAsInt(), result.get("name_user").getAsString());

                                                        } else if (result.get("retorno").getAsString().equals("EMAIL_ERROR")) {
                                                            Toast.makeText(getBaseContext(), "Contato não existe!", Toast.LENGTH_LONG).show();

                                                        } else if (result.get("retorno").getAsString().equals("CONTACT_EXIST")) {
                                                            Toast.makeText(getBaseContext(), "Continuaçao do chamado!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                    } else {

                                        Toast.makeText(getBaseContext(), ":(", Toast.LENGTH_LONG).show();

                                    }

                                }


                            }
                        });

            }
        });
    }
}
