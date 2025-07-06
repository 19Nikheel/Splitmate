package com.example.Splitmate.Security;

import com.example.Splitmate.Config.CustomUserDetailService;
import com.example.Splitmate.Config.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private Logger logger= LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtHelper jwthelper;

    @Autowired
    private CustomUserDetailService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getServletPath();

        // Skip JWT validation for /signup
        if (requestPath.equals("/signup") || requestPath.equals("/login")||
                requestPath.equals("/signup-guest")|| requestPath.equals("/guest-login")||
                requestPath.equals("/login-user")|| requestPath.equals("/invite-request/**")|| requestPath.equals("/push-request")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header=request.getHeader("Authorization");


        String username=null;
        String token=null;
        if(header!=null && header.startsWith("Bearer")){

            token=header.substring(7);
            try {
                username = this.jwthelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {

                logger.info("Illegal Argument while fetching the username !!");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid token argument.");
            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired.");
            } catch (MalformedJwtException e) {
                logger.info("Some changed has done in token !! Invalid Token");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed token.");
            } catch (Exception e) {

                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token processing failed.");
            }


        }else{
            logger.info("Authorization header is missing or invalid");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or invalid.");

        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            CustomUserDetails user=this.userDetailsService.loadUserByUsername(username);

            boolean validatetoken=this.jwthelper.validateToken(token,user);

            if(validatetoken){
                UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println(SecurityContextHolder.getContext().getAuthentication().getDetails());
            }else{
                logger.info("Token validation failed.");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid  token.");

            }
        }
        filterChain.doFilter(request,response);
    }
}