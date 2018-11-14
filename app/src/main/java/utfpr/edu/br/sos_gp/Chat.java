package utfpr.edu.br.sos_gp;

public class Chat {

    private int id_chat;
    private int contact_user;
    private int id_user;
    private String message;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId_chat() {
        return id_chat;
    }

    public void setId_chat(int id_chat) {
        this.id_chat = id_chat;
    }

    public int getContact_user() {
        return contact_user;
    }

    public void setContact_user(int contact_user) {
        this.contact_user = contact_user;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getMensagem() {
        return message;
    }

    public void setMensagem(String message) {
        this.message = message;
    }



}
