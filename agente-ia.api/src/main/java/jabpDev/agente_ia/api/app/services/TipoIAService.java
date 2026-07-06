package jabpDev.agente_ia.api.app.services;


import jabpDev.agente_ia.api.app.dto.response.TipoIADTOResponse;
import jabpDev.agente_ia.api.app.dto.response.UsuarioDTOResponse;
import jabpDev.agente_ia.api.app.entity.TipoIA;
import jabpDev.agente_ia.api.app.entity.Usuario;
import jabpDev.agente_ia.api.app.entity.UsuarioTipoIA;
import jabpDev.agente_ia.api.app.repository.TipoIARepository;
import jabpDev.agente_ia.api.app.repository.UsuarioRepository;
import jabpDev.agente_ia.api.app.repository.UsuarioTipoIARepository;
import jabpDev.agente_ia.api.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TipoIAService {

    private TipoIARepository tipoIARepository;
    private UsuarioRepository usuarioRepository;
    private UsuarioTipoIARepository usuarioTipoIARepository;

    public List<TipoIADTOResponse> listar(){
        return tipoIARepository.findAll().stream()
                .map(tIA -> new TipoIADTOResponse(tIA.getId(), tIA.getNome(),tIA.getDescricao(),tIA.getIcone()))
                .toList();
    }

    @Transactional
    public UsuarioDTOResponse escolherTipos(String idUsuario, List<Long> idsTiposIA) {
        Usuario usuario = usuarioRepository.findByEmail(idUsuario)
                .orElseThrow(() -> new ErrorException("Usuário não encontrado.", 404));

        if (usuario.getPlano() == null) {
            throw new ErrorException("Escolha um plano antes de selecionar os tipos de IA.", 400);
        }

        int limite = usuario.getPlano().getLimiteTiposIA(); // 0 = ilimitado
        if (limite != 0 && idsTiposIA.size() > limite) {
            throw new ErrorException("Seu plano permite no máximo " + limite + " tipo(s) de IA.", 403);
        }

        List<TipoIA> tipos = tipoIARepository.findAllById(idsTiposIA);
        if (tipos.size() != idsTiposIA.size()) {
            throw new ErrorException("Um ou mais tipos de IA não existem.", 404);
        }

        usuario.getTiposIA().clear();

        usuario.getTiposIA().addAll(
                tipos.stream()
                        .map(t -> UsuarioTipoIA.builder()
                                .usuario(usuario)
                                .tipoIA(t)
                                .build())
                        .toList()
        );

        usuarioRepository.save(usuario);

        return new UsuarioDTOResponse(usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPlano().getId(),
                usuario.getPlano().getNome(),
                usuario.getTiposIA().stream().map(tipoIA -> new TipoIADTOResponse(
                        tipoIA.getTipoIA().getId(),
                        tipoIA.getTipoIA().getNome(),
                        tipoIA.getTipoIA().getDescricao(),
                        tipoIA.getTipoIA().getIcone()
                )).toList());
    }
}
