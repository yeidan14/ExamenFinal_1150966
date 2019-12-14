/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yeison
 */
@Entity
@Table(name = "tarifa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarifa.findAll", query = "SELECT t FROM Tarifa t"),
    @NamedQuery(name = "Tarifa.findById", query = "SELECT t FROM Tarifa t WHERE t.id = :id"),
    @NamedQuery(name = "Tarifa.findByDescripcion", query = "SELECT t FROM Tarifa t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Tarifa.findByFechainicio", query = "SELECT t FROM Tarifa t WHERE t.fechainicio = :fechainicio"),
    @NamedQuery(name = "Tarifa.findByFechafin", query = "SELECT t FROM Tarifa t WHERE t.fechafin = :fechafin"),
    @NamedQuery(name = "Tarifa.findByValor", query = "SELECT t FROM Tarifa t WHERE t.valor = :valor")})
public class Tarifa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fechainicio")
    @Temporal(TemporalType.DATE)
    private Date fechainicio;
    @Column(name = "fechafin")
    @Temporal(TemporalType.DATE)
    private Date fechafin;
    @Column(name = "valor")
    private Integer valor;
    @JoinColumn(name = "tipotarifa", referencedColumnName = "id")
    @ManyToOne
    private Tipotarifa tipotarifa;
    @JoinColumn(name = "tipovehiculo", referencedColumnName = "id")
    @ManyToOne
    private Tipovehiculo tipovehiculo;

    public Tarifa() {
    }

    public Tarifa(Integer id) {
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

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Tipotarifa getTipotarifa() {
        return tipotarifa;
    }

    public void setTipotarifa(Tipotarifa tipotarifa) {
        this.tipotarifa = tipotarifa;
    }

    public Tipovehiculo getTipovehiculo() {
        return tipovehiculo;
    }

    public void setTipovehiculo(Tipovehiculo tipovehiculo) {
        this.tipovehiculo = tipovehiculo;
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
        if (!(object instanceof Tarifa)) {
            return false;
        }
        Tarifa other = (Tarifa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dao.Tarifa[ id=" + id + " ]";
    }
    
}
