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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yeison
 */
@Entity
@Table(name = "pagomensual")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pagomensual.findAll", query = "SELECT p FROM Pagomensual p"),
    @NamedQuery(name = "Pagomensual.findById", query = "SELECT p FROM Pagomensual p WHERE p.id = :id"),
    @NamedQuery(name = "Pagomensual.findByValor", query = "SELECT p FROM Pagomensual p WHERE p.valor = :valor"),
    @NamedQuery(name = "Pagomensual.findByFecha", query = "SELECT p FROM Pagomensual p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Pagomensual.findByFecharegistro", query = "SELECT p FROM Pagomensual p WHERE p.fecharegistro = :fecharegistro")})
public class Pagomensual implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "valor")
    private Long valor;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "fecharegistro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecharegistro;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Pago pago;
    @JoinColumn(name = "tarjeta", referencedColumnName = "rfid")
    @ManyToOne
    private Tarjeta tarjeta;

    public Pagomensual() {
    }

    public Pagomensual(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(Date fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
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
        if (!(object instanceof Pagomensual)) {
            return false;
        }
        Pagomensual other = (Pagomensual) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dao.Pagomensual[ id=" + id + " ]";
    }
    
}
