package com.openclassrooms.mddapi.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service gérant la génération, l'extraction et la validation des tokens JWT.
 * <p>
 * Cette implémentation utilise la bibliothèque <em>io.jsonwebtoken</em>
 * pour créer et parser les tokens. Les clés de signature et la durée
 * d'expiration sont récupérées depuis la configuration de l'application
 * via les annotations {@code @Value}.
 */
@Service
public class JwtService implements JwtInterface {

    /**
     * Clé secrète utilisée pour signer et vérifier les tokens JWT.
     * Récupérée depuis la configuration de l'application.
     */
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * Durée d'expiration (en millisecondes) des tokens JWT.
     * Également récupérée depuis la configuration.
     */
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extrait le nom d'utilisateur
     * d'un token JWT.
     *
     * @param token le token JWT
     * @return le nom d'utilisateur contenu dans le token
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait une information spécifique d'un token JWT, sous forme de {@link Claims},
     * en appliquant une fonction de résolution sur les réclamations extraites.
     *
     * @param token          le token JWT
     * @param claimsResolver une fonction qui détermine quelle partie des {@link Claims} renvoyer
     * @param <T>            le type de l'information extraite (ex. {@code String}, {@code Date}, etc.)
     * @return la valeur extraite, selon la fonction de résolution
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Génère un token JWT pour un utilisateur, sans réclamations supplémentaires.
     *
     * @param userDetails les informations de l'utilisateur (implémentation de {@code UserDetails})
     * @return le token JWT
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Génère un token JWT pour un utilisateur, en y ajoutant des réclamations (claims)
     * supplémentaires et en appliquant la durée d'expiration configurée.
     *
     * @param extraClaims  un ensemble de paires clé-valeur à inclure dans le token
     * @param userDetails  les détails de l'utilisateur
     * @return le token JWT généré
     */
    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Retourne la durée d'expiration (en millisecondes) configurée pour les tokens JWT.
     *
     * @return la durée d'expiration des tokens
     */
    @Override
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Construit un token JWT en utilisant les réclamations fournies,
     * le nom d'utilisateur du {@link UserDetails} et une durée d'expiration spécifique.
     *
     * @param extraClaims un ensemble de paires clé-valeur à inclure dans le token
     * @param userDetails les détails de l'utilisateur
     * @param expiration  la durée (en millisecondes) avant expiration du token
     * @return le token JWT construit
     */
    @Override
    public String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        //> date d'émission : maintenant + 24 heures
        //> date d'expiration : maintenant + expiration
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Vérifie si un token JWT est valide pour un utilisateur donné,
     * notamment en comparant le nom d'utilisateur et en testant son expiration.
     *
     * @param token        le token à vérifier
     * @param userDetails  les détails de l'utilisateur qui devrait être présent dans le token
     * @return {@code true} si le token est valide, {@code false} dans le cas contraire
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Vérifie si un token JWT est déjà expiré.
     *
     * @param token le token à vérifier
     * @return {@code true} si le token est expiré, {@code false} sinon
     */
    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrait la date d'expiration d'un token JWT.
     *
     * @param token le token JWT
     * @return la date d'expiration du token
     */
    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrait la totalité des {@link Claims} contenues dans un token JWT.
     *
     * @param token le token JWT à parser
     * @return un objet {@link Claims} représentant toutes les informations
     *         contenues dans le token
     */
    @Override
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retourne la clé servant à signer et à vérifier les tokens JWT.
     * Décode d'abord la clé secrète en Base64, puis construit
     * une clé HMAC-SHA adaptée à l'algorithme {@code HS256}.
     *
     * @return la clé de signature/verrou pour les tokens JWT
     */
    @Override
    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
