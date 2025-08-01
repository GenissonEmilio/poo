package models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Reserva implements Comparable<Reserva> {
    private int id;
    private LocalDateTime  dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String idUsuario;
    private String idSala;

    public Reserva(LocalDateTime  dataHoraFim, LocalDateTime  dataHoraInicio, int id, String idSala, String idUsuario) {
        if (dataHoraInicio.isAfter(dataHoraFim)) {
            throw new IllegalArgumentException("Inicio da reseva deve ser antes do fim");
        }

        this.dataHoraFim = dataHoraFim;
        this.dataHoraInicio = dataHoraInicio;
        this.id = id;
        this.idSala = idSala;
        this.idUsuario = idUsuario;
    }

    @Override
    public int compareTo(Reserva outra) {
        return this.tempoTotal().compareTo(outra.tempoTotal());
    }

    public Duration tempoTotal() {
        return Duration.between(this.getDataHoraInicio(), this.getDataHoraFim());
    }

    public boolean isConflito(Reserva reserva) {
        return this.dataHoraInicio.isBefore(reserva.dataHoraFim) && reserva.dataHoraInicio.isBefore(this.dataHoraFim);
    }

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }

    public LocalDateTime  getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime  dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public LocalDateTime  getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime  dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
