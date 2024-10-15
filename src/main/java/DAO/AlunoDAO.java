package DAO;

import Model.Aluno;
import Model.PersonalTrainer;
import Model.Plano;
import db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public void addAluno(Aluno aluno) {
        String sql = "INSERT INTO aluno (pessoa_id, data_inicio, idade, personal_id, plano_id) VALUES (?, ?, ?, ?, ?)";
        String pessoaSql = "INSERT INTO pessoa (nome, cpf, telefone, email) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pessoaStmt = connection.prepareStatement(pessoaSql, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            pessoaStmt.setString(1, aluno.getNome());
            pessoaStmt.setString(2, aluno.getCPF());
            pessoaStmt.setString(3, aluno.getTelefone());
            pessoaStmt.setString(4, aluno.getEmail());
            pessoaStmt.executeUpdate();

            ResultSet rs = pessoaStmt.getGeneratedKeys();
            if (rs.next()) {
                aluno.setId(rs.getInt(1));
            }

            stmt.setInt(1, aluno.getId());
            stmt.setDate(2, aluno.getDataInicio());
            stmt.setInt(3, aluno.getIdade());
            stmt.setInt(4, aluno.getPersonal() != null ? aluno.getPersonal().getId() : null);
            stmt.setInt(5, aluno.getPlano() != null ? aluno.getPlano().getId() : null);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAluno(Aluno aluno) {
        String sql = "UPDATE aluno a JOIN pessoa p ON a.pessoa_id = p.id SET p.nome = ?, p.cpf = ?, p.telefone = ?, p.email = ?, a.data_inicio = ?, a.idade = ?, a.personal_id = ?, a.plano_id = ? WHERE a.id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCPF());
            stmt.setString(3, aluno.getTelefone());
            stmt.setString(4, aluno.getEmail());
            stmt.setDate(5, aluno.getDataInicio());
            stmt.setInt(6, aluno.getIdade());
            stmt.setInt(7, aluno.getPersonal() != null ? aluno.getPersonal().getId() : null);
            stmt.setInt(8, aluno.getPlano() != null ? aluno.getPlano().getId() : null);
            stmt.setInt(9, aluno.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAluno(int alunoId) {
        String sql = "DELETE FROM aluno WHERE id = ?";
        String pessoaSql = "DELETE FROM pessoa WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             PreparedStatement pessoaStmt = connection.prepareStatement(pessoaSql)) {

            stmt.setInt(1, alunoId);
            stmt.executeUpdate();

            pessoaStmt.setInt(1, alunoId);
            pessoaStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Aluno> getAllAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT a.id, p.nome, p.cpf, p.telefone, p.email, a.data_inicio, a.idade, a.personal_id, a.plano_id " +
                "FROM aluno a " +
                "JOIN pessoa p ON a.pessoa_id = p.id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));



                aluno.setDataInicio(rs.getDate("data_inicio"));
                aluno.setIdade(rs.getInt("idade"));

                // Carregar o PersonalTrainer, se existir
                int personalId = rs.getInt("personal_id");
                if (personalId != 0) {
                    PersonalTrainerDAO personalDAO = new PersonalTrainerDAO();
                    PersonalTrainer personal = personalDAO.getPersonalTrainerById(personalId);
                    aluno.setPersonal(personal);
                }

                // Carregar o Plano, se existir
                int planoId = rs.getInt("plano_id");
                if (planoId != 0) {
                    PlanoDAO planoDAO = new PlanoDAO();
                    Plano plano = planoDAO.getPlanoById(planoId);
                    aluno.setPlano(plano);
                }

                alunos.add(aluno);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alunos;
    }


    public List<Aluno> getAlunosByName(String nome) {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT a.id, p.nome, a.data_inicio, a.idade, a.personal_id, a.plano_id " +
                "FROM aluno a " +
                "JOIN pessoa p ON a.pessoa_id = p.id " +
                "WHERE p.nome LIKE ?";  // Usar LIKE para buscar por parte do nome

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");  // Busca parcial do nome
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));
                aluno.setDataInicio(rs.getDate("data_inicio"));
                aluno.setIdade(rs.getInt("idade"));

                // Carregar o PersonalTrainer, se existir
                int personalId = rs.getInt("personal_id");
                if (personalId != 0) {
                    PersonalTrainerDAO personalDAO = new PersonalTrainerDAO();
                    PersonalTrainer personal = personalDAO.getPersonalTrainerById(personalId);
                    aluno.setPersonal(personal);  // Associar o personal ao aluno
                }

                // Carregar o Plano, se existir
                int planoId = rs.getInt("plano_id");
                if (planoId != 0) {
                    PlanoDAO planoDAO = new PlanoDAO();
                    Plano plano = planoDAO.getPlanoById(planoId);
                    aluno.setPlano(plano);  // Associar o plano ao aluno
                }

                alunos.add(aluno);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alunos;
    }

    public Aluno getAlunoById(int alunoId) {
        Aluno aluno = null;
        String sql = "SELECT a.id, p.nome, p.cpf, p.telefone, p.email, a.data_inicio, a.idade, a.personal_id, a.plano_id " +
                "FROM aluno a " +
                "JOIN pessoa p ON a.pessoa_id = p.id " +
                "WHERE a.id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                aluno = new Aluno();
                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));
                aluno.setCPF(rs.getString("cpf"));
                aluno.setTelefone(rs.getString("telefone"));
                aluno.setEmail(rs.getString("email"));
                aluno.setDataInicio(rs.getDate("data_inicio"));
                aluno.setIdade(rs.getInt("idade"));

                // Carregar o PersonalTrainer, se existir
                int personalId = rs.getInt("personal_id");
                if (personalId != 0) {
                    PersonalTrainerDAO personalDAO = new PersonalTrainerDAO();
                    PersonalTrainer personal = personalDAO.getPersonalTrainerById(personalId);
                    aluno.setPersonal(personal);
                }

                // Carregar o Plano, se existir
                int planoId = rs.getInt("plano_id");
                if (planoId != 0) {
                    PlanoDAO planoDAO = new PlanoDAO();
                    Plano plano = planoDAO.getPlanoById(planoId);
                    aluno.setPlano(plano);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aluno;
    }

}
