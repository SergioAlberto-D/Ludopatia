package mx.edu.utez.Ludopatia.exception.customExceptions;

public class CustomBadRequestException extends RuntimeException{
    public CustomBadRequestException(String mensaje){
        super(mensaje);
    }
}
