package utfpr.edu.br.sos_gp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShowMenu();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Button btnLogar = (Button) findViewById(R.id.btnLogar);

        //Botao para logar no sistema
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtLoginEmail = (EditText) findViewById(R.id.txtLoginEmail);
                EditText txtLoginSenha = (EditText) findViewById(R.id.txtLoginSenha);

                int error = 0;


                //verifica se os campos estão em branco, se não, ele continua
                if(txtLoginEmail.getText().toString().equals("")){
                    txtLoginEmail.setError("Preencha o campo e-mail");
                    txtLoginEmail.requestFocus();
                    error = 1;
                } else if(txtLoginSenha.getText().toString().equals("")){
                    txtLoginSenha.setError("Preencha o campo senha");
                    txtLoginSenha.requestFocus();
                    error = 1;
                }


                if(error == 0) {

                    //verifica o id do usuario e manda para o webserice fazer a consulta e o login
                    SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
                    String token_user = preferences.getString("token_user", "");

                    String URL = Constants.IP_ADDRESS + "login_user.php";

                    Ion.with(getBaseContext())
                            .load(URL)
                            .setBodyParameter("email_user", txtLoginEmail.getText().toString())
                            .setBodyParameter("pwd_user", txtLoginSenha.getText().toString())
                            .setBodyParameter("token_user", token_user)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (result.get("retorno").getAsInt() > 0) {
                                        SharedPreferences.Editor preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE).edit();
                                        preferences.putInt("id_user",result.get("retorno").getAsInt() );
                                        preferences.putString("name_user",result.get("name_user").getAsString() );
                                        preferences.putString("email_user",result.get("email_user").getAsString() );
                                        preferences.putString("role_user",result.get("role_user").getAsString() );
                                        preferences.commit();

                                        SaveContact(result.get("retorno").getAsInt());

                                        ShowMenu();

                                    } else {
                                        Toast.makeText(getBaseContext(), "Erro!", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                }

            }
        });
    }

    private void ShowMenu(){

        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);


        //Faz a verificaçao do papel do usuario, se e usuario comum ou admin
        if(preferences.getInt("id_user", 0) > 0) {
            if (preferences.getString("role_user", "").equals("admin")){
               Intent it = new Intent(MainActivity.this, ChamadosActivity.class);
                startActivity(it);
                finish();
            } else {
                Intent it = new Intent(MainActivity.this, SosActivity.class);
                startActivity(it);
                finish();
            }



        }


    }

    private void SaveContact(final int id_user){
        String URL = Constants.IP_ADDRESS + "get_all_contacts.php";

        //salva o contato no banco interno do android
        Ion.with(getBaseContext())
                .load(URL)
                .setBodyParameter("id_user", String.valueOf((id_user)))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for(int i=0; i< result.size(); i++){

                            JsonObject obj = result.get(i).getAsJsonObject();

                            SQLiteHelper db = new SQLiteHelper(getBaseContext());
                            db.saveContact(id_user, Integer.parseInt(obj.get("id_user").getAsString()), obj.get("name_user").getAsString());
                        }
                    }

                });
    }

}
