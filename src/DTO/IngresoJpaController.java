/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;

import javax.persistence.EntityManagerFactory;

import DTO.Exepcion.*;
import DAO.*; 
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class IngresoJpaController implements Serializable {

    public IngresoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ingreso ingreso) {
        if (ingreso.getPagoList() == null) {
            ingreso.setPagoList(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingresonormal ingresonormal = ingreso.getIngresonormal();
            if (ingresonormal != null) {
                ingresonormal = em.getReference(ingresonormal.getClass(), ingresonormal.getId());
                ingreso.setIngresonormal(ingresonormal);
            }
            Tipovehiculo tipo = ingreso.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getId());
                ingreso.setTipo(tipo);
            }
            Ingresotarjeta ingresotarjeta = ingreso.getIngresotarjeta();
            if (ingresotarjeta != null) {
                ingresotarjeta = em.getReference(ingresotarjeta.getClass(), ingresotarjeta.getId());
                ingreso.setIngresotarjeta(ingresotarjeta);
            }
            List<Pago> attachedPagoList = new ArrayList<Pago>();
            for (Pago pagoListPagoToAttach : ingreso.getPagoList()) {
                pagoListPagoToAttach = em.getReference(pagoListPagoToAttach.getClass(), pagoListPagoToAttach.getId());
                attachedPagoList.add(pagoListPagoToAttach);
            }
            ingreso.setPagoList(attachedPagoList);
            em.persist(ingreso);
            if (ingresonormal != null) {
                Ingreso oldIngresoOfIngresonormal = ingresonormal.getIngreso();
                if (oldIngresoOfIngresonormal != null) {
                    oldIngresoOfIngresonormal.setIngresonormal(null);
                    oldIngresoOfIngresonormal = em.merge(oldIngresoOfIngresonormal);
                }
                ingresonormal.setIngreso(ingreso);
                ingresonormal = em.merge(ingresonormal);
            }
            if (tipo != null) {
                tipo.getIngresoList().add(ingreso);
                tipo = em.merge(tipo);
            }
            if (ingresotarjeta != null) {
                Ingreso oldIngresoOfIngresotarjeta = ingresotarjeta.getIngreso();
                if (oldIngresoOfIngresotarjeta != null) {
                    oldIngresoOfIngresotarjeta.setIngresotarjeta(null);
                    oldIngresoOfIngresotarjeta = em.merge(oldIngresoOfIngresotarjeta);
                }
                ingresotarjeta.setIngreso(ingreso);
                ingresotarjeta = em.merge(ingresotarjeta);
            }
            for (Pago pagoListPago : ingreso.getPagoList()) {
                Ingreso oldIngresoOfPagoListPago = pagoListPago.getIngreso();
                pagoListPago.setIngreso(ingreso);
                pagoListPago = em.merge(pagoListPago);
                if (oldIngresoOfPagoListPago != null) {
                    oldIngresoOfPagoListPago.getPagoList().remove(pagoListPago);
                    oldIngresoOfPagoListPago = em.merge(oldIngresoOfPagoListPago);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ingreso ingreso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingreso persistentIngreso = em.find(Ingreso.class, ingreso.getId());
            Ingresonormal ingresonormalOld = persistentIngreso.getIngresonormal();
            Ingresonormal ingresonormalNew = ingreso.getIngresonormal();
            Tipovehiculo tipoOld = persistentIngreso.getTipo();
            Tipovehiculo tipoNew = ingreso.getTipo();
            Ingresotarjeta ingresotarjetaOld = persistentIngreso.getIngresotarjeta();
            Ingresotarjeta ingresotarjetaNew = ingreso.getIngresotarjeta();
            List<Pago> pagoListOld = persistentIngreso.getPagoList();
            List<Pago> pagoListNew = ingreso.getPagoList();
            List<String> illegalOrphanMessages = null;
            if (ingresonormalOld != null && !ingresonormalOld.equals(ingresonormalNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Ingresonormal " + ingresonormalOld + " since its ingreso field is not nullable.");
            }
            if (ingresotarjetaOld != null && !ingresotarjetaOld.equals(ingresotarjetaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Ingresotarjeta " + ingresotarjetaOld + " since its ingreso field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (ingresonormalNew != null) {
                ingresonormalNew = em.getReference(ingresonormalNew.getClass(), ingresonormalNew.getId());
                ingreso.setIngresonormal(ingresonormalNew);
            }
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getId());
                ingreso.setTipo(tipoNew);
            }
            if (ingresotarjetaNew != null) {
                ingresotarjetaNew = em.getReference(ingresotarjetaNew.getClass(), ingresotarjetaNew.getId());
                ingreso.setIngresotarjeta(ingresotarjetaNew);
            }
            List<Pago> attachedPagoListNew = new ArrayList<Pago>();
            for (Pago pagoListNewPagoToAttach : pagoListNew) {
                pagoListNewPagoToAttach = em.getReference(pagoListNewPagoToAttach.getClass(), pagoListNewPagoToAttach.getId());
                attachedPagoListNew.add(pagoListNewPagoToAttach);
            }
            pagoListNew = attachedPagoListNew;
            ingreso.setPagoList(pagoListNew);
            ingreso = em.merge(ingreso);
            if (ingresonormalNew != null && !ingresonormalNew.equals(ingresonormalOld)) {
                Ingreso oldIngresoOfIngresonormal = ingresonormalNew.getIngreso();
                if (oldIngresoOfIngresonormal != null) {
                    oldIngresoOfIngresonormal.setIngresonormal(null);
                    oldIngresoOfIngresonormal = em.merge(oldIngresoOfIngresonormal);
                }
                ingresonormalNew.setIngreso(ingreso);
                ingresonormalNew = em.merge(ingresonormalNew);
            }
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getIngresoList().remove(ingreso);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getIngresoList().add(ingreso);
                tipoNew = em.merge(tipoNew);
            }
            if (ingresotarjetaNew != null && !ingresotarjetaNew.equals(ingresotarjetaOld)) {
                Ingreso oldIngresoOfIngresotarjeta = ingresotarjetaNew.getIngreso();
                if (oldIngresoOfIngresotarjeta != null) {
                    oldIngresoOfIngresotarjeta.setIngresotarjeta(null);
                    oldIngresoOfIngresotarjeta = em.merge(oldIngresoOfIngresotarjeta);
                }
                ingresotarjetaNew.setIngreso(ingreso);
                ingresotarjetaNew = em.merge(ingresotarjetaNew);
            }
            for (Pago pagoListOldPago : pagoListOld) {
                if (!pagoListNew.contains(pagoListOldPago)) {
                    pagoListOldPago.setIngreso(null);
                    pagoListOldPago = em.merge(pagoListOldPago);
                }
            }
            for (Pago pagoListNewPago : pagoListNew) {
                if (!pagoListOld.contains(pagoListNewPago)) {
                    Ingreso oldIngresoOfPagoListNewPago = pagoListNewPago.getIngreso();
                    pagoListNewPago.setIngreso(ingreso);
                    pagoListNewPago = em.merge(pagoListNewPago);
                    if (oldIngresoOfPagoListNewPago != null && !oldIngresoOfPagoListNewPago.equals(ingreso)) {
                        oldIngresoOfPagoListNewPago.getPagoList().remove(pagoListNewPago);
                        oldIngresoOfPagoListNewPago = em.merge(oldIngresoOfPagoListNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ingreso.getId();
                if (findIngreso(id) == null) {
                    throw new NonexistentEntityException("The ingreso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingreso ingreso;
            try {
                ingreso = em.getReference(Ingreso.class, id);
                ingreso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ingreso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Ingresonormal ingresonormalOrphanCheck = ingreso.getIngresonormal();
            if (ingresonormalOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ingreso (" + ingreso + ") cannot be destroyed since the Ingresonormal " + ingresonormalOrphanCheck + " in its ingresonormal field has a non-nullable ingreso field.");
            }
            Ingresotarjeta ingresotarjetaOrphanCheck = ingreso.getIngresotarjeta();
            if (ingresotarjetaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ingreso (" + ingreso + ") cannot be destroyed since the Ingresotarjeta " + ingresotarjetaOrphanCheck + " in its ingresotarjeta field has a non-nullable ingreso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tipovehiculo tipo = ingreso.getTipo();
            if (tipo != null) {
                tipo.getIngresoList().remove(ingreso);
                tipo = em.merge(tipo);
            }
            List<Pago> pagoList = ingreso.getPagoList();
            for (Pago pagoListPago : pagoList) {
                pagoListPago.setIngreso(null);
                pagoListPago = em.merge(pagoListPago);
            }
            em.remove(ingreso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ingreso> findIngresoEntities() {
        return findIngresoEntities(true, -1, -1);
    }

    public List<Ingreso> findIngresoEntities(int maxResults, int firstResult) {
        return findIngresoEntities(false, maxResults, firstResult);
    }

    private List<Ingreso> findIngresoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ingreso.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Ingreso findIngreso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ingreso.class, id);
        } finally {
            em.close();
        }
    }

    public int getIngresoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ingreso> rt = cq.from(Ingreso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
