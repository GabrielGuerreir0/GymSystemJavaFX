package Controller;

import DAO.AulaDAO;
import DAO.PersonalTrainerDAO;
import Model.Aula;
import Model.PersonalTrainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;

public class CadastroAulaController {

    @FXML
    private TextField nomeField;
    @FXML
    private DatePicker dataField;
    @FXML
    private TextField duracaoField;
    @FXML
    private ComboBox<PersonalTrainer> personalComboBox;

    private AulaDAO aulaDAO = new AulaDAO();
    private Aula aulaAtual;  // Para identificar se é uma atualização

    // Preencher o formulário com os dados da aula selecionada
    public void setAula(Aula aula) {
        this.aulaAtual = aula;
        nomeField.setText(aula.getNomeAula());
        dataField.setValue(aula.getData().toLocalDate());
        duracaoField.setText(String.valueOf(aula.getDuracao()));

        // Selecionar o personal no ComboBox
        personalComboBox.getSelectionModel().select(aula.getPersonalTrainer());
    }

    @FXML
    public void initialize() {
        // Carregar a lista de Personal Trainers no ComboBox
        PersonalTrainerDAO personalDAO = new PersonalTrainerDAO();
        List<PersonalTrainer> personais = personalDAO.getAllPersonalTrainers();
        ObservableList<PersonalTrainer> personalList = FXCollections.observableArrayList(personais);
        personalComboBox.setItems(personalList);
    }

    @FXML
    public void handleRegistrarAula() {
        String nome = nomeField.getText();
        java.sql.Date data = java.sql.Date.valueOf(dataField.getValue());
        int duracao;

        // Validação da duração
        try {
            duracao = Integer.parseInt(duracaoField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erro", "Campo Duração", "A duração deve ser um número inteiro válido.");
            return;
        }

        PersonalTrainer personalSelecionado = personalComboBox.getSelectionModel().getSelectedItem();

        // Verificar se o nome da aula foi preenchido
        if (nome == null || nome.isEmpty()) {
            showAlert("Erro", "Campo Nome", "O campo Nome da aula não pode estar vazio.");
            return;
        }

        // Verificar se o PersonalTrainer foi selecionado
        if (personalSelecionado == null) {
            showAlert("Erro", "Personal Trainer", "Por favor, selecione um Personal Trainer.");
            return;
        }

        // Se estiver editando uma aula existente, atualizar
        if (aulaAtual != null) {
            aulaAtual.setNomeAula(nome);
            aulaAtual.setData(data);
            aulaAtual.setDuracao(duracao);
            aulaAtual.setPersonalTrainer(personalSelecionado);  // Usando o id correto do personal_trainer

            // Atualizar no banco de dados
            aulaDAO.updateAula(aulaAtual);
        } else {
            // Criar uma nova aula
            Aula novaAula = new Aula();
            novaAula.setNomeAula(nome);
            novaAula.setData(data);
            novaAula.setDuracao(duracao);
            novaAula.setPersonalTrainer(personalSelecionado);  // Usando o id correto do personal_trainer

            // Adicionar a nova aula no banco de dados
            aulaDAO.addAula(novaAula);
        }

        // Fechar a janela após o registro
        Stage stage = (Stage) nomeField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
