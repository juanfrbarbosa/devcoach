/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t2m.devcoach.controller;

import com.t2m.devcoach.exceptions.NonexistentEntityException;
import com.t2m.devcoach.exceptions.PreexistingEntityException;
import com.t2m.devcoach.model.Endereco;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.t2m.devcoach.model.Pessoa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Juan Felipe dos Reis Barbosa, Rafael Yamagawa Ukiharu, Tiago
 * Magalhães
 */
public class EnderecoJpaController implements Serializable {

    public EnderecoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Endereco endereco) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa documentoPessoa = endereco.getDocumentoPessoa();
            if (documentoPessoa != null) {
                documentoPessoa = em.getReference(documentoPessoa.getClass(), documentoPessoa.getDocumento());
                endereco.setDocumentoPessoa(documentoPessoa);
            }
            em.persist(endereco);
            if (documentoPessoa != null) {
                documentoPessoa.getEnderecoCollection().add(endereco);
                documentoPessoa = em.merge(documentoPessoa);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEndereco(endereco.getIdEndereco()) != null) {
                throw new PreexistingEntityException("Endereco " + endereco + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Endereco endereco) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco persistentEndereco = em.find(Endereco.class, endereco.getIdEndereco());
            Pessoa documentoPessoaOld = persistentEndereco.getDocumentoPessoa();
            Pessoa documentoPessoaNew = endereco.getDocumentoPessoa();
            if (documentoPessoaNew != null) {
                documentoPessoaNew = em.getReference(documentoPessoaNew.getClass(), documentoPessoaNew.getDocumento());
                endereco.setDocumentoPessoa(documentoPessoaNew);
            }
            endereco = em.merge(endereco);
            if (documentoPessoaOld != null && !documentoPessoaOld.equals(documentoPessoaNew)) {
                documentoPessoaOld.getEnderecoCollection().remove(endereco);
                documentoPessoaOld = em.merge(documentoPessoaOld);
            }
            if (documentoPessoaNew != null && !documentoPessoaNew.equals(documentoPessoaOld)) {
                documentoPessoaNew.getEnderecoCollection().add(endereco);
                documentoPessoaNew = em.merge(documentoPessoaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = endereco.getIdEndereco();
                if (findEndereco(id) == null) {
                    throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco endereco;
            try {
                endereco = em.getReference(Endereco.class, id);
                endereco.getIdEndereco();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.", enfe);
            }
            Pessoa documentoPessoa = endereco.getDocumentoPessoa();
            if (documentoPessoa != null) {
                documentoPessoa.getEnderecoCollection().remove(endereco);
                documentoPessoa = em.merge(documentoPessoa);
            }
            em.remove(endereco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Endereco> findEnderecoEntities() {
        return findEnderecoEntities(true, -1, -1);
    }

    public List<Endereco> findEnderecoEntities(int maxResults, int firstResult) {
        return findEnderecoEntities(false, maxResults, firstResult);
    }

    private List<Endereco> findEnderecoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Endereco.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Endereco findEndereco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Endereco.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnderecoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Endereco> rt = cq.from(Endereco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
