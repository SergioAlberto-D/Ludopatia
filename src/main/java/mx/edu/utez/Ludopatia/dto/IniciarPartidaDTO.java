package mx.edu.utez.Ludopatia.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IniciarPartidaDTO {
    private Long jugadorId;
    @NotNull(message = "El monto de la apuesta es obligatorio")
    @DecimalMin(value = "0.01",message = "La apuesta minima debe ser mayor a 0")
    private BigDecimal apuesta;
}
