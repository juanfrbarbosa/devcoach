/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t2m.devcoach.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Juan Felipe dos Reis Barbosa, Rafael Yamagawa Ukiharu, Tiago
 * Magalhães
 */
@Entity
@Table(name = "ENDERECO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Endereco.findAll", query = "SELECT e FROM Endereco e")
    , @NamedQuery(name = "Endereco.findByRua", query = "SELECT e FROM Endereco e WHERE e.rua = :rua")
    , @NamedQuery(name = "Endereco.findByNumero", query = "SELECT e FROM Endereco e WHERE e.numero = :numero")
    , @NamedQuery(name = "Endereco.findByBairro", query = "SELECT e FROM Endereco e WHERE e.bairro = :bairro")
    , @NamedQuery(name = "Endereco.findByCidade", query = "SELECT e FROM Endereco e WHERE e.cidade = :cidade")
    , @NamedQuery(name = "Endereco.findByEstado", query = "SELECT e FROM Endereco e WHERE e.estado = :estado")
    , @NamedQuery(name = "Endereco.findByPais", query = "SELECT e FROM Endereco e WHERE e.pais = :pais")
    , @NamedQuery(name = "Endereco.findByIdEndereco", query = "SELECT e FROM Endereco e WHERE e.idEndereco = :idEndereco")})
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 30)
    @Column(name = "RUA")
    private String rua;
    @Size(max = 5)
    @Column(name = "NUMERO")
    private String numero;
    @Size(max = 30)
    @Column(name = "BAIRRO")
    private String bairro;
    @Size(max = 20)
    @Column(name = "CIDADE")
    private String cidade;
    @Size(max = 2)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 20)
    @Column(name = "PAIS")
    private String pais;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ENDERECO")
    private Integer idEndereco;
    @JoinColumn(name = "DOCUMENTO_PESSOA", referencedColumnName = "DOCUMENTO")
    @ManyToOne(optional = false)
    private Pessoa documentoPessoa;

    public Endereco() {
    }

    public Endereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Integer getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
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
        hash += (idEndereco != null ? idEndereco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Endereco)) {
            return false;
        }
        Endereco other = (Endereco) object;
        if ((this.idEndereco == null && other.idEndereco != null) || (this.idEndereco != null && !this.idEndereco.equals(other.idEndereco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.t2m.devcoach.t2m.postgreJPA.Endereco[ idEndereco=" + idEndereco + " ]";
    }

}
