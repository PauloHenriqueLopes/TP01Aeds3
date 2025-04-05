package visao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import entidades.Serie;
import modelo.ArquivoSerie;

public class MenuSeries {
     ArquivoSerie arqSerie;
     private static Scanner scanner = new Scanner(System.in);
     private static final String[] STREAMINGS = {
               "Netflix",
               "Amazon Prime",
               "HBO Max",
               "Disney+",
               "Globoplay",
               "Apple TV+",
               "Paramount+"
     };

     public MenuSeries() throws Exception {
          arqSerie = new ArquivoSerie();
     }

     public void menu() {
          int opition;
          do {
               System.out.println("\n\nPUCFlix 1.0");
               System.out.println("-----------");
               System.out.println("> Início > Séries\n");
               System.out.println("1) Adicionar");
               System.out.println("2) Buscar");
               System.out.println("3) Alterar");
               System.out.println("4) Excluir");
               System.out.println("5) Listar Séries");
               System.out.println("0) Retornar");

               System.out.print("\nOpção: ");
               try {
                    opition = Integer.valueOf(scanner.nextLine());
               } catch (Exception e) {
                    opition = -1;
               }

               switch (opition) {
                    case 1:
                         addSerie();
                         break;
                    case 2:
                         buscarSerie();
                         break;
                    case 3:
                         alterarSerie();
                         break;
                    case 4:
                         excluirSerie();
                         break;
                    case 5:
                         listarSeries();
                         break;
                    case 0:
                         break;
                    default:
                         System.out.println("Opção inválida!");
                         break;
               }
          } while (opition != 0);
     }

     public void addSerie() {
          System.out.println("\nAdicionar Série");
          String nome = "";
          LocalDate lancamento = null;
          String sinopse = "";
          String streaming = "";
          boolean dadosCorretos = false;
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

          dadosCorretos = false;
          do {
               System.out.print("Nome da série (Mínimo 3 letras): ");
               nome = scanner.nextLine();
               if (nome.length() >= 3)
                    dadosCorretos = true;
               else
                    System.err.println("O nome da série deve ter no mínimo 3 caracteres.");
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
               System.out.print("Sinópse: ");
               sinopse = scanner.nextLine();
               if (sinopse.length() > 20 && sinopse.length() < 500)
                    dadosCorretos = true;
               else
                    System.err.println(
                              "Escreva a sinópse dentro dos padrões definidos (mínimo 20, máximo 500 caracteres).");
          } while (!dadosCorretos);

          dadosCorretos = false;
          System.out.println("\nEscolha a plataforma de streaming:");
          for (int i = 0; i < STREAMINGS.length; i++) {
               System.out.printf("%d) %s%n", i + 1, STREAMINGS[i]);
          }
          int escolha = -1;
          do {
               System.out.print("Opção: ");
               try {
                    escolha = Integer.parseInt(scanner.nextLine());
                    if (escolha >= 1 && escolha <= STREAMINGS.length) {
                         streaming = STREAMINGS[escolha - 1];
                    } else {
                         System.err.println("Escolha inválida.");
                         escolha = -1;
                    }
               } catch (NumberFormatException e) {
                    System.err.println("Digite um número válido.");
               }
          } while (escolha == -1);

          System.out.print("\nConfirma a inclusão da série? (S/N): ");
          char resp = scanner.nextLine().charAt(0);
          if (resp == 'S' || resp == 's') {
               try {
                    Serie s = new Serie(nome, lancamento, sinopse, streaming);
                    arqSerie.create(s);
                    System.out.println("Série incluida com sucesso!");

               } catch (Exception e) {
                    System.out.println("Erro de sistema. Não foi possível incluir a série");
                    e.printStackTrace();
               }
          }
     }

