package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Représente un commentaire dans l'application. Chaque commentaire est associé
 * à un post et à un utilisateur, et contient un contenu textuel ainsi qu'une
 * date de création.
 */
@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    /**
     * Identifiant unique du commentaire.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Le post auquel ce commentaire est rattaché.
     * <p>
     * Utilise un chargement en mode "LAZY" pour charger le post uniquement
     * lorsque cela est nécessaire.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /**
     * L'utilisateur ayant créé ce commentaire.
     * <p>
     * Utilise également un chargement en mode "LAZY" pour ne charger l'utilisateur
     * qu'en cas de besoin.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Contenu textuel du commentaire.
     * <p>
     * Stocké en tant que champ de type "LOB" (Large Object) pour accueillir
     * un texte potentiellement long.
     */
    @Lob
    @Column(nullable = false)
    private String content;

    /**
     * Date et heure de création du commentaire.
     * <p>
     * Renseignée automatiquement lors de la première insertion en base de données
     * grâce à la méthode {@link #onCreate()}.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Initialise le champ {@code createdAt} avec la date et l'heure courantes
     * juste avant l'insertion en base de données (persist).
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
