/*package com.library.library_backend.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.Key;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt=request.getHeader(JwtConstant.JWT_HEADER);

        if (jwt!=null){
            jwt=jwt.substring(7);
            try{
                SecretKey key= Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                Claims claims= Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
                String email=String.valueOf(claims.get("email"));
                String authories=String.valueOf(claims.get("authorities"));
                List<GrantedAuthority> authorityList= AuthorityUtils.commaSeparatedStringToAuthorityList(authories);
                Authentication auth=new UsernamePasswordAuthenticationToken(email,null,authorityList);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                throw new BadCredentialsException("Indalid JWT Token! "+e);
            }
            filterChain.doFilter(request,response);
        }
    }
}
 */

package com.library.library_backend.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // PUBLIC ENDPOINTS - Skip JWT validation
        if (isPublicEndpoint(requestURI)) {
            System.out.println("Skipping JWT validation for: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // PROTECTED ENDPOINTS - Validate JWT
        System.out.println("Validating JWT for: " + requestURI);
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if (jwt == null || !jwt.startsWith("Bearer ")) {
            System.out.println("JWT token missing or invalid format");
            throw new BadCredentialsException("Missing or invalid JWT token");
        }

        jwt = jwt.substring(7); // Remove "Bearer " prefix
        try {
            SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

            String email = String.valueOf(claims.get("email"));
            String authorities = String.valueOf(claims.get("authorities"));
            List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            Authentication auth = new UsernamePasswordAuthenticationToken(email, null, authorityList);
            SecurityContextHolder.getContext().setAuthentication(auth);

            System.out.println("JWT validated successfully for: " + email);

        } catch (Exception e) {
            System.out.println("JWT validation failed: " + e.getMessage());
            throw new BadCredentialsException("Invalid JWT Token!");
        }

        filterChain.doFilter(request, response);
    }

    // List of public endpoints that don't require JWT
    private boolean isPublicEndpoint(String requestURI) {
        return requestURI.startsWith("/auth") ||
                requestURI.startsWith("/api/auth") ||
                requestURI.equals("/") ||
                requestURI.contains("swagger") ||
                requestURI.contains("api-docs") ||
                requestURI.contains("actuator");
    }
}
