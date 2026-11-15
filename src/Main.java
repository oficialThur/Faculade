import dao.AlunoDAO;
import dao.CursoDAO;
import dao.DatabaseConnection;
import dao.DisciplinaDAO;
import menu.Menu;

/**
 * Ponto de entrada do sistema.
 * 1) Estabelece a conexão com o banco de dados via Singleton;
 * 2) Garante que as tabelas necessárias existam (cursos -> disciplinas -> alunos);
 * 3) Abre o Menu principal interativo;
 * 4) Encerra a conexão de forma segura ao escolher 'SAIR'.
 */
public class Main {


    public static void main(String[] args) {
        System.out.println("Iniciando Sistema Acadêmico...");

        try {
            DatabaseConnection.getConnection();
            CursoDAO.CreateTable();
            DisciplinaDAO.CreateTable();
            AlunoDAO.CreateTable();

            Menu.Show();

        } catch (Exception e) {
            System.err.println("Erro fatal durante a execução: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
