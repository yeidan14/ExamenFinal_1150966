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

public class PagoJpaController implements Serializable {

    public PagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pagoservicio pagoservicio = pago.getPagoservicio();
            if (pagoservicio != null) {
                pagoservicio = em.getReference(pagoservicio.getClass(), pagoservicio.getId());
                pago.setPagoservicio(pagoservicio);
            }
            Caja caja = pago.getCaja();
            if (caja != null) {
                caja = em.getReference(caja.getClass(), caja.getId());
                pago.setCaja(caja);
            }
            Ingreso ingreso = pago.getIngreso();
            if (ingreso != null) {
                ingreso = em.getReference(ingreso.getClass(), ingreso.getId());
                pago.setIngreso(ingreso);
            }
            Usuario usuario = pago.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                pago.setUsuario(usuario);
            }
            Pagomensual pagomensual = pago.getPagomensual();
            if (pagomensual != null) {
                pagomensual = em.getReference(pagomensual.getClass(), pagomensual.getId());
                pago.setPagomensual(pagomensual);
            }
            em.persist(pago);
            if (pagoservicio != null) {
                Pago oldPagoOfPagoservicio = pagoservicio.getPago();
                if (oldPagoOfPagoservicio != null) {
                    oldPagoOfPagoservicio.setPagoservicio(null);
                    oldPagoOfPagoservicio = em.merge(oldPagoOfPagoservicio);
                }
                pagoservicio.setPago(pago);
                pagoservicio = em.merge(pagoservicio);
            }
            if (caja != null) {
                caja.getPagoList().add(pago);
                caja = em.merge(caja);
            }
            if (ingreso != null) {
                ingreso.getPagoList().add(pago);
                ingreso = em.merge(ingreso);
            }
            if (usuario != null) {
                usuario.getPagoList().add(pago);
                usuario = em.merge(usuario);
            }
            if (pagomensual != null) {
                Pago oldPagoOfPagomensual = pagomensual.getPago();
                if (oldPagoOfPagomensual != null) {
                    oldPagoOfPagomensual.setPagomensual(null);
                    oldPagoOfPagomensual = em.merge(oldPagoOfPagomensual);
                }
                pagomensual.setPago(pago);
                pagomensual = em.merge(pagomensual);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getId());
            Pagoservicio pagoservicioOld = persistentPago.getPagoservicio();
            Pagoservicio pagoservicioNew = pago.getPagoservicio();
            Caja cajaOld = persistentPago.getCaja();
            Caja cajaNew = pago.getCaja();
            Ingreso ingresoOld = persistentPago.getIngreso();
            Ingreso ingresoNew = pago.getIngreso();
            Usuario usuarioOld = persistentPago.getUsuario();
            Usuario usuarioNew = pago.getUsuario();
            Pagomensual pagomensualOld = persistentPago.getPagomensual();
            Pagomensual pagomensualNew = pago.getPagomensual();
            List<String> illegalOrphanMessages = null;
            if (pagoservicioOld != null && !pagoservicioOld.equals(pagoservicioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Pagoservicio " + pagoservicioOld + " since its pago field is not nullable.");
            }
            if (pagomensualOld != null && !pagomensualOld.equals(pagomensualNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Pagomensual " + pagomensualOld + " since its pago field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pagoservicioNew != null) {
                pagoservicioNew = em.getReference(pagoservicioNew.getClass(), pagoservicioNew.getId());
                pago.setPagoservicio(pagoservicioNew);
            }
            if (cajaNew != null) {
                cajaNew = em.getReference(cajaNew.getClass(), cajaNew.getId());
                pago.setCaja(cajaNew);
            }
            if (ingresoNew != null) {
                ingresoNew = em.getReference(ingresoNew.getClass(), ingresoNew.getId());
                pago.setIngreso(ingresoNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                pago.setUsuario(usuarioNew);
            }
            if (pagomensualNew != null) {
                pagomensualNew = em.getReference(pagomensualNew.getClass(), pagomensualNew.getId());
                pago.setPagomensual(pagomensualNew);
            }
            pago = em.merge(pago);
            if (pagoservicioNew != null && !pagoservicioNew.equals(pagoservicioOld)) {
                Pago oldPagoOfPagoservicio = pagoservicioNew.getPago();
                if (oldPagoOfPagoservicio != null) {
                    oldPagoOfPagoservicio.setPagoservicio(null);
                    oldPagoOfPagoservicio = em.merge(oldPagoOfPagoservicio);
                }
                pagoservicioNew.setPago(pago);
                pagoservicioNew = em.merge(pagoservicioNew);
            }
            if (cajaOld != null && !cajaOld.equals(cajaNew)) {
                cajaOld.getPagoList().remove(pago);
                cajaOld = em.merge(cajaOld);
            }
            if (cajaNew != null && !cajaNew.equals(cajaOld)) {
                cajaNew.getPagoList().add(pago);
                cajaNew = em.merge(cajaNew);
            }
            if (ingresoOld != null && !ingresoOld.equals(ingresoNew)) {
                ingresoOld.getPagoList().remove(pago);
                ingresoOld = em.merge(ingresoOld);
            }
            if (ingresoNew != null && !ingresoNew.equals(ingresoOld)) {
                ingresoNew.getPagoList().add(pago);
                ingresoNew = em.merge(ingresoNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getPagoList().remove(pago);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getPagoList().add(pago);
                usuarioNew = em.merge(usuarioNew);
            }
            if (pagomensualNew != null && !pagomensualNew.equals(pagomensualOld)) {
                Pago oldPagoOfPagomensual = pagomensualNew.getPago();
                if (oldPagoOfPagomensual != null) {
                    oldPagoOfPagomensual.setPagomensual(null);
                    oldPagoOfPagomensual = em.merge(oldPagoOfPagomensual);
                }
                pagomensualNew.setPago(pago);
                pagomensualNew = em.merge(pagomensualNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pago.getId();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
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
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Pagoservicio pagoservicioOrphanCheck = pago.getPagoservicio();
            if (pagoservicioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pago (" + pago + ") cannot be destroyed since the Pagoservicio " + pagoservicioOrphanCheck + " in its pagoservicio field has a non-nullable pago field.");
            }
            Pagomensual pagomensualOrphanCheck = pago.getPagomensual();
            if (pagomensualOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pago (" + pago + ") cannot be destroyed since the Pagomensual " + pagomensualOrphanCheck + " in its pagomensual field has a non-nullable pago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Caja caja = pago.getCaja();
            if (caja != null) {
                caja.getPagoList().remove(pago);
                caja = em.merge(caja);
            }
            Ingreso ingreso = pago.getIngreso();
            if (ingreso != null) {
                ingreso.getPagoList().remove(pago);
                ingreso = em.merge(ingreso);
            }
            Usuario usuario = pago.getUsuario();
            if (usuario != null) {
                usuario.getPagoList().remove(pago);
                usuario = em.merge(usuario);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
