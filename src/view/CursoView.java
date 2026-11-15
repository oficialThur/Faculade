package view;

import model.Curso;
import model.Disciplina;

import java.util.List;

/**
 * Camada de visualização (View) para Curso.
 */
public class CursoView {

    public static void Create(Curso curso) {
        System.out.println("\n=== Cadastro de Curso ===");
        int codigo = InputHelper.readInt("Código do curso: ");
        String nome = InputHelper.readString("Nome do curso: ");
        String turno = InputHelper.readString("Turno (ex: Matutino, Vespertino, Noturno): ");

        curso.setCodigo(codigo);
        curso.setNome(nome);
        curso.setTurno(turno);
    }

    public static int Read() {
        return InputHelper.readInt("Informe o código do curso: ");
    }

    public static void Update(Curso curso) {
        System.out.println("\n=== Atualização de Curso (pressione Enter para manter o atual) ===");
        String novoNome = InputHelper.readOptionalString("Novo nome", curso.getNome());
        String novoTurno = InputHelper.readOptionalString("Novo turno", curso.getTurno());

        curso.setNome(novoNome);
        curso.setTurno(novoTurno);
    }

    public static void Show(Curso curso) {
        System.out.println("\n-------------------------------------------");
        System.out.println("Código: " + curso.getCodigo());
        System.out.println("Nome:   " + curso.getNome());
        System.out.println("Turno:  " + curso.getTurno());

        List<Disciplina> disciplinas = curso.getDisciplinas();
        if (disciplinas == null || disciplinas.isEmpty()) {
            System.out.println("Disciplinas: Nenhuma cadastrada neste curso.");
        } else {
            System.out.println("Disciplinas (" + disciplinas.size() + "):");
            for (Disciplina d : disciplinas) {
                System.out.println("  - [" + d.getCodigo() + "] " + d.getNome() + " (" + d.getCargaHoraria() + "h)");
            }
        }
        System.out.println("-------------------------------------------");
    }

    public static void ShowAll(List<Curso> cursos) {
        if (cursos == null || cursos.isEmpty()) {
            System.out.println("Nenhum curso cadastrado.");
            return;
        }
        System.out.println("\n=== Cursos Cadastrados ===");
        for (Curso c : cursos) {
            int totalDisc = (c.getDisciplinas() != null) ? c.getDisciplinas().size() : 0;
            System.out.println("  [" + c.getCodigo() + "] " + c.getNome() + " - Turno: " + c.getTurno() + " (" + totalDisc + " disciplinas)");
        }
    }

    public static void Message(String msg) {
        System.out.println(msg);
    }
}
