package exceptions;

public class UsuarioNaoAutorizadoException extends Exception {
    public UsuarioNaoAutorizadoException(String message) {
        super(message);
    }
}
