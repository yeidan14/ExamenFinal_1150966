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

public class TarifaJpaController implements Serializable {

    public TarifaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarifa tarifa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipotarifa tipotarifa = tarifa.getTipotarifa();
            if (tipotarifa != null) {
                tipotarifa = em.getReference(tipotarifa.getClass(), tipotarifa.getId());
                tarifa.setTipotarifa(tipotarifa);
            }
            Tipovehiculo tipovehiculo = tarifa.getTipovehiculo();
            if (tipovehiculo != null) {
                tipovehiculo = em.getReference(tipovehiculo.getClass(), tipovehiculo.getId());
                tarifa.setTipovehiculo(tipovehiculo);
            }
            em.persist(tarifa);
            if (tipotarifa != null) {
                tipotarifa.getTarifaList().add(tarifa);
                tipotarifa = em.merge(tipotarifa);
            }
            if (tipovehiculo != null) {
                tipovehiculo.getTarifaList().add(tarifa);
                tipovehiculo = em.merge(tipovehiculo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarifa tarifa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarifa persistentTarifa = em.find(Tarifa.class, tarifa.getId());
            Tipotarifa tipotarifaOld = persistentTarifa.getTipotarifa();
            Tipotarifa tipotarifaNew = tarifa.getTipotarifa();
            Tipovehiculo tipovehiculoOld = persistentTarifa.getTipovehiculo();
            Tipovehiculo tipovehiculoNew = tarifa.getTipovehiculo();
            if (tipotarifaNew != null) {
                tipotarifaNew = em.getReference(tipotarifaNew.getClass(), tipotarifaNew.getId());
                tarifa.setTipotarifa(tipotarifaNew);
            }
            if (tipovehiculoNew != null) {
                tipovehiculoNew = em.getReference(tipovehiculoNew.getClass(), tipovehiculoNew.getId());
                tarifa.setTipovehiculo(tipovehiculoNew);
            }
            tarifa = em.merge(tarifa);
            if (tipotarifaOld != null && !tipotarifaOld.equals(tipotarifaNew)) {
                tipotarifaOld.getTarifaList().remove(tarifa);
                tipotarifaOld = em.merge(tipotarifaOld);
            }
            if (tipotarifaNew != null && !tipotarifaNew.equals(tipotarifaOld)) {
                tipotarifaNew.getTarifaList().add(tarifa);
                tipotarifaNew = em.merge(tipotarifaNew);
            }
            if (tipovehiculoOld != null && !tipovehiculoOld.equals(tipovehiculoNew)) {
                tipovehiculoOld.getTarifaList().remove(tarifa);
                tipovehiculoOld = em.merge(tipovehiculoOld);
            }
            if (tipovehiculoNew != null && !tipovehiculoNew.equals(tipovehiculoOld)) {
                tipovehiculoNew.getTarifaList().add(tarifa);
                tipovehiculoNew = em.merge(tipovehiculoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tarifa.getId();
                if (findTarifa(id) == null) {
                    throw new NonexistentEntityException("The tarifa with id " + id + " no longer exists.");
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
            Tarifa tarifa;
            try {
                tarifa = em.getReference(Tarifa.class, id);
                tarifa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarifa with id " + id + " no longer exists.", enfe);
            }
            Tipotarifa tipotarifa = tarifa.getTipotarifa();
            if (tipotarifa != null) {
                tipotarifa.getTarifaList().remove(tarifa);
                tipotarifa = em.merge(tipotarifa);
            }
            Tipovehiculo tipovehiculo = tarifa.getTipovehiculo();
            if (tipovehiculo != null) {
                tipovehiculo.getTarifaList().remove(tarifa);
                tipovehiculo = em.merge(tipovehiculo);
            }
            em.remove(tarifa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarifa> findTarifaEntities() {
        return findTarifaEntities(true, -1, -1);
    }

    public List<Tarifa> findTarifaEntities(int maxResults, int firstResult) {
        return findTarifaEntities(false, maxResults, firstResult);
    }

    private List<Tarifa> findTarifaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarifa.class));
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

    public Tarifa findTarifa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarifa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarifaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarifa> rt = cq.from(Tarifa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
