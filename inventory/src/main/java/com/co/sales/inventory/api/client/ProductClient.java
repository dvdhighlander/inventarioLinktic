package com.co.sales.inventory.api.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.co.sales.inventory.api.constants.InventoryConstants;

import reactor.core.publisher.Mono;

@Component
public class ProductClient {

	private final WebClient webClient;

	public ProductClient(WebClient.Builder builder) {
		this.webClient = builder.baseUrl(InventoryConstants.BASE_URL_PRODUCTS).build();
	}
	
	public Mono<Products> findProductbyId(int id){
		return webClient.get().uri("/Products/{id}",id).header(InventoryConstants.AUTH_TOKEN_HEADER_NAME, InventoryConstants.AUTH_TOKEN).retrieve().
			    onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
			            clientResponse -> {
			            	System.out.println("Producto con código: " + id + " no existe");
			              return Mono.error(new Exception("Producto con código: " + id + " no existe"));
			            })
				.bodyToMono(Products.class);
	}
}
