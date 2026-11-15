package dao;

import model.Aluno;
import model.Curso;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO de Aluno: operações no MySQL com chave estrangeira para Curso.
 */
public class AlunoDAO {

    public static List<Aluno> alunoList = new ArrayList<>();

    public static void CreateTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS alunos (
                    matricula INT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    idade INT NOT NULL,
                    curso_codigo INT NOT NULL,
                    FOREIGN KEY (curso_codigo) REFERENCES cursos(codigo)
                );
                """;

        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar a tabela 'alunos': " + e.getMessage());
            throw new RuntimeException("Erro ao criar a tabela alunos", e);
        }
    }

    public static void Create(Aluno aluno) {
        if (aluno.getCurso() == null) {
            System.out.println("O aluno precisa de um curso!");
            return;
        }

        String sql = "INSERT INTO alunos (matricula, nome, idade, curso_codigo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, aluno.getMatricula());
            stmt.setString(2, aluno.getNome());
            stmt.setInt(3, aluno.getIdade());
            stmt.setInt(4, aluno.getCurso().getCodigo());
            stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Matrícula já existe ou o curso informado não existe!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar aluno: " + e.getMessage());
            throw new RuntimeException("Erro ao cadastrar aluno no banco de dados", e);
        }
    }

    public static Optional<Aluno> Read(int matricula) {
        String sql = "SELECT matricula, nome, idade, curso_codigo FROM alunos WHERE matricula = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, matricula);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Aluno aluno = new Aluno(
                            rs.getInt("matricula"),
                            rs.getString("nome"),
                            rs.getInt("idade")
                    );
                    Optional<Curso> curso = CursoDAO.Read(rs.getInt("curso_codigo"));
                    curso.ifPresent(aluno::setCurso);
                    return Optional.of(aluno);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar aluno: " + e.getMessage());
            throw new RuntimeException("Erro ao consultar aluno no banco de dados", e);
        }

        return Optional.empty();
    }

    public static void Update(Aluno aluno) {
        String sql = "UPDATE alunos SET nome = ?, idade = ?, curso_codigo = ? WHERE matricula = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setInt(2, aluno.getIdade());
            stmt.setInt(3, aluno.getCurso().getCodigo());
            stmt.setInt(4, aluno.getMatricula());
            stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("O curso informado não existe!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar aluno: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar aluno no banco de dados", e);
        }
    }

    public static boolean Delete(int matricula) {
        String sql = "DELETE FROM alunos WHERE matricula = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, matricula);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao remover aluno: " + e.getMessage());
            throw new RuntimeException("Erro ao remover aluno do banco de dados", e);
        }
    }

    public static List<Aluno> ListAll() {
        String sql = "SELECT matricula FROM alunos ORDER BY matricula";
        List<Aluno> resultado = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Integer> matriculas = new ArrayList<>();
            while (rs.next()) {
                matriculas.add(rs.getInt("matricula"));
            }
            for (int matricula : matriculas) {
                Read(matricula).ifPresent(resultado::add);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar alunos: " + e.getMessage());
            throw new RuntimeException("Erro ao listar alunos do banco de dados", e);
        }

        alunoList = resultado;
        return alunoList;
    }
}
