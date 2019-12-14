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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "tarjeta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarjeta.findAll", query = "SELECT t FROM Tarjeta t"),
    @NamedQuery(name = "Tarjeta.findByRfid", query = "SELECT t FROM Tarjeta t WHERE t.rfid = :rfid"),
    @NamedQuery(name = "Tarjeta.findByEstado", query = "SELECT t FROM Tarjeta t WHERE t.estado = :estado"),
    @NamedQuery(name = "Tarjeta.findByFechainicio", query = "SELECT t FROM Tarjeta t WHERE t.fechainicio = :fechainicio"),
    @NamedQuery(name = "Tarjeta.findByFechafin", query = "SELECT t FROM Tarjeta t WHERE t.fechafin = :fechafin")})
public class Tarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "rfid")
    private String rfid;
    @Column(name = "estado")
    private Integer estado;
    @Column(name = "fechainicio")
    @Temporal(TemporalType.DATE)
    private Date fechainicio;
    @Column(name = "fechafin")
    @Temporal(TemporalType.DATE)
    private Date fechafin;
    @OneToMany(mappedBy = "tarjeta")
    private List<Pagomensual> pagomensualList;
    @OneToMany(mappedBy = "tarjeta")
    private List<Ingresotarjeta> ingresotarjetaList;
    @JoinColumn(name = "cliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente cliente;
    @JoinColumn(name = "tipovehiculo", referencedColumnName = "id")
    @ManyToOne
    private Tipovehiculo tipovehiculo;
    @JoinColumn(name = "usuarioactivo", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuarioactivo;

    public Tarjeta() {
    }

    public Tarjeta(String rfid) {
        this.rfid = rfid;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
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

    @XmlTransient
    public List<Pagomensual> getPagomensualList() {
        return pagomensualList;
    }

    public void setPagomensualList(List<Pagomensual> pagomensualList) {
        this.pagomensualList = pagomensualList;
    }

    @XmlTransient
    public List<Ingresotarjeta> getIngresotarjetaList() {
        return ingresotarjetaList;
    }

    public void setIngresotarjetaList(List<Ingresotarjeta> ingresotarjetaList) {
        this.ingresotarjetaList = ingresotarjetaList;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Tipovehiculo getTipovehiculo() {
        return tipovehiculo;
    }

    public void setTipovehiculo(Tipovehiculo tipovehiculo) {
        this.tipovehiculo = tipovehiculo;
    }

    public Usuario getUsuarioactivo() {
        return usuarioactivo;
    }

    public void setUsuarioactivo(Usuario usuarioactivo) {
        this.usuarioactivo = usuarioactivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rfid != null ? rfid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarjeta)) {
            return false;
        }
        Tarjeta other = (Tarjeta) object;
        if ((this.rfid == null && other.rfid != null) || (this.rfid != null && !this.rfid.equals(other.rfid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dao.Tarjeta[ rfid=" + rfid + " ]";
    }
    
}
