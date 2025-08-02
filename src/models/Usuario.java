package models;

import java.util.Objects;
import java.util.regex.Pattern;

public abstract class Usuario {
    protected String nome;
    protected String id;
    protected String email;

    public Usuario(String nome, String id, String email) {
        this.nome = nome;
        this.id = id;
        this.setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", email)) {
            throw new IllegalArgumentException("Email inválido.");
        }
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    public abstract boolean podeReservarComAntecedencia(java.time.LocalDateTime data);
}
