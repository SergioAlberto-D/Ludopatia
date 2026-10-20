package mx.edu.utez.Ludopatia.dto;

import lombok.Builder;
import lombok.Data;
import mx.edu.utez.Ludopatia.model.EstadoPartida;

import java.math.BigDecimal;

@Data
@Builder
public class TiroResponseDTO {
    private Long tiroId;
    private Long partidaId;
    private Integer dado1;
    private Integer dado2;
    private Integer suma;
    private Boolean esGanador;
    private EstadoPartida estadoPartida;
    private String mensaje;
    private BigDecimal saldoActual;
}
