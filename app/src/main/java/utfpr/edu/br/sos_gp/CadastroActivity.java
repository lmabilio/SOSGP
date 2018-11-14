package utfpr.edu.br.sos_gp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Ao clicar no botão Cadastrar, via webservice o usuário será cadastrado
        Button btn = findViewById(R.id.btnCadastrar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtNome = (EditText) findViewById(R.id.txtNome);
                final EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
                EditText txtSenha = (EditText) findViewById(R.id.txtSenha);
                EditText txtSenha2 = (EditText) findViewById(R.id.txtSenha2);

                int error = 0;

                //verificaçao se os campos estão vazios, todos os campos devem
                //ser preenchidos
                if(txtNome.getText().toString().equals("")){
                    txtNome.setError("Preencha o campo nome");
                    txtNome.requestFocus();
                    error = 1;
                } else if(txtEmail.getText().toString().equals("")){
                    txtEmail.setError("Preencha o campo e-mail");
                    txtEmail.requestFocus();
                    error = 1;
                } else if(txtSenha.getText().toString().equals("")){
                    txtSenha.setError("Preencha o campo senha");
                    txtSenha.requestFocus();
                    error = 1;
                } else if (txtSenha2.getText().toString().equals("")){
                    txtSenha2.setError("Preencha o campo confirmação de senha");
                    txtSenha2.requestFocus();
                    error = 1;
                }

                if(!txtSenha.getText().toString().equals(txtSenha2.getText().toString())){
                    txtSenha2.setError("Os dois campos de senha devem ser iguais");
                    txtSenha2.requestFocus();
                    error = 1;
                }

                if(error == 0) {
                    String URL = Constants.IP_ADDRESS + "insert_user.php";

                    //Biblioteca Ion para chamada HTTP
                    Ion.with(getBaseContext())
                            .load(URL)
                            .setBodyParameter("name_user", txtNome.getText().toString())
                            .setBodyParameter("email_user", txtEmail.getText().toString())
                            .setBodyParameter("pwd_user", txtSenha.getText().toString())
                            //recebe objeto do tipo json
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (result.get("retorno").getAsString().equals("YES")) {
                                        Toast.makeText(getBaseContext(), "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else if (result.get("retorno").getAsString().equals("EMAIL_ERROR")) {
                                        txtEmail.setError("E-mail já existe!");
                                        txtEmail.requestFocus();
                                    }
                                }
                            });

                }

            }
        });



    }

}
