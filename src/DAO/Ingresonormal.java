/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yeison
 */
@Entity
@Table(name = "ingresonormal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ingresonormal.findAll", query = "SELECT i FROM Ingresonormal i"),
    @NamedQuery(name = "Ingresonormal.findById", query = "SELECT i FROM Ingresonormal i WHERE i.id = :id")})
public class Ingresonormal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ingresonormal")
    private Pagoservicio pagoservicio;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Ingreso ingreso;

    public Ingresonormal() {
    }

    public Ingresonormal(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pagoservicio getPagoservicio() {
        return pagoservicio;
    }

    public void setPagoservicio(Pagoservicio pagoservicio) {
        this.pagoservicio = pagoservicio;
    }

    public Ingreso getIngreso() {
        return ingreso;
    }

    public void setIngreso(Ingreso ingreso) {
        this.ingreso = ingreso;
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
        if (!(object instanceof Ingresonormal)) {
            return false;
        }
        Ingresonormal other = (Ingresonormal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dao.Ingresonormal[ id=" + id + " ]";
    }
    
}
