package utfpr.edu.br.sos_gp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class PerfilFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        final TextView tvNomeCompl = (TextView) getView().findViewById(R.id.tvNomeCompl);
        final TextView tvIdade = (TextView) getView().findViewById(R.id.tvIdade);
        final TextView tvAlergia = (TextView) getView().findViewById(R.id.tvAlergia);
        final TextView tvEndereco = (TextView) getView().findViewById(R.id.tvEndereco);
        final TextView tvBairro = (TextView) getView().findViewById(R.id.tvBairro);
        final TextView tvSangue = (TextView) getView().findViewById(R.id.tvSangue);
        final TextView tvPressao = (TextView) getView().findViewById(R.id.tvPressao);
        final TextView tvDiabete = (TextView) getView().findViewById(R.id.tvDiabete);

        final Intent it = getActivity().getIntent();

        SQLiteHelper db = new SQLiteHelper(getContext());
        Contato contato = db.getUser(it.getIntExtra("contact_user", 0));
        int contact_user = contato.getContact_user();

        String URL = Constants.IP_ADDRESS + "get_profile.php";
        Ion.with(getContext())
                .load(URL)
                .setBodyParameter("id_user", String.valueOf(contact_user))
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
                        }

                    }

                });




    }

}
