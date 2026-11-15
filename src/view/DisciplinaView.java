package view;

import model.Curso;
import model.Disciplina;

/**
 * Camada de visualização (View) para Disciplina.
 */
public class DisciplinaView {

    public static void Create(Disciplina disciplina) {
        System.out.println("\n=== Cadastro de Disciplina ===");
        int codigo = InputHelper.readInt("Código da disciplina: ");
        String nome = InputHelper.readString("Nome da disciplina: ");
        int cargaHoraria = InputHelper.readInt("Carga horária (horas): ");

        disciplina.setCodigo(codigo);
        disciplina.setNome(nome);
        disciplina.setCargaHoraria(cargaHoraria);
    }

    public static int Read() {
        return InputHelper.readInt("Informe o código da disciplina: ");
    }

    public static void Update(Disciplina d) {
        System.out.println("\n=== Atualização de Disciplina (pressione Enter para manter o atual) ===");
        String novoNome = InputHelper.readOptionalString("Novo nome", d.getNome());
        int novaCH = InputHelper.readOptionalInt("Nova carga horária", d.getCargaHoraria());

        d.setNome(novoNome);
        d.setCargaHoraria(novaCH);
    }

    public static void Show(Disciplina d) {
        System.out.println("\n-------------------------------------------");
        System.out.println("Código:        " + d.getCodigo());
        System.out.println("Nome:          " + d.getNome());
        System.out.println("Carga Horária: " + d.getCargaHoraria() + "h");
        Curso curso = d.getCurso();
        if (curso != null) {
            System.out.println("Curso:         [" + curso.getCodigo() + "] " + curso.getNome() + " (" + curso.getTurno() + ")");
        } else {
            System.out.println("Curso:         Nenhum vinculado");
        }
        System.out.println("-------------------------------------------");
    }

    public static void Message(String msg) {
        System.out.println(msg);
    }
}
