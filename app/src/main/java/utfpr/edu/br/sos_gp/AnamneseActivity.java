package utfpr.edu.br.sos_gp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnamneseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamnese);

        // Todos os spinner sáo usados para selecionar as opçoes da anamnese
        //de Tipo de Sangue, Pressao Alta e Diabetes
        final Spinner spinSangue = (Spinner) findViewById(R.id.spinSangue);
        final Spinner spinPressao = (Spinner) findViewById(R.id.spinPressao);
        final Spinner spinDiabete = (Spinner) findViewById(R.id.spinDiabete);

        // Iniciando a Array
        String[] sangue = new String[]{
                "Tipo Sanguino",
                "A+",
                "A-",
                "B+",
                "B-",
                "AB+",
                "AB-",
                "O+",
                "O-"
        };

        // Iniciando a Array
        String[] pressao = new String[]{
                "Pressão Arterial",
                "Baixa",
                "Normal",
                "Alta"
        };

        // Iniciando a Array
        String[] diabete = new String[]{
                "Diabete",
                "Sim",
                "Não"
        };

        final List<String> sangueList = new ArrayList<>(Arrays.asList(sangue));


        // Iniciando o ArrayAdapter, isso foi feito para mostrar a primeira opçao do
        // spinner como se fosse um hint, ou seja, para que o nome do campo aparecesse
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,sangueList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {

                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinSangue.setAdapter(spinnerArrayAdapter);

        spinSangue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);

                if(position > 0){

                    Toast.makeText
                            (getApplicationContext(), selectedItemText + " Selecionado", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final List<String> pressaoList = new ArrayList<>(Arrays.asList(pressao));

        // Iniciando o ArrayAdapter, isso foi feito para mostrar a primeira opçao do
        // spinner como se fosse um hint, ou seja, para que o nome do campo aparecesse
        final ArrayAdapter<String> spinnerArrayAdapterPressao = new ArrayAdapter<String>(
                this,R.layout.spinner_item,pressaoList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapterPressao.setDropDownViewResource(R.layout.spinner_item);
        spinPressao.setAdapter(spinnerArrayAdapterPressao);

        spinPressao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), selectedItemText + " Selecionado", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final List<String> diabeteList = new ArrayList<>(Arrays.asList(diabete));

        // Iniciando o ArrayAdapter, isso foi feito para mostrar a primeira opçao do
        // spinner como se fosse um hint, ou seja, para que o nome do campo aparecesse
        final ArrayAdapter<String> spinnerArrayAdapterDiabete = new ArrayAdapter<String>(
                this,R.layout.spinner_item,diabeteList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapterDiabete.setDropDownViewResource(R.layout.spinner_item);
        spinDiabete.setAdapter(spinnerArrayAdapterDiabete);

        spinDiabete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String diabetesItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), diabetesItemText + " Selecionado", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Ao clicar no botao Cadastrar, será feita uma a inserçao no banco
        // via webservice

        Button btn = findViewById(R.id.btnCadastrar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtNomeCompl = (EditText) findViewById(R.id.txtNomeCompl);
                EditText txtIdade = (EditText) findViewById(R.id.txtIdade);
                EditText txtAlergia = (EditText) findViewById(R.id.txtAlergia);
                EditText txtEndereco = (EditText) findViewById(R.id.txtEndereco);
                EditText txtBairro = (EditText) findViewById(R.id.txtBairro);
                String valSangue = spinSangue.getSelectedItem().toString();
                String valPressa = spinPressao.getSelectedItem().toString();
                String valDiabetes = spinDiabete.getSelectedItem().toString();

                //verificaçao se os campos estão vazios, todos os campos devem
                //ser preenchidos
                int error = 0;

                if(txtNomeCompl.getText().toString().equals("")){
                    txtNomeCompl.setError("Preencha o campo nome");
                    txtNomeCompl.requestFocus();
                    error = 1;
                } else if(txtIdade.getText().toString().equals("")){
                    txtIdade.setError("Preencha o campo idade");
                    txtIdade.requestFocus();
                    error = 1;
                } else if(txtAlergia.getText().toString().equals("")){
                    txtAlergia.setError("Preencha o campo alergia");
                    txtAlergia.requestFocus();
                    error = 1;
                } else if (txtEndereco.getText().toString().equals("")){
                    txtEndereco.setError("Preencha o campo endereço");
                    txtEndereco.requestFocus();
                    error = 1;
                } else if (txtBairro.getText().toString().equals("")){
                    txtBairro.setError("Preencha o campo endereço");
                    txtBairro.requestFocus();
                    error = 1;
                } else if(spinDiabete.getSelectedItem().equals(null)){
                    spinDiabete.setPrompt("Escolha uma opção");
                    spinDiabete.requestFocus();
                    error = 1;
                } else if(spinPressao.getSelectedItem().equals(null)){
                    spinPressao.setPrompt("Escolha uma opção");
                    spinPressao.requestFocus();
                    error = 1;
                } else if(spinSangue.getSelectedItem().equals(null)){
                    spinSangue.setPrompt("Escolha uma opção");
                    spinSangue.requestFocus();
                    error = 1;
                }

                SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
                String id_user = String.valueOf(preferences.getInt("id_user", 0));

                if(error == 0) {
                    String URL = Constants.IP_ADDRESS + "insert_profile.php";

                    //Código para inserçao
                    //Biblioteca Ion para chamada HTTP
                    Ion.with(getBaseContext())
                            .load(URL)
                            .setBodyParameter("id_user", id_user)
                            .setBodyParameter("full_name", txtNomeCompl.getText().toString())
                            .setBodyParameter("age", txtIdade.getText().toString())
                            .setBodyParameter("blood", valSangue)
                            .setBodyParameter("pressure", valPressa)
                            .setBodyParameter("diabetes",valDiabetes)
                            .setBodyParameter("allergy", txtAlergia.getText().toString())
                            .setBodyParameter("address", txtEndereco.getText().toString())
                            .setBodyParameter("district", txtBairro.getText().toString())
                            //recebe objeto do tipo json
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (result.get("retorno").getAsString().equals("YES")) {
                                        SharedPreferences.Editor preferences = getSharedPreferences("USER_ANAMNESE", MODE_PRIVATE).edit();
                                        preferences.putString("id_user",result.get("retorno").getAsString());
                                        preferences.commit();

                                        Toast.makeText(getBaseContext(), "Sucesso", Toast.LENGTH_LONG).show();
                                        Intent it = new Intent(AnamneseActivity.this, ProfileActivity.class);
                                        startActivity(it);
                                        finish();
                                    } else if (result.get("retorno").getAsString().equals("NO")) {
                                        Toast.makeText(getBaseContext(), "NO", Toast.LENGTH_LONG).show();
                                    } else if (result.get("retorno").getAsString().equals("PROFILE_ERROR")) {
                                        Toast.makeText(getBaseContext(), "ERROR", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }

            }
        });

    }

}
