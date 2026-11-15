package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Padrão Singleton para gerenciar a conexão com o banco de dados MySQL.
 * Mantém uma única instância de Connection durante toda a execução.
 * Configurado para o Docker rodando em localhost:3307 com senha 'root'.
 */
public class DatabaseConnection {

    private static Connection connection = null;
    private static final String HOST = "jdbc:mysql://localhost:3307/";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static final String DATABASE = "faculdade?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private DatabaseConnection() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DatabaseConnection.class) {
                if (connection == null) {
                    try {
                        System.out.println("Conectando ao banco de dados...");
                        connection = DriverManager.getConnection(HOST + DATABASE, DB_USER, DB_PASS);
                        System.out.println("Conexão bem-sucedida!");
                    } catch (SQLException e) {
                        System.out.println("Erro ao conectar: " + e.getMessage());
                        throw new RuntimeException("Falha ao conectar ao banco de dados", e);
                    }
                }
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Conexão fechada.");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
