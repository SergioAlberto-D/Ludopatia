package mx.edu.utez.Ludopatia.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.Ludopatia.dto.CrearJugadorDTO;
import mx.edu.utez.Ludopatia.dto.IniciarPartidaDTO;
import mx.edu.utez.Ludopatia.dto.RecargarSaldoDTO;
import mx.edu.utez.Ludopatia.dto.TiroResponseDTO;
import mx.edu.utez.Ludopatia.model.jugador.Jugador;
import mx.edu.utez.Ludopatia.model.partida.Partida;
import mx.edu.utez.Ludopatia.service.CasinoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CasinoController {

    private final CasinoService casinoService;

    @PostMapping("/jugadores")
    public ResponseEntity<Jugador> crearJugador(@Valid @RequestBody CrearJugadorDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(casinoService.crearJugador(payload));
    }

    @PostMapping("/partidas/iniciar")
    public ResponseEntity<Partida> iniciarPartida(@Valid @RequestBody IniciarPartidaDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(casinoService.iniciarPartida(payload));
    }

    @PostMapping("/partidas/{id}/tiro")
    public ResponseEntity<TiroResponseDTO> realizarTiro(@PathVariable("id") Long partidaId) {
        return ResponseEntity.ok(casinoService.realizarTiro(partidaId));
    }


    @PutMapping("/partidas/{id}/finalizar")
    public ResponseEntity<Partida> finalizarPartidaManual(@PathVariable("id") Long partidaId) {
        return ResponseEntity.ok(casinoService.finalizarPartidaManual(partidaId));
    }

    @PutMapping("/jugadores/{id}/recargar")
    public ResponseEntity<Jugador> recargarSaldo(
            @PathVariable("id") Long jugadorId,
            @Valid @RequestBody RecargarSaldoDTO payload) {
        return ResponseEntity.ok(casinoService.recargarSaldo(jugadorId, payload));
    }

    @GetMapping("/jugadores/{id}/partidas")
    public ResponseEntity<List<Partida>> obtenerHistorialPartida(@PathVariable("id") Long jugadorId) {
        return ResponseEntity.ok(casinoService.obtenerHistorialPartida(jugadorId));
    }
}
