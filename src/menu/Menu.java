package menu;

import view.InputHelper;

public class Menu {

    public static void Show() {
        int opcao = -1;

        do {
            System.out.println("\n==================================");
            System.out.println("     SISTEMA ACADÊMICO - POO      ");
            System.out.println("==================================");
            System.out.println("1 - Gerenciar ALUNOS");
            System.out.println("2 - Gerenciar DISCIPLINAS");
            System.out.println("3 - Gerenciar CURSOS");
            System.out.println("4 - SAIR");
            System.out.println("==================================");

            opcao = InputHelper.readInt("Opção: ");

            switch (opcao) {
                case 1 -> SubmenuAluno.Show();
                case 2 -> SubmenuDisciplina.Show();
                case 3 -> SubmenuCurso.Show();
                case 4 -> System.out.println("\nEncerrando o sistema. Até logo!");
                default -> System.out.println("Opção inválida! Escolha entre 1 e 4.");
            }
        } while (opcao != 4);
    }
}
