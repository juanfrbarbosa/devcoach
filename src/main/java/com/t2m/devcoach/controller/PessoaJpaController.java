/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t2m.devcoach.controller;

import com.t2m.devcoach.exceptions.IllegalOrphanException;
import com.t2m.devcoach.exceptions.NonexistentEntityException;
import com.t2m.devcoach.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.t2m.devcoach.model.Telefone;
import java.util.ArrayList;
import java.util.Collection;
import com.t2m.devcoach.model.Programa;
import com.t2m.devcoach.model.Endereco;
import com.t2m.devcoach.model.Pessoa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Juan Felipe dos Reis Barbosa, Rafael Yamagawa Ukiharu, Tiago
 * Magalhães
 */
public class PessoaJpaController implements Serializable {

    public PessoaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoa pessoa) throws PreexistingEntityException, Exception {
        if (pessoa.getTelefoneCollection() == null) {
            pessoa.setTelefoneCollection(new ArrayList<Telefone>());
        }
        if (pessoa.getProgramaCollection() == null) {
            pessoa.setProgramaCollection(new ArrayList<Programa>());
        }
        if (pessoa.getEnderecoCollection() == null) {
            pessoa.setEnderecoCollection(new ArrayList<Endereco>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Telefone> attachedTelefoneCollection = new ArrayList<Telefone>();
            for (Telefone telefoneCollectionTelefoneToAttach : pessoa.getTelefoneCollection()) {
                telefoneCollectionTelefoneToAttach = em.getReference(telefoneCollectionTelefoneToAttach.getClass(), telefoneCollectionTelefoneToAttach.getId());
                attachedTelefoneCollection.add(telefoneCollectionTelefoneToAttach);
            }
            pessoa.setTelefoneCollection(attachedTelefoneCollection);
            Collection<Programa> attachedProgramaCollection = new ArrayList<Programa>();
            for (Programa programaCollectionProgramaToAttach : pessoa.getProgramaCollection()) {
                programaCollectionProgramaToAttach = em.getReference(programaCollectionProgramaToAttach.getClass(), programaCollectionProgramaToAttach.getId());
                attachedProgramaCollection.add(programaCollectionProgramaToAttach);
            }
            pessoa.setProgramaCollection(attachedProgramaCollection);
            Collection<Endereco> attachedEnderecoCollection = new ArrayList<Endereco>();
            for (Endereco enderecoCollectionEnderecoToAttach : pessoa.getEnderecoCollection()) {
                enderecoCollectionEnderecoToAttach = em.getReference(enderecoCollectionEnderecoToAttach.getClass(), enderecoCollectionEnderecoToAttach.getIdEndereco());
                attachedEnderecoCollection.add(enderecoCollectionEnderecoToAttach);
            }
            pessoa.setEnderecoCollection(attachedEnderecoCollection);
            em.persist(pessoa);
            for (Telefone telefoneCollectionTelefone : pessoa.getTelefoneCollection()) {
                telefoneCollectionTelefone.getPessoaCollection().add(pessoa);
                telefoneCollectionTelefone = em.merge(telefoneCollectionTelefone);
            }
            for (Programa programaCollectionPrograma : pessoa.getProgramaCollection()) {
                Pessoa oldDocumentoPessoaOfProgramaCollectionPrograma = programaCollectionPrograma.getDocumentoPessoa();
                programaCollectionPrograma.setDocumentoPessoa(pessoa);
                programaCollectionPrograma = em.merge(programaCollectionPrograma);
                if (oldDocumentoPessoaOfProgramaCollectionPrograma != null) {
                    oldDocumentoPessoaOfProgramaCollectionPrograma.getProgramaCollection().remove(programaCollectionPrograma);
                    oldDocumentoPessoaOfProgramaCollectionPrograma = em.merge(oldDocumentoPessoaOfProgramaCollectionPrograma);
                }
            }
            for (Endereco enderecoCollectionEndereco : pessoa.getEnderecoCollection()) {
                Pessoa oldDocumentoPessoaOfEnderecoCollectionEndereco = enderecoCollectionEndereco.getDocumentoPessoa();
                enderecoCollectionEndereco.setDocumentoPessoa(pessoa);
                enderecoCollectionEndereco = em.merge(enderecoCollectionEndereco);
                if (oldDocumentoPessoaOfEnderecoCollectionEndereco != null) {
                    oldDocumentoPessoaOfEnderecoCollectionEndereco.getEnderecoCollection().remove(enderecoCollectionEndereco);
                    oldDocumentoPessoaOfEnderecoCollectionEndereco = em.merge(oldDocumentoPessoaOfEnderecoCollectionEndereco);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoa(pessoa.getDocumento()) != null) {
                throw new PreexistingEntityException("Pessoa " + pessoa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoa pessoa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa persistentPessoa = em.find(Pessoa.class, pessoa.getDocumento());
            Collection<Telefone> telefoneCollectionOld = persistentPessoa.getTelefoneCollection();
            Collection<Telefone> telefoneCollectionNew = pessoa.getTelefoneCollection();
            Collection<Programa> programaCollectionOld = persistentPessoa.getProgramaCollection();
            Collection<Programa> programaCollectionNew = pessoa.getProgramaCollection();
            Collection<Endereco> enderecoCollectionOld = persistentPessoa.getEnderecoCollection();
            Collection<Endereco> enderecoCollectionNew = pessoa.getEnderecoCollection();
            List<String> illegalOrphanMessages = null;
            for (Endereco enderecoCollectionOldEndereco : enderecoCollectionOld) {
                if (!enderecoCollectionNew.contains(enderecoCollectionOldEndereco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Endereco " + enderecoCollectionOldEndereco + " since its documentoPessoa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Telefone> attachedTelefoneCollectionNew = new ArrayList<Telefone>();
            for (Telefone telefoneCollectionNewTelefoneToAttach : telefoneCollectionNew) {
                telefoneCollectionNewTelefoneToAttach = em.getReference(telefoneCollectionNewTelefoneToAttach.getClass(), telefoneCollectionNewTelefoneToAttach.getId());
                attachedTelefoneCollectionNew.add(telefoneCollectionNewTelefoneToAttach);
            }
            telefoneCollectionNew = attachedTelefoneCollectionNew;
            pessoa.setTelefoneCollection(telefoneCollectionNew);
            Collection<Programa> attachedProgramaCollectionNew = new ArrayList<Programa>();
            for (Programa programaCollectionNewProgramaToAttach : programaCollectionNew) {
                programaCollectionNewProgramaToAttach = em.getReference(programaCollectionNewProgramaToAttach.getClass(), programaCollectionNewProgramaToAttach.getId());
                attachedProgramaCollectionNew.add(programaCollectionNewProgramaToAttach);
            }
            programaCollectionNew = attachedProgramaCollectionNew;
            pessoa.setProgramaCollection(programaCollectionNew);
            Collection<Endereco> attachedEnderecoCollectionNew = new ArrayList<Endereco>();
            for (Endereco enderecoCollectionNewEnderecoToAttach : enderecoCollectionNew) {
                enderecoCollectionNewEnderecoToAttach = em.getReference(enderecoCollectionNewEnderecoToAttach.getClass(), enderecoCollectionNewEnderecoToAttach.getIdEndereco());
                attachedEnderecoCollectionNew.add(enderecoCollectionNewEnderecoToAttach);
            }
            enderecoCollectionNew = attachedEnderecoCollectionNew;
            pessoa.setEnderecoCollection(enderecoCollectionNew);
            pessoa = em.merge(pessoa);
            for (Telefone telefoneCollectionOldTelefone : telefoneCollectionOld) {
                if (!telefoneCollectionNew.contains(telefoneCollectionOldTelefone)) {
                    telefoneCollectionOldTelefone.getPessoaCollection().remove(pessoa);
                    telefoneCollectionOldTelefone = em.merge(telefoneCollectionOldTelefone);
                }
            }
            for (Telefone telefoneCollectionNewTelefone : telefoneCollectionNew) {
                if (!telefoneCollectionOld.contains(telefoneCollectionNewTelefone)) {
                    telefoneCollectionNewTelefone.getPessoaCollection().add(pessoa);
                    telefoneCollectionNewTelefone = em.merge(telefoneCollectionNewTelefone);
                }
            }
            for (Programa programaCollectionOldPrograma : programaCollectionOld) {
                if (!programaCollectionNew.contains(programaCollectionOldPrograma)) {
                    programaCollectionOldPrograma.setDocumentoPessoa(null);
                    programaCollectionOldPrograma = em.merge(programaCollectionOldPrograma);
                }
            }
            for (Programa programaCollectionNewPrograma : programaCollectionNew) {
                if (!programaCollectionOld.contains(programaCollectionNewPrograma)) {
                    Pessoa oldDocumentoPessoaOfProgramaCollectionNewPrograma = programaCollectionNewPrograma.getDocumentoPessoa();
                    programaCollectionNewPrograma.setDocumentoPessoa(pessoa);
                    programaCollectionNewPrograma = em.merge(programaCollectionNewPrograma);
                    if (oldDocumentoPessoaOfProgramaCollectionNewPrograma != null && !oldDocumentoPessoaOfProgramaCollectionNewPrograma.equals(pessoa)) {
                        oldDocumentoPessoaOfProgramaCollectionNewPrograma.getProgramaCollection().remove(programaCollectionNewPrograma);
                        oldDocumentoPessoaOfProgramaCollectionNewPrograma = em.merge(oldDocumentoPessoaOfProgramaCollectionNewPrograma);
                    }
                }
            }
            for (Endereco enderecoCollectionNewEndereco : enderecoCollectionNew) {
                if (!enderecoCollectionOld.contains(enderecoCollectionNewEndereco)) {
                    Pessoa oldDocumentoPessoaOfEnderecoCollectionNewEndereco = enderecoCollectionNewEndereco.getDocumentoPessoa();
                    enderecoCollectionNewEndereco.setDocumentoPessoa(pessoa);
                    enderecoCollectionNewEndereco = em.merge(enderecoCollectionNewEndereco);
                    if (oldDocumentoPessoaOfEnderecoCollectionNewEndereco != null && !oldDocumentoPessoaOfEnderecoCollectionNewEndereco.equals(pessoa)) {
                        oldDocumentoPessoaOfEnderecoCollectionNewEndereco.getEnderecoCollection().remove(enderecoCollectionNewEndereco);
                        oldDocumentoPessoaOfEnderecoCollectionNewEndereco = em.merge(oldDocumentoPessoaOfEnderecoCollectionNewEndereco);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pessoa.getDocumento();
                if (findPessoa(id) == null) {
                    throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa pessoa;
            try {
                pessoa = em.getReference(Pessoa.class, id);
                pessoa.getDocumento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Endereco> enderecoCollectionOrphanCheck = pessoa.getEnderecoCollection();
            for (Endereco enderecoCollectionOrphanCheckEndereco : enderecoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the Endereco " + enderecoCollectionOrphanCheckEndereco + " in its enderecoCollection field has a non-nullable documentoPessoa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Telefone> telefoneCollection = pessoa.getTelefoneCollection();
            for (Telefone telefoneCollectionTelefone : telefoneCollection) {
                telefoneCollectionTelefone.getPessoaCollection().remove(pessoa);
                telefoneCollectionTelefone = em.merge(telefoneCollectionTelefone);
            }
            Collection<Programa> programaCollection = pessoa.getProgramaCollection();
            for (Programa programaCollectionPrograma : programaCollection) {
                programaCollectionPrograma.setDocumentoPessoa(null);
                programaCollectionPrograma = em.merge(programaCollectionPrograma);
            }
            em.remove(pessoa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoa> findPessoaEntities() {
        return findPessoaEntities(true, -1, -1);
    }

    public List<Pessoa> findPessoaEntities(int maxResults, int firstResult) {
        return findPessoaEntities(false, maxResults, firstResult);
    }

    private List<Pessoa> findPessoaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoa.class));
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

    public Pessoa findPessoa(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoa.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoa> rt = cq.from(Pessoa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
