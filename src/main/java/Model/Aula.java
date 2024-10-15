package Model;

import java.util.Date;

public class Aula {
    private int id;
    private String nomeAula;
    private Date data;
    private int duracao;  // Em minutos
    private PersonalTrainer personal;

    public Aula(String nomeAula, Date data, int duracao, PersonalTrainer personal) {

        this.nomeAula = nomeAula;
        this.data = data;
        this.duracao = duracao;
        this.personal = personal;
    }

    public Aula() {

    }

    public Aula(int id, String nome, String data, int duracao, String personal) {
    }



    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeAula() {
        return nomeAula;
    }

    public void setNomeAula(String nomeAula) {
        this.nomeAula = nomeAula;
    }

    public java.sql.Date getData() {
        return (java.sql.Date) data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public PersonalTrainer getPersonalTrainer() {
        return personal;
    }

    public void setPersonalTrainer(PersonalTrainer personal) {
        this.personal = personal;
    }


}
