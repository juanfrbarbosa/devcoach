/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t2m.devcoach.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Juan Felipe dos Reis Barbosa, Rafael Yamagawa Ukiharu, Tiago
 * Magalhães
 */
@Entity
@Table(name = "PROGRAMA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Programa.findAll", query = "SELECT p FROM Programa p")
    , @NamedQuery(name = "Programa.findByNomePrograma", query = "SELECT p FROM Programa p WHERE p.nomePrograma = :nomePrograma")
    , @NamedQuery(name = "Programa.findById", query = "SELECT p FROM Programa p WHERE p.id = :id")})
public class Programa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 30)
    @Column(name = "NOME_PROGRAMA")
    private String nomePrograma;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @JoinTable(name = "PROGRAMA_SESSAO", joinColumns = {
        @JoinColumn(name = "ID_PROGRAMA", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_SESSAO", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<Sessao> sessaoCollection;
    @JoinColumn(name = "DOCUMENTO_PESSOA", referencedColumnName = "DOCUMENTO")
    @ManyToOne
    private Pessoa documentoPessoa;

    public Programa() {
    }

    public Programa(Integer id) {
        this.id = id;
    }

    public String getNomePrograma() {
        return nomePrograma;
    }

    public void setNomePrograma(String nomePrograma) {
        this.nomePrograma = nomePrograma;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public Collection<Sessao> getSessaoCollection() {
        return sessaoCollection;
    }

    public void setSessaoCollection(Collection<Sessao> sessaoCollection) {
        this.sessaoCollection = sessaoCollection;
    }

    public Pessoa getDocumentoPessoa() {
        return documentoPessoa;
    }

    public void setDocumentoPessoa(Pessoa documentoPessoa) {
        this.documentoPessoa = documentoPessoa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Programa)) {
            return false;
        }
        Programa other = (Programa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.t2m.devcoach.t2m.postgreJPA.Programa[ id=" + id + " ]";
    }

}