     public void buscarSerie() {
          System.out.println("\nBuscar Série");
          System.out.println("\n\nNome da Série: ");
          String nome = scanner.nextLine();

          if (nome.isEmpty())
               return;

          try {
               Serie[] series = arqSerie.readNome(nome);
               if (series.length > 0) {
                    int n = 1;
                    for (Serie s : series) {
                         System.out.println((n++) + ") " + s.getName());
                    }
                    System.out.println("Escolha uma Série: ");
                    int op;
                    do {
                         try {
                              op = Integer.valueOf(scanner.nextLine());
                         } catch (NumberFormatException e) {
                              op = -1;
                         }
                         if (op <= 0 || op > n - 1)
                              System.out.println("Escolha um número entre 1 e " + (n - 1));
                    } while (op <= 0 || op > n - 1);
                    mostraSerie(series[op - 1]);
               } else {
                    System.out.println("Nenhuma série encontrada.");
               }
          } catch (Exception e) {
               System.out.println("Erro do sistema. Não foi possível buscar a série!");
               e.printStackTrace();
          }
     }

     public void mostraSerie(Serie serie) {
          if (serie != null) {
               System.out.println("----------------------");
               System.out.printf("Nome.........: %s%n", serie.getName());
               System.out.printf("Sinopse......: %s%n", serie.getSinopse());
               System.out.printf("Streaming....: %s%n", serie.getStreaming());
               System.out.printf("Lançamento...: %s%n",
                         serie.getLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
               System.out.println("----------------------");
          }
     }

     public void alterarSerie() {
          System.out.println("\nAlterção de Série");
          String nome;
          Boolean dadosCorretos;

          dadosCorretos = false;
          do {
               System.out.print("Nome da série (mínimo 3 caractéres): ");
               nome = scanner.nextLine();
               if (nome.isEmpty())
                    return;
               if (nome.length() >= 3)
                    dadosCorretos = true;
               else
                    System.out.println("Nome inválido. O nome da série deve conter pelomenos 3 caractéres.");
          } while (!dadosCorretos);

          try {
               Serie[] serie = arqSerie.readNome(nome);
               if (serie.length > 0) {
                    int op = 0;
                    if (serie.length > 1) {
                         int n = 1;

                         for (Serie s : serie) {
                              System.out.println((n++) + ") " + s.getName());
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
                    } else if (serie.length == 1) {
                         op = 1;
                    }

                    // Alteração Nome
                    String novoNome;
                    dadosCorretos = false;
                    do {
                         System.out.print("\nNovo nome da série (deixe em branco para manter o anterior): ");
                         novoNome = scanner.nextLine();
                         if (!novoNome.isEmpty()) {
                              if (nome.length() >= 3) {
                                   serie[op - 1].setName(novoNome);
                                   dadosCorretos = true;
                              } else
                                   System.err.println("O nome da série deve ter no mínimo 3 caracteres.");
                         } else
                              dadosCorretos = true;
                    } while (!dadosCorretos);
                    // Alteração Data de Lançamento
                    String novaData;
                    dadosCorretos = false;
                    do {
                         System.out.print(
                                   "\nNova data de lançamento da série (DD/MM/YYYY) (deixe em branco para manter o anterior): ");
                         novaData = scanner.nextLine();
                         if (!novaData.isEmpty()) {
                              try {
                                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                   serie[op - 1].setLancamento(LocalDate.parse(novaData, formatter));
                              } catch (Exception e) {
                                   System.err.println("Data inválida. Valor mantido.");
                              }
                         } else
                              dadosCorretos = true;
                    } while (!dadosCorretos);
                    // Alteração Sinopse
                    String novaSinopse;
                    dadosCorretos = false;
                    do {
                         System.out.print("Nova sinópse da série (deixe em branco para manter o anterior): ");
                         novaSinopse = scanner.nextLine();
                         if (!novaSinopse.isEmpty()) {
                              if (novaSinopse.length() > 20 && novaSinopse.length() < 500) {
                                   serie[op - 1].setSinopse(novaSinopse);
                                   dadosCorretos = true;
                              } else
                                   System.err.println(
                                             "Escreva a sinópse dentro dos padrões definidos (mínimo 20, máximo 500 caracteres).");
                         } else
                              dadosCorretos = true;
                    } while (!dadosCorretos);
                    // Alteração Streaming
                    System.out.println("\nEscolha o novo streaming (ou 0 para manter o atual):");
                    for (int i = 0; i < STREAMINGS.length; i++) {
                         System.out.printf("%d) %s%n", i + 1, STREAMINGS[i]);
                    }
                    int escolha = -1;
                    do {
                         System.out.print("Opção: ");
                         try {
                              escolha = Integer.parseInt(scanner.nextLine());
                              if (escolha == 0)
                                   break;
                              if (escolha >= 1 && escolha <= STREAMINGS.length) {
                                   serie[op - 1].setStreaming(STREAMINGS[escolha - 1]);
                                   break;
                              } else {
                                   System.err.println("Escolha inválida.");
                              }
                         } catch (NumberFormatException e) {
                              System.err.println("Digite um número válido.");
                         }
                    } while (true);

                    // confirmação
                    System.out.print("\nDeseja confirmar as alterações? (S/N): ");
                    char resp = scanner.next().charAt(0);
                    if (resp == 'S' || resp == 's') {
                         boolean alterado = arqSerie.update(serie[op - 1]);
                         if (alterado)
                              System.out.println("Série alterado com sucesso.");
                         else
                              System.out.println("Erro ao alterar a série.");
                    } else {
                         System.out.println("Alterações canceladas.");
                    }
                    scanner.nextLine();
               } else {
                    System.out.println("Série não encontrada.");
               }
          } catch (Exception e) {
               System.out.println("Erro do sistema. Não foi possuvel buscar as séries!");
               e.printStackTrace();
          }
     }

     public void excluirSerie() {
          System.out.println("\n Exclusão de Série");

          String nome;
          Boolean dadosCorretos;

          dadosCorretos = false;
          do {
               System.out.print("Nome da série (mínimo 3 caractéres): ");
               nome = scanner.nextLine();
               if (nome.isEmpty())
                    return;
               if (nome.length() >= 3)
                    dadosCorretos = true;
               else
                    System.out.println("Nome inválido!");
          } while (!dadosCorretos);

          try {
               Serie[] serie = arqSerie.readNome(nome);
               if (serie.length > 0) {
                    int op = 0;
                    if (serie.length > 1) {
                         int n = 1;

                         for (Serie s : serie) {
                              System.out.println((n++) + ") " + s.getName());
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
                    } else if (serie.length == 1) {
                         op = 1;
                    }
                    mostraSerie(serie[op - 1]);

                    System.out.print("\nConfirma a exclusão da série? (S/N) ");
                    char resp = scanner.next().charAt(0);
                    scanner.nextLine();
                    if (resp == 'S' || resp == 's') {
                         boolean excluido = arqSerie.delete(nome);
                         if (excluido) {
                              System.out.println("Série excluída com sucesso.");
                         } else {
                              System.out.println("Erro ao excluir a Série.");
                         }
                    } else {
                         System.out.println("Exclusão cancelada.");
                    }
               } else {
                    System.out.println("Série não encontrada.");
               }
          } catch (Exception e) {
               System.out.println("Erro do sistema. Não foi possível excluir a série");
               e.printStackTrace();
          }
     }

     public void listarSeries() {
          System.out.println("\nListar todas as Séries:");
          try {
               ArrayList<Serie> series = arqSerie.readAll();

               if(series.isEmpty()) {
                    System.out.println("Nenhuma série foi encontrada!");
                    return;
               }

               for(Serie serie : series) {
                    mostraSerie(serie);
               }
          } catch (Exception e) {
               System.out.println("Erro ao listar todas as séries.");
               e.printStackTrace();
          }
     }
}
