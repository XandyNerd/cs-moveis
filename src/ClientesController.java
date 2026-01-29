import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;

import java.util.function.UnaryOperator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import dao.ClienteDAO;
import dao.DuplicateFieldException;

public class ClientesController {

    // Campos existentes
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtCep;
    @FXML
    private TextField txtEndereco;
    @FXML
    private TextField txtNumero;
    @FXML
    private TextField txtComplemento;
    @FXML
    private TextArea txtObservacoes;

    // Novos campos
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtRg;
    @FXML
    private TextField txtDataNascimento;
    @FXML
    private TextField txtTelefoneSecundario;
    @FXML
    private TextField txtBairro;
    @FXML
    private TextField txtCidade;
    @FXML
    private ComboBox<String> cmbEstado;

    // Referência ao controller pai (tela principal de clientes)
    private ClienteController clienteControllerPai;

    // Cliente sendo editado (null se for novo cadastro)
    private Cliente clienteEditando;

    // DAO para acesso ao banco de dados
    private ClienteDAO clienteDAO;

    /**
     * Inicializa o controller e configura as máscaras de formatação
     */
    @FXML
    public void initialize() {
        // Inicializa o DAO
        clienteDAO = new ClienteDAO();

        // Máscaras dos campos existentes
        setupCpfMask();
        setupTelefoneMask();
        setupCepMask();
        setupNumeroMask();

        // Máscaras dos novos campos
        setupRgMask();
        setupTelefoneSecundarioMask();
        setupDataNascimentoMask();

        // Popular ComboBox de Estados
        popularEstados();
    }

    /**
     * Popular ComboBox com UFs brasileiras
     */
    private void popularEstados() {
        cmbEstado.setItems(FXCollections.observableArrayList(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
                "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
                "RS", "RO", "RR", "SC", "SP", "SE", "TO"));
    }

