package jabpDev.agente_ia.api.app.services;


import jabpDev.agente_ia.api.app.dto.response.PagamentoDTOResponse;
import jabpDev.agente_ia.api.app.entity.Pagamento;
import jabpDev.agente_ia.api.app.entity.Plano;
import jabpDev.agente_ia.api.app.entity.StatusPagamento;
import jabpDev.agente_ia.api.app.entity.Usuario;
import jabpDev.agente_ia.api.app.repository.PagamentoRepository;
import jabpDev.agente_ia.api.app.repository.PlanoRepository;
import jabpDev.agente_ia.api.app.repository.UsuarioRepository;
import jabpDev.agente_ia.api.exception.ErrorException;
import lombok.AllArgsConstructor;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private PagamentoRepository pagamentoRepository;
    private UsuarioRepository usuarioRepository;
    private PlanoRepository planoRepository;

    @Value("${app.front-url}")
    private String frontUrl;

    @Value("${app.api-url}")
    private String apiUrl;

    public PagamentoDTOResponse criarCheckout(String idUsuario, Long idPlano) {
        Usuario usuario = usuarioRepository.findByEmail(idUsuario)
                .orElseThrow(() -> new ErrorException("Usuário não encontrado.", 404));
        Plano plano = planoRepository.findById(idPlano)
                .orElseThrow(() -> new ErrorException("Plano não encontrado.", 404));

        String referenciaExterna = UUID.randomUUID().toString();

        pagamentoRepository.save(Pagamento.builder()
                .usuario(usuario)
                .plano(plano)
                .referenciaExterna(referenciaExterna)
                .status(StatusPagamento.PENDENTE)
                .build());

        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .title("Plano " + plano.getNome() + " - Front IA")
                .quantity(1)
                .currencyId("BRL")
                .unitPrice(plano.getPreco())
                .build();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(frontUrl + "/pagamento/retorno?status=aprovado")
                .pending(frontUrl + "/pagamento/retorno?status=pendente")
                .failure(frontUrl + "/pagamento/retorno?status=recusado")
                .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(List.of(item))
                .externalReference(referenciaExterna)
                .backUrls(backUrls)
                .autoReturn("approved")
                .notificationUrl(apiUrl + "/pagamento/webhook")
                .build();

        try {
            Preference preference = new PreferenceClient().create(request);
            return new PagamentoDTOResponse(preference.getInitPoint());
        } catch (MPException | MPApiException e) {
            throw new ErrorException("Erro ao iniciar o pagamento com o Mercado Pago.", 500);
        }
    }

    @Transactional
    public void processarWebhook(Map<String, Object> payload) {
        Object dataObj = payload.get("data");
        if (!(dataObj instanceof Map<?, ?> data) || !"payment".equals(payload.get("type"))) return;

        String paymentId = String.valueOf(data.get("id"));

        try {
            Payment payment = new PaymentClient().get(Long.parseLong(paymentId));
            String referenciaExterna = payment.getExternalReference();

            Pagamento pagamento = pagamentoRepository.findByReferenciaExterna(referenciaExterna).orElse(null);
            if (pagamento == null || pagamento.getStatus() == StatusPagamento.APROVADO) return;

            if ("approved".equals(payment.getStatus())) {
                pagamento.setStatus(StatusPagamento.APROVADO);
                pagamento.setAtualizadoEm(LocalDateTime.now());
                pagamentoRepository.save(pagamento);

                Usuario usuario = pagamento.getUsuario();
                usuario.setPlano(pagamento.getPlano());
                usuarioRepository.save(usuario);
            } else if ("rejected".equals(payment.getStatus())) {
                pagamento.setStatus(StatusPagamento.RECUSADO);
                pagamento.setAtualizadoEm(LocalDateTime.now());
                pagamentoRepository.save(pagamento);
            }
        } catch (MPException | MPApiException e) {
            throw new ErrorException("Erro ao processar notificação do Mercado Pago.", 500);
        }
    }
}
