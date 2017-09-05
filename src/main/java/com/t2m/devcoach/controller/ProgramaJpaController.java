/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t2m.devcoach.controller;

import com.t2m.devcoach.exceptions.NonexistentEntityException;
import com.t2m.devcoach.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.t2m.devcoach.model.Pessoa;
import com.t2m.devcoach.model.Programa;
import com.t2m.devcoach.model.Sessao;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Juan Felipe dos Reis Barbosa, Rafael Yamagawa Ukiharu, Tiago
 * Magalhães
 */
public class ProgramaJpaController implements Serializable {

    public ProgramaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Programa programa) throws PreexistingEntityException, Exception {
        if (programa.getSessaoCollection() == null) {
            programa.setSessaoCollection(new ArrayList<Sessao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa documentoPessoa = programa.getDocumentoPessoa();
            if (documentoPessoa != null) {
                documentoPessoa = em.getReference(documentoPessoa.getClass(), documentoPessoa.getDocumento());
                programa.setDocumentoPessoa(documentoPessoa);
            }
            Collection<Sessao> attachedSessaoCollection = new ArrayList<Sessao>();
            for (Sessao sessaoCollectionSessaoToAttach : programa.getSessaoCollection()) {
                sessaoCollectionSessaoToAttach = em.getReference(sessaoCollectionSessaoToAttach.getClass(), sessaoCollectionSessaoToAttach.getId());
                attachedSessaoCollection.add(sessaoCollectionSessaoToAttach);
            }
            programa.setSessaoCollection(attachedSessaoCollection);
            em.persist(programa);
            if (documentoPessoa != null) {
                documentoPessoa.getProgramaCollection().add(programa);
                documentoPessoa = em.merge(documentoPessoa);
            }
            for (Sessao sessaoCollectionSessao : programa.getSessaoCollection()) {
                sessaoCollectionSessao.getProgramaCollection().add(programa);
                sessaoCollectionSessao = em.merge(sessaoCollectionSessao);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPrograma(programa.getId()) != null) {
                throw new PreexistingEntityException("Programa " + programa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Programa programa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Programa persistentPrograma = em.find(Programa.class, programa.getId());
            Pessoa documentoPessoaOld = persistentPrograma.getDocumentoPessoa();
            Pessoa documentoPessoaNew = programa.getDocumentoPessoa();
            Collection<Sessao> sessaoCollectionOld = persistentPrograma.getSessaoCollection();
            Collection<Sessao> sessaoCollectionNew = programa.getSessaoCollection();
            if (documentoPessoaNew != null) {
                documentoPessoaNew = em.getReference(documentoPessoaNew.getClass(), documentoPessoaNew.getDocumento());
                programa.setDocumentoPessoa(documentoPessoaNew);
            }
            Collection<Sessao> attachedSessaoCollectionNew = new ArrayList<Sessao>();
            for (Sessao sessaoCollectionNewSessaoToAttach : sessaoCollectionNew) {
                sessaoCollectionNewSessaoToAttach = em.getReference(sessaoCollectionNewSessaoToAttach.getClass(), sessaoCollectionNewSessaoToAttach.getId());
                attachedSessaoCollectionNew.add(sessaoCollectionNewSessaoToAttach);
            }
            sessaoCollectionNew = attachedSessaoCollectionNew;
            programa.setSessaoCollection(sessaoCollectionNew);
            programa = em.merge(programa);
            if (documentoPessoaOld != null && !documentoPessoaOld.equals(documentoPessoaNew)) {
                documentoPessoaOld.getProgramaCollection().remove(programa);
                documentoPessoaOld = em.merge(documentoPessoaOld);
            }
            if (documentoPessoaNew != null && !documentoPessoaNew.equals(documentoPessoaOld)) {
                documentoPessoaNew.getProgramaCollection().add(programa);
                documentoPessoaNew = em.merge(documentoPessoaNew);
            }
            for (Sessao sessaoCollectionOldSessao : sessaoCollectionOld) {
                if (!sessaoCollectionNew.contains(sessaoCollectionOldSessao)) {
                    sessaoCollectionOldSessao.getProgramaCollection().remove(programa);
                    sessaoCollectionOldSessao = em.merge(sessaoCollectionOldSessao);
                }
            }
            for (Sessao sessaoCollectionNewSessao : sessaoCollectionNew) {
                if (!sessaoCollectionOld.contains(sessaoCollectionNewSessao)) {
                    sessaoCollectionNewSessao.getProgramaCollection().add(programa);
                    sessaoCollectionNewSessao = em.merge(sessaoCollectionNewSessao);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = programa.getId();
                if (findPrograma(id) == null) {
                    throw new NonexistentEntityException("The programa with id " + id + " no longer exists.");
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
            Programa programa;
            try {
                programa = em.getReference(Programa.class, id);
                programa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The programa with id " + id + " no longer exists.", enfe);
            }
            Pessoa documentoPessoa = programa.getDocumentoPessoa();
            if (documentoPessoa != null) {
                documentoPessoa.getProgramaCollection().remove(programa);
                documentoPessoa = em.merge(documentoPessoa);
            }
            Collection<Sessao> sessaoCollection = programa.getSessaoCollection();
            for (Sessao sessaoCollectionSessao : sessaoCollection) {
                sessaoCollectionSessao.getProgramaCollection().remove(programa);
                sessaoCollectionSessao = em.merge(sessaoCollectionSessao);
            }
            em.remove(programa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Programa> findProgramaEntities() {
        return findProgramaEntities(true, -1, -1);
    }

    public List<Programa> findProgramaEntities(int maxResults, int firstResult) {
        return findProgramaEntities(false, maxResults, firstResult);
    }

    private List<Programa> findProgramaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Programa.class));
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

    public Programa findPrograma(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Programa.class, id);
        } finally {
            em.close();
        }
    }

    public int getProgramaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Programa> rt = cq.from(Programa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
