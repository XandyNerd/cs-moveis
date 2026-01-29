import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClienteController {

    @FXML
    private TextField txtBusca;
    @FXML
    private TableView<Cliente> tableClientes;
    @FXML
    private TableColumn<Cliente, String> colNome;
    @FXML
    private TableColumn<Cliente, String> colCpf;
    @FXML
    private TableColumn<Cliente, String> colTelefone;
    @FXML
    private TableColumn<Cliente, String> colEmail;
    @FXML
    private TableColumn<Cliente, String> colStatus;
    @FXML
    private Label lblResultados;

    // Lista de clientes (dados mockados por enquanto)
    private ObservableList<Cliente> clientes;

    // Cliente atualmente selecionado para edição
    private Cliente clienteSelecionado;

    /**
     * Inicializa a tela de clientes
     */
    @FXML
    public void initialize() {
        configurarColunas();
        carregarDadosMockados();
        configurarSelecaoTabela();
    }

    /**
     * Configura as colunas da tabela
     */
    private void configurarColunas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /**
     * Carrega dados mockados para teste
     * TODO: Substituir por dados do MySQL quando driver estiver configurado
     */
    private void carregarDadosMockados() {
        clientes = FXCollections.observableArrayList(
                new Cliente(1, "Ana Silva", "123.456.789-00", "(11) 98765-4321",
                        "01001-000", "Av. Paulista", "1000", "Apto 42",
                        "Cliente desde 2021", "ana.silva@email.com", "Ativo"),

                new Cliente(2, "Carlos Júnior", "987.654.321-11", "(21) 91234-5678",
                        "20000-000", "Rua das Flores", "250", "",
                        "Cliente VIP", "carlos.jr@empresa.com", "Ativo"),

                new Cliente(3, "Mariana Alves", "456.789.012-34", "(31) 99887-7665",
                        "30000-000", "Rua Central", "500", "Casa",
                        "", "mariana.alves@design.com", "Inativo"),

                new Cliente(4, "Roberto Pereira", "321.654.987-00", "(41) 95544-3322",
                        "80000-000", "Av. Brasil", "1500", "Sala 10",
                        "", "roberto@tech.com.br", "Ativo"),

                new Cliente(5, "Lucas Mendes", "000.111.222-33", "(51) 91122-3344",
                        "90000-000", "Rua das Palmeiras", "80", "",
                        "", "lucas.mendes@web.com", "Pendente"));

        tableClientes.setItems(clientes);
        atualizarLabelResultados();
    }

    /**
     * Configura listener para seleção na tabela
     */
    private void configurarSelecaoTabela() {
        tableClientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    clienteSelecionado = newSelection;
                });
    }

    /**
     * Atualiza label de resultados
     */
    private void atualizarLabelResultados() {
        int total = clientes.size();
        lblResultados.setText("Mostrando 1 a " + total + " de " + total + " resultados");
    }

    /**
     * Busca clientes conforme digitação
     */
    @FXML
    public void buscarClientes() {
        String termo = txtBusca.getText().toLowerCase();

        if (termo.isEmpty()) {
            tableClientes.setItems(clientes);
        } else {
            ObservableList<Cliente> filtrados = FXCollections.observableArrayList();

            for (Cliente c : clientes) {
                if (c.getNome().toLowerCase().contains(termo) ||
                        c.getCpf().contains(termo) ||
                        c.getEmail().toLowerCase().contains(termo)) {
                    filtrados.add(c);
                }
            }

            tableClientes.setItems(filtrados);
        }

        atualizarLabelResultados();
    }

    /**
     * Abre modal para cadastrar novo cliente
     */
    @FXML
    public void novoCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientesCadastro.fxml"));
            Scene scene = new Scene(loader.load());

            // Obtém o controller do modal
            ClientesController modalController = loader.getController();

            // Passa referência deste controller para que o modal possa atualizar a tabela
            modalController.setClienteControllerPai(this);

            // Configuração da janela modal
            Stage modalStage = new Stage();
            modalStage.setTitle("Novo Cliente");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.UNDECORATED);
            modalStage.setScene(scene);
            modalStage.setResizable(false);

            // Exibe o modal e aguarda fechamento
            modalStage.showAndWait();

        } catch (Exception e) {
            System.err.println("Erro ao abrir modal de cadastro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Edita o cliente selecionado
     */
    @FXML
    public void editarCliente() {
        // Verifica se há cliente selecionado
        if (clienteSelecionado == null) {
            mostrarAlerta("Nenhum cliente selecionado",
                    "Por favor, selecione um cliente para editar.");
            return;
        }

        try {
            // Carrega o FXML do formulário de edição
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClientesEdicao.fxml"));
            Scene scene = new Scene(loader.load());

            // Obtém o controller do formulário de edição
            ClientesController controller = loader.getController();

            // Passa os dados do cliente para o controller
            controller.setCliente(clienteSelecionado);

            // Cria um Stage modal
            Stage modalStage = new Stage();
            modalStage.setTitle("Editar Cliente");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initStyle(StageStyle.UTILITY);
            modalStage.setScene(scene);
            modalStage.setResizable(false);

            // Exibe o modal e aguarda fechamento
            modalStage.showAndWait();

            // Atualiza a tabela (refresh)
            tableClientes.refresh();

        } catch (Exception e) {
            System.err.println("Erro ao abrir modal de edição: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exclui o cliente selecionado
     */
    @FXML
    public void excluirCliente() {
        // Verifica se há cliente selecionado
        if (clienteSelecionado == null) {
            mostrarAlerta("Nenhum cliente selecionado",
                    "Por favor, selecione um cliente para excluir.");
            return;
        }

        // Confirmação de exclusão
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Tem certeza que deseja excluir o cliente " +
                clienteSelecionado.getNome() + "?\n\n" +
                "Esta ação não pode ser desfeita.");

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                clientes.remove(clienteSelecionado);
                clienteSelecionado = null;
                atualizarLabelResultados();
                System.out.println("Cliente excluído (apenas da memória - TODO: excluir do banco)");
            }
        });
    }

    /**
     * Exibe alerta de informação
     */
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Navega para página anterior
     */
    @FXML
    public void paginaAnterior() {
        System.out.println("Página anterior");
        // TODO: Implementar paginação
    }

    /**
     * Navega para próxima página
     */
    @FXML
    public void proximaPagina() {
        System.out.println("Próxima página");
        // TODO: Implementar paginação
    }

    /**
     * Adiciona um novo cliente à lista e atualiza a tabela
     * Este método é chamado pelo modal após salvar com sucesso
     * 
     * @throws RuntimeException se CPF ou RG já existir
     */
    public void adicionarCliente(Cliente novoCliente) throws RuntimeException {
        // Valida CPF duplicado
        for (Cliente c : clientes) {
            if (c.getCpf().equals(novoCliente.getCpf())) {
                throw new RuntimeException("CPF já cadastrado: " + novoCliente.getCpf());
            }

            // Valida RG duplicado (se ambos tiverem RG preenchido)
            if (novoCliente.getRg() != null && !novoCliente.getRg().isEmpty() &&
                    c.getRg() != null && !c.getRg().isEmpty()) {
                if (c.getRg().equals(novoCliente.getRg())) {
                    throw new RuntimeException("RG já cadastrado: " + novoCliente.getRg());
                }
            }
        }

        // Gera um ID temporário baseado no tamanho da lista
        int novoId = clientes.size() + 1;
        novoCliente.setId(novoId);

        // Adiciona à lista (que está vinculada à tabela)
        clientes.add(novoCliente);

        // Atualiza label de contagem
        atualizarLabelResultados();

        System.out.println("✅ Cliente adicionado à tabela: " + novoCliente.getNome());
    }

    /**
     * Exibe alert simples
     */
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
