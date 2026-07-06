package jabpDev.agente_ia.api.app.services;

import jabpDev.agente_ia.api.app.dto.request.LoginDTORequest;
import jabpDev.agente_ia.api.app.dto.request.RegisterDTORequest;
import jabpDev.agente_ia.api.app.dto.response.LoginDTOResponse;
import jabpDev.agente_ia.api.app.dto.response.TipoIADTOResponse;
import jabpDev.agente_ia.api.app.dto.response.UsuarioDTOResponse;
import jabpDev.agente_ia.api.app.dto.response.ValidarTokenDTOResponse;
import jabpDev.agente_ia.api.app.entity.Plano;
import jabpDev.agente_ia.api.app.entity.Usuario;
import jabpDev.agente_ia.api.app.repository.PlanoRepository;
import jabpDev.agente_ia.api.app.repository.UsuarioRepository;
import jabpDev.agente_ia.api.app.utils.JwtUtils;
import jabpDev.agente_ia.api.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private PlanoRepository planoRepository;


    @Transactional
    public LoginDTOResponse cadastrar(RegisterDTORequest dto){
        if (usuarioRepository.existsByEmail(dto.email()) ) throw new ErrorException("Já existe uma conta com esse e-mail.", 409);

        Plano planoFree = planoRepository.findByNome("Free").orElseThrow(() -> new ErrorException("Plano padrão não encontrado.", 500));

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .plano(planoFree)
                .build();

        usuario = usuarioRepository.save(usuario);
        String token = jwtUtils.gerarToken(usuario.getEmail());

        return new LoginDTOResponse(token, new UsuarioDTOResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPlano().getId(),
                usuario.getPlano().getNome(),
                List.of()
        ));
    }

    public LoginDTOResponse login(LoginDTORequest dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ErrorException("Credenciais inválidas.", 401));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha()))
            throw new ErrorException("Credenciais inválidas.", 401);

        String token = jwtUtils.gerarToken(usuario.getEmail());

        return new LoginDTOResponse(token, new UsuarioDTOResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPlano().getId(),
                usuario.getPlano().getNome(),
                usuario.getTiposIA().stream().map(tipoIA -> new TipoIADTOResponse(
                        tipoIA.getTipoIA().getId(),
                        tipoIA.getTipoIA().getNome(),
                        tipoIA.getTipoIA().getDescricao(),
                        tipoIA.getTipoIA().getIcone()
                )).toList()
        ));
    }

    public ValidarTokenDTOResponse validar(String idUsuario){
        return usuarioRepository.findByEmail(idUsuario)
                .map(u -> new ValidarTokenDTOResponse(true, new UsuarioDTOResponse(
                        u.getId(),
                        u.getNome(),
                        u.getEmail(),
                        u.getPlano().getId(),
                        u.getPlano().getNome(),
                        u.getTiposIA().stream().map(tipoIA -> new TipoIADTOResponse(
                                tipoIA.getTipoIA().getId(),
                                tipoIA.getTipoIA().getNome(),
                                tipoIA.getTipoIA().getDescricao(),
                                tipoIA.getTipoIA().getIcone()
                        )).toList()
                )))
                .orElse(new ValidarTokenDTOResponse(false, null));
    }
}
