package models;

import exceptions.ReservaInvalidaException;
import interfaces.Reservavel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sala implements Reservavel {
    private String id;
    private List<Reserva> reservas;

    public Sala(String id) {
        this.id = id;
        this.reservas = new ArrayList<>();
    }

    @Override
    public boolean reservar(Reserva r) throws ReservaInvalidaException {
        for (Reserva existente : reservas) {
            if (r.isConflito(existente)) {
                throw new ReservaInvalidaException("Conflito na reserva");
            };
        }

        reservas.add(r);
        return true;
    }

    @Override
    public boolean cancelar(String idReserva) {
        return reservas.removeIf(r -> r.getId().equals(idReserva));
    }

    @Override
    public List<Reserva> listarReservas() {
        return reservas;
    }

    public List<Reserva> buscarFuturas() {
        return reservas.stream().filter(r -> r.getDataHoraInicio().isAfter(java.time.LocalDateTime.now())).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}
