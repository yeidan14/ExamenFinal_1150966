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

public class PagoservicioJpaController implements Serializable {

    public PagoservicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pagoservicio pagoservicio) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Ingresonormal ingresonormalOrphanCheck = pagoservicio.getIngresonormal();
        if (ingresonormalOrphanCheck != null) {
            Pagoservicio oldPagoservicioOfIngresonormal = ingresonormalOrphanCheck.getPagoservicio();
            if (oldPagoservicioOfIngresonormal != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Ingresonormal " + ingresonormalOrphanCheck + " already has an item of type Pagoservicio whose ingresonormal column cannot be null. Please make another selection for the ingresonormal field.");
            }
        }
        Pago pagoOrphanCheck = pagoservicio.getPago();
        if (pagoOrphanCheck != null) {
            Pagoservicio oldPagoservicioOfPago = pagoOrphanCheck.getPagoservicio();
            if (oldPagoservicioOfPago != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pago " + pagoOrphanCheck + " already has an item of type Pagoservicio whose pago column cannot be null. Please make another selection for the pago field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingresonormal ingresonormal = pagoservicio.getIngresonormal();
            if (ingresonormal != null) {
                ingresonormal = em.getReference(ingresonormal.getClass(), ingresonormal.getId());
                pagoservicio.setIngresonormal(ingresonormal);
            }
            Pago pago = pagoservicio.getPago();
            if (pago != null) {
                pago = em.getReference(pago.getClass(), pago.getId());
                pagoservicio.setPago(pago);
            }
            em.persist(pagoservicio);
            if (ingresonormal != null) {
                ingresonormal.setPagoservicio(pagoservicio);
                ingresonormal = em.merge(ingresonormal);
            }
            if (pago != null) {
                pago.setPagoservicio(pagoservicio);
                pago = em.merge(pago);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPagoservicio(pagoservicio.getId()) != null) {
                throw new PreexistingEntityException("Pagoservicio " + pagoservicio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pagoservicio pagoservicio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pagoservicio persistentPagoservicio = em.find(Pagoservicio.class, pagoservicio.getId());
            Ingresonormal ingresonormalOld = persistentPagoservicio.getIngresonormal();
            Ingresonormal ingresonormalNew = pagoservicio.getIngresonormal();
            Pago pagoOld = persistentPagoservicio.getPago();
            Pago pagoNew = pagoservicio.getPago();
            List<String> illegalOrphanMessages = null;
            if (ingresonormalNew != null && !ingresonormalNew.equals(ingresonormalOld)) {
                Pagoservicio oldPagoservicioOfIngresonormal = ingresonormalNew.getPagoservicio();
                if (oldPagoservicioOfIngresonormal != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Ingresonormal " + ingresonormalNew + " already has an item of type Pagoservicio whose ingresonormal column cannot be null. Please make another selection for the ingresonormal field.");
                }
            }
            if (pagoNew != null && !pagoNew.equals(pagoOld)) {
                Pagoservicio oldPagoservicioOfPago = pagoNew.getPagoservicio();
                if (oldPagoservicioOfPago != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pago " + pagoNew + " already has an item of type Pagoservicio whose pago column cannot be null. Please make another selection for the pago field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (ingresonormalNew != null) {
                ingresonormalNew = em.getReference(ingresonormalNew.getClass(), ingresonormalNew.getId());
                pagoservicio.setIngresonormal(ingresonormalNew);
            }
            if (pagoNew != null) {
                pagoNew = em.getReference(pagoNew.getClass(), pagoNew.getId());
                pagoservicio.setPago(pagoNew);
            }
            pagoservicio = em.merge(pagoservicio);
            if (ingresonormalOld != null && !ingresonormalOld.equals(ingresonormalNew)) {
                ingresonormalOld.setPagoservicio(null);
                ingresonormalOld = em.merge(ingresonormalOld);
            }
            if (ingresonormalNew != null && !ingresonormalNew.equals(ingresonormalOld)) {
                ingresonormalNew.setPagoservicio(pagoservicio);
                ingresonormalNew = em.merge(ingresonormalNew);
            }
            if (pagoOld != null && !pagoOld.equals(pagoNew)) {
                pagoOld.setPagoservicio(null);
                pagoOld = em.merge(pagoOld);
            }
            if (pagoNew != null && !pagoNew.equals(pagoOld)) {
                pagoNew.setPagoservicio(pagoservicio);
                pagoNew = em.merge(pagoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pagoservicio.getId();
                if (findPagoservicio(id) == null) {
                    throw new NonexistentEntityException("The pagoservicio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pagoservicio pagoservicio;
            try {
                pagoservicio = em.getReference(Pagoservicio.class, id);
                pagoservicio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pagoservicio with id " + id + " no longer exists.", enfe);
            }
            Ingresonormal ingresonormal = pagoservicio.getIngresonormal();
            if (ingresonormal != null) {
                ingresonormal.setPagoservicio(null);
                ingresonormal = em.merge(ingresonormal);
            }
            Pago pago = pagoservicio.getPago();
            if (pago != null) {
                pago.setPagoservicio(null);
                pago = em.merge(pago);
            }
            em.remove(pagoservicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pagoservicio> findPagoservicioEntities() {
        return findPagoservicioEntities(true, -1, -1);
    }

    public List<Pagoservicio> findPagoservicioEntities(int maxResults, int firstResult) {
        return findPagoservicioEntities(false, maxResults, firstResult);
    }

    private List<Pagoservicio> findPagoservicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pagoservicio.class));
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

    public Pagoservicio findPagoservicio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pagoservicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoservicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pagoservicio> rt = cq.from(Pagoservicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
