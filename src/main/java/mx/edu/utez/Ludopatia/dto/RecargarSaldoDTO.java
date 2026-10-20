package mx.edu.utez.Ludopatia.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecargarSaldoDTO {
    @NotNull(message = "El monto a recargar es obligatorio")
    @DecimalMin(value = "0.01",message = "El monto debe de ser mayor a  0")
    private BigDecimal monto;
}
