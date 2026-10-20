package mx.edu.utez.Ludopatia.service;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.Ludopatia.dto.CrearJugadorDTO;
import mx.edu.utez.Ludopatia.dto.IniciarPartidaDTO;
import mx.edu.utez.Ludopatia.dto.RecargarSaldoDTO;
import mx.edu.utez.Ludopatia.dto.TiroResponseDTO;
import mx.edu.utez.Ludopatia.exception.customExceptions.CustomBadRequestException;
import mx.edu.utez.Ludopatia.model.EstadoPartida;
import mx.edu.utez.Ludopatia.model.Tiro.Tiro;
import mx.edu.utez.Ludopatia.model.jugador.Jugador;
import mx.edu.utez.Ludopatia.model.partida.Partida;
import mx.edu.utez.Ludopatia.repository.JugadorRepository;
import mx.edu.utez.Ludopatia.repository.PartidaRepository;
import mx.edu.utez.Ludopatia.repository.TiroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CasinoService {
    private final JugadorRepository jugadorRepository;
    private final PartidaRepository partidaRepository;
    private final TiroRepository tiroRepository;
    private final Random random = new Random();

    @Transactional
    public Jugador crearJugador(CrearJugadorDTO payload) {
        Jugador jugador = Jugador.builder()
                .nombre(payload.getNombre())
                .saldo(payload.getSaldoInicial())
                .activo(true)
                .build();
        return jugadorRepository.save(jugador);
    }
    @Transactional
    public Partida iniciarPartida(IniciarPartidaDTO payload) {
        Jugador jugador = jugadorRepository.findById(payload.getJugadorId()).
                orElseThrow(() -> new CustomBadRequestException("El jugador con ID " + payload.getJugadorId() + " no existe."));

        if (!Boolean.TRUE.equals(jugador.getActivo())) {
            throw new CustomBadRequestException("El jugador se encuentra inactivo y no puede jugar.");
        }

        if (jugador.getSaldo().compareTo(payload.getApuesta()) < 0) {
            throw new CustomBadRequestException("Saldo insuficiente. Tu saldo actual es: " + jugador.getSaldo());
        }

        jugador.setSaldo(jugador.getSaldo().subtract(payload.getApuesta()));
        jugadorRepository.save(jugador);

        Partida partida = Partida.builder()
                .jugador(jugador)
                .apuesta(payload.getApuesta())
                .fecha(LocalDateTime.now())
                .estado(EstadoPartida.EN_JUEGO)
                .build();
        return partidaRepository.save(partida);
    }
    @Transactional
    public TiroResponseDTO realizarTiro(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new CustomBadRequestException("La partida con ID " + partidaId + " no existe."));

        if (partida.getEstado() != EstadoPartida.EN_JUEGO) {
            throw new CustomBadRequestException("No se puede tirar: la partida ya está " + partida.getEstado());
        }

        Jugador jugador = partida.getJugador();

        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;
        int suma = dado1 + dado2;

        boolean esGanador = false;
        String mensaje;

        if (suma == 7 || suma == 11) {
            esGanador = true;
            partida.setEstado(EstadoPartida.FINALIZADA);
            BigDecimal ganancia = partida.getApuesta().multiply(BigDecimal.valueOf(2));
            jugador.setSaldo(jugador.getSaldo().add(ganancia));
            jugadorRepository.save(jugador);
            mensaje = "¡Felicidades, ganaste! Has recibido el doble de tu apuesta.";
        } else if (suma == 2 || suma == 3 || suma == 12) {
            partida.setEstado(EstadoPartida.FINALIZADA);
            mensaje = "¡Mala suerte, perdiste! Fin de la partida.";
        } else {
            mensaje = "Tiro neutro. Continúa la partida en juego.";
        }

        partidaRepository.save(partida);

        Tiro tiro = Tiro.builder()
                .partida(partida)
                .valorDado1(dado1)
                .valorDado2(dado2)
                .suma(suma)
                .esGanador(esGanador)
                .build();

        tiro = tiroRepository.save(tiro);

        return TiroResponseDTO.builder()
                .tiroId(tiro.getId())
                .partidaId(partida.getId())
                .dado1(dado1)
                .dado2(dado2)
                .suma(suma)
                .esGanador(esGanador)
                .estadoPartida(partida.getEstado())
                .mensaje(mensaje)
                .saldoActual(jugador.getSaldo())
                .build();
    }
    @Transactional
    public Partida finalizarPartidaManual(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new CustomBadRequestException("La partida con ID " + partidaId + " no existe."));

        if (partida.getEstado() == EstadoPartida.FINALIZADA) {
            throw new CustomBadRequestException("La partida ya está finalizada.");
        }

        partida.setEstado(EstadoPartida.FINALIZADA);
        return partidaRepository.save(partida);
    }
    @Transactional
    public Jugador recargarSaldo(Long jugadorId, RecargarSaldoDTO dto) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new CustomBadRequestException("El jugador con ID " + jugadorId + " no existe."));

        jugador.setSaldo(jugador.getSaldo().add(dto.getMonto()));
        return jugadorRepository.save(jugador);
    }
    @Transactional(readOnly = true)
    public List<Partida> obtenerHistorialPartida(Long jugadorId) {
        if (!jugadorRepository.existsById(jugadorId)) {
            throw new CustomBadRequestException("El jugador con ID " + jugadorId + " no existe.");
        }
        return partidaRepository.findByJugadorId(jugadorId);
    }
}
