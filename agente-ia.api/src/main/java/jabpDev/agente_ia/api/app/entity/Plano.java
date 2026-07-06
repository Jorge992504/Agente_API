package jabpDev.agente_ia.api.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "plano_aia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Plano {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(name = "limite_tipos_ia", nullable = false)
    private Integer limiteTiposIA; // 0 = ilimitado (evita usar null pra essa regra)

    @Column(name = "permite_foto", nullable = false)
    private boolean permiteFoto;

    @Column(name = "permite_arquivo", nullable = false)
    private boolean permiteArquivo;

    @Column(name = "limite_arquivos_mensagem", nullable = false)
    private Integer limiteArquivosMensagem; // 0 = ilimitado

    @Column(name = "permite_audio", nullable = false)
    private boolean permiteAudio;
}
