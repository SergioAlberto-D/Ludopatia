package mx.edu.utez.Ludopatia.repository;

import mx.edu.utez.Ludopatia.model.Tiro.Tiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiroRepository extends JpaRepository<Tiro, Long> {
}
