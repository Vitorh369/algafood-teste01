package com.algaworks.algafood.api.v1.openapi.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Api("Links")
@Setter
@Getter
public class LinksModelOpenApi {

	private LinkModel rel;
	
	
	@Getter
	@Setter
	@ApiModel("link")
	private class LinkModel{
		
		private String href;
		private boolean templated;
	}
}
