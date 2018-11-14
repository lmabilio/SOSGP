package utfpr.edu.br.sos_gp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Ao clicar no botão flutuante chama a tela de cadastro
        FloatingActionButton btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(ProfileActivity.this, AnamneseActivity.class);
                startActivity(it);
                finish();
            }
        });


        //Os campos de anamnese a serem preenchidos quando o cadastro estiver no campo
        final TextView tvNomeCompl = (TextView) findViewById(R.id.tvNomeCompl);
        final TextView tvIdade = (TextView) findViewById(R.id.tvIdade);
        final TextView tvAlergia = (TextView) findViewById(R.id.tvAlergia);
        final TextView tvEndereco = (TextView) findViewById(R.id.tvEndereco);
        final TextView tvBairro = (TextView) findViewById(R.id.tvBairro);
        final TextView tvSangue = (TextView) findViewById(R.id.tvSangue);
        final TextView tvPressao = (TextView) findViewById(R.id.tvPressao);
        final TextView tvDiabete = (TextView) findViewById(R.id.tvDiabete);

        SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
        String id_user = String.valueOf(preferences.getInt("id_user", 0));

        //Consulta pelo webservice para verificar se tem o cadastro no banco
        //com o id do usuário, se não tiver ele mostra uma mensagem pedindo
        //para fazer o cadastro
        String URL = Constants.IP_ADDRESS + "get_profile.php";
        Ion.with(getBaseContext())
                .load(URL)
                .setBodyParameter("id_user", id_user)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if(result.size() != 0) {

                            for (int i = 0; i < result.size(); i++) {


                                JsonObject obj = result.get(i).getAsJsonObject();
                                tvNomeCompl.setText(obj.get("full_name").getAsString());
                                tvIdade.setText(obj.get("age").getAsString());
                                tvAlergia.setText(obj.get("allergy").getAsString());
                                tvEndereco.setText(obj.get("address").getAsString());
                                tvBairro.setText(obj.get("district").getAsString());
                                tvSangue.setText(obj.get("blood").getAsString());
                                tvPressao.setText(obj.get("pressure").getAsString());
                                tvDiabete.setText(obj.get("diabetes").getAsString());

                            }
                        }else{
                            Toast.makeText(getBaseContext(), "Cadastre suas informações!", Toast.LENGTH_LONG).show();

                        }


                    }

                });


    }
}
