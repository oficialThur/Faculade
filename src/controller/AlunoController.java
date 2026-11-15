package controller;

import dao.AlunoDAO;
import dao.CursoDAO;
import model.Aluno;
import model.Curso;
import view.AlunoView;
import view.CursoView;
import view.InputHelper;

import java.util.List;
import java.util.Optional;

/**
 * Controller de Aluno.
 */
public class AlunoController {

    public static void Create() {
        List<Curso> cursos = CursoDAO.ListAll();
        if (cursos.isEmpty()) {
            AlunoView.Message("Nenhum curso cadastrado! Cadastre um curso primeiro.");
            return;
        }

        Aluno aluno = new Aluno();
        AlunoView.Create(aluno);

        if (aluno.getMatricula() <= 0) {
            AlunoView.Message("Matrícula inválida! Deve ser um número positivo.");
            return;
        }

        if (aluno.getIdade() <= 0) {
            AlunoView.Message("Idade inválida!");
            return;
        }

        if (AlunoDAO.Read(aluno.getMatricula()).isPresent()) {
            AlunoView.Message("Já existe um aluno com a matrícula " + aluno.getMatricula() + "!");
            return;
        }

        CursoView.ShowAll(cursos);
        int codCurso = InputHelper.readInt("Informe o código do curso do aluno: ");
        Optional<Curso> cursoEscolhido = CursoDAO.Read(codCurso);

        if (cursoEscolhido.isEmpty()) {
            AlunoView.Message("Curso não encontrado! Cadastro cancelado.");
            return;
        }

        aluno.setCurso(cursoEscolhido.get());
        AlunoDAO.Create(aluno);
        AlunoView.Message("Aluno cadastrado com sucesso!");
    }

    public static void Read() {
        int matricula = AlunoView.Read();
        Optional<Aluno> aluno = AlunoDAO.Read(matricula);

        if (aluno.isPresent()) {
            AlunoView.Show(aluno.get());
        } else {
            AlunoView.Message("Aluno não encontrado!");
        }
    }

    public static void Update() {
        int matricula = AlunoView.Read();
        Optional<Aluno> alunoOpt = AlunoDAO.Read(matricula);

        if (alunoOpt.isPresent()) {
            Aluno aluno = alunoOpt.get();
            AlunoView.Update(aluno);

            if (aluno.getCurso() != null) {
                System.out.println("Curso atual: [" + aluno.getCurso().getCodigo() + "] " + aluno.getCurso().getNome());
            }
            String trocar = InputHelper.readOptionalString("Deseja trocar o curso do aluno? (s/N)", "N");
            if (trocar.equalsIgnoreCase("s")) {
                CursoView.ShowAll(CursoDAO.ListAll());
                int novoCodCurso = InputHelper.readInt("Novo código do curso: ");
                Optional<Curso> novoCurso = CursoDAO.Read(novoCodCurso);
                if (novoCurso.isPresent()) {
                    aluno.setCurso(novoCurso.get());
                } else {
                    AlunoView.Message("Curso não encontrado, mantendo o curso anterior.");
                }
            }

            AlunoDAO.Update(aluno);
            AlunoView.Message("Aluno atualizado com sucesso!");
        } else {
            AlunoView.Message("Aluno não encontrado!");
        }
    }

    public static void Delete() {
        int matricula = AlunoView.Read();
        if (AlunoDAO.Delete(matricula)) {
            AlunoView.Message("Aluno excluído com sucesso!");
        } else {
            AlunoView.Message("Aluno não encontrado!");
        }
    }
}
