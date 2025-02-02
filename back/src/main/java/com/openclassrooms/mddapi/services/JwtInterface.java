package com.openclassrooms.mddapi.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Interface définissant les opérations liées à la gestion des JSON Web Tokens (JWT).
 * Permet notamment d'extraire des informations (nom d'utilisateur, date d'expiration, etc.),
 * de générer et de valider des tokens.
 */
public interface JwtInterface {

    /**
     * Extrait le nom d'utilisateur
     * depuis un token JWT.
     *
     * @param token le token JWT
     * @return le nom d'utilisateur contenu dans le token
     */
    String extractUsername(String token);

    /**
     * Extrait une réclamation spécifique (une information) depuis le token JWT
     * en appliquant une fonction de résolution sur les {@link Claims}.
     *
     * @param token          le token JWT
     * @param claimsResolver la fonction appliquée aux {@link Claims}
     * @param <T>            le type de retour attendu (String, Date, etc.)
     * @return la valeur extraite depuis les {@link Claims}
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Génère un token JWT pour un utilisateur spécifique à partir
     * de ses informations de connexion (implémentation de {@link UserDetails}).
     *
     * @param userDetails les détails de l'utilisateur (nom, mots de passe, etc.)
     * @return une chaîne de caractères représentant le token JWT
     */
    String generateToken(UserDetails userDetails);

    /**
     * Génère un token JWT pour un utilisateur donné en y ajoutant
     * des réclamations (claims) supplémentaires.
     *
     * @param extraClaims  un ensemble de paires clé-valeur à inclure dans le token
     * @param userDetails  les détails de l'utilisateur
     * @return le token JWT généré
     */
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Retourne la durée (en millisecondes ou en secondes, selon l'implémentation)
     * pendant laquelle un token reste valide avant son expiration.
     *
     * @return la durée d'expiration
     */
    long getExpirationTime();

    /**
     * Construit un token JWT en y intégrant des claims supplémentaires,
     * les informations de l'utilisateur et une durée d'expiration spécifique.
     *
     * @param extraClaims un ensemble de paires clé-valeur à inclure dans le token
     * @param userDetails les détails de l'utilisateur
     * @param expiration  la durée ou la date d'expiration à appliquer
     * @return le token JWT construit
     */
    String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    );

    /**
     * Vérifie si un token est valide pour un utilisateur donné,
     * par exemple en comparant le nom d'utilisateur contenu dans
     * le token et le {@link UserDetails} fourni, ainsi qu'en vérifiant
     * que le token n'est pas expiré.
     *
     * @param token        le token à valider
     * @param userDetails  les informations de l'utilisateur attendu
     * @return {@code true} si le token est valide, {@code false} sinon
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Vérifie si un token JWT est expiré.
     *
     * @param token le token JWT à vérifier
     * @return {@code true} si le token est expiré, sinon {@code false}
     */
    boolean isTokenExpired(String token);

    /**
     * Extrait la date d'expiration d'un token JWT.
     *
     * @param token le token JWT
     * @return la date d'expiration du token
     */
    Date extractExpiration(String token);

    /**
     * Extrait l'ensemble des {@link Claims} (toutes les informations contenues)
     * du token JWT.
     *
     * @param token le token JWT
     * @return les réclamations contenues dans le token
     */
    Claims extractAllClaims(String token);

    /**
     * Retourne la clé secrète ou publique utilisée pour signer ou vérifier
     * les tokens JWT.
     *
     * @return la clé de signature ou de vérification du token
     */
    Key getSignInKey();
}
