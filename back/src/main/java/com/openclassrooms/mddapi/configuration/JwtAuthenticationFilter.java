package com.openclassrooms.mddapi.configuration;

import com.openclassrooms.mddapi.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Filtre Spring Security chargé de valider un jeton JWT
 * et d'authentifier l'utilisateur correspondant.
 * <p>
 * Hérite de {@link OncePerRequestFilter}, ce qui signifie qu'il s'exécute
 * exactement une fois par requête.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Composant chargé de résoudre et de gérer les exceptions
     * qui peuvent survenir dans le filtre.
     */
    private final HandlerExceptionResolver handlerExceptionResolver;

    /**
     * Service fournissant la logique nécessaire pour extraire,
     * valider et manipuler les tokens JWT.
     */
    private final JwtService jwtService;

    /**
     * Service Spring Security permettant de charger les détails
     * d'un utilisateur
     * via son identifiant (email, username, etc.).
     */
    private final UserDetailsService userDetailsService;

    /**
     * Construit le filtre avec les dépendances nécessaires :
     * <ul>
     *     <li>Un {@link JwtService} pour manipuler les tokens JWT,</li>
     *     <li>Un {@link UserDetailsService} pour charger l'utilisateur,</li>
     *     <li>Un {@link HandlerExceptionResolver} pour gérer les exceptions.</li>
     * </ul>
     *
     * @param jwtService              service de manipulation des tokens JWT
     * @param userDetailsService      service pour charger les détails de l'utilisateur
     * @param handlerExceptionResolver composant pour résoudre et gérer les exceptions
     */
    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserDetailsService userDetailsService,
                                   HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    /**
     * Méthode principale du filtre, exécutée une seule fois par requête HTTP.
     * <p>
     * Elle vérifie la présence d'un en-tête {@code Authorization} commençant
     * par la chaîne "Bearer ". Si c'est le cas, elle extrait le jeton (JWT),
     * récupère l'email de l'utilisateur depuis le token, charge l'utilisateur
     * correspondant, vérifie la validité du token, et s'il est valide,
     * elle enregistre l'authentification dans le {@link SecurityContextHolder}.
     * <p>
     * En cas d'erreur (par exemple token invalide, problème de parsing...),
     * elle utilise le {@link HandlerExceptionResolver} pour gérer l'exception
     * et renvoyer une réponse appropriée.
     *
     * @param request     l'objet {@link HttpServletRequest} représentant la requête HTTP
     * @param response    l'objet {@link HttpServletResponse} représentant la réponse HTTP
     * @param filterChain la chaîne de filtres à poursuivre si la requête est légitime
     * @throws ServletException en cas de problème général avec le filtre
     * @throws IOException en cas de problème d'E/S lors de l'analyse de la requête
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");

            // Vérifie la présence et le format de l'en-tête d'authentification
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extrait le token JWT à partir de l'en-tête
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);

            // Si l'email de l'utilisateur est présent et que la requête n'est pas encore authentifiée
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Vérifie la validité du token JWT
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Création du token d'authentification pour Spring Security
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Stocke l'authentification dans le contexte de sécurité
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.err.println("Exception in JwtAuthenticationFilter: " + e.getMessage());
            // Délègue la gestion de l'exception à l'objet handlerExceptionResolver
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
