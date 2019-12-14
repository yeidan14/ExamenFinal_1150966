/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Yeison
 */
@Entity
@Table(name = "tipovehiculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipovehiculo.findAll", query = "SELECT t FROM Tipovehiculo t"),
    @NamedQuery(name = "Tipovehiculo.findById", query = "SELECT t FROM Tipovehiculo t WHERE t.id = :id"),
    @NamedQuery(name = "Tipovehiculo.findByDescripcion", query = "SELECT t FROM Tipovehiculo t WHERE t.descripcion = :descripcion")})
public class Tipovehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "tipovehiculo")
    private List<Tarifa> tarifaList;
    @OneToMany(mappedBy = "tipo")
    private List<Ingreso> ingresoList;
    @OneToMany(mappedBy = "tipovehiculo")
    private List<Tarjeta> tarjetaList;

    public Tipovehiculo() {
    }

    public Tipovehiculo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Tarifa> getTarifaList() {
        return tarifaList;
    }

    public void setTarifaList(List<Tarifa> tarifaList) {
        this.tarifaList = tarifaList;
    }

    @XmlTransient
    public List<Ingreso> getIngresoList() {
        return ingresoList;
    }

    public void setIngresoList(List<Ingreso> ingresoList) {
        this.ingresoList = ingresoList;
    }

    @XmlTransient
    public List<Tarjeta> getTarjetaList() {
        return tarjetaList;
    }

    public void setTarjetaList(List<Tarjeta> tarjetaList) {
        this.tarjetaList = tarjetaList;
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
        if (!(object instanceof Tipovehiculo)) {
            return false;
        }
        Tipovehiculo other = (Tipovehiculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dao.Tipovehiculo[ id=" + id + " ]";
    }
    
}
