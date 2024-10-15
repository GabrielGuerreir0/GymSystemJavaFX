package Controller;

import DAO.PlanoDAO;
import Model.Plano;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroPlanoController {

    @FXML
    private TextField txtTipo;
    @FXML
    private TextField txtValor;
    @FXML
    private TextField txtDescricao;

    private Plano plano;
    private PlanoDAO planoDAO = new PlanoDAO();

    // Este método é chamado para pré-carregar os dados do plano selecionado na tela de edição
    public void setPlano(Plano plano) {
        this.plano = plano;
        if (plano != null) {
            txtTipo.setText(plano.getTipo());
            txtValor.setText(String.valueOf(plano.getValor()));
            txtDescricao.setText(plano.getDescricao());
        }
    }

    @FXML
    public void salvarPlano() {
        if (plano == null) {
            plano = new Plano();  // Novo plano
        }

        // Atualiza os valores do plano
        plano.setTipo(txtTipo.getText());
        plano.setValor(Double.parseDouble(txtValor.getText()));
        plano.setDescricao(txtDescricao.getText());

        // Verifica se o plano já existe no banco de dados (edição) ou é um novo
        if (plano.getId() == 0) {
            planoDAO.addPlano(plano);  // Insere novo plano
        } else {
            planoDAO.updatePlano(plano);  // Atualiza plano existente
        }

        // Fecha a janela após salvar
        Stage stage = (Stage) txtTipo.getScene().getWindow();
        stage.close();
    }
}
