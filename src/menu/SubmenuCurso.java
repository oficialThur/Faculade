package menu;

import controller.CursoController;
import view.InputHelper;

public class SubmenuCurso {

    public static void Show() {
        int opcao = -1;

        do {
            System.out.println("\n--- Gerenciar CURSOS ---");
            System.out.println("1 - Cadastrar CURSO");
            System.out.println("2 - Consultar CURSO");
            System.out.println("3 - Remover CURSO");
            System.out.println("4 - Atualizar CURSO");
            System.out.println("5 - Voltar ao MENU INICIAL");
            System.out.println("------------------------");

            opcao = InputHelper.readInt("Opção: ");

            switch (opcao) {
                case 1 -> CursoController.Create();
                case 2 -> CursoController.Read();
                case 3 -> CursoController.Delete();
                case 4 -> CursoController.Update();
                case 5 -> System.out.println("Voltando ao Menu Inicial...");
                default -> System.out.println("Opção inválida! Escolha entre 1 e 5.");
            }
        } while (opcao != 5);
    }
}
