package com.springboot.jpa.springbootjpa.models.dao;

import java.util.List;

import com.springboot.jpa.springbootjpa.models.entity.Cliente;

public interface IClienteDao {
    public List<Cliente> findAll();
}
