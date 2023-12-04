package com.springboot.jpa.springbootjpa.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.jpa.springbootjpa.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
    /*public List<Cliente> findAll();
    public void save(Cliente cliente);
    public Cliente findOne(Long id);
    public void delete(Long id);*/
    
}
