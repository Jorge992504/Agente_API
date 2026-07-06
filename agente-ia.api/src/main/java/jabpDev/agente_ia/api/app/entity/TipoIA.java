package jabpDev.agente_ia.api.app.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_ia_aia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TipoIA {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String icone;

    // O que vai virar a "personalidade" da IA no prompt enviado pra Groq
    @Column(name = "prompt_sistema", columnDefinition = "TEXT")
    private String promptSistema;

    @Column(name = "modelo_ia")
    private String modeloIA; // ex: llama-3.3-70b-versatile
}
