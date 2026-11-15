package view;

import model.Aluno;
import model.Curso;
import model.Disciplina;

import java.util.List;

/**
 * Camada de visualização (View) para Aluno.
 * Exibe aluno com seu curso e grade completa de disciplinas.
 */
public class AlunoView {

    public static void Create(Aluno aluno) {
        System.out.println("\n=== Cadastro de Aluno ===");
        int matricula = InputHelper.readInt("Matrícula: ");
        String nome = InputHelper.readString("Nome completo: ");
        int idade = InputHelper.readInt("Idade: ");

        aluno.setMatricula(matricula);
        aluno.setNome(nome);
        aluno.setIdade(idade);
    }

    public static int Read() {
        return InputHelper.readInt("Informe a matrícula do aluno: ");
    }

    public static void Update(Aluno aluno) {
        System.out.println("\n=== Atualização de Aluno (pressione Enter para manter o atual) ===");
        String novoNome = InputHelper.readOptionalString("Novo nome", aluno.getNome());
        int novaIdade = InputHelper.readOptionalInt("Nova idade", aluno.getIdade());

        aluno.setNome(novoNome);
        aluno.setIdade(novaIdade);
    }

    public static void Show(Aluno aluno) {
        System.out.println("\n===========================================");
        System.out.println("DADOS DO ALUNO");
        System.out.println("-------------------------------------------");
        System.out.println("Matrícula: " + aluno.getMatricula());
        System.out.println("Nome:      " + aluno.getNome());
        System.out.println("Idade:     " + aluno.getIdade());

        Curso curso = aluno.getCurso();
        if (curso != null) {
            System.out.println("\nCURSO: [" + curso.getCodigo() + "] " + curso.getNome() + " (" + curso.getTurno() + ")");
            List<Disciplina> disciplinas = curso.getDisciplinas();
            if (disciplinas == null || disciplinas.isEmpty()) {
                System.out.println("  (Este curso ainda não possui disciplinas cadastradas)");
            } else {
                System.out.println("Disciplinas da grade (" + disciplinas.size() + "):");
                for (Disciplina d : disciplinas) {
                    System.out.println("  - [" + d.getCodigo() + "] " + d.getNome() + " (" + d.getCargaHoraria() + "h)");
                }
            }
        } else {
            System.out.println("\nCURSO: Nenhum curso vinculado");
        }
        System.out.println("===========================================");
    }

    public static void Message(String msg) {
        System.out.println(msg);
    }
}
