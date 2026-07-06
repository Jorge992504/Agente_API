package jabpDev.agente_ia.api.app.repository;

import jabpDev.agente_ia.api.app.entity.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanoRepository extends JpaRepository<Plano,Long> {
    Optional<Plano> findByNome(String nome);
}
