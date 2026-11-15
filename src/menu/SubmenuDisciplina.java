package menu;

import controller.DisciplinaController;
import view.InputHelper;

public class SubmenuDisciplina {

    public static void Show() {
        int opcao = -1;

        do {
            System.out.println("\n--- Gerenciar DISCIPLINAS ---");
            System.out.println("1 - Cadastrar DISCIPLINA");
            System.out.println("2 - Consultar DISCIPLINA");
            System.out.println("3 - Remover DISCIPLINA");
            System.out.println("4 - Atualizar DISCIPLINA");
            System.out.println("5 - Voltar ao MENU INICIAL");
            System.out.println("-----------------------------");

            opcao = InputHelper.readInt("Opção: ");

            switch (opcao) {
                case 1 -> DisciplinaController.Create();
                case 2 -> DisciplinaController.Read();
                case 3 -> DisciplinaController.Delete();
                case 4 -> DisciplinaController.Update();
                case 5 -> System.out.println("Voltando ao Menu Inicial...");
                default -> System.out.println("Opção inválida! Escolha entre 1 e 5.");
            }
        } while (opcao != 5);
    }
}
