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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Alexander
 */
@Embeddable
public class AccesocajaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "caja")
    private int caja;
    @Basic(optional = false)
    @Column(name = "usuario")
    private int usuario;
    @Basic(optional = false)
    @Column(name = "fechainicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicio;

    public AccesocajaPK() {
    }

    public AccesocajaPK(int caja, int usuario, Date fechainicio) {
        this.caja = caja;
        this.usuario = usuario;
        this.fechainicio = fechainicio;
    }

    public int getCaja() {
        return caja;
    }

    public void setCaja(int caja) {
        this.caja = caja;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) caja;
        hash += (int) usuario;
        hash += (fechainicio != null ? fechainicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccesocajaPK)) {
            return false;
        }
        AccesocajaPK other = (AccesocajaPK) object;
        if (this.caja != other.caja) {
            return false;
        }
        if (this.usuario != other.usuario) {
            return false;
        }
        if ((this.fechainicio == null && other.fechainicio != null) || (this.fechainicio != null && !this.fechainicio.equals(other.fechainicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dao.AccesocajaPK[ caja=" + caja + ", usuario=" + usuario + ", fechainicio=" + fechainicio + " ]";
    }
    
}
