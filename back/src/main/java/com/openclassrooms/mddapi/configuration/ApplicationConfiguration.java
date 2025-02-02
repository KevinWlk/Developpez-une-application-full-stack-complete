package com.openclassrooms.mddapi.configuration;

import com.openclassrooms.mddapi.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe de configuration principale pour l'application,
 * gérant la configuration de Spring Security et définissant
 * les beans nécessaires à l'authentification.
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * Référentiel permettant la recherche d'utilisateurs
     * en base de données via leur adresse email.
     */
    private final UserRepository userRepository;

    /**
     * Initialise la configuration avec le référentiel d'utilisateurs.
     *
     * @param userRepository référentiel Spring Data JPA pour {@code User}
     */
    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Définit un {@link UserDetailsService} qui charge un utilisateur
     * à partir de l'adresse email fournie.
     * <p>
     * Lance une {@link UsernameNotFoundException} si l'utilisateur n'existe pas.
     *
     * @return un composant {@link UserDetailsService} pour l'authentification
     */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non référencé."));
    }

    /**
     * Crée et configure un encodeur de mots de passe utilisant l'algorithme
     * {@code BCrypt}.
     *
     * @return une instance de {@link BCryptPasswordEncoder}
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fournit un {@link AuthenticationManager} à partir de la
     * configuration Spring Security existante.
     *
     * @param config objet {@link AuthenticationConfiguration} gérant la configuration
     *               d'authentification de Spring Security
     * @return une instance configurée de {@link AuthenticationManager}
     * @throws Exception si un problème survient lors de la configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Définit un {@link AuthenticationProvider} basé sur un
     * {@link DaoAuthenticationProvider}, qui s'appuie sur
     * {@link UserDetailsService} pour charger l'utilisateur
     * et sur {@link BCryptPasswordEncoder} pour vérifier son mot de passe.
     *
     * @return un composant {@link AuthenticationProvider} pour la gestion
     *         de l'authentification
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
