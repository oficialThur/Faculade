package dao;

import model.Curso;
import model.Disciplina;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO de Curso: operações no MySQL com espelho em List<Curso>.
 */
public class CursoDAO {

    public static List<Curso> cursoList = new ArrayList<>();

    public static void CreateTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS cursos (
                    codigo INT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    turno VARCHAR(30) NOT NULL
                );
                """;

        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar a tabela 'cursos': " + e.getMessage());
            throw new RuntimeException("Erro ao criar a tabela cursos", e);
        }
    }

    public static void Create(Curso curso) {
        String sql = "INSERT INTO cursos (codigo, nome, turno) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, curso.getCodigo());
            stmt.setString(2, curso.getNome());
            stmt.setString(3, curso.getTurno());
            stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Já existe um curso com o código " + curso.getCodigo() + "!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar curso: " + e.getMessage());
            throw new RuntimeException("Erro ao cadastrar curso no banco de dados", e);
        }
    }

    public static Optional<Curso> Read(int codigo) {
        String sql = "SELECT codigo, nome, turno FROM cursos WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Curso curso = new Curso(
                            rs.getInt("codigo"),
                            rs.getString("nome"),
                            rs.getString("turno")
                    );
                    for (Disciplina d : DisciplinaDAO.ListByCurso(codigo)) {
                        curso.adicionarDisciplina(d);
                    }
                    return Optional.of(curso);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar curso: " + e.getMessage());
            throw new RuntimeException("Erro ao consultar curso no banco de dados", e);
        }

        return Optional.empty();
    }

    public static void Update(Curso curso) {
        String sql = "UPDATE cursos SET nome = ?, turno = ? WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getTurno());
            stmt.setInt(3, curso.getCodigo());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar curso: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar curso no banco de dados", e);
        }
    }

    public static boolean Delete(int codigo) {
        String sql = "DELETE FROM cursos WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            return stmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Não é possível remover: existem disciplinas ou alunos neste curso!");
            return false;
        } catch (SQLException e) {
            System.out.println("Erro ao remover curso: " + e.getMessage());
            throw new RuntimeException("Erro ao remover curso do banco de dados", e);
        }
    }

    public static List<Curso> ListAll() {
        String sql = "SELECT codigo FROM cursos ORDER BY codigo";
        List<Curso> resultado = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Integer> codigos = new ArrayList<>();
            while (rs.next()) {
                codigos.add(rs.getInt("codigo"));
            }
            for (int codigo : codigos) {
                Read(codigo).ifPresent(resultado::add);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar cursos: " + e.getMessage());
            throw new RuntimeException("Erro ao listar cursos do banco de dados", e);
        }

        cursoList = resultado;
        return cursoList;
    }
}
