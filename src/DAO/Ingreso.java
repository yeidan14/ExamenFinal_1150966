/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Yeison
 */
@Entity
@Table(name = "ingreso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ingreso.findAll", query = "SELECT i FROM Ingreso i"),
    @NamedQuery(name = "Ingreso.findById", query = "SELECT i FROM Ingreso i WHERE i.id = :id"),
    @NamedQuery(name = "Ingreso.findByFechaingreso", query = "SELECT i FROM Ingreso i WHERE i.fechaingreso = :fechaingreso"),
    @NamedQuery(name = "Ingreso.findByFechasalida", query = "SELECT i FROM Ingreso i WHERE i.fechasalida = :fechasalida"),
    @NamedQuery(name = "Ingreso.findByNumero", query = "SELECT i FROM Ingreso i WHERE i.numero = :numero")})
public class Ingreso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fechaingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaingreso;
    @Column(name = "fechasalida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasalida;
    @Column(name = "numero")
    private String numero;
    @OneToMany(mappedBy = "ingreso")
    private List<Pago> pagoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ingreso")
    private Ingresonormal ingresonormal;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private Tipovehiculo tipo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ingreso")
    private Ingresotarjeta ingresotarjeta;

    public Ingreso() {
    }

    public Ingreso(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Date getFechasalida() {
        return fechasalida;
    }

    public void setFechasalida(Date fechasalida) {
        this.fechasalida = fechasalida;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @XmlTransient
    public List<Pago> getPagoList() {
        return pagoList;
    }

    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }

    public Ingresonormal getIngresonormal() {
        return ingresonormal;
    }

    public void setIngresonormal(Ingresonormal ingresonormal) {
        this.ingresonormal = ingresonormal;
    }

    public Tipovehiculo getTipo() {
        return tipo;
    }

    public void setTipo(Tipovehiculo tipo) {
        this.tipo = tipo;
    }

    public Ingresotarjeta getIngresotarjeta() {
        return ingresotarjeta;
    }

    public void setIngresotarjeta(Ingresotarjeta ingresotarjeta) {
        this.ingresotarjeta = ingresotarjeta;
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
        if (!(object instanceof Ingreso)) {
            return false;
        }
        Ingreso other = (Ingreso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dao.Ingreso[ id=" + id + " ]";
    }
    
}
