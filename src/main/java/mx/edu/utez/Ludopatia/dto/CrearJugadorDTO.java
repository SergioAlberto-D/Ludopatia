package mx.edu.utez.Ludopatia.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CrearJugadorDTO {
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;
    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0",inclusive = true, message = "El saldo no puede ser negativo")
    private BigDecimal saldoInicial;
}
