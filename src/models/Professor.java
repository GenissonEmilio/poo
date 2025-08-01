package models;

public class Professor extends Usuario {

    public Professor(String nome, int id, String email) {
        super(nome, id, email);
    }

    public boolean podeReservar(int diasAntecedencia) {
        return diasAntecedencia <= 5;
    }
}
