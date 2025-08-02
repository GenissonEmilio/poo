package models;

import exceptions.ReservaInvalidaException;
import exceptions.UsuarioNaoAutorizadoException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SistemaDeReservas {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, Sala> salas = new HashMap<>();

    public void listarSalas() {
        salas.values().forEach(sala -> System.out.println(sala.getId()));
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    public void adicionarSala(Sala sala) {
        salas.put(sala.getId(), sala);
    }

    public Usuario getUsuario(String id) {
        return usuarios.get(id);
    }

    public Sala getSala(String id) {
        return salas.get(id);
    }

    public List<Reserva> listarReservasUsuario(String idUsario) {
        return salas.values().stream().flatMap(sala -> sala.listarReservas().stream()).filter(reserva -> reserva.getIdUsuario().equals(idUsario)).collect(Collectors.toList());
    }

    public List<Reserva> buscarPorPeriodo(LocalDate dataHoraInicio, LocalDate dataHoraFim) {
        return salas.values().stream().flatMap(sala -> sala.listarReservas().stream()).filter(reserva -> {
            LocalDate date = reserva.getDataHoraInicio().toLocalDate();
            return (date.isEqual(dataHoraInicio) || date.isAfter(dataHoraInicio)) && (date.isEqual(dataHoraFim) || date.isBefore(dataHoraFim));
        }).collect(Collectors.toList());
    }

    public boolean cancelarReserva(String idReserva, Usuario solicitante) throws UsuarioNaoAutorizadoException {
        for (Sala sala : salas.values()) {
            for (Reserva reserva : sala.listarReservas()) {
                if (reserva.getId().equals(idReserva)) {
                    if (!reserva.getIdUsuario().equals(solicitante.getId()) && !(solicitante instanceof Professor)) {
                        throw new UsuarioNaoAutorizadoException("Sem permissão para cancelar");
                    }
                    return sala.cancelar(idReserva);
                }
            }
        }

        return false;
    }

    public void gerarRelatorio(String path) {
        List<Reserva> todasReservas = salas.values().stream()
                .flatMap(sala -> sala.listarReservas().stream())
                .sorted(Comparator.reverseOrder())
                .toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("===== RELATÓRIO DE RESERVAS =====\n");
            for (Reserva reserva : todasReservas) {
                writer.write(String.format(
                        "ID: %s | Sala: %s | Início: %s | Fim: %s | Duração: %d minutos | Usuário: %s\n",
                        reserva.getId(),
                        reserva.getIdSala(),
                        reserva.getDataHoraInicio(),
                        reserva.getDataHoraFim(),
                        reserva.getDuracao().toMinutes(),
                        reserva.getIdUsuario()
                ));
            }
            writer.write("==================================\n");
            System.out.println("Arquivo salvo em: " + path);

        } catch (IOException e) {
            System.out.println("Erro ao salvar relatorio: " + e.getMessage());
        }
    }

    public void carregarUsuarios(String caminhoArquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 4) {
                    String tipo = dados[0];
                    String nome = dados[1];
                    String id = dados[2];
                    String email = dados[3];

                    if (tipo.equals("PROFESSOR")) {
                        adicionarUsuario(new Professor(nome, id, email));
                    } else if (tipo.equals("ALUNO")) {
                        adicionarUsuario(new Aluno(nome, id, email));
                    }
                }
            }
        }
    }

    public void carregarSalas(String caminhoArquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    adicionarSala(new Sala(linha.trim()));
                }
            }
        }
    }

    public void carregarReservas(String caminhoArquivo) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 5) {
                    String idReserva = dados[0];
                    LocalDateTime inicio = LocalDateTime.parse(dados[1], formatter);
                    LocalDateTime fim = LocalDateTime.parse(dados[2], formatter);
                    String idSala = dados[3];
                    String idUsuario = dados[4];

                    Sala sala = salas.get(idSala);
                    if (sala != null) {
                        try {
                            sala.reservar(new Reserva(inicio, fim, idReserva, idSala, idUsuario));
                        } catch (ReservaInvalidaException e) {
                            System.out.println("Erro ao carregar reserva: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

}
