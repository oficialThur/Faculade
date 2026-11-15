package menu;

import controller.AlunoController;
import view.InputHelper;

public class SubmenuAluno {

    public static void Show() {
        int opcao = -1;

        do {
            System.out.println("\n--- Gerenciar ALUNOS ---");
            System.out.println("1 - Cadastrar ALUNO");
            System.out.println("2 - Consultar ALUNO");
            System.out.println("3 - Remover ALUNO");
            System.out.println("4 - Atualizar ALUNO");
            System.out.println("5 - Voltar ao MENU INICIAL");
            System.out.println("------------------------");

            opcao = InputHelper.readInt("Opção: ");

            switch (opcao) {
                case 1 -> AlunoController.Create();
                case 2 -> AlunoController.Read();
                case 3 -> AlunoController.Delete();
                case 4 -> AlunoController.Update();
                case 5 -> System.out.println("Voltando ao Menu Inicial...");
                default -> System.out.println("Opção inválida! Escolha entre 1 e 5.");
            }
        } while (opcao != 5);
    }
}
