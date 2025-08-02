import exceptions.ReservaInvalidaException;
import exceptions.UsuarioNaoAutorizadoException;
import models.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SistemaDeReservas sistemaDeReservas = new SistemaDeReservas();
    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {

        //Carregamento de professores, alunos e reservas dos arquivos
        try {
            sistemaDeReservas.carregarUsuarios("usuarios.txt");
            sistemaDeReservas.carregarSalas("salas.txt");
            sistemaDeReservas.carregarReservas("reservas.txt");
            System.out.println("Dados carregados com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }

        exibirMenuPrincipal();
    }

    //Menur principal do fluxo
    private static void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Login");
            System.out.println("2. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    fazerLogin();
                    break;
                case "2":
                    System.out.println("Saindo do sistema...");
                    sistemaDeReservas.gerarRelatorio("relatorio_final.txt");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opcão inválida!");
            }
        }
    }

    //Simulação de login por usuario
    private static void fazerLogin() {
        System.out.print("\nDigite o ID do usuário: ");
        String userId = scanner.nextLine();

        usuarioLogado = sistemaDeReservas.getUsuario(userId);
        if (usuarioLogado == null) {
            System.out.println("Usuário não encontrado!");
            return;
        }

        System.out.println("Bem-vindo, " + usuarioLogado.getNome() + "!");
        exibirMenuUsuario();
    }

    //Menu do usuario para listar, reservar, cancelar e gerar relatorio das reservas
    private static void exibirMenuUsuario() {
        while (usuarioLogado != null) {
            System.out.println("\n=== MENU DO USUÁRIO ===");
            System.out.println("1. Listar salas disponíveis");
            System.out.println("2. Fazer reserva");
            System.out.println("3. Listar minhas reservas");
            System.out.println("4. Cancelar reserva");
            System.out.println("5. Gerar relatório");
            System.out.println("6. Logout");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    listarSalas();
                    break;
                case "2":
                    fazerReserva();
                    break;
                case "3":
                    listarMinhasReservas();
                    break;
                case "4":
                    cancelarReserva();
                    break;
                case "5":
                    gerarRelatorio();
                    break;
                case "6":
                    usuarioLogado = null;
                    System.out.println("Logout realizado com sucesso!");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    //Metodo para listagem de salas
    private static void listarSalas() {
        System.out.println("\nSalas disponíveis:");
        sistemaDeReservas.listarSalas();
    }

    //Metodo para fazer a reserva
    private static void fazerReserva() {
        System.out.println("\n=== NOVA RESERVA ===");
        listarSalas();

        System.out.print("\nQual sala deseja reservar? ");
        String idSala = scanner.nextLine();
        Sala sala = sistemaDeReservas.getSala(idSala);

        if (sala == null) {
            System.out.println("Sala não encontrada!");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime inicio, fim;

        try {
            if (usuarioLogado instanceof Aluno) {
                // Aluno só pode reservar para hoje
                LocalDateTime hoje = LocalDateTime.now();

                System.out.print("Horário de início [HH:mm]: ");
                String[] horaInicio = scanner.nextLine().split(":");
                inicio = hoje.withHour(Integer.parseInt(horaInicio[0]))
                        .withMinute(Integer.parseInt(horaInicio[1]));

                System.out.print("Horário de término [HH:mm]: ");
                String[] horaFim = scanner.nextLine().split(":");
                fim = hoje.withHour(Integer.parseInt(horaFim[0]))
                        .withMinute(Integer.parseInt(horaFim[1]));
            } else {
                // Professor pode reservar para qualquer data
                System.out.print("Data/hora de início [dd/MM/yyyy HH:mm]: ");
                inicio = LocalDateTime.parse(scanner.nextLine(), formatter);

                System.out.print("Data/hora de término [dd/MM/yyyy HH:mm]: ");
                fim = LocalDateTime.parse(scanner.nextLine(), formatter);
            }

            if (!usuarioLogado.podeReservarComAntecedencia(inicio)) {
                System.out.println("Você não pode reservar com essa antecedência!");
                return;
            }

            String idReserva = "RES" + System.currentTimeMillis();
            Reserva reserva = new Reserva(inicio, fim, idReserva, idSala, usuarioLogado.getId());

            try {
                boolean sucesso = sala.reservar(reserva);
                if (sucesso) {
                    System.out.println("Reserva realizada com sucesso! ID: " + idReserva);
                }
            } catch (ReservaInvalidaException e) {
                System.out.println("Erro na reserva: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Formato inválido! " + e.getMessage());
        }
    }

    //Metodo para listagem das reservas do usuario
    private static void listarMinhasReservas() {
        System.out.println("\nMinhas reservas:");
        List<Reserva> reservas = sistemaDeReservas.listarReservasUsuario(usuarioLogado.getId());

        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva encontrada.");
        } else {
            reservas.forEach(r -> System.out.println(
                    "ID: " + r.getId() +
                            " | Sala: " + r.getIdSala() +
                            " | Início: " + r.getDataHoraInicio() +
                            " | Fim: " + r.getDataHoraFim()
            ));
        }
    }

    //Metodo para cancelamento de reserva
    private static void cancelarReserva() {
        listarMinhasReservas();

        System.out.print("\nDigite o ID da reserva que deseja cancelar: ");
        String idReserva = scanner.nextLine();

        try {
            boolean cancelado = sistemaDeReservas.cancelarReserva(idReserva, usuarioLogado);
            if (cancelado) {
                System.out.println("Reserva cancelada com sucesso!");
            } else {
                System.out.println("Reserva não encontrada.");
            }
        } catch (UsuarioNaoAutorizadoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    //Metodo para gerar o relatorio
    private static void gerarRelatorio() {
        System.out.print("Digite o nome do arquivo para o relatório: ");
        String nomeArquivo = scanner.nextLine();
        sistemaDeReservas.gerarRelatorio(nomeArquivo);
    }
}