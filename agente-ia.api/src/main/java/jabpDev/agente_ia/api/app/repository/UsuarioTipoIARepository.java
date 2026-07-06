package jabpDev.agente_ia.api.app.repository;

import jabpDev.agente_ia.api.app.entity.UsuarioTipoIA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioTipoIARepository extends JpaRepository<UsuarioTipoIA,Long> {
    void deleteByUsuarioEmail(String email);
}
