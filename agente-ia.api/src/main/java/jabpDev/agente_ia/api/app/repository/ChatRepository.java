package jabpDev.agente_ia.api.app.repository;

import jabpDev.agente_ia.api.app.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    List<Chat> findByUsuarioEmailOrderByAtualizadoEmDesc(String email);
}
