package utfpr.edu.br.sos_gp;

public class Contato {

    private int id_contact;
    private int id_user;
    private int contact_user;
    private String name_contact;
    private String role_user;


    public int getId_contact() {
        return id_contact;
    }

    public void setId_contact(int id_contact) {
        this.id_contact = id_contact;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getContact_user() {
        return contact_user;
    }

    public void setContact_user(int contact_user) {
        this.contact_user = contact_user;
    }

    public String getName_contact() {
        return name_contact;
    }

    public void setName_contact(String name_contact) {
        this.name_contact = name_contact;
    }

    public String getRole_user() {
        return role_user;
    }

    public void setRole_user(String role_user) {
        this.role_user = role_user;
    }
}
