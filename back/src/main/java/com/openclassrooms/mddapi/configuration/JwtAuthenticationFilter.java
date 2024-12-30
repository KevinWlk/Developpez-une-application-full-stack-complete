package com.openclassrooms.mddapi.configuration;


import com.openclassrooms.mddapi.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component // Indique à Spring qu'il doit gérer cette classe comme un composant
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    // Méthode principale du filtre qui est exécutée à chaque requête entrante
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Récupère le jeton JWT de l'en-tête de la requête
        final String authHeader = request.getHeader("Authorization");

        // Vérifie si le jeton existe et commence par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Si ce n'est pas le cas, la requête continue son chemin sans modification
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extrait le token sans le préfixe "Bearer "
            final String jwt = authHeader.substring(7);
            // Extrait l'email de l'utilisateur à partir du token JWT
            final String userEmail = jwtService.extractUsername(jwt);

            // Vérifie si l'utilisateur n'est pas déjà authentifié
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                // Charge les détails de l'utilisateur à partir de la base de données
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Vérifie si le token JWT est valide
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Crée un token d'authentification pour l'utilisateur
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // Ajoute des détails supplémentaires à l'authentification
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Définit l'utilisateur comme étant authentifié
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Continue le traitement de la requête
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}