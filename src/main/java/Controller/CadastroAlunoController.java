package Controller;

import DAO.AlunoDAO;
import DAO.PersonalTrainerDAO;
import DAO.PlanoDAO;
import Model.Aluno;
import Model.PersonalTrainer;
import Model.Plano;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroAlunoController {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField cpfField;

    @FXML
    private TextField telefoneField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker dataInicioPicker;

    @FXML
    private TextField idadeField;

    @FXML
    private ComboBox<PersonalTrainer> personalComboBox;

    @FXML
    private ComboBox<Plano> planoComboBox;

    private AlunoDAO alunoDAO = new AlunoDAO();
    private Aluno alunoAtual;  // Aluno a ser editado ou novo cadastro

    @FXML
    public void initialize() {
        // Carregar personal trainers e planos nos ComboBox
        personalComboBox.setItems(FXCollections.observableArrayList(new PersonalTrainerDAO().getAllPersonalTrainers()));
        planoComboBox.setItems(FXCollections.observableArrayList(new PlanoDAO().getAllPlanos()));
    }

    // Método para setar o aluno a ser editado e preencher os campos
    public void setAluno(Aluno aluno) {
        this.alunoAtual = aluno;

        // Preencher os campos com os dados do aluno
        nomeField.setText(aluno.getNome());
        cpfField.setText(aluno.getCPF());
        telefoneField.setText(aluno.getTelefone());
        emailField.setText(aluno.getEmail());
        dataInicioPicker.setValue(aluno.getDataInicio().toLocalDate());
        idadeField.setText(String.valueOf(aluno.getIdade()));

        // Preencher Personal Trainer e Plano
        personalComboBox.setValue(aluno.getPersonal());
        planoComboBox.setValue(aluno.getPlano());
    }



    @FXML
    public void handleRegistrarAluno() {
        // Verificar se estamos editando um aluno existente ou criando um novo
        if (alunoAtual != null) {
            // Atualizar os dados do aluno
            alunoAtual.setNome(nomeField.getText());
            alunoAtual.setCPF(cpfField.getText());
            alunoAtual.setTelefone(telefoneField.getText());
            alunoAtual.setEmail(emailField.getText());
            alunoAtual.setDataInicio(java.sql.Date.valueOf(dataInicioPicker.getValue()));
            alunoAtual.setIdade(Integer.parseInt(idadeField.getText()));

            // Verificar se personal e plano estão definidos
            PersonalTrainer personalSelecionado = personalComboBox.getValue();
            if (personalSelecionado != null) {
                alunoAtual.setPersonal(personalSelecionado);
            }

            Plano planoSelecionado = planoComboBox.getValue();
            if (planoSelecionado != null) {
                alunoAtual.setPlano(planoSelecionado);
            }

            // Atualizar o aluno no banco de dados
            try {
                alunoDAO.updateAluno(alunoAtual);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erro", "Erro ao atualizar aluno", "Falha ao tentar atualizar os dados do aluno.");
            }
        } else {
            // Caso seja um novo aluno, criar um novo registro
            Aluno novoAluno = new Aluno(nomeField.getText(), cpfField.getText(), telefoneField.getText(), emailField.getText());
            novoAluno.setDataInicio(java.sql.Date.valueOf(dataInicioPicker.getValue()));
            novoAluno.setIdade(Integer.parseInt(idadeField.getText()));

            // Verificar se personal e plano estão definidos
            PersonalTrainer personalSelecionado = personalComboBox.getValue();
            if (personalSelecionado != null) {
                novoAluno.setPersonal(personalSelecionado);
            }

            Plano planoSelecionado = planoComboBox.getValue();
            if (planoSelecionado != null) {
                novoAluno.setPlano(planoSelecionado);
            }

            // Adicionar o novo aluno no banco de dados
            try {
                alunoDAO.addAluno(novoAluno);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erro", "Erro ao adicionar aluno", "Falha ao tentar adicionar um novo aluno.");
            }
        }

        // Fechar a janela de cadastro/edição
        Stage stage = (Stage) nomeField.getScene().getWindow();
        stage.close();
    }

    public void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
