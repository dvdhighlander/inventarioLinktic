package com.co.sales.products.api.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.co.sales.products.api.constants.ProductsConstants;
import com.co.sales.products.api.security.AuthenticationMapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {

	public static Authentication getAuthentication(HttpServletRequest request) {
		String apiKey = request.getHeader(ProductsConstants.AUTH_TOKEN_HEADER_NAME);
		if (apiKey == null || !apiKey.equals(ProductsConstants.AUTH_TOKEN)) {
			throw new BadCredentialsException("Invalid API Key");
		}

		return new AuthenticationMapper(apiKey, AuthorityUtils.NO_AUTHORITIES);
	}
}
