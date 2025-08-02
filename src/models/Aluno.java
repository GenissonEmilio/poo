package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Aluno extends Usuario {
    public Aluno(String nome, String id, String email) {
        super(nome, id, email);
    }

    @Override
    public boolean podeReservarComAntecedencia(LocalDateTime data) {
        return data.toLocalDate().isEqual(LocalDate.now());
    }
}
