package com.t2m.devcoach.controller;

import com.t2m.devcoach.model.Pessoa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class T2mApplication {

    public static void main(String[] args) {
        SpringApplication.run(T2mApplication.class, args);
        Pessoa pessoa = new Pessoa();
        pessoa.setDocumento("111.111");
        pessoa.setNome("Teste 1 nome");
        pessoa.setDatanasc(null);
        pessoa.setEmail("teste1@gmail.com");
        
        System.out.println("Inserido no banco com sucesso");
    }
}
