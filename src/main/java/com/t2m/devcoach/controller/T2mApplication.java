package com.t2m.devcoach.controller;

import com.t2m.devcoach.model.Pessoa;
import com.t2m.devcoach.controller.PessoaJpaController;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

@SpringBootApplication
public class T2mApplication {

    public static void main(String[] args) throws Exception {
        
                Pessoa pessoa = new Pessoa();
        pessoa.setDocumento("1112111");
        pessoa.setNome("Teste 1 nome");
        pessoa.setDatanasc(null); //(Date.valueOf("1990-11-11"));
        pessoa.setEmail("teste1@gmail.com");

        EntityManagerFactory objFactory = Persistence.createEntityManagerFactory("com.t2m.devcoach_t2m_jar_0.0.1-SNAPSHOTPU");
        EntityManager manager = objFactory.createEntityManager();
        PessoaJpaController jpa = new PessoaJpaController(objFactory);
        List<Pessoa> lista = jpa.findPessoaEntities();

        jpa.create(pessoa);
        
        manager.getTransaction().begin();
        manager.persist(pessoa);
        manager.getTransaction().commit();
        manager.close();
        objFactory.close();

        for (Pessoa p : lista) {
            System.out.println(" Documento: " + p.getDocumento() + "Nome"
                    + p.getNome() + " Data: " + p.getDatanasc()
                    + " email: " + p.getEmail());
        }
    }
}
