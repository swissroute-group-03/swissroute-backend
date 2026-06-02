package com.swissroute.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swissroute.audit.ModelAudit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "historial_busquedas", schema = "swissroute")
@Schema(
        name = "SearchHistory",
        description = "Entidad que representa el historial de búsquedas de conexiones del usuario autenticado"
)
public class SearchHistory extends ModelAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del historial", example = "1")
    private Long id;

    @NotBlank(message = "El origen no puede estar vacío")
    @Column(name = "origen", nullable = false, length = 255)
    private String origen;

    @NotBlank(message = "El destino no puede estar vacío")
    @Column(name = "destino", nullable = false, length = 255)
    private String destino;

    @NotNull(message = "La fecha de consulta es obligatoria")
    @Column(name = "fecha_consulta", nullable = false)
    private LocalDateTime fechaConsulta;

    @NotNull(message = "El número de resultados es obligatorio")
    @Column(name = "num_resultados", nullable = false)
    private Integer numResultados;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "El usuario es obligatorio")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;
}