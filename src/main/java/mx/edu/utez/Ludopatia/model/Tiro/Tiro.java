package mx.edu.utez.Ludopatia.model.Tiro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import mx.edu.utez.Ludopatia.model.partida.Partida;

@Entity
@Table(name = "tiros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Partida partida;

    @Column(nullable = false)
    private Integer valorDado1;

    @Column(nullable = false)
    private Integer valorDado2;

    @Column(nullable = false)
    private Integer suma;

    @Column(nullable = false)
    private Boolean esGanador;

}
