package jabpDev.agente_ia.api.app.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensagem_aia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class Mensagem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chat", nullable = false)
    private Chat chat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Remetente remetente;

    @Column(columnDefinition = "TEXT")
    private String texto;

    // Guardando a URL do arquivo, não o base64 (ver observação abaixo)
    private String foto;
    private String arquivo;
    private String audio;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
