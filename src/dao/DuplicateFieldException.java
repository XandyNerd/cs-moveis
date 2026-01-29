package dao;

/**
 * Exceção lançada quando há tentativa de inserir/atualizar
 * um campo único que já existe no banco de dados
 */
public class DuplicateFieldException extends Exception {
    private String fieldName;
    private String fieldValue;

    public DuplicateFieldException(String fieldName, String fieldValue) {
        super("Campo '" + fieldName + "' com valor '" + fieldValue + "' já existe no sistema");
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
