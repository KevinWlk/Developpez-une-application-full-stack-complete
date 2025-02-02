package com.openclassrooms.mddapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration principale de la sécurité Spring Security pour l'application.
 * <p>
 * Cette classe définit notamment :
 * <ul>
 *     <li>Les routes accessibles sans authentification,</li>
 *     <li>La politique de session (sans état),</li>
 *     <li>Le filtre d'authentification JWT,</li>
 *     <li>La configuration CORS.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Fournisseur d'authentification (par exemple {@code DaoAuthenticationProvider})
     * permettant à Spring Security de gérer la validation des utilisateurs
     * et de leurs mots de passe.
     */
    private final AuthenticationProvider authenticationProvider;

    /**
     * Filtre personnalisé qui intercepte chaque requête HTTP afin de
     * vérifier la présence et la validité d'un token JWT.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Initialise la configuration de sécurité avec le filtre JWT et
     * le fournisseur d'authentification.
     *
     * @param jwtAuthenticationFilter le filtre d'authentification JWT
     * @param authenticationProvider  le fournisseur d'authentification
     */
    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
                                 AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Déclare la chaîne de filtres de sécurité (SecurityFilterChain)
     * gérée par Spring Security.
     * <p>
     * Cette configuration :
     * <ul>
     *   <li>Désactive la protection CSRF ({@code http.csrf().disable()}).</li>
     *   <li>Permet l'accès libre à certaines routes (ex. : {@code /api/auth/**},
     *       {@code /swagger-ui/**}, {@code /v3/api-docs/**}).</li>
     *   <li>Exige une authentification pour toutes les autres routes.</li>
     *   <li>Spécifie une stratégie de session sans état (JWT), via
     *       {@link SessionCreationPolicy#STATELESS}.</li>
     *   <li>Enregistre le {@link AuthenticationProvider} et ajoute le filtre
     *       {@link JwtAuthenticationFilter} avant {@link UsernamePasswordAuthenticationFilter}.</li>
     *   <li>Active la configuration CORS par défaut.</li>
     * </ul>
     *
     * @param http l'objet {@link HttpSecurity} utilisé pour configurer la sécurité
     * @return une instance de {@link SecurityFilterChain} décrivant la configuration
     * @throws Exception si la configuration échoue
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Désactive la protection CSRF
                .authorizeHttpRequests(authorize -> authorize
                        // Routes publiques
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated() // Toutes les autres routes nécessitent une authentification
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Gestion de session sans état
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Filtre JWT
                .cors(withDefaults()); // Active la configuration CORS

    }

    /**
     * Configure la gestion des origines (CORS) pour l'application.
     * <p>
     * Cette méthode :
     * <ul>
     *     <li>Autorise l'origine "http://localhost:4200",</li>
     *     <li>Permet l'utilisation des méthodes "GET", "POST", "PUT", "DELETE", "OPTIONS",</li>
     *     <li>Accepte les en-têtes "Authorization" et "Content-Type",</li>
     *     <li>Autorise l'envoi des identifiants (cookies, tokens) via
     *         {@link CorsConfiguration#setAllowCredentials(boolean)},</li>
     *     <li>Applique cette configuration pour l'ensemble des routes ({@code "/**"}).</li>
     * </ul>
     *
     * @return un {@link CorsConfigurationSource} pour configurer les règles CORS
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Origine Angular
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Méthodes autorisées
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // En-têtes autorisés
        configuration.setAllowCredentials(true); // Autorise l'envoi des cookies ou tokens

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applique la configuration à toutes les routes
        return source;
    }
}
