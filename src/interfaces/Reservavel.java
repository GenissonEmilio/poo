package interfaces;

import exceptions.ReservaInvalidaException;
import models.Reserva;

import java.util.List;

public interface Reservavel {
    public boolean reservar(Reserva r) throws ReservaInvalidaException;
    boolean cancelar(String idReserva);
    List<Reserva> listarReservas();
}
