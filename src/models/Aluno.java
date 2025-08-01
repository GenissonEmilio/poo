package models;

public class Aluno extends Usuario{
    public Aluno(String nome, int id, String email) {
        super(nome, id, email);
    }

    public boolean podeReservar(int diasAntecedencia) {
        return diasAntecedencia == 0;
    }
}
