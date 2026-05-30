package com.swissroute.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swissroute.audit.ModelAudit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "rutas_favoritas", schema = "swissroute")
@Schema(
        name = "RutaFavorita",
        description = "Entidad que representa una ruta favorita guardada por un usuario autenticado"
)
public class RutaFavorita extends ModelAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la ruta favorita", example = "1")
    private Long id;

    @NotBlank(message = "El nombre de la ruta favorita no puede estar vacío")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    @Column(name = "nombre", nullable = false, length = 255)
    @Schema(
            description = "Nombre personalizado de la ruta favorita",
            example = "Ruta a la universidad",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @NotBlank(message = "El ID del origen no puede estar vacío")
    @Size(max = 50, message = "El ID del origen no puede superar los 50 caracteres")
    @Column(name = "origen_id", nullable = false, length = 50)
    @Schema(
            description = "ID de la estación de origen según la API externa",
            example = "008501120",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String origenId;

    @NotBlank(message = "El nombre del origen no puede estar vacío")
    @Size(max = 255, message = "El nombre del origen no puede superar los 255 caracteres")
    @Column(name = "origen_nombre", nullable = false, length = 255)
    @Schema(
            description = "Nombre de la estación de origen",
            example = "Lausanne",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String origenNombre;

    @NotBlank(message = "El ID del destino no puede estar vacío")
    @Size(max = 50, message = "El ID del destino no puede superar los 50 caracteres")
    @Column(name = "destino_id", nullable = false, length = 50)
    @Schema(
            description = "ID de la estación de destino según la API externa",
            example = "008501008",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String destinoId;

    @NotBlank(message = "El nombre del destino no puede estar vacío")
    @Size(max = 255, message = "El nombre del destino no puede superar los 255 caracteres")
    @Column(name = "destino_nombre", nullable = false, length = 255)
    @Schema(
            description = "Nombre de la estación de destino",
            example = "Genève",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String destinoNombre;

    @NotBlank(message = "El tipo de transporte no puede estar vacío")
    @Size(max = 100, message = "El tipo de transporte no puede superar los 100 caracteres")
    @Pattern(
            regexp = "(?i)train|tram|ship|bus|cableway",
            message = "El tipo de transporte debe ser: train, tram, ship, bus o cableway"
    )
    @Column(name = "tipo_transporte", nullable = false, length = 100)
    @Schema(
            description = "Tipo de transporte preferido para la ruta",
            example = "train",
            allowableValues = {"train", "tram", "ship", "bus", "cableway"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String tipoTransporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "El usuario es obligatorio")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Schema(description = "Usuario autenticado al que pertenece la ruta favorita")
    private User user;
}