package models;

import java.time.LocalDateTime;

public class Professor extends Usuario {

    public Professor(String nome, String id, String email) {
        super(nome, id, email);
    }

    @Override
    public boolean podeReservarComAntecedencia(LocalDateTime data) {
        return true;
    }
}
