package jabpDev.agente_ia.api.app.services;


import jabpDev.agente_ia.api.app.dto.response.PlanoDTOResponse;
import jabpDev.agente_ia.api.app.dto.response.TipoIADTOResponse;
import jabpDev.agente_ia.api.app.dto.response.UsuarioDTOResponse;
import jabpDev.agente_ia.api.app.entity.Plano;
import jabpDev.agente_ia.api.app.entity.Usuario;
import jabpDev.agente_ia.api.app.repository.PlanoRepository;
import jabpDev.agente_ia.api.app.repository.UsuarioRepository;
import jabpDev.agente_ia.api.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlanoService {

    private PlanoRepository planoRepository;
    private UsuarioRepository usuarioRepository;

    public List<PlanoDTOResponse> listar(){
        return planoRepository.findAll().stream()
                .filter(p -> p.getPreco().compareTo(BigDecimal.ZERO) > 0)
                .map(p -> new PlanoDTOResponse(p.getId(), p.getNome(), p.getPreco(), montarRecursos(p), p.getId() == 2L))
                .toList();
    }

    private List<String> montarRecursos(Plano p) {
        List<String> recursos = new ArrayList<>();
        recursos.add(p.getLimiteTiposIA() == 0 ? "Tipos de IA ilimitados" : p.getLimiteTiposIA() + " tipo(s) de IA");
        recursos.add("Mensagens de texto");
        if (p.isPermiteFoto()) recursos.add("Envio de foto");
        if (p.isPermiteArquivo()) {
            recursos.add(p.getLimiteArquivosMensagem() == 0
                    ? "Arquivos ilimitados"
                    : p.getLimiteArquivosMensagem() + " arquivo por mensagem");
        }
        if (p.isPermiteAudio()) recursos.add("Envio de áudio");
        return recursos;
    }

    @Transactional
    public UsuarioDTOResponse escolherPlano(String idUsuario, Long idPlano) {
        Usuario usuario = usuarioRepository.findByEmail(idUsuario)
                .orElseThrow(() -> new ErrorException("Usuário não encontrado.", 404));
        Plano plano = planoRepository.findById(idPlano)
                .orElseThrow(() -> new ErrorException("Plano não encontrado.", 404));

        usuario.setPlano(plano);
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
