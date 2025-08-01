import models.Professor;
import models.Reserva;
import models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Professor> professores = new ArrayList<>();
        professores.add(new Professor("João Silva", "reidelas@gmail.com"));
    }
}