package visao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import entidades.Episodio;
import entidades.Serie;
import modelo.ArquivoEpisodio;
import modelo.ArquivoSerie;

public class MenuEpisodio {
    ArquivoEpisodio arqEpisodio;
    ArquivoSerie arqSerie;
    private static Scanner scanner = new Scanner(System.in);

    public MenuEpisodio() throws Exception {
        arqEpisodio = new ArquivoEpisodio();
        arqSerie = new ArquivoSerie();
    }

    public void menu() {
        int opition;
        do {
            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-----------");
            System.out.println("> Início > Episódios\n");
            System.out.println("1) Adicionar");
            System.out.println("2) Buscar");
            System.out.println("3) Alterar");
            System.out.println("4) Excluir");
            System.out.println("0) Retornar");

            System.out.print("\nOpção: ");
            try {
                opition = Integer.valueOf(scanner.nextLine());
            } catch (Exception e) {
                opition = -1;
            }

            switch (opition) {
                case 1:
                    addEpisodio();
                    break;
                case 2:
                    buscarEpisodio();
                    break;
                case 3:
                    alterarEpisodio();
                    break;
                case 4:
                    excluirEpisodio();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opition != 0);
    }

    public void addEpisodio() {
        System.out.println("\nAdicionar Episodio");
        int idSerie = 0;
        String nome = "";
        int temporada = 0;
        LocalDate lancamento = null;
        int duracao = 0;
        String sinopse = "";
        boolean dadosCorretos = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Serie[] s = null;

        do {
            System.out.println("Qual série o episódio pertence: ");
            String nomeSerie = scanner.nextLine();

            try {
                s = arqSerie.readNome(nomeSerie);
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível buscar a série!");
                e.printStackTrace();
            }

            if (s == null) {
                System.out.println("Erro série não encontrada!");
            } else {
                idSerie = s[0].getId();
            }
        } while (s == null);

        do {
            System.out.print("Nome do Episódio(Mínimo 1 letra): ");
            nome = scanner.nextLine();
            if (nome.length() >= 1)
                dadosCorretos = true;
            else
                System.err.println("O nome da série deve ter no mínimo 1 caractere.");
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Temporada: ");
            temporada = Integer.parseInt(scanner.nextLine());
            if (temporada > 0)
                dadosCorretos = true;
            else
                System.err.println("A temporada deve ser constituida por um número inteiro e positivo.");
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Data de lançamento (DD/MM/YYYY): ");
            String data = scanner.nextLine();
            try {
                lancamento = LocalDate.parse(data, formatter);
                dadosCorretos = true;
            } catch (Exception e) {
                System.err.println("Data inválida! Use o formato (DD/MM/YYYY): ");
            }
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Duração em minutos: ");
            duracao = Integer.parseInt(scanner.nextLine());
            if (duracao > 0)
                dadosCorretos = true;
            else
                System.err.println("A Duração deve ser constituida por um número inteiro e positivo.");
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Sinópse: ");
            sinopse = scanner.nextLine();
            if (sinopse.length() > 20 && sinopse.length() < 500)
                dadosCorretos = true;
            else
                System.err
                        .println("Escreva a sinópse dentro dos padrões definidos (mínimo 20, máximo 500 caracteres).");
        } while (!dadosCorretos);

        System.out.println("\nConfirma a inclusão do episódio? (S/N): ");
        char resp = scanner.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Episodio episodio = new Episodio(idSerie, nome, temporada, lancamento, duracao, sinopse);
                arqEpisodio.create(episodio);
                System.out.println("Episódio incluido com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro de sistema. Não foi possível incluir a série");
            }
        }
    }

    public void buscarEpisodio() {
        System.out.println("\nBuscar Episódio:");
        System.out.println("Nome doepisódio:");
        String nome = scanner.nextLine();

        try {
            Episodio[] episodios = arqEpisodio.readNome(nome);
            int op = 0;
            if (episodios.length > 1) {
                int n = 1;
                for (Episodio e : episodios) {
                    System.out.println((n++) + ") " + e.getName());
                }
                System.out.print("Escolha um Episódio: ");
                do {
                    try {
                        op = Integer.valueOf(scanner.nextLine());
                    } catch (Exception e) {
                        op = -1;
                    }
                    if (op <= 0 || op > n - 1)
                        System.out.println("Escolha um número entre 1 e " + (n - 1));
                } while (op <= 0 || op > n - 1);
                mostrarEpisodio(episodios[op - 1]);
            } else if (episodios.length == 1) {
                op = 1;
            } else {
                System.out.println("Nenhum episódio encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar o episódio!");
            e.printStackTrace();
        }
    }

    public void mostrarEpisodio(Episodio episodio) {
        if (episodio != null) {
            System.out.println("----------------------");
            try {
                Serie s = arqSerie.read(episodio.getSerieId());
                System.out.printf("Série........: %s\n", (s != null ? s.getName() : "Série não encontrada"));
            } catch (Exception e) {
                System.out.println("Erro: nao foi possível buscar a série do episodio.");
            }
            System.out.printf("Nome.........: %s\n", episodio.getName());
            System.out.printf("Temporada....: %d\n", episodio.getTemporada());
            System.out.printf("lancamento...: %s\n",
                    episodio.getLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.printf("Duracao......: %d\n", episodio.getDuracao());
            System.out.printf("Sinopse......: %s\n", episodio.getSinopse());
            System.out.println("----------------------");
        }
    }

    public void alterarEpisodio() {
        System.out.println("Alteração de Episódio");
        String nome;
        Boolean dadosCorretos;

        dadosCorretos = false;
        do {
            System.out.print("Nome do episódio (mínimo 1 caractére): ");
            nome = scanner.nextLine();
            if (nome.isEmpty())
                return;
            if (nome.length() >= 1)
                dadosCorretos = true;
            else
                System.out.println("Nome inválido. O nome do episódio deve conter pelomenos 1 caractére.");
        } while (!dadosCorretos);

        try {
            Episodio[] episodios = arqEpisodio.readNome(nome);
            if (episodios.length > 0) {
                int op = 0;
                if (episodios.length > 1) {
                    int n = 1;

                    for (Episodio s : episodios) {
                        System.out.println((n++) + ") " + s.getName());
                    }

                    System.out.println("Escolha um episódio: ");
                    do {
                        try {
                            op = Integer.valueOf(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            op = -1;
                        }
                        if (op <= 0 || op > n - 1)
                            System.out.println("Escolha um número entre 1 e " + (n - 1));
                    } while (op <= 0 || op > n - 1);
                } else if (episodios.length == 1) {
                    op = 1;
                }

                // Alteração Nome
                String novoNome;
                dadosCorretos = false;
                do {
                    System.out.print("\nNovo nome do episódio (deixe em branco para manter o anterior): ");
                    novoNome = scanner.nextLine();
                    if (!novoNome.isEmpty()) {
                        if (nome.length() >= 1) {
                            episodios[op - 1].setName(novoNome);
                            dadosCorretos = true;
                        } else
                            System.err.println("O nome do episódio deve ter no mínimo 1 caractere.");
                    } else
                        dadosCorretos = true;
                } while (!dadosCorretos);
                // Alteração Temporada
                int novaTemporada;
                dadosCorretos = false;
                do {
                    System.out.print("\nNova temporada da série (deixe em branco para manter o anterior): ");
                    novaTemporada = Integer.parseInt(scanner.nextLine());
                    if (novaTemporada > 0) {
                        episodios[op - 1].setTemporada(novaTemporada);
                        dadosCorretos = true;
                    } else {
                        System.out.println("A temporada deve ser um numero inteiro e positivo.");
                    }
                } while (!dadosCorretos);
                // Alteração Lancamento
                String novaData;
                dadosCorretos = false;
                do {
                    System.out.print(
                            "\nNova data de lançamento do episódio (DD/MM/YYYY) (deixe em branco para manter o anterior): ");
                    novaData = scanner.nextLine();
                    if (!novaData.isEmpty()) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            episodios[op - 1].setLancamento(LocalDate.parse(novaData, formatter));
                        } catch (Exception e) {
                            System.err.println("Data inválida. Valor mantido.");
                        }
                    } else
                        dadosCorretos = true;
                } while (!dadosCorretos);
                // Alteração Duração
                int novaDuracao;
                dadosCorretos = false;
                do {
                    System.out.print("\nNova duração do episódio (deixe em branco para manter o anterior): ");
                    novaDuracao = Integer.parseInt(scanner.nextLine());
                    if (novaDuracao > 0) {
                        episodios[op - 1].setTemporada(novaDuracao);
                        dadosCorretos = true;
                    } else {
                        System.out.println("A duração deve ser um numero inteiro e positivo.");
                    }
                } while (!dadosCorretos);
                // Altração Sinopse
                String novaSinopse;
                dadosCorretos = false;
                do {
                    System.out.print("Nova sinópse do episódio (deixe em branco para manter o anterior): ");
                    novaSinopse = scanner.nextLine();
                    if (!novaSinopse.isEmpty()) {
                        if (novaSinopse.length() > 20 && novaSinopse.length() < 500) {
                            episodios[op - 1].setSinopse(novaSinopse);
                            dadosCorretos = true;
                        } else
                            System.err.println(
                                    "Escreva a sinópse dentro dos padrões definidos (mínimo 20, máximo 500 caracteres).");
                    } else
                        dadosCorretos = true;
                } while (!dadosCorretos);
                // confirmação
                System.out.print("\nDeseja confirmar as alterações? (S/N): ");
                char resp = scanner.next().charAt(0);
                if (resp == 'S' || resp == 's') {
                    boolean alterado = arqEpisodio.update(episodios[op - 1]);
                    if (alterado)
                        System.out.println("Episódio alterado com sucesso.");
                    else
                        System.out.println("Erro ao alterar a episódio.");
                } else {
                    System.out.println("Alterações canceladas.");
                }
                scanner.nextLine();
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possuvel buscar as séries!");
            e.printStackTrace();
        }
    }

    public void excluirEpisodio() {
        System.out.println("Exclusão de Episódio");

        String nome;
        Boolean dadosCorretos;

        dadosCorretos = false;
        do {
            System.out.println("Nome do episódio (Mínimo 1 caractere): ");
            nome = scanner.nextLine();
            if (nome.isEmpty())
                return;
            if (nome.length() >= 1)
                dadosCorretos = true;
            else
                System.out.println("Nome inválido!");
        } while (!dadosCorretos);

        try {
            Episodio[] episodio = arqEpisodio.readNome(nome);
            int op = 0;
            if (episodio.length > 1) {
                int n = 1;

                for (Episodio p : episodio) {
                    System.out.println((n++) + ") " + p.getName());
                }

                System.out.println("Escolha uma Série: ");
                do {
                    try {
                        op = Integer.valueOf(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        op = -1;
                    }
                    if (op <= 0 || op > n - 1)
                        System.out.println("Escolha um número entre 1 e " + (n - 1));
                } while (op <= 0 || op > n - 1);
            } else if (episodio.length == 1) {
                op = 1;
            }
            mostrarEpisodio(episodio[op - 1]);

            System.out.print("\nConfirma a exclusão do episódio? (S/N) ");
            char resp = scanner.nextLine().charAt(0);
            if (resp == 'S' || resp == 's') {
                boolean excluido = arqEpisodio.delete(nome);
                if (excluido) {
                    System.out.println("Episódio excluída com sucesso.");
                } else {
                    System.out.println("Erro ao excluir o episódio.");
                }
            } else {
                System.out.println("Episódio não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir a série");
            e.printStackTrace();
        }
    }
}
