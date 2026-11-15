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
 * DAO de Disciplina: operações no MySQL com chave estrangeira para Curso.
 */
public class DisciplinaDAO {

    public static List<Disciplina> disciplinaList = new ArrayList<>();

    public static void CreateTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS disciplinas (
                    codigo INT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    carga_horaria INT NOT NULL,
                    curso_codigo INT NOT NULL,
                    FOREIGN KEY (curso_codigo) REFERENCES cursos(codigo)
                );
                """;

        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar a tabela 'disciplinas': " + e.getMessage());
            throw new RuntimeException("Erro ao criar a tabela disciplinas", e);
        }
    }

    public static void Create(Disciplina disciplina) {
        if (disciplina.getCurso() == null) {
            System.out.println("A disciplina precisa pertencer a um curso!");
            return;
        }

        String sql = "INSERT INTO disciplinas (codigo, nome, carga_horaria, curso_codigo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, disciplina.getCodigo());
            stmt.setString(2, disciplina.getNome());
            stmt.setInt(3, disciplina.getCargaHoraria());
            stmt.setInt(4, disciplina.getCurso().getCodigo());
            stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Código de disciplina já existe ou o curso informado não existe!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar disciplina: " + e.getMessage());
            throw new RuntimeException("Erro ao cadastrar disciplina no banco de dados", e);
        }
    }

    public static Optional<Disciplina> Read(int codigo) {
        String sql = "SELECT codigo, nome, carga_horaria, curso_codigo FROM disciplinas WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Disciplina disciplina = new Disciplina(
                            rs.getInt("codigo"),
                            rs.getString("nome"),
                            rs.getInt("carga_horaria")
                    );
                    Optional<Curso> curso = CursoDAO.Read(rs.getInt("curso_codigo"));
                    curso.ifPresent(disciplina::setCurso);
                    return Optional.of(disciplina);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar disciplina: " + e.getMessage());
            throw new RuntimeException("Erro ao consultar disciplina no banco de dados", e);
        }

        return Optional.empty();
    }

    public static void Update(Disciplina disciplina) {
        String sql = "UPDATE disciplinas SET nome = ?, carga_horaria = ?, curso_codigo = ? WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, disciplina.getNome());
            stmt.setInt(2, disciplina.getCargaHoraria());
            stmt.setInt(3, disciplina.getCurso().getCodigo());
            stmt.setInt(4, disciplina.getCodigo());
            stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("O curso informado não existe!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar disciplina: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar disciplina no banco de dados", e);
        }
    }

    public static boolean Delete(int codigo) {
        String sql = "DELETE FROM disciplinas WHERE codigo = ?";

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao remover disciplina: " + e.getMessage());
            throw new RuntimeException("Erro ao remover disciplina do banco de dados", e);
        }
    }

    public static List<Disciplina> ListByCurso(int codCurso) {
        String sql = "SELECT codigo, nome, carga_horaria FROM disciplinas WHERE curso_codigo = ? ORDER BY codigo";
        List<Disciplina> resultado = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codCurso);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new Disciplina(
                            rs.getInt("codigo"),
                            rs.getString("nome"),
                            rs.getInt("carga_horaria")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar disciplinas do curso: " + e.getMessage());
            throw new RuntimeException("Erro ao listar disciplinas do banco de dados", e);
        }

        return resultado;
    }

    public static List<Disciplina> ListAll() {
        String sql = "SELECT codigo FROM disciplinas ORDER BY codigo";
        List<Disciplina> resultado = new ArrayList<>();

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
            System.out.println("Erro ao listar disciplinas: " + e.getMessage());
            throw new RuntimeException("Erro ao listar disciplinas do banco de dados", e);
        }

        disciplinaList = resultado;
        return disciplinaList;
    }
}
