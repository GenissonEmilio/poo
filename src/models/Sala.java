package models;
import interfaces.Reservavel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sala implements Reservavel {

    private String id;
    private List<Reserva> reservas;
    //private List<Reserva> reservas = new ArrayList<>(); // TIPADA!


    public Sala(String id) {
        this.id = id;
        this.reservas = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean reservar(Reserva r) {
        for (Reserva existente : reservas) {
            if (r.isConflito(existente)) return false;
        }
        reservas.add(r);
        return true;
    }

    @Override
    public boolean cancelar(int idReserva) {
        return reservas.removeIf(r -> r.getId() == (idReserva));
    }


    @Override
    public List<Reserva> listarReservas() {
        return reservas;
    }

    public List<Reserva> buscarFuturas() {
        return reservas.stream()
                .filter(r -> r.getDataHoraInicio().isAfter(java.time.LocalDateTime.now()))
                .collect(Collectors.toList());
    }

}