    /**
     * Configura máscara para CPF (000.000.000-00)
     */
    private void setupCpfMask() {
        txtCpf.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Remove tudo que não é número
            String numbersOnly = newText.replaceAll("[^0-9]", "");

            // Limita a 11 dígitos
            if (numbersOnly.length() > 11) {
                return null;
            }

            // Aplica formatação
            String formatted = formatCpf(numbersOnly);
            change.setText(formatted);
            change.setRange(0, change.getControlText().length());
            change.setCaretPosition(formatted.length());
            change.setAnchor(formatted.length());

            return change;
        }));
    }

    /**
     * Formata string numérica como CPF
     */
    private String formatCpf(String numbers) {
        if (numbers.isEmpty())
            return "";

        StringBuilder formatted = new StringBuilder();
        int length = numbers.length();

        for (int i = 0; i < length; i++) {
            formatted.append(numbers.charAt(i));
            if (i == 2 || i == 5) {
                formatted.append('.');
            } else if (i == 8) {
                formatted.append('-');
            }
        }

        return formatted.toString();
    }

    /**
     * Configura máscara para Telefone ((00) 00000-0000)
     */
    private void setupTelefoneMask() {
        txtTelefone.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Remove tudo que não é número
            String numbersOnly = newText.replaceAll("[^0-9]", "");

            // Limita a 11 dígitos (DDD + 9 dígitos)
            if (numbersOnly.length() > 11) {
                return null;
            }

            // Aplica formatação
            String formatted = formatTelefone(numbersOnly);
            change.setText(formatted);
            change.setRange(0, change.getControlText().length());
            change.setCaretPosition(formatted.length());
            change.setAnchor(formatted.length());

            return change;
        }));
    }

    /**
     * Formata string numérica como Telefone
     */
    private String formatTelefone(String numbers) {
        if (numbers.isEmpty())
            return "";

        StringBuilder formatted = new StringBuilder();
        int length = numbers.length();

        formatted.append('(');

        for (int i = 0; i < length; i++) {
            formatted.append(numbers.charAt(i));
            if (i == 1) {
                formatted.append(") ");
            } else if (i == 6 && length == 11) { // Celular com 9 dígitos
                formatted.append('-');
            } else if (i == 5 && length == 10) { // Fixo com 8 dígitos
                formatted.append('-');
            }
        }

        return formatted.toString();
    }

    /**
     * Configura máscara para CEP (00000-000)
     */
    private void setupCepMask() {
        txtCep.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Remove tudo que não é número
            String numbersOnly = newText.replaceAll("[^0-9]", "");

            // Limita a 8 dígitos
            if (numbersOnly.length() > 8) {
                return null;
            }

            // Aplica formatação
            String formatted = formatCep(numbersOnly);
            change.setText(formatted);
            change.setRange(0, change.getControlText().length());
            change.setCaretPosition(formatted.length());
            change.setAnchor(formatted.length());

            return change;
        }));
    }

    /**
     * Formata string numérica como CEP
     */
    private String formatCep(String numbers) {
        if (numbers.isEmpty())
            return "";

        StringBuilder formatted = new StringBuilder();
        int length = numbers.length();

        for (int i = 0; i < length; i++) {
            formatted.append(numbers.charAt(i));
            if (i == 4) {
                formatted.append('-');
            }
        }

        return formatted.toString();
    }

    /**
     * Configura campo Número para aceitar apenas dígitos
     */
    private void setupNumeroMask() {
        txtNumero.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Permite apenas números e limita a 10 caracteres
            if (newText.matches("[0-9]*") && newText.length() <= 10) {
                return change;
            }

            return null;
        }));
    }

    /**
     * Configura máscara para RG (aceita números e letras, sem formatação
     * específica)
     */
    private void setupRgMask() {
        txtRg.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Remove caracteres especiais, mantém apenas letras, números, pontos e hífens
            String filtered = newText.replaceAll("[^0-9A-Za-z.\\-]", "");

            // Limita a 20 caracteres
            if (filtered.length() > 20) {
                return null;
            }

            change.setText(filtered);
            change.setRange(0, change.getControlText().length());
            change.setCaretPosition(filtered.length());
            change.setAnchor(filtered.length());

            return change;
        }));
    }

    /**
     * Configura máscara para Telefone Secundário (mesma máscara do telefone
     * principal)
     */
    private void setupTelefoneSecundarioMask() {
        txtTelefoneSecundario.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Remove tudo que não é número
            String numbersOnly = newText.replaceAll("[^0-9]", "");

            // Limita a 11 dígitos (DDD + 9 dígitos)
            if (numbersOnly.length() > 11) {
                return null;
            }

            // Aplica formatação
            String formatted = formatTelefone(numbersOnly);
            change.setText(formatted);
            change.setRange(0, change.getControlText().length());
            change.setCaretPosition(formatted.length());
            change.setAnchor(formatted.length());

            return change;
        }));
    }

    /**
     * Configura máscara para Data de Nascimento (DD/MM/YYYY)
     */
    private void setupDataNascimentoMask() {
        txtDataNascimento.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Remove tudo que não é número
            String numbersOnly = newText.replaceAll("[^0-9]", "");

            // Limita a 8 dígitos (DDMMYYYY)
            if (numbersOnly.length() > 8) {
                return null;
            }

            // Aplica formatação DD/MM/YYYY
            String formatted = formatDataNascimento(numbersOnly);
            change.setText(formatted);
            change.setRange(0, change.getControlText().length());
            change.setCaretPosition(formatted.length());
            change.setAnchor(formatted.length());

            return change;
        }));
    }

    /**
     * Formata string numérica como Data (DD/MM/YYYY)
     */
    private String formatDataNascimento(String numbers) {
        if (numbers.isEmpty())
            return "";

        StringBuilder formatted = new StringBuilder();
        int length = numbers.length();

        for (int i = 0; i < length; i++) {
            formatted.append(numbers.charAt(i));
            if (i == 1 || i == 3) { // Adiciona / após DD e MM
                formatted.append('/');
            }
        }

        return formatted.toString();
    }

    /**
     * Valida se todos os campos obrigatórios foram preenchidos
     */
    private boolean validarCamposObrigatorios() {
        StringBuilder erros = new StringBuilder();

        if (txtNome.getText().trim().isEmpty()) {
            erros.append("• Nome Completo\n");
        }

        if (txtCpf.getText().trim().isEmpty() || txtCpf.getText().replaceAll("[^0-9]", "").length() < 11) {
            erros.append("• CPF (deve conter 11 dígitos)\n");
        }

        // Validação de Email (obrigatório)
        if (txtEmail.getText().trim().isEmpty()) {
            erros.append("• Email\n");
        } else if (!validarEmail(txtEmail.getText().trim())) {
            erros.append("• Email (formato inválido - ex: nome@dominio.com)\n");
        }

        if (txtTelefone.getText().trim().isEmpty() || txtTelefone.getText().replaceAll("[^0-9]", "").length() < 10) {
            erros.append("• Telefone (deve conter 10 ou 11 dígitos)\n");
        }

        if (txtCep.getText().trim().isEmpty() || txtCep.getText().replaceAll("[^0-9]", "").length() < 8) {
            erros.append("• CEP (deve conter 8 dígitos)\n");
        }

        if (txtEndereco.getText().trim().isEmpty()) {
            erros.append("• Endereço\n");
        }

        if (txtNumero.getText().trim().isEmpty()) {
            erros.append("• Número\n");
        }

        if (erros.length() > 0) {
            mostrarErro("Campos Obrigatórios não preenchidos",
                    "Por favor, preencha os seguintes campos:\n\n" + erros.toString());
            return false;
        }

        return true;
    }

    /**
     * Valida formato de email
     */
    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }

    /**
     * Converte data formatada (DD/MM/YYYY) para LocalDate
     */
    private LocalDate converterDataNascimento(String dataFormatada) {
        if (dataFormatada == null || dataFormatada.trim().isEmpty()) {
            return null;
        }

        // Remove espaços e valida formato completo (10 caracteres: DD/MM/YYYY)
        dataFormatada = dataFormatada.trim();
        if (dataFormatada.length() != 10) {
            return null; // Data incompleta
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dataFormatada, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("⚠️ Data inválida: " + dataFormatada);
            return null;
        }
    }

    /**
     * Exibe dialog de erro
     */
    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe dialog de sucesso
     */
    private void mostrarSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Salva o cliente após validação
     */
    @FXML
    public void salvarCliente() {
        System.out.println("\n========== SALVANDO CLIENTE ==========");

        // Valida campos obrigatórios
        if (!validarCamposObrigatorios()) {
            System.out.println("❌ Validação de campos falhou");
            return;
        }

        System.out.println("✅ Campos obrigatórios validados");

        try {
            // Cria objeto Cliente com os dados do formulário
            Cliente cliente = new Cliente();
            System.out.println("📝 Criando objeto Cliente...");

            // Dados pessoais
            cliente.setNome(txtNome.getText().trim());
            cliente.setCpf(txtCpf.getText().trim());
            cliente.setRg(txtRg.getText().trim().isEmpty() ? null : txtRg.getText().trim());
            System.out.println("   Nome: " + cliente.getNome());
            System.out.println("   CPF: " + cliente.getCpf());

            // Converter data de nascimento (DD/MM/YYYY -> LocalDate)
            cliente.setDataNascimento(converterDataNascimento(txtDataNascimento.getText().trim()));

            // Contato
            cliente.setEmail(txtEmail.getText().trim());
            cliente.setTelefone(txtTelefone.getText().trim());
            cliente.setTelefoneSecundario(
                    txtTelefoneSecundario.getText().trim().isEmpty() ? null
                            : txtTelefoneSecundario.getText().trim());

            // Endereço
            cliente.setCep(txtCep.getText().trim());
            cliente.setEndereco(txtEndereco.getText().trim());
            cliente.setNumero(txtNumero.getText().trim());
            cliente.setComplemento(txtComplemento.getText().trim().isEmpty() ? null : txtComplemento.getText().trim());
            cliente.setBairro(txtBairro.getText().trim());
            cliente.setCidade(txtCidade.getText().trim());
            cliente.setEstado(cmbEstado.getValue());

            // Observações
            cliente.setObservacoes(
                    txtObservacoes.getText().trim().isEmpty() ? null : txtObservacoes.getText().trim());

            // Status padrão
            cliente.setStatus("Ativo");

            // IMPORTANTE: Por enquanto, adiciona direto na tabela
            // Quando o MySQL estiver configurado, usaremos clienteDAO.inserir(cliente)
            if (clienteControllerPai != null) {
                try {
                    clienteControllerPai.adicionarCliente(cliente);
                    System.out.println("✅ Cliente adicionado à tabela!");
                    mostrarSucesso("✅ Cliente Salvo",
                            "Cliente " + cliente.getNome() + " foi cadastrado com sucesso!");
                    fecharModal();
                } catch (RuntimeException e) {
                    // CPF ou RG duplicado
                    System.err.println("⚠️ Erro: " + e.getMessage());

                    if (e.getMessage().contains("CPF")) {
                        mostrarErro("❌ CPF Duplicado",
                                e.getMessage()
                                        + "\n\nEste CPF já está cadastrado no sistema.\nPor favor, verifique os dados.");
                    } else if (e.getMessage().contains("RG")) {
                        mostrarErro("❌ RG Duplicado",
                                e.getMessage()
                                        + "\n\nEste RG já está cadastrado no sistema.\nPor favor, verifique os dados.");
                    } else {
                        mostrarErro("❌ Erro ao Salvar", e.getMessage());
                    }
                }
            } else {
                mostrarErro("Erro", "Erro interno: Controller pai não foi configurado.");
            }

        } catch (Exception e) {
            // Trata erros inesperados
            System.err.println("❌ Erro ao salvar cliente:");
            e.printStackTrace();

            mostrarErro("Erro Inesperado",
                    "Ocorreu um erro ao salvar o cliente:\n" + e.getMessage());
        }
    }

    /**
     * Limpa todos os campos do formulário
     */
    @FXML
    public void limparFormulario() {
        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        txtCep.clear();
        txtEndereco.clear();
        txtNumero.clear();
        txtComplemento.clear();
        txtObservacoes.clear();
    }

    /**
     * Fecha o modal (dialog)
     */
    @FXML
    public void fecharModal() {
        // Obtém o Stage (janela) a partir de qualquer componente
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    /**
     * Define o cliente a ser editado e preenche os campos
     */
    public void setCliente(Cliente cliente) {
        this.clienteEditando = cliente;

        // Preenche os campos com os dados do cliente
        txtNome.setText(cliente.getNome());
        txtCpf.setText(cliente.getCpf());
        txtTelefone.setText(cliente.getTelefone());
        txtCep.setText(cliente.getCep());
        txtEndereco.setText(cliente.getEndereco());
        txtNumero.setText(cliente.getNumero());
        txtComplemento.setText(cliente.getComplemento());
        txtObservacoes.setText(cliente.getObservacoes());
    }

    /**
     * Define o controller pai (chamado pela tela principal)
     */
    public void setClienteControllerPai(ClienteController controller) {
        this.clienteControllerPai = controller;
    }

    /**
     * Salva as alterações do cliente (modo edição)
     */
    @FXML
    public void salvarAlteracoes() {
        // Valida campos obrigatórios
        if (!validarCamposObrigatorios()) {
            return;
        }

        // Atualiza os dados do cliente
        clienteEditando.setNome(txtNome.getText());
        clienteEditando.setCpf(txtCpf.getText());
        clienteEditando.setTelefone(txtTelefone.getText());
        clienteEditando.setCep(txtCep.getText());
        clienteEditando.setEndereco(txtEndereco.getText());
        clienteEditando.setNumero(txtNumero.getText());
        clienteEditando.setComplemento(txtComplemento.getText());
        clienteEditando.setObservacoes(txtObservacoes.getText());

        System.out.println("=== CLIENTE ATUALIZADO ===");
        System.out.println(clienteEditando.toString());

        // Exibe mensagem de sucesso
        mostrarSucesso("Alterações Salvas",
                "Os dados de " + clienteEditando.getNome() + " foram atualizados!");

        // Fecha o modal
        fecharModal();
    }
}
