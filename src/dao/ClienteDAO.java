package dao;

import database.DatabaseManager;
import model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private final Connection connection;

    public ClienteDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /*
     * =========================
     * INSERIR CLIENTE
     * =========================
     */
    public boolean inserir(Cliente cliente) {

        String sql = """
                    INSERT INTO clientes
                    (nome, cpf, rg, data_nascimento, telefone, email,
                     telefone_secundario, cep, endereco, numero, complemento,
                     bairro, cidade, estado, status, observacoes)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getRg());
            stmt.setDate(4, cliente.getDataNascimento() != null
                    ? Date.valueOf(cliente.getDataNascimento())
                    : null);
            stmt.setString(5, cliente.getTelefone());
            stmt.setString(6, cliente.getEmail());
            stmt.setString(7, cliente.getTelefoneSecundario());
            stmt.setString(8, cliente.getCep());
            stmt.setString(9, cliente.getEndereco());
            stmt.setString(10, cliente.getNumero());
            stmt.setString(11, cliente.getComplemento());
            stmt.setString(12, cliente.getBairro());
            stmt.setString(13, cliente.getCidade());
            stmt.setString(14, cliente.getEstado());
            stmt.setString(15,
                    cliente.getStatus() == null ? "Ativo" : cliente.getStatus());
            stmt.setString(16, cliente.getObservacoes());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId(rs.getInt(1));
                    }
                }
            }

            System.out.println("✅ Cliente salvo no banco");
            return true;

        } catch (SQLException e) {
            System.err.println("❌ ERRO AO SALVAR CLIENTE NO BANCO");
            e.printStackTrace();
            return false;
        }
    }

    /*
     * =========================
     * LISTAR TODOS
     * =========================
     */
    public List<Cliente> listarTodos() {

        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nome";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /*
     * =========================
     * BUSCAR POR ID
     * =========================
     */
    public Cliente buscarPorId(int id) {

        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * =========================
     * ATUALIZAR
     * =========================
     */
    public boolean atualizar(Cliente cliente) {

        String sql = """
                    UPDATE clientes SET
                    nome=?, cpf=?, rg=?, data_nascimento=?, telefone=?, email=?,
                    telefone_secundario=?, cep=?, endereco=?, numero=?, complemento=?,
                    bairro=?, cidade=?, estado=?, status=?, observacoes=?
                    WHERE id=?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getRg());
            stmt.setDate(4, cliente.getDataNascimento() != null
                    ? Date.valueOf(cliente.getDataNascimento())
                    : null);
            stmt.setString(5, cliente.getTelefone());
            stmt.setString(6, cliente.getEmail());
            stmt.setString(7, cliente.getTelefoneSecundario());
            stmt.setString(8, cliente.getCep());
            stmt.setString(9, cliente.getEndereco());
            stmt.setString(10, cliente.getNumero());
            stmt.setString(11, cliente.getComplemento());
            stmt.setString(12, cliente.getBairro());
            stmt.setString(13, cliente.getCidade());
            stmt.setString(14, cliente.getEstado());
            stmt.setString(15, cliente.getStatus());
            stmt.setString(16, cliente.getObservacoes());
            stmt.setInt(17, cliente.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * =========================
     * DELETAR
     * =========================
     */
    public boolean deletar(int id) {

        String sql = "DELETE FROM clientes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * =========================
     * MAPEAMENTO
     * =========================
     */
    private Cliente mapearCliente(ResultSet rs) throws SQLException {

        new ClienteDAO().inserir(cliente);

        Cliente c = new Cliente();

        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setRg(rs.getString("rg"));

        Date data = rs.getDate("data_nascimento");
        if (data != null) {
            c.setDataNascimento(data.toLocalDate());
        }

        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setTelefoneSecundario(rs.getString("telefone_secundario"));
        c.setCep(rs.getString("cep"));
        c.setEndereco(rs.getString("endereco"));
        c.setNumero(rs.getString("numero"));
        c.setComplemento(rs.getString("complemento"));
        c.setBairro(rs.getString("bairro"));
        c.setCidade(rs.getString("cidade"));
        c.setEstado(rs.getString("estado"));
        c.setStatus(rs.getString("status"));
        c.setObservacoes(rs.getString("observacoes"));

        return c;
    }
}
