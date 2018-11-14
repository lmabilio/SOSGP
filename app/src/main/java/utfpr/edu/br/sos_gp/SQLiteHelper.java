package utfpr.edu.br.sos_gp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String name_db = "AppChat.db";
    private static final int version_db = 5;

    public SQLiteHelper(Context context){
        super(context, name_db, null, version_db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CONTATOS = "CREATE TABLE contacts (id_contact integer primary key autoincrement,";
        SQL_CONTATOS += "id_user integer, contact_user integer, name_contact varchar(100))";

        db.execSQL(SQL_CONTATOS);

        String SQL_MESSAGES = "CREATE TABLE messages (id_message integer primary key autoincrement,";
        SQL_MESSAGES += "id_user integer, contact_user integer, message TEXT, data datetime)";

        db.execSQL(SQL_MESSAGES);

    }

    public void saveContact(int id_user, int contact_user, String name_contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues ctv = new ContentValues();
        ctv.put("id_user", id_user);
        ctv.put("contact_user", contact_user);
        ctv.put("name_contact", name_contact);

        db.insert("contacts", "id_contact", ctv);

    }

    public long insert_message(int id_user, int contact_user, String message, String data){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues ctv = new ContentValues();
        ctv.put("id_user", id_user);
        ctv.put("contact_user", contact_user);
        ctv.put("message", message);
        ctv.put("data", data);

        return db.insert("messages", "id_message", ctv);

    }

    public void update_message(long id_reg, String data){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues ctv = new ContentValues();
        ctv.put("data", data);

        db.update("messages", ctv, "id_message = ?", new String[]{String.valueOf(id_reg)});

    }

    public List<Contato> getContacts(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contacts", null);

        List<Contato> lista = new ArrayList<Contato>();

        while (cursor.moveToNext()){
            Contato c = new Contato();
            c.setId_contact(cursor.getInt(cursor.getColumnIndex("id_contact")));
            c.setId_user(cursor.getInt(cursor.getColumnIndex("id_user")));
            c.setContact_user(cursor.getInt(cursor.getColumnIndex("contact_user")));
            c.setName_contact(cursor.getString(cursor.getColumnIndex("name_contact")));
            lista.add(c);

        }

        return lista;

    }

    public List<Chat> getMessages(int contact_user, int id_user){

        SQLiteDatabase db = this.getReadableDatabase();
        String[] p = new String[]{String.valueOf(contact_user), String.valueOf(id_user), String.valueOf(id_user), String.valueOf(contact_user)};
        Cursor cursor = db.rawQuery("SELECT * FROM messages WHERE (contact_user = ? and id_user = ?) or (contact_user = ? and id_user = ?)", p);

        List<Chat> lista = new ArrayList<Chat>();

        while (cursor.moveToNext()){
            Chat c = new Chat();
            c.setId_chat(cursor.getInt(cursor.getColumnIndex("id_message")));
            c.setId_user(cursor.getInt(cursor.getColumnIndex("id_user")));
            c.setContact_user(cursor.getInt(cursor.getColumnIndex("contact_user")));
            c.setMensagem(cursor.getString(cursor.getColumnIndex("message")));
            c.setData(cursor.getString(cursor.getColumnIndex("data")));
            lista.add(c);


        }

        return lista;

    }

    public List<Chat> getChats(int id_user){

        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder strSQL = new StringBuilder();
        strSQL.append("SELECT m.* FROM messages m WHERE m.id_message IN (");
        strSQL.append("SELECT MAX(m2.id_message) FROM messages m2 WHERE ");
        strSQL.append(String.valueOf(id_user));
        strSQL.append(" IN (m2.id_user, m2.contact_user) GROUP BY MAX(m2.id_user, m2.contact_user))");
        strSQL.append(" ORDER BY m.id_message DESC");


        Cursor cursor = db.rawQuery(strSQL.toString(), null);

        List<Chat> lista = new ArrayList<Chat>();

        while (cursor.moveToNext()){
            Chat c = new Chat();
            c.setId_chat(cursor.getInt(cursor.getColumnIndex("id_message")));
            c.setId_user(cursor.getInt(cursor.getColumnIndex("id_user")));
            c.setContact_user(cursor.getInt(cursor.getColumnIndex("contact_user")));
            c.setMensagem(cursor.getString(cursor.getColumnIndex("message")));
            c.setData(cursor.getString(cursor.getColumnIndex("data")));
            lista.add(c);


        }

        return lista;

    }

    public Contato getUser(int contact_user){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contacts WHERE contact_user = ?", new String[]{String.valueOf(contact_user)});

        Contato c = new Contato();

        while (cursor.moveToNext()){
            c.setId_contact(cursor.getInt(cursor.getColumnIndex("id_contact")));
            c.setId_user(cursor.getInt(cursor.getColumnIndex("id_user")));
            c.setContact_user(cursor.getInt(cursor.getColumnIndex("contact_user")));
            c.setName_contact(cursor.getString(cursor.getColumnIndex("name_contact")));

        }

        return c;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
