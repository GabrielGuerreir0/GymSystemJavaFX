package DAO;

import Model.Aula;
import Model.PersonalTrainer;
import db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AulaDAO {

    // Método para adicionar uma nova aula no banco de dados
    public void addAula(Aula aula) {
        String sql = "INSERT INTO aula (nome_aula, data, duracao, personal_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, aula.getNomeAula());
            stmt.setDate(2, aula.getData());
            stmt.setInt(3, aula.getDuracao());

            // Verificar se o Personal Trainer não é nulo e usar o ID correto de personal_trainer
            if (aula.getPersonalTrainer() != null) {
                stmt.setInt(4, aula.getPersonalTrainer().getId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);  // Se for nulo, inserir como NULL
            }

            stmt.executeUpdate();

            // Obter o ID gerado automaticamente
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                aula.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para atualizar uma aula existente no banco de dados
    public void updateAula(Aula aula) {
        String sql = "UPDATE aula SET nome_aula = ?, data = ?, duracao = ?, personal_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, aula.getNomeAula());
            stmt.setDate(2, aula.getData());
            stmt.setInt(3, aula.getDuracao());

            // Verificar se o Personal Trainer não é nulo e usar o ID correto de personal_trainer
            if (aula.getPersonalTrainer() != null) {
                stmt.setInt(4, aula.getPersonalTrainer().getId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.setInt(5, aula.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Método para deletar uma aula do banco de dados pelo ID
    public void deleteAula(int aulaId) {
        String sql = "DELETE FROM aula WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, aulaId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obter todas as aulas com informações de personal trainer
    public List<Aula> getAllAulas() {
        List<Aula> aulas = new ArrayList<>();
        String sql = "SELECT a.id, a.nome_aula, a.data, a.duracao, p.id AS personal_id, p.nome AS personal_nome " +
                "FROM aula a " +
                "LEFT JOIN personal_trainer pt ON a.personal_id = pt.id " +
                "LEFT JOIN pessoa p ON pt.pessoa_id = p.id";  // Associação com a tabela 'pessoa'

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Aula aula = new Aula();
                aula.setId(rs.getInt("id"));
                aula.setNomeAula(rs.getString("nome_aula"));
                aula.setData(rs.getDate("data"));
                aula.setDuracao(rs.getInt("duracao"));

                // Verificar se há um PersonalTrainer associado
                int personalId = rs.getInt("personal_id");
                if (personalId != 0) {
                    PersonalTrainer personal = new PersonalTrainer();
                    personal.setId(personalId);
                    personal.setNome(rs.getString("personal_nome"));
                    aula.setPersonalTrainer(personal);  // Associar o PersonalTrainer à Aula
                }

                aulas.add(aula);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aulas;
    }

    // Método para buscar aulas pelo nome
    public List<Aula> getAulasByName(String nome) {
        List<Aula> aulas = new ArrayList<>();
        String sql = "SELECT a.id, a.nome_aula, a.data, a.duracao, p.id AS personal_id, p.nome AS personal_nome " +
                "FROM aula a " +
                "LEFT JOIN personal_trainer pt ON a.personal_id = pt.id " +
                "LEFT JOIN pessoa p ON pt.pessoa_id = p.id " +
                "WHERE a.nome_aula LIKE ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Aula aula = new Aula();
                aula.setId(rs.getInt("id"));
                aula.setNomeAula(rs.getString("nome_aula"));
                aula.setData(rs.getDate("data"));
                aula.setDuracao(rs.getInt("duracao"));

                // Verificar se há um PersonalTrainer associado
                int personalId = rs.getInt("personal_id");
                if (personalId != 0) {
                    PersonalTrainer personal = new PersonalTrainer();
                    personal.setId(personalId);
                    personal.setNome(rs.getString("personal_nome"));
                    aula.setPersonalTrainer(personal);  // Associar o PersonalTrainer à Aula
                }

                aulas.add(aula);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aulas;
    }

    // Método para buscar uma aula pelo ID
    public Aula getAulaById(int id) {
        Aula aula = null;
        String sql = "SELECT a.id, a.nome_aula, a.data, a.duracao, pt.id AS personal_id, p.nome AS personal_nome " +
                "FROM aula a " +
                "LEFT JOIN personal_trainer pt ON a.personal_id = pt.id " +
                "LEFT JOIN pessoa p ON pt.pessoa_id = p.id " +
                "WHERE a.id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                aula = new Aula();
                aula.setId(rs.getInt("id"));
                aula.setNomeAula(rs.getString("nome_aula"));
                aula.setData(rs.getDate("data"));
                aula.setDuracao(rs.getInt("duracao"));

                // Verificar se há um PersonalTrainer associado
                int personalId = rs.getInt("personal_id");
                if (personalId != 0) {
                    PersonalTrainer personal = new PersonalTrainer();
                    personal.setId(personalId);
                    personal.setNome(rs.getString("personal_nome"));
                    aula.setPersonalTrainer(personal);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aula;
    }
}
