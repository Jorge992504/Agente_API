package jabpDev.agente_ia.api.app.repository;

import jabpDev.agente_ia.api.app.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    Optional<Pagamento> findByReferenciaExterna(String referenciaExterna);
}
