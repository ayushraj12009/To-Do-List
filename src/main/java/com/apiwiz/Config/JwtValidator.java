package com.apiwiz.Config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Extract JWT token from the request header
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        // Check if JWT token is present
        if(jwt != null){

            // Extract email from JWT token
            try {
                System.out.println("mee  is");
                String email = JwtProvider.getEmailFromJwtToken(jwt);
                System.out.println("mee  toooooo");
                List<GrantedAuthority>authorities = new ArrayList<>();

                System.out.println("this is");

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (Exception e){
                // Throw BadCredentialsException if token is invalid or wrong
                throw new BadCredentialsException("Invalid Token....");
            }
        }


        filterChain.doFilter(request,response);

    }
}
