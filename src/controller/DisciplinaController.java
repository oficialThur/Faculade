package controller;

import dao.CursoDAO;
import dao.DisciplinaDAO;
import model.Curso;
import model.Disciplina;
import view.CursoView;
import view.DisciplinaView;
import view.InputHelper;

import java.util.List;
import java.util.Optional;

/**
 * Controller de Disciplina.
 */
public class DisciplinaController {

    public static void Create() {
        List<Curso> cursos = CursoDAO.ListAll();
        if (cursos.isEmpty()) {
            DisciplinaView.Message("Nenhum curso cadastrado! Cadastre um curso primeiro.");
            return;
        }

        Disciplina disciplina = new Disciplina();
        DisciplinaView.Create(disciplina);

        if (disciplina.getCodigo() <= 0) {
            DisciplinaView.Message("Código da disciplina inválido! Deve ser um número positivo.");
            return;
        }

        if (DisciplinaDAO.Read(disciplina.getCodigo()).isPresent()) {
            DisciplinaView.Message("Já existe uma disciplina com o código " + disciplina.getCodigo() + "!");
            return;
        }

        CursoView.ShowAll(cursos);
        int codCurso = InputHelper.readInt("Informe o código do curso desta disciplina: ");
        Optional<Curso> cursoEscolhido = CursoDAO.Read(codCurso);

        if (cursoEscolhido.isEmpty()) {
            DisciplinaView.Message("Curso não encontrado! Cadastro cancelado.");
            return;
        }

        disciplina.setCurso(cursoEscolhido.get());
        DisciplinaDAO.Create(disciplina);
        DisciplinaView.Message("Disciplina cadastrada com sucesso!");
    }

    public static void Read() {
        int codigo = DisciplinaView.Read();
        Optional<Disciplina> d = DisciplinaDAO.Read(codigo);

        if (d.isPresent()) {
            DisciplinaView.Show(d.get());
        } else {
            DisciplinaView.Message("Disciplina não encontrada!");
        }
    }

    public static void Update() {
        int codigo = DisciplinaView.Read();
        Optional<Disciplina> dOpt = DisciplinaDAO.Read(codigo);

        if (dOpt.isPresent()) {
            Disciplina d = dOpt.get();
            DisciplinaView.Update(d);

            System.out.println("Curso atual: [" + d.getCurso().getCodigo() + "] " + d.getCurso().getNome());
            String trocar = InputHelper.readOptionalString("Deseja trocar o curso da disciplina? (s/N)", "N");
            if (trocar.equalsIgnoreCase("s")) {
                CursoView.ShowAll(CursoDAO.ListAll());
                int novoCodCurso = InputHelper.readInt("Novo código do curso: ");
                Optional<Curso> novoCurso = CursoDAO.Read(novoCodCurso);
                if (novoCurso.isPresent()) {
                    d.setCurso(novoCurso.get());
                } else {
                    DisciplinaView.Message("Curso não encontrado, mantendo o curso anterior.");
                }
            }

            DisciplinaDAO.Update(d);
            DisciplinaView.Message("Disciplina atualizada com sucesso!");
        } else {
            DisciplinaView.Message("Disciplina não encontrada!");
        }
    }

    public static void Delete() {
        int codigo = DisciplinaView.Read();
        if (DisciplinaDAO.Delete(codigo)) {
            DisciplinaView.Message("Disciplina excluída com sucesso!");
        } else {
            DisciplinaView.Message("Disciplina não encontrada!");
        }
    }
}
