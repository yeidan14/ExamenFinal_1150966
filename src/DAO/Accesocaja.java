/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
 * @author Daniel
 */
@Entity
@Table(name = "accesocaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Accesocaja.findAll", query = "SELECT a FROM Accesocaja a"),
    @NamedQuery(name = "Accesocaja.findByCaja", query = "SELECT a FROM Accesocaja a WHERE a.accesocajaPK.caja = :caja"),
    @NamedQuery(name = "Accesocaja.findByUsuario", query = "SELECT a FROM Accesocaja a WHERE a.accesocajaPK.usuario = :usuario"),
    @NamedQuery(name = "Accesocaja.findByFechainicio", query = "SELECT a FROM Accesocaja a WHERE a.accesocajaPK.fechainicio = :fechainicio"),
    @NamedQuery(name = "Accesocaja.findByFechacierre", query = "SELECT a FROM Accesocaja a WHERE a.fechacierre = :fechacierre"),
    @NamedQuery(name = "Accesocaja.findByTotalrecibido", query = "SELECT a FROM Accesocaja a WHERE a.totalrecibido = :totalrecibido"),
    @NamedQuery(name = "Accesocaja.findByTotalcierre", query = "SELECT a FROM Accesocaja a WHERE a.totalcierre = :totalcierre")})
public class Accesocaja implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AccesocajaPK accesocajaPK;
    @Column(name = "fechacierre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacierre;
    @Column(name = "totalrecibido")
    private Integer totalrecibido;
    @Column(name = "totalcierre")
    private Integer totalcierre;
    @JoinColumn(name = "caja", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Caja caja1;
    @JoinColumn(name = "usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario1;

    public Accesocaja() {
    }

    public Accesocaja(AccesocajaPK accesocajaPK) {
        this.accesocajaPK = accesocajaPK;
    }

    public Accesocaja(int caja, int usuario, Date fechainicio) {
        this.accesocajaPK = new AccesocajaPK(caja, usuario, fechainicio);
    }

    public AccesocajaPK getAccesocajaPK() {
        return accesocajaPK;
    }

    public void setAccesocajaPK(AccesocajaPK accesocajaPK) {
        this.accesocajaPK = accesocajaPK;
    }

    public Date getFechacierre() {
        return fechacierre;
    }

    public void setFechacierre(Date fechacierre) {
        this.fechacierre = fechacierre;
    }

    public Integer getTotalrecibido() {
        return totalrecibido;
    }

    public void setTotalrecibido(Integer totalrecibido) {
        this.totalrecibido = totalrecibido;
    }

    public Integer getTotalcierre() {
        return totalcierre;
    }

    public void setTotalcierre(Integer totalcierre) {
        this.totalcierre = totalcierre;
    }

    public Caja getCaja1() {
        return caja1;
    }

    public void setCaja1(Caja caja1) {
        this.caja1 = caja1;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accesocajaPK != null ? accesocajaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Accesocaja)) {
            return false;
        }
        Accesocaja other = (Accesocaja) object;
        if ((this.accesocajaPK == null && other.accesocajaPK != null) || (this.accesocajaPK != null && !this.accesocajaPK.equals(other.accesocajaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dao.Accesocaja[ accesocajaPK=" + accesocajaPK + " ]";
    }
    
}
