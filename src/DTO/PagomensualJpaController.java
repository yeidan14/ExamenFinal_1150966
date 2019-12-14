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

public class PagomensualJpaController implements Serializable {

    public PagomensualJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pagomensual pagomensual) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pago pagoOrphanCheck = pagomensual.getPago();
        if (pagoOrphanCheck != null) {
            Pagomensual oldPagomensualOfPago = pagoOrphanCheck.getPagomensual();
            if (oldPagomensualOfPago != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pago " + pagoOrphanCheck + " already has an item of type Pagomensual whose pago column cannot be null. Please make another selection for the pago field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago pago = pagomensual.getPago();
            if (pago != null) {
                pago = em.getReference(pago.getClass(), pago.getId());
                pagomensual.setPago(pago);
            }
            Tarjeta tarjeta = pagomensual.getTarjeta();
            if (tarjeta != null) {
                tarjeta = em.getReference(tarjeta.getClass(), tarjeta.getRfid());
                pagomensual.setTarjeta(tarjeta);
            }
            em.persist(pagomensual);
            if (pago != null) {
                pago.setPagomensual(pagomensual);
                pago = em.merge(pago);
            }
            if (tarjeta != null) {
                tarjeta.getPagomensualList().add(pagomensual);
                tarjeta = em.merge(tarjeta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPagomensual(pagomensual.getId()) != null) {
                throw new PreexistingEntityException("Pagomensual " + pagomensual + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pagomensual pagomensual) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pagomensual persistentPagomensual = em.find(Pagomensual.class, pagomensual.getId());
            Pago pagoOld = persistentPagomensual.getPago();
            Pago pagoNew = pagomensual.getPago();
            Tarjeta tarjetaOld = persistentPagomensual.getTarjeta();
            Tarjeta tarjetaNew = pagomensual.getTarjeta();
            List<String> illegalOrphanMessages = null;
            if (pagoNew != null && !pagoNew.equals(pagoOld)) {
                Pagomensual oldPagomensualOfPago = pagoNew.getPagomensual();
                if (oldPagomensualOfPago != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pago " + pagoNew + " already has an item of type Pagomensual whose pago column cannot be null. Please make another selection for the pago field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pagoNew != null) {
                pagoNew = em.getReference(pagoNew.getClass(), pagoNew.getId());
                pagomensual.setPago(pagoNew);
            }
            if (tarjetaNew != null) {
                tarjetaNew = em.getReference(tarjetaNew.getClass(), tarjetaNew.getRfid());
                pagomensual.setTarjeta(tarjetaNew);
            }
            pagomensual = em.merge(pagomensual);
            if (pagoOld != null && !pagoOld.equals(pagoNew)) {
                pagoOld.setPagomensual(null);
                pagoOld = em.merge(pagoOld);
            }
            if (pagoNew != null && !pagoNew.equals(pagoOld)) {
                pagoNew.setPagomensual(pagomensual);
                pagoNew = em.merge(pagoNew);
            }
            if (tarjetaOld != null && !tarjetaOld.equals(tarjetaNew)) {
                tarjetaOld.getPagomensualList().remove(pagomensual);
                tarjetaOld = em.merge(tarjetaOld);
            }
            if (tarjetaNew != null && !tarjetaNew.equals(tarjetaOld)) {
                tarjetaNew.getPagomensualList().add(pagomensual);
                tarjetaNew = em.merge(tarjetaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pagomensual.getId();
                if (findPagomensual(id) == null) {
                    throw new NonexistentEntityException("The pagomensual with id " + id + " no longer exists.");
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
            Pagomensual pagomensual;
            try {
                pagomensual = em.getReference(Pagomensual.class, id);
                pagomensual.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pagomensual with id " + id + " no longer exists.", enfe);
            }
            Pago pago = pagomensual.getPago();
            if (pago != null) {
                pago.setPagomensual(null);
                pago = em.merge(pago);
            }
            Tarjeta tarjeta = pagomensual.getTarjeta();
            if (tarjeta != null) {
                tarjeta.getPagomensualList().remove(pagomensual);
                tarjeta = em.merge(tarjeta);
            }
            em.remove(pagomensual);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pagomensual> findPagomensualEntities() {
        return findPagomensualEntities(true, -1, -1);
    }

    public List<Pagomensual> findPagomensualEntities(int maxResults, int firstResult) {
        return findPagomensualEntities(false, maxResults, firstResult);
    }

    private List<Pagomensual> findPagomensualEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pagomensual.class));
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

    public Pagomensual findPagomensual(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pagomensual.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagomensualCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pagomensual> rt = cq.from(Pagomensual.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
