package DAO;

import Model.Plano;
import db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanoDAO {

    public void addPlano(Plano plano) {
        String sql = "INSERT INTO plano (tipo, valor, descricao) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, plano.getTipo());
            stmt.setDouble(2, plano.getValor());
            stmt.setString(3, plano.getDescricao());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Atualiza um plano existente no banco de dados
    public void updatePlano(Plano plano) {
        String sql = "UPDATE plano SET tipo = ?, valor = ?, descricao = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, plano.getTipo());
            stmt.setDouble(2, plano.getValor());
            stmt.setString(3, plano.getDescricao());
            stmt.setInt(4, plano.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlano(int planoId) {
        String sql = "DELETE FROM plano WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, planoId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Plano> getAllPlanos() {
        List<Plano> planos = new ArrayList<>();
        String sql = "SELECT * FROM plano";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Plano plano = new Plano(rs.getString("tipo"), rs.getDouble("valor"), rs.getString("descricao"));
                plano.setId(rs.getInt("id"));
                planos.add(plano);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return planos;
    }

    public List<Plano> getPlanosByTipo(String tipo) {
        List<Plano> planos = new ArrayList<>();
        String sql = "SELECT * FROM plano WHERE tipo LIKE ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, "%" + tipo + "%");  // Utilizando LIKE para permitir busca parcial
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Plano plano = new Plano();
                plano.setId(rs.getInt("id"));
                plano.setTipo(rs.getString("tipo"));
                plano.setValor(rs.getDouble("valor"));
                plano.setDescricao(rs.getString("descricao"));

                planos.add(plano);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return planos;
    }


    public Plano getPlanoById(int planoId) {
        Plano plano = null;
        String sql = "SELECT * FROM plano WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, planoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                plano = new Plano();
                plano.setId(rs.getInt("id"));
                plano.setTipo(rs.getString("tipo"));
                plano.setValor(rs.getDouble("valor"));
                plano.setDescricao(rs.getString("descricao"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plano;
    }

}
