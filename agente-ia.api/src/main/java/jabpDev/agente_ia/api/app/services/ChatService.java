package jabpDev.agente_ia.api.app.services;

import jabpDev.agente_ia.api.app.dto.request.MensagemDTORequest;
import jabpDev.agente_ia.api.app.dto.response.ChatDTOResponse;
import jabpDev.agente_ia.api.app.dto.response.MensagemDTOResponse;
import jabpDev.agente_ia.api.app.entity.*;
import jabpDev.agente_ia.api.app.repository.ChatRepository;
import jabpDev.agente_ia.api.app.repository.MensagemRepository;
import jabpDev.agente_ia.api.app.repository.TipoIARepository;
import jabpDev.agente_ia.api.app.repository.UsuarioRepository;
import jabpDev.agente_ia.api.app.utils.FileStorageUtils;
import jabpDev.agente_ia.api.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepository chatRepository;
    private MensagemRepository mensagemRepository;
    private UsuarioRepository usuarioRepository;
    private TipoIARepository tipoIARepository;
    private FileStorageUtils fileStorageService;
    private IAService iaService;
    private MemoriaIAService memoriaIAService;

    public List<ChatDTOResponse> listar(String idUsuario) {
        return chatRepository.findByUsuarioEmailOrderByAtualizadoEmDesc(idUsuario)
                .stream()
                .map(c -> new ChatDTOResponse(
                        c.getId(),
                        c.getTitulo(),
                        c.getTipoIA().getId(),
                        c.getTipoIA().getNome(),
                        c.getAtualizadoEm()))
                .toList();
    }

    public List<MensagemDTOResponse> listarMensagens(String idUsuario, Long idChat) {
        Chat chat = buscarChatDoUsuario(idUsuario, idChat);
        return mensagemRepository.findByChatIdOrderByCriadoEmAsc(chat.getId())
                .stream()
                .map(m -> new MensagemDTOResponse(
                        m.getId(),
                        m.getRemetente().name().toUpperCase(),
                        m.getTexto(),
                        m.getFoto(),
                        m.getArquivo(),
                        m.getAudio(),
                        m.getCriadoEm()
                ))
                .toList();
    }

    @Transactional
    public ChatDTOResponse criar(String idUsuario, Long idTipoIA) {
        Usuario usuario = usuarioRepository.findByEmail(idUsuario)
                .orElseThrow(() -> new ErrorException("Usuário não encontrado.", 404));
        TipoIA tipoIA = tipoIARepository.findById(idTipoIA)
                .orElseThrow(() -> new ErrorException("Tipo de IA não encontrado.", 404));

        boolean liberado = usuario.getTiposIA().stream()
                .anyMatch(ut -> ut.getTipoIA().getId().equals(idTipoIA));
        if (!liberado) {
            throw new ErrorException("Esse tipo de IA não está liberado pro seu plano.", 403);
        }

        Chat chat = Chat.builder().usuario(usuario).tipoIA(tipoIA).titulo(tipoIA.getNome()).build();
        chat = chatRepository.save(chat);
        return new ChatDTOResponse(
                chat.getId(),
                chat.getTitulo(),
                chat.getTipoIA().getId(),
                chat.getTipoIA().getNome(),
                chat.getAtualizadoEm());
    }

    public void excluir(String idUsuario, Long idChat) {
        chatRepository.delete(buscarChatDoUsuario(idUsuario, idChat));
    }

    @Transactional
    public MensagemDTOResponse processarMensagem(String idUsuario, MensagemDTORequest dto) {

        Chat chat = buscarChatDoUsuario(idUsuario, dto.idConversa());
        Plano plano = chat.getUsuario().getPlano();

        if (dto.foto() != null && !plano.isPermiteFoto())
            throw new ErrorException("Seu plano não permite envio de foto.", 403);
        if (dto.arquivo() != null && !plano.isPermiteArquivo())
            throw new ErrorException("Seu plano não permite envio de arquivo.", 403);
        if (dto.audio() != null && !plano.isPermiteAudio())
            throw new ErrorException("Seu plano não permite envio de áudio.", 403);

        String urlFoto = fileStorageService.salvarBase64(dto.foto(), "foto");
        String urlArquivo = fileStorageService.salvarBase64(dto.arquivo(), "arquivo");
        String urlAudio = fileStorageService.salvarBase64(dto.audio(), "audio");

        List<Mensagem> historicoRecente = mensagemRepository
                .findByChatIdOrderByCriadoEmDesc(chat.getId(), PageRequest.of(0,10))
                .reversed();

        mensagemRepository.save(Mensagem.builder()
                .chat(chat).remetente(Remetente.USUARIO)
                .texto(dto.message()).foto(urlFoto).arquivo(urlArquivo).audio(urlAudio)
                .build());

        List<String> contexto = memoriaIAService.buscarContextoRelevante(chat.getId(), dto.message());

        String respostaTexto = iaService.gerarResposta(chat, historicoRecente, dto.message(),contexto);

        Mensagem mensagemIA = mensagemRepository.save(Mensagem.builder()
                .chat(chat).remetente(Remetente.IA).texto(respostaTexto)
                .build());

        memoriaIAService.salvarMemoria(chat.getId(), dto.idConversa() , "usuario", dto.message());
        memoriaIAService.salvarMemoria(chat.getId(), mensagemIA.getId(), "ia", respostaTexto);

        chat.setAtualizadoEm(LocalDateTime.now());
        chatRepository.save(chat);

        return new MensagemDTOResponse(
                mensagemIA.getId(),
                mensagemIA.getRemetente().name().toUpperCase(),
                mensagemIA.getTexto(),
                mensagemIA.getFoto(),
                mensagemIA.getArquivo(),
                mensagemIA.getAudio(),
                mensagemIA.getCriadoEm()
                );
    }

    private Chat buscarChatDoUsuario(String idUsuario, Long idChat) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ErrorException("Conversa não encontrada.", 404));
        if (!chat.getUsuario().getEmail().equals(idUsuario)) {
            throw new ErrorException("Essa conversa não pertence a você.", 403);
        }
        return chat;
    }
}
