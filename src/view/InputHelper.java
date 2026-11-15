package view;

import java.util.Scanner;

/**
 * Centraliza a leitura do teclado, resolvendo os problemas comuns do Scanner.
 */
public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);

    public static Scanner getScanner() {
        return scanner;
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número inteiro.");
            }
        }
    }

    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("O campo não pode ficar vazio! Digite novamente.");
        }
    }

    public static String readOptionalString(String prompt, String valorAtual) {
        System.out.print(prompt + " (" + valorAtual + "): ");
        String line = scanner.nextLine().trim();
        return line.isEmpty() ? valorAtual : line;
    }

    public static int readOptionalInt(String prompt, int valorAtual) {
        while (true) {
            System.out.print(prompt + " (" + valorAtual + "): ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                return valorAtual;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número inteiro ou aperte Enter para manter.");
            }
        }
    }
}
