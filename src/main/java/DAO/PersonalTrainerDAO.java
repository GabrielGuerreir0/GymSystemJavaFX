package DAO;

import Model.PersonalTrainer;
import db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonalTrainerDAO {

    // Método para adicionar PersonalTrainer
    public void addPersonalTrainer(PersonalTrainer personal) {
        String sqlPessoa = "INSERT INTO pessoa (nome, cpf, telefone, email) VALUES (?, ?, ?, ?)";
        String sqlPersonal = "INSERT INTO personal_trainer (pessoa_id, registro_profissional, especialidade) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmtPessoa = connection.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtPersonal = connection.prepareStatement(sqlPersonal)) {

            // Inserir na tabela pessoa
            stmtPessoa.setString(1, personal.getNome());
            stmtPessoa.setString(2, personal.getCPF());
            stmtPessoa.setString(3, personal.getTelefone());
            stmtPessoa.setString(4, personal.getEmail());
            stmtPessoa.executeUpdate();

            // Obter o ID da pessoa inserida
            ResultSet rs = stmtPessoa.getGeneratedKeys();
            if (rs.next()) {
                int pessoaId = rs.getInt(1);
                personal.setId(pessoaId);  // Definir o ID da pessoa no PersonalTrainer
            }

            // Inserir na tabela personal_trainer usando o pessoa_id correto
            stmtPersonal.setInt(1, personal.getId());
            stmtPersonal.setString(2, personal.getRegistroProfissional());
            stmtPersonal.setString(3, personal.getEspecialidade());
            stmtPersonal.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para deletar PersonalTrainer e a pessoa associada
    public void deletePersonalTrainer(int personalId) {
        String sqlPersonal = "DELETE FROM personal_trainer WHERE pessoa_id = ?";
        String sqlPessoa = "DELETE FROM pessoa WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmtPersonal = connection.prepareStatement(sqlPersonal);
             PreparedStatement stmtPessoa = connection.prepareStatement(sqlPessoa)) {

            // Excluir o registro da tabela personal_trainer
            stmtPersonal.setInt(1, personalId);
            stmtPersonal.executeUpdate();

            // Excluir o registro da tabela pessoa
            stmtPessoa.setInt(1, personalId);
            stmtPessoa.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para buscar todos os PersonalTrainers
    public List<PersonalTrainer> getAllPersonalTrainers() {
        List<PersonalTrainer> personalTrainers = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, pt.registro_profissional, pt.especialidade " +
                "FROM personal_trainer pt " +
                "JOIN pessoa p ON pt.pessoa_id = p.id";  // Usando 'pessoa_id', não 'personal_id'

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PersonalTrainer personal = new PersonalTrainer();
                personal.setId(rs.getInt("id"));
                personal.setNome(rs.getString("nome"));
                personal.setRegistroProfissional(rs.getString("registro_profissional"));
                personal.setEspecialidade(rs.getString("especialidade"));
                personalTrainers.add(personal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personalTrainers;
    }


    // Método para buscar PersonalTrainer por nome
    public List<PersonalTrainer> getPersonaisByName(String nome) {
        List<PersonalTrainer> personais = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, pt.registro_profissional, pt.especialidade " +
                "FROM personal_trainer pt " +
                "JOIN pessoa p ON pt.pessoa_id = p.id " +
                "WHERE p.nome LIKE ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PersonalTrainer personal = new PersonalTrainer();
                personal.setId(rs.getInt("id"));
                personal.setNome(rs.getString("nome"));
                personal.setRegistroProfissional(rs.getString("registro_profissional"));
                personal.setEspecialidade(rs.getString("especialidade"));
                personais.add(personal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personais;
    }

    // Método para buscar PersonalTrainer por ID
    public PersonalTrainer getPersonalTrainerById(int id) {
        PersonalTrainer personal = null;
        String sql = "SELECT p.id, p.nome, p.cpf, p.telefone, p.email, pt.registro_profissional, pt.especialidade " +
                "FROM personal_trainer pt " +
                "JOIN pessoa p ON pt.pessoa_id = p.id " +
                "WHERE p.id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                personal = new PersonalTrainer();
                personal.setId(rs.getInt("id"));
                personal.setNome(rs.getString("nome"));
                personal.setCPF(rs.getString("cpf"));
                personal.setTelefone(rs.getString("telefone"));
                personal.setEmail(rs.getString("email"));
                personal.setRegistroProfissional(rs.getString("registro_profissional"));
                personal.setEspecialidade(rs.getString("especialidade"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personal;
    }

    // Método para atualizar um PersonalTrainer e sua pessoa associada
    public void updatePersonalTrainer(PersonalTrainer personal) {
        String sqlPessoa = "UPDATE pessoa SET nome = ?, cpf = ?, telefone = ?, email = ? WHERE id = ?";
        String sqlPersonal = "UPDATE personal_trainer SET registro_profissional = ?, especialidade = ? WHERE pessoa_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmtPessoa = connection.prepareStatement(sqlPessoa);
             PreparedStatement stmtPersonal = connection.prepareStatement(sqlPersonal)) {

            // Atualizar dados na tabela pessoa
            stmtPessoa.setString(1, personal.getNome());
            stmtPessoa.setString(2, personal.getCPF());
            stmtPessoa.setString(3, personal.getTelefone());
            stmtPessoa.setString(4, personal.getEmail());
            stmtPessoa.setInt(5, personal.getId());
            stmtPessoa.executeUpdate();

            // Atualizar dados na tabela personal_trainer
            stmtPersonal.setString(1, personal.getRegistroProfissional());
            stmtPersonal.setString(2, personal.getEspecialidade());
            stmtPersonal.setInt(3, personal.getId());
            stmtPersonal.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
