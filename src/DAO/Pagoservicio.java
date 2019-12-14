/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(name = "pagoservicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pagoservicio.findAll", query = "SELECT p FROM Pagoservicio p"),
    @NamedQuery(name = "Pagoservicio.findById", query = "SELECT p FROM Pagoservicio p WHERE p.id = :id"),
    @NamedQuery(name = "Pagoservicio.findByIngreso", query = "SELECT p FROM Pagoservicio p WHERE p.ingreso = :ingreso")})
public class Pagoservicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ingreso")
    private Integer ingreso;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Ingresonormal ingresonormal;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pago pago;

    public Pagoservicio() {
    }

    public Pagoservicio(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIngreso() {
        return ingreso;
    }

    public void setIngreso(Integer ingreso) {
        this.ingreso = ingreso;
    }

    public Ingresonormal getIngresonormal() {
        return ingresonormal;
    }

    public void setIngresonormal(Ingresonormal ingresonormal) {
        this.ingresonormal = ingresonormal;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
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
        if (!(object instanceof Pagoservicio)) {
            return false;
        }
        Pagoservicio other = (Pagoservicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dao.Pagoservicio[ id=" + id + " ]";
    }
    
}
