package com.yhh.weblogjwt.filter;

import com.yhh.weblogjwt.utils.JwtTokenHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] PROTECTED_PREFIXES = {
            "/admin", "/blog", "/article", "/category", "/tag", "/archive", "/statistics"
    };

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.tokenHeaderKey}")
    private String tokenHeaderKey;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (!shouldAuthenticate(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(tokenHeaderKey);
        if (StringUtils.startsWith(header, tokenPrefix)) {
            String token = StringUtils.substring(header, tokenPrefix.length()).trim();
            if (StringUtils.isNotBlank(token)) {
                try {
                    jwtTokenHelper.validateToken(token);
                } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
                    authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token 不可用"));
                    return;
                } catch (ExpiredJwtException e) {
                    authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token 已失效"));
                    return;
                }

                String username = jwtTokenHelper.getUsernameByToken(token);
                if (StringUtils.isNotBlank(username)
                        && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean shouldAuthenticate(String requestURI) {
        for (String prefix : PROTECTED_PREFIXES) {
            if (requestURI.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
