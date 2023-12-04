package com.springboot.jpa.springbootjpa.util;

import org.springframework.data.domain.Page;

public class PageRender<T> {
    private String url;
    private Page<T> page;

    private int totalPaginas;
    private int numElementosPorPagina;

    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;

        numElementosPorPagina = page.getSize();
        totalPaginas = page.getTotalPages();

        int desde, hasta;
        if (totalPaginas < numElementosPorPagina) {
            desde = 1;
            hasta = totalPaginas;
        } else {
            
        }

    }

}
