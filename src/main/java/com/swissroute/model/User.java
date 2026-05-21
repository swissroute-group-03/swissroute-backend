package com.swissroute.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swissroute.audit.ModelAudit;

import io.swagger.v3.oas.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users", schema="swissroute")
@Schema(name="User", description = "Entidad que representa a un usuario del sistema")
public class User extends ModelAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String name;

    @Email(message = "El formato de correo no es válido")
    @NotBlank(message = "El correo no puede estar vacío")
    @Column(nullable=false, unique = true)
    @Schema(description = "Correo electrónico del usuario (único)", example = "juan@gmail.com", required = true)
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()]).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, un número y un símbolo especial")
    @Column(nullable = false)
    @Schema(description = "Contraseña del usuario (encriptada)", example = "$2a$10$....")
    private String password;

    @Column(name = "ciudad_base")
    private String ciudadBase;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Fecha de registro del usuario", example = "08/04/2025")
    private LocalDate signUpDate;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull(message = "El rol es obligatorio")
    @Schema(description = "Rol asignado al usuario", example = "1")
    private Role role;

    @PrePersist
    public void prePersist() {
        this.signUpDate = LocalDate.now();
    }

}