package interfaces;

import models.Reserva;
import java.util.List;


public interface Reservavel {
    public boolean reservar(Reserva r);
    public boolean cancelar(int idReserva);
    public List<Reserva> listarReservas();

}
