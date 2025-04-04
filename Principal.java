import java.util.Scanner;
import visao.MenuSeries;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner;
        try {
            scanner = new Scanner(System.in);
            int opition;
            do{
                System.out.println("PUCFlix 1.0");
                System.out.println( "-----------");
                System.out.println("> Início\n");
                System.out.println( "1) Séries");
                System.out.println( "2) Episódios");
                System.out.println( "3) Atores");
                System.out.println( "0) Sair");

                System.out.print("\nOpção: ");
                try{
                    opition = Integer.valueOf(scanner.nextLine());
                } catch(NumberFormatException e) {
                    opition = -1;
                }

                switch(opition) {
                    case 1:
                        (new MenuSeries()).menu();
                        break;
                    case 2:
                        // (new MenuEpsodios()).menu();
                        break;
                    case 3:
                        //Mostrar Atores
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } while (opition != 0);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
