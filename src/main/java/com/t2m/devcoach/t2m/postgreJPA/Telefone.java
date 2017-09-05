/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t2m.devcoach.t2m.postgreJPA;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "TELEFONE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Telefone.findAll", query = "SELECT t FROM Telefone t")
    , @NamedQuery(name = "Telefone.findByCodigoDoPais", query = "SELECT t FROM Telefone t WHERE t.codigoDoPais = :codigoDoPais")
    , @NamedQuery(name = "Telefone.findByCodigoDaArea", query = "SELECT t FROM Telefone t WHERE t.codigoDaArea = :codigoDaArea")
    , @NamedQuery(name = "Telefone.findByNumero", query = "SELECT t FROM Telefone t WHERE t.numero = :numero")
    , @NamedQuery(name = "Telefone.findByTipo", query = "SELECT t FROM Telefone t WHERE t.tipo = :tipo")
    , @NamedQuery(name = "Telefone.findById", query = "SELECT t FROM Telefone t WHERE t.id = :id")})
public class Telefone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 3)
    @Column(name = "CODIGO_DO_PAIS")
    private String codigoDoPais;
    @Size(max = 3)
    @Column(name = "CODIGO_DA\u001f_AREA")
    private String codigoDaArea;
    @Size(max = 10)
    @Column(name = "NUMERO")
    private String numero;
    @Size(max = 20)
    @Column(name = "TIPO")
    private String tipo;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @ManyToMany(mappedBy = "telefoneCollection")
    private Collection<Pessoa> pessoaCollection;

    public Telefone() {
    }

    public Telefone(Integer id) {
        this.id = id;
    }

    public String getCodigoDoPais() {
        return codigoDoPais;
    }

    public void setCodigoDoPais(String codigoDoPais) {
        this.codigoDoPais = codigoDoPais;
    }

    public String getCodigoDaArea() {
        return codigoDaArea;
    }

    public void setCodigoDaArea(String codigoDaArea) {
        this.codigoDaArea = codigoDaArea;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public Collection<Pessoa> getPessoaCollection() {
        return pessoaCollection;
    }

    public void setPessoaCollection(Collection<Pessoa> pessoaCollection) {
        this.pessoaCollection = pessoaCollection;
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
        if (!(object instanceof Telefone)) {
            return false;
        }
        Telefone other = (Telefone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.t2m.devcoach.t2m.postgreJPA.Telefone[ id=" + id + " ]";
    }
    
}
