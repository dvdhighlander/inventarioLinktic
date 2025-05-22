package com.co.sales.inventory.api.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.co.sales.inventory.api.constants.InventoryConstants;
import com.co.sales.inventory.api.security.AuthenticationMapper;


import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {



    public static Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(InventoryConstants.AUTH_TOKEN_HEADER_NAME);
		String path = request.getRequestURI();
		if(path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
			return new AuthenticationMapper(InventoryConstants.AUTH_TOKEN, AuthorityUtils.NO_AUTHORITIES);
		}
        if (apiKey == null || !apiKey.equals(InventoryConstants.AUTH_TOKEN)) {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new AuthenticationMapper(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
