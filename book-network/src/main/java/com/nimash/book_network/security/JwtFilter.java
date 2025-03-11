package com.nimash.book_network.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    // some request are not want to authorized then passed that no jwt checks
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain ) throws ServletException, IOException
    {
        System.out.println(request);
        System.out.println(request.getRequestURI());

        if(request.getRequestURI().contains("/api/v1/auth/")) {

            filterChain.doFilter(request, response);
            return;

            //If the request matches "api/v1/auth", it bypasses the filter and continues processing the request.

        }

        // JWT validation logic (Example)
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);//HttpHeaders.AUTHORIZATION="Authorization"
        final String jwt;
        final String userEmail;

        // check request has token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: No JWT Token");
            return;
        }

        jwt=authHeader.substring(7); //first 6 characters in the authHeader=> "Bearer "
        //get the username extract from the jwt
        userEmail= jwtService.extractUsername(jwt);
                                //is used to check if the current user is not authenticated.(Prevents unnecessary re-authentication.)
        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails=userDetailsService.loadUserByUsername(userEmail);
            //Retrieves user information such as roles and credentials.

            if(jwtService.isTokenValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);

    }
}
