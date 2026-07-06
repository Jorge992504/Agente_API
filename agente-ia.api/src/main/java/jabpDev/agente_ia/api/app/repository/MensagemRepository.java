package jabpDev.agente_ia.api.app.repository;

import jabpDev.agente_ia.api.app.entity.Mensagem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem,Long> {
    List<Mensagem> findByChatIdOrderByCriadoEmAsc(Long idChat);
    List<Mensagem> findByChatIdOrderByCriadoEmDesc(Long idChat, Pageable pageable);
}
