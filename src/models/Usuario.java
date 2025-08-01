package models;

import java.util.Objects;

public abstract class Usuario {
    protected String nome;
    protected int id;
    protected String email;

    public Usuario(String nome, int id, String email) {
        String padrao = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        this.nome = nome;
        this.id = id;
        this.email = email.matches(padrao) ? email : "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id && Objects.equals(nome, usuario.nome) && Objects.equals(email, usuario.email);
    }
}
