package utfpr.edu.br.sos_gp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class ChamadosActivity extends AppCompatActivity {

    ListView ltwContatos = null;
    List<Contato> lista = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        ltwContatos = this.findViewById(R.id.ltwContatos);
        refreshListView();


        //Lista de contatos que pediram ajuda
        ltwContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(ChamadosActivity.this, ChatsActivity.class);
                it.putExtra("contact_user", lista.get(position).getContact_user());
                it.putExtra("name_contact", lista.get(position).getName_contact());
                startActivity(it);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("CONTACT"));

        //botao flutuante para chamar a tela de cadastro de usuario
        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(ChamadosActivity.this, CadastroActivity.class);
                startActivity(it);
            }
        });


    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshListView();
        }
    };

    //funçao para atualizar a lista de contatos
    private void refreshListView(){
        SQLiteHelper db = new SQLiteHelper(this);

        this.lista =  db.getContacts();
        ltwContatos.setAdapter(new ContactsAdapter(this, this.lista) );
    }


}
