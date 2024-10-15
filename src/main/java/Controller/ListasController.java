package Controller;

import DAO.AlunoDAO;
import DAO.PersonalTrainerDAO;
import DAO.PlanoDAO;
import DAO.AulaDAO;
import Model.Aluno;
import Model.PersonalTrainer;
import Model.Plano;
import Model.Aula;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ListasController {

    @FXML
    private TableView<Aluno> tableAlunos;
    @FXML
    private TableColumn<Aluno, Integer> colAlunoId;
    @FXML
    private TableColumn<Aluno, String> colAlunoNome;
    @FXML
    private TableColumn<Aluno, String> colAlunoDataInicio;
    @FXML
    private TableColumn<Aluno, Integer> colAlunoIdade;
    @FXML
    private TableColumn<Aluno, String> colAlunoPersonal;
    @FXML
    private TableColumn<Aluno, String> colAlunoPlano;

    @FXML
    private TableView<PersonalTrainer> tablePersonais;
    @FXML
    private TableColumn<PersonalTrainer, Integer> colPersonalId;
    @FXML
    private TableColumn<PersonalTrainer, String> colPersonalNome;
    @FXML
    private TableColumn<PersonalTrainer, String> colPersonalRegistro;
    @FXML
    private TableColumn<PersonalTrainer, String> colPersonalEspecialidade;

    @FXML
    private TableView<Aula> tableAulas;
    @FXML
    private TableColumn<Aula, Integer> colAulaId;
    @FXML
    private TableColumn<Aula, String> colAulaNome;
    @FXML
    private TableColumn<Aula, String> colAulaData;
    @FXML
    private TableColumn<Aula, Integer> colAulaDuracao;
    @FXML
    private TableColumn<Aula, String> colAulaPersonal;

    @FXML
    private TableView<Plano> tablePlanos;
    @FXML
    private TableColumn<Plano, Integer> colPlanoId;
    @FXML
    private TableColumn<Plano, String> colPlanoTipo;
    @FXML
    private TableColumn<Plano, Double> colPlanoValor;
    @FXML
    private TableColumn<Plano, String> colPlanoDescricao;


    @FXML
    private Button btnExcluirAluno;
    @FXML
    private Button btnExcluirPersonal;
    @FXML
    private Button btnExcluirAula;
    @FXML
    private Button btnExcluirPlano;

    @FXML
    private TextField searchAluno;
    @FXML
    private TextField searchPersonal;
    @FXML
    private TextField searchAula;
    @FXML
    private TextField searchPlano;


    private AlunoDAO alunoDAO = new AlunoDAO();
    private PersonalTrainerDAO personalTrainerDAO = new PersonalTrainerDAO();
    private PlanoDAO planoDAO = new PlanoDAO();
    private AulaDAO aulaDAO = new AulaDAO();

    @FXML
    public void initialize() {
        // Inicializa as colunas da tabela de Alunos
        colAlunoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAlunoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAlunoDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        colAlunoIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        // Aqui personal e plano são String temporários na classe aluno
        colAlunoPersonal.setCellValueFactory(new PropertyValueFactory<>("personal"));
        colAlunoPlano.setCellValueFactory(new PropertyValueFactory<>("plano"));


        // Adiciona um listener para o campo de busca
        searchAluno.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarAlunosPorNome(newValue);  // Chama o método de busca ao digitar
        });

        // Inicializa as colunas da tabela de Personais
        colPersonalId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPersonalNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPersonalRegistro.setCellValueFactory(new PropertyValueFactory<>("registroProfissional"));
        colPersonalEspecialidade.setCellValueFactory(new PropertyValueFactory<>("especialidade"));

        searchPersonal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadPersonais();
            } else {
                buscarPersonal(newValue);
            }
        });

        // Inicializa as colunas da tabela de Aulas
        colAulaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAulaNome.setCellValueFactory(new PropertyValueFactory<>("nomeAula"));
        colAulaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colAulaDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
        colAulaPersonal.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getPersonalTrainer() != null ?
                        cellData.getValue().getPersonalTrainer().getNome() : "N/A"));

        searchAula.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarAulaPorNome(newValue);  // Chama o método de busca ao digitar
        });

        // Inicializa as colunas da tabela de Planos
        colPlanoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPlanoTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colPlanoValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colPlanoDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        searchPlano.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarPlanoPorTipo(newValue);  // Chama o método de busca ao digitar
        });

        // Carregar os dados das tabelas
        loadAlunos();
        loadPersonais();
        loadAulas();
        loadPlanos();

        tableAlunos.setRowFactory(tv -> {
            TableRow<Aluno> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Aluno alunoSelecionado = row.getItem();
                    openEditarAluno(alunoSelecionado);  // Abre a janela de edição com os dados do aluno
                }
            });
            return row;
        });

        tablePersonais.setRowFactory(tv -> {
            TableRow<PersonalTrainer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    PersonalTrainer personalSelecionado = row.getItem();
                    openEditarPersonal(personalSelecionado);  // Abrir janela de edição com o personal selecionado
                }
            });
            return row;
        });
        tableAulas.setRowFactory(tv -> {
            TableRow<Aula> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Aula aulaSelecionado = row.getItem();
                    openEditarAula(aulaSelecionado);  // Abrir janela de edição com o personal selecionado
                }
            });
            return row;
        });




    }

    @FXML
    private void openEditarAluno(Aluno alunoSelecionado) {
        try {
            // Buscar os dados completos do aluno no banco de dados
            AlunoDAO alunoDAO = new AlunoDAO();
            Aluno alunoCompleto = alunoDAO.getAlunoById(alunoSelecionado.getId());

            // Carregar o FXML do cadastro/edição de aluno
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ifce/gymsystemjavafx/CadastroAluno.fxml"));
            Parent root = loader.load();

            // Obter o controller da nova janela
            CadastroAlunoController controller = loader.getController();

            // Enviar o aluno completo para o controller da janela de edição
            controller.setAluno(alunoCompleto);

            // Criar uma nova cena e exibir
            Stage stage = new Stage();
            stage.setTitle("Editar Aluno");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Após fechar a janela de edição, atualizar a tabela
            loadAlunos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openEditarPersonal(PersonalTrainer personalSelecionado) {
        try {
            // Buscar o Personal Trainer diretamente do banco de dados usando o ID
            PersonalTrainerDAO personalDAO = new PersonalTrainerDAO();
            PersonalTrainer personalCompleto = personalDAO.getPersonalTrainerById(personalSelecionado.getId());

            // Carregar o FXML da tela de cadastro de personal trainer
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ifce/gymsystemjavafx/CadastroPersonal.fxml"));
            Parent root = loader.load();

            // Obter o controlador da janela de cadastro
            CadastroPersonalController controller = loader.getController();

            // Preencher o formulário com os dados do personal completo
            controller.setPersonal(personalCompleto);

            // Criar uma nova janela para a edição
            Stage stage = new Stage();
            stage.setTitle("Editar Personal Trainer");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Após fechar a janela, atualizar a tabela
            loadPersonais();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openEditarAula(Aula aulaSelecionada) {
        try {
            // Buscar a aula completa no banco de dados
            AulaDAO aulaDAO = new AulaDAO();
            Aula aulaCompleta = aulaDAO.getAulaById(aulaSelecionada.getId());

            // Carregar o FXML da tela de cadastro de aulas
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ifce/gymsystemjavafx/CadastroAula.fxml"));
            Parent root = loader.load();

            // Obter o controlador da janela de cadastro
            CadastroAulaController controller = loader.getController();

            // Preencher o formulário com os dados da aula selecionada
            controller.setAula(aulaCompleta);

            // Criar uma nova janela para a edição
            Stage stage = new Stage();
            stage.setTitle("Editar Aula");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Atualizar a tabela de aulas após a edição
            loadAulas();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void excluirAluno() {
        Aluno alunoSelecionado = tableAlunos.getSelectionModel().getSelectedItem();

        if (alunoSelecionado != null) {
            alunoDAO.deleteAluno(alunoSelecionado.getId());  // Exclui o aluno do banco de dados
            loadAlunos();  // Atualiza a tabela após a exclusão
        } else {
            // Exibir mensagem de erro se nenhum aluno estiver selecionado
            showAlert("Erro", "Nenhum aluno selecionado", "Por favor, selecione um aluno para excluir.");
        }
    }

    @FXML
    private void excluirPersonal() {
        PersonalTrainer personalSelecionado = tablePersonais.getSelectionModel().getSelectedItem();

        if (personalSelecionado != null) {
            personalTrainerDAO.deletePersonalTrainer(personalSelecionado.getId());  // Exclui o personal do banco de dados
            loadPersonais();  // Atualiza a tabela após a exclusão
        } else {
            showAlert("Erro", "Nenhum personal selecionado", "Por favor, selecione um personal para excluir.");
        }
    }

    @FXML
    private void excluirAula() {
        Aula aulaSelecionada = tableAulas.getSelectionModel().getSelectedItem();

        if (aulaSelecionada != null) {
            aulaDAO.deleteAula(aulaSelecionada.getId());  // Exclui a aula do banco de dados
            loadAulas();  // Atualiza a tabela após a exclusão
        } else {
            showAlert("Erro", "Nenhuma aula selecionada", "Por favor, selecione uma aula para excluir.");
        }
    }

    @FXML
    private void excluirPlano() {
        Plano planoSelecionado = tablePlanos.getSelectionModel().getSelectedItem();

        if (planoSelecionado != null) {
            planoDAO.deletePlano(planoSelecionado.getId());  // Exclui o plano do banco de dados
            loadPlanos();  // Atualiza a tabela após a exclusão
        } else {
            showAlert("Erro", "Nenhum plano selecionado", "Por favor, selecione um plano para excluir.");
        }
    }

    @FXML
    private void openCadastroAluno() {
        try {
            // Carregar o FXML do cadastro de aluno
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ifce/gymsystemjavafx/CadastroAluno.fxml"));
            Parent root = loader.load();

            // Criar uma nova cena e exibir
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Aluno");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Após fechar o cadastro, atualizar a tabela
            loadAlunos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openCadastroPersonal() {
        try {
            // Carregar o FXML da tela de cadastro de personal trainer
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ifce/gymsystemjavafx/CadastroPersonal.fxml"));
            Parent root = loader.load();

            // Criar uma nova janela para o cadastro
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Personal Trainer");
            stage.setScene(new Scene(root));
            stage.showAndWait();  // Espera até que a janela de cadastro seja fechada

            // Após o cadastro, recarregar a tabela de Personais
            loadPersonais();  // Atualizar a tabela após o cadastro

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openCadastroAula() {
        try {
            // Carregar o FXML da tela de cadastro de aulas
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ifce/gymsystemjavafx/CadastroAula.fxml"));
            Parent root = loader.load();

            // Criar uma nova janela para o cadastro
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Aula");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Atualizar a tabela de aulas após o cadastro
            loadAulas();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void editarPlano() {
        Plano planoSelecionado = tablePlanos.getSelectionModel().getSelectedItem();

        if (planoSelecionado != null) {
            openEditarPlano(planoSelecionado);  // Abre a janela de edição para o plano selecionado
        } else {
            showAlert("Erro", "Nenhum plano selecionado", "Por favor, selecione um plano para editar.");
        }
    }


    private void openEditarPlano(Plano planoSelecionado) {
        try {
            // Carregar o FXML da tela de cadastro/edição de planos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ifce/gymsystemjavafx/CadastroPlano.fxml"));
            Parent root = loader.load();

            // Obter o controlador da janela de cadastro
            CadastroPlanoController controller = loader.getController();

            // Preencher o formulário com os dados do plano selecionado
            controller.setPlano(planoSelecionado);

            // Criar uma nova janela para a edição
            Stage stage = new Stage();
            stage.setTitle("Editar Plano");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Atualizar a tabela de planos após a edição
            loadPlanos();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void openCadastroPlano() {
        try {
            // Carregar o FXML da tela de cadastro de planos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ifce/gymsystemjavafx/CadastroPlano.fxml"));
            Parent root = loader.load();

            // Obter o controlador da janela de cadastro
            CadastroPlanoController controller = loader.getController();

            // Criar uma nova janela para o cadastro de um novo plano
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Plano");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Atualizar a tabela de planos após o cadastro
            loadPlanos();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void buscarAlunosPorNome(String nome) {
        AlunoDAO alunoDAO = new AlunoDAO();
        List<Aluno> alunos = alunoDAO.getAlunosByName(nome);  // Busca alunos pelo nome no banco
        ObservableList<Aluno> alunosList = FXCollections.observableArrayList(alunos);
        tableAlunos.setItems(alunosList);  // Atualiza a tabela com os resultados
    }
    private void buscarAulaPorNome(String nome) {
        AulaDAO aulaDAO = new AulaDAO();
        List<Aula> aulas = aulaDAO.getAulasByName(nome);  // Buscar aulas pelo nome no banco
        ObservableList<Aula> aulasList = FXCollections.observableArrayList(aulas);
        tableAulas.setItems(aulasList);  // Atualiza a tabela com o resultado da pesquisa
    }
    private void buscarPersonal(String nome) {
        List<PersonalTrainer> personais = personalTrainerDAO.getPersonaisByName(nome);
        ObservableList<PersonalTrainer> personalList = FXCollections.observableArrayList(personais);
        tablePersonais.setItems(personalList);
    }
    private void buscarPlanoPorTipo(String tipo) {
        PlanoDAO planoDAO = new PlanoDAO();

        // Busca os planos no banco de dados pelo tipo
        List<Plano> planos = planoDAO.getPlanosByTipo(tipo);

        // Atualiza a tabela de planos com os resultados encontrados
        ObservableList<Plano> planosList = FXCollections.observableArrayList(planos);
        tablePlanos.setItems(planosList);  // Atualiza a tabela com os resultados da busca
    }


    private void loadAlunos() {
        AlunoDAO alunoDAO = new AlunoDAO();
        List<Aluno> alunos = alunoDAO.getAllAlunos();  // Pega todos os alunos do banco
        ObservableList<Aluno> alunosList = FXCollections.observableArrayList(alunos);
        tableAlunos.setItems(alunosList);  // Atualiza a tabela
    }

    private void loadPersonais() {
        List<PersonalTrainer> personais = personalTrainerDAO.getAllPersonalTrainers();  // Carregar todos os personais do banco
        ObservableList<PersonalTrainer> personalList = FXCollections.observableArrayList(personais);
        tablePersonais.setItems(personalList);  // Atualizar a tabela com os dados carregados
    }


    private void loadAulas() {
        List<Aula> aulas = aulaDAO.getAllAulas();  // Buscar aulas do banco
        ObservableList<Aula> aulasList = FXCollections.observableArrayList(aulas);

        // Definindo a tabela com as aulas recuperadas
        tableAulas.setItems(aulasList);
    }


    private void loadPlanos() {
        ObservableList<Plano> planosList = FXCollections.observableArrayList(planoDAO.getAllPlanos());
        tablePlanos.setItems(planosList);
    }
}
