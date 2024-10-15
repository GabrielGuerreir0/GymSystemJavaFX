package Controller;

import DAO.PersonalTrainerDAO;
import Model.PersonalTrainer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroPersonalController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField cpfField;
    @FXML
    private TextField telefoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField registroProfField;
    @FXML
    private TextField especialidadeField;

    private PersonalTrainerDAO personalDAO = new PersonalTrainerDAO();
    private PersonalTrainer personalAtual;  // Para identificar se é uma atualização

    // Preencher o formulário com os dados do personal selecionado
    public void setPersonal(PersonalTrainer personal) {
        this.personalAtual = personal;
        nomeField.setText(personal.getNome());
        cpfField.setText(personal.getCPF());
        telefoneField.setText(personal.getTelefone());
        emailField.setText(personal.getEmail());
        registroProfField.setText(personal.getRegistroProfissional());
        especialidadeField.setText(personal.getEspecialidade());
    }

    @FXML
    public void handleRegistrarPersonal() {
        // Capturar os dados do formulário
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String telefone = telefoneField.getText();
        String email = emailField.getText();
        String registroProfissional = registroProfField.getText();
        String especialidade = especialidadeField.getText();

        // Verificar se todos os campos foram preenchidos
        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || email.isEmpty() ||
                registroProfissional.isEmpty() || especialidade.isEmpty()) {
            showAlert("Erro", "Preencha todos os campos.");
            return;
        }

        // Se estiver editando um personal existente, atualizar
        if (personalAtual != null) {
            personalAtual.setNome(nome);
            personalAtual.setCPF(cpf);
            personalAtual.setTelefone(telefone);
            personalAtual.setEmail(email);
            personalAtual.setRegistroProfissional(registroProfissional);
            personalAtual.setEspecialidade(especialidade);

            // Atualizar no banco de dados
            personalDAO.updatePersonalTrainer(personalAtual);
        } else {
            // Caso contrário, criar um novo personal
            PersonalTrainer novoPersonal = new PersonalTrainer(nome, cpf, telefone, email, registroProfissional, especialidade);
            personalDAO.addPersonalTrainer(novoPersonal);
        }

        // Fechar a janela
        Stage stage = (Stage) nomeField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
