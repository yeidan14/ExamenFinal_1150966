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

public class TipotarifaJpaController implements Serializable {

    public TipotarifaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipotarifa tipotarifa) {
        if (tipotarifa.getTarifaList() == null) {
            tipotarifa.setTarifaList(new ArrayList<Tarifa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tarifa> attachedTarifaList = new ArrayList<Tarifa>();
            for (Tarifa tarifaListTarifaToAttach : tipotarifa.getTarifaList()) {
                tarifaListTarifaToAttach = em.getReference(tarifaListTarifaToAttach.getClass(), tarifaListTarifaToAttach.getId());
                attachedTarifaList.add(tarifaListTarifaToAttach);
            }
            tipotarifa.setTarifaList(attachedTarifaList);
            em.persist(tipotarifa);
            for (Tarifa tarifaListTarifa : tipotarifa.getTarifaList()) {
                Tipotarifa oldTipotarifaOfTarifaListTarifa = tarifaListTarifa.getTipotarifa();
                tarifaListTarifa.setTipotarifa(tipotarifa);
                tarifaListTarifa = em.merge(tarifaListTarifa);
                if (oldTipotarifaOfTarifaListTarifa != null) {
                    oldTipotarifaOfTarifaListTarifa.getTarifaList().remove(tarifaListTarifa);
                    oldTipotarifaOfTarifaListTarifa = em.merge(oldTipotarifaOfTarifaListTarifa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipotarifa tipotarifa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipotarifa persistentTipotarifa = em.find(Tipotarifa.class, tipotarifa.getId());
            List<Tarifa> tarifaListOld = persistentTipotarifa.getTarifaList();
            List<Tarifa> tarifaListNew = tipotarifa.getTarifaList();
            List<Tarifa> attachedTarifaListNew = new ArrayList<Tarifa>();
            for (Tarifa tarifaListNewTarifaToAttach : tarifaListNew) {
                tarifaListNewTarifaToAttach = em.getReference(tarifaListNewTarifaToAttach.getClass(), tarifaListNewTarifaToAttach.getId());
                attachedTarifaListNew.add(tarifaListNewTarifaToAttach);
            }
            tarifaListNew = attachedTarifaListNew;
            tipotarifa.setTarifaList(tarifaListNew);
            tipotarifa = em.merge(tipotarifa);
            for (Tarifa tarifaListOldTarifa : tarifaListOld) {
                if (!tarifaListNew.contains(tarifaListOldTarifa)) {
                    tarifaListOldTarifa.setTipotarifa(null);
                    tarifaListOldTarifa = em.merge(tarifaListOldTarifa);
                }
            }
            for (Tarifa tarifaListNewTarifa : tarifaListNew) {
                if (!tarifaListOld.contains(tarifaListNewTarifa)) {
                    Tipotarifa oldTipotarifaOfTarifaListNewTarifa = tarifaListNewTarifa.getTipotarifa();
                    tarifaListNewTarifa.setTipotarifa(tipotarifa);
                    tarifaListNewTarifa = em.merge(tarifaListNewTarifa);
                    if (oldTipotarifaOfTarifaListNewTarifa != null && !oldTipotarifaOfTarifaListNewTarifa.equals(tipotarifa)) {
                        oldTipotarifaOfTarifaListNewTarifa.getTarifaList().remove(tarifaListNewTarifa);
                        oldTipotarifaOfTarifaListNewTarifa = em.merge(oldTipotarifaOfTarifaListNewTarifa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipotarifa.getId();
                if (findTipotarifa(id) == null) {
                    throw new NonexistentEntityException("The tipotarifa with id " + id + " no longer exists.");
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
            Tipotarifa tipotarifa;
            try {
                tipotarifa = em.getReference(Tipotarifa.class, id);
                tipotarifa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipotarifa with id " + id + " no longer exists.", enfe);
            }
            List<Tarifa> tarifaList = tipotarifa.getTarifaList();
            for (Tarifa tarifaListTarifa : tarifaList) {
                tarifaListTarifa.setTipotarifa(null);
                tarifaListTarifa = em.merge(tarifaListTarifa);
            }
            em.remove(tipotarifa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipotarifa> findTipotarifaEntities() {
        return findTipotarifaEntities(true, -1, -1);
    }

    public List<Tipotarifa> findTipotarifaEntities(int maxResults, int firstResult) {
        return findTipotarifaEntities(false, maxResults, firstResult);
    }

    private List<Tipotarifa> findTipotarifaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipotarifa.class));
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

    public Tipotarifa findTipotarifa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipotarifa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipotarifaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipotarifa> rt = cq.from(Tipotarifa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
