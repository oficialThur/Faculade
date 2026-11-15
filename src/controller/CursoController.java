package controller;

import dao.CursoDAO;
import model.Curso;
import view.CursoView;

import java.util.Optional;

/**
 * Controller de Curso: orquestra a comunicação entre CursoView e CursoDAO.
 */
public class CursoController {

    public static void Create() {
        Curso curso = new Curso();
        CursoView.Create(curso);

        if (curso.getCodigo() <= 0) {
            CursoView.Message("Código de curso inválido! Deve ser um número positivo.");
            return;
        }

        if (CursoDAO.Read(curso.getCodigo()).isPresent()) {
            CursoView.Message("Já existe um curso com o código " + curso.getCodigo() + "!");
            return;
        }

        CursoDAO.Create(curso);
        CursoView.Message("Curso cadastrado com sucesso!");
    }

    public static void Read() {
        int codigo = CursoView.Read();
        Optional<Curso> curso = CursoDAO.Read(codigo);

        if (curso.isPresent()) {
            CursoView.Show(curso.get());
        } else {
            CursoView.Message("Curso não encontrado!");
        }
    }

    public static void Update() {
        int codigo = CursoView.Read();
        Optional<Curso> cursoOpt = CursoDAO.Read(codigo);

        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            CursoView.Update(curso);
            CursoDAO.Update(curso);
            CursoView.Message("Curso atualizado com sucesso!");
        } else {
            CursoView.Message("Curso não encontrado!");
        }
    }

    public static void Delete() {
        int codigo = CursoView.Read();
        if (CursoDAO.Delete(codigo)) {
            CursoView.Message("Curso excluído com sucesso!");
        } else {
            CursoView.Message("Não foi possível excluir o curso (não encontrado ou possui alunos/disciplinas vinculados).");
        }
    }
}
