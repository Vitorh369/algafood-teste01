package com.algaworks.algafood.core.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

//aula 19.17
public class PageWrapper<T> extends PageImpl<T> {

	private static final long serialVersionUID = 1L;

	private Pageable pageable;
	
	public PageWrapper(Page<T> page, Pageable pageable) {
		super(page.getContent(), pageable, page.getTotalElements());
	}
	
	@Override
	public Pageable getPageable() {
		return this.pageable;
	}

}