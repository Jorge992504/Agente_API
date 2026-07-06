package jabpDev.agente_ia.api.app.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_tipo_ia_aia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UsuarioTipoIA {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_ia", nullable = false)
    private TipoIA tipoIA;
}
