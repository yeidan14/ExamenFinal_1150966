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

public class TipovehiculoJpaController implements Serializable {

    public TipovehiculoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipovehiculo tipovehiculo) {
        if (tipovehiculo.getTarifaList() == null) {
            tipovehiculo.setTarifaList(new ArrayList<Tarifa>());
        }
        if (tipovehiculo.getIngresoList() == null) {
            tipovehiculo.setIngresoList(new ArrayList<Ingreso>());
        }
        if (tipovehiculo.getTarjetaList() == null) {
            tipovehiculo.setTarjetaList(new ArrayList<Tarjeta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tarifa> attachedTarifaList = new ArrayList<Tarifa>();
            for (Tarifa tarifaListTarifaToAttach : tipovehiculo.getTarifaList()) {
                tarifaListTarifaToAttach = em.getReference(tarifaListTarifaToAttach.getClass(), tarifaListTarifaToAttach.getId());
                attachedTarifaList.add(tarifaListTarifaToAttach);
            }
            tipovehiculo.setTarifaList(attachedTarifaList);
            List<Ingreso> attachedIngresoList = new ArrayList<Ingreso>();
            for (Ingreso ingresoListIngresoToAttach : tipovehiculo.getIngresoList()) {
                ingresoListIngresoToAttach = em.getReference(ingresoListIngresoToAttach.getClass(), ingresoListIngresoToAttach.getId());
                attachedIngresoList.add(ingresoListIngresoToAttach);
            }
            tipovehiculo.setIngresoList(attachedIngresoList);
            List<Tarjeta> attachedTarjetaList = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaListTarjetaToAttach : tipovehiculo.getTarjetaList()) {
                tarjetaListTarjetaToAttach = em.getReference(tarjetaListTarjetaToAttach.getClass(), tarjetaListTarjetaToAttach.getRfid());
                attachedTarjetaList.add(tarjetaListTarjetaToAttach);
            }
            tipovehiculo.setTarjetaList(attachedTarjetaList);
            em.persist(tipovehiculo);
            for (Tarifa tarifaListTarifa : tipovehiculo.getTarifaList()) {
                Tipovehiculo oldTipovehiculoOfTarifaListTarifa = tarifaListTarifa.getTipovehiculo();
                tarifaListTarifa.setTipovehiculo(tipovehiculo);
                tarifaListTarifa = em.merge(tarifaListTarifa);
                if (oldTipovehiculoOfTarifaListTarifa != null) {
                    oldTipovehiculoOfTarifaListTarifa.getTarifaList().remove(tarifaListTarifa);
                    oldTipovehiculoOfTarifaListTarifa = em.merge(oldTipovehiculoOfTarifaListTarifa);
                }
            }
            for (Ingreso ingresoListIngreso : tipovehiculo.getIngresoList()) {
                Tipovehiculo oldTipoOfIngresoListIngreso = ingresoListIngreso.getTipo();
                ingresoListIngreso.setTipo(tipovehiculo);
                ingresoListIngreso = em.merge(ingresoListIngreso);
                if (oldTipoOfIngresoListIngreso != null) {
                    oldTipoOfIngresoListIngreso.getIngresoList().remove(ingresoListIngreso);
                    oldTipoOfIngresoListIngreso = em.merge(oldTipoOfIngresoListIngreso);
                }
            }
            for (Tarjeta tarjetaListTarjeta : tipovehiculo.getTarjetaList()) {
                Tipovehiculo oldTipovehiculoOfTarjetaListTarjeta = tarjetaListTarjeta.getTipovehiculo();
                tarjetaListTarjeta.setTipovehiculo(tipovehiculo);
                tarjetaListTarjeta = em.merge(tarjetaListTarjeta);
                if (oldTipovehiculoOfTarjetaListTarjeta != null) {
                    oldTipovehiculoOfTarjetaListTarjeta.getTarjetaList().remove(tarjetaListTarjeta);
                    oldTipovehiculoOfTarjetaListTarjeta = em.merge(oldTipovehiculoOfTarjetaListTarjeta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipovehiculo tipovehiculo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipovehiculo persistentTipovehiculo = em.find(Tipovehiculo.class, tipovehiculo.getId());
            List<Tarifa> tarifaListOld = persistentTipovehiculo.getTarifaList();
            List<Tarifa> tarifaListNew = tipovehiculo.getTarifaList();
            List<Ingreso> ingresoListOld = persistentTipovehiculo.getIngresoList();
            List<Ingreso> ingresoListNew = tipovehiculo.getIngresoList();
            List<Tarjeta> tarjetaListOld = persistentTipovehiculo.getTarjetaList();
            List<Tarjeta> tarjetaListNew = tipovehiculo.getTarjetaList();
            List<Tarifa> attachedTarifaListNew = new ArrayList<Tarifa>();
            for (Tarifa tarifaListNewTarifaToAttach : tarifaListNew) {
                tarifaListNewTarifaToAttach = em.getReference(tarifaListNewTarifaToAttach.getClass(), tarifaListNewTarifaToAttach.getId());
                attachedTarifaListNew.add(tarifaListNewTarifaToAttach);
            }
            tarifaListNew = attachedTarifaListNew;
            tipovehiculo.setTarifaList(tarifaListNew);
            List<Ingreso> attachedIngresoListNew = new ArrayList<Ingreso>();
            for (Ingreso ingresoListNewIngresoToAttach : ingresoListNew) {
                ingresoListNewIngresoToAttach = em.getReference(ingresoListNewIngresoToAttach.getClass(), ingresoListNewIngresoToAttach.getId());
                attachedIngresoListNew.add(ingresoListNewIngresoToAttach);
            }
            ingresoListNew = attachedIngresoListNew;
            tipovehiculo.setIngresoList(ingresoListNew);
            List<Tarjeta> attachedTarjetaListNew = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaListNewTarjetaToAttach : tarjetaListNew) {
                tarjetaListNewTarjetaToAttach = em.getReference(tarjetaListNewTarjetaToAttach.getClass(), tarjetaListNewTarjetaToAttach.getRfid());
                attachedTarjetaListNew.add(tarjetaListNewTarjetaToAttach);
            }
            tarjetaListNew = attachedTarjetaListNew;
            tipovehiculo.setTarjetaList(tarjetaListNew);
            tipovehiculo = em.merge(tipovehiculo);
            for (Tarifa tarifaListOldTarifa : tarifaListOld) {
                if (!tarifaListNew.contains(tarifaListOldTarifa)) {
                    tarifaListOldTarifa.setTipovehiculo(null);
                    tarifaListOldTarifa = em.merge(tarifaListOldTarifa);
                }
            }
            for (Tarifa tarifaListNewTarifa : tarifaListNew) {
                if (!tarifaListOld.contains(tarifaListNewTarifa)) {
                    Tipovehiculo oldTipovehiculoOfTarifaListNewTarifa = tarifaListNewTarifa.getTipovehiculo();
                    tarifaListNewTarifa.setTipovehiculo(tipovehiculo);
                    tarifaListNewTarifa = em.merge(tarifaListNewTarifa);
                    if (oldTipovehiculoOfTarifaListNewTarifa != null && !oldTipovehiculoOfTarifaListNewTarifa.equals(tipovehiculo)) {
                        oldTipovehiculoOfTarifaListNewTarifa.getTarifaList().remove(tarifaListNewTarifa);
                        oldTipovehiculoOfTarifaListNewTarifa = em.merge(oldTipovehiculoOfTarifaListNewTarifa);
                    }
                }
            }
            for (Ingreso ingresoListOldIngreso : ingresoListOld) {
                if (!ingresoListNew.contains(ingresoListOldIngreso)) {
                    ingresoListOldIngreso.setTipo(null);
                    ingresoListOldIngreso = em.merge(ingresoListOldIngreso);
                }
            }
            for (Ingreso ingresoListNewIngreso : ingresoListNew) {
                if (!ingresoListOld.contains(ingresoListNewIngreso)) {
                    Tipovehiculo oldTipoOfIngresoListNewIngreso = ingresoListNewIngreso.getTipo();
                    ingresoListNewIngreso.setTipo(tipovehiculo);
                    ingresoListNewIngreso = em.merge(ingresoListNewIngreso);
                    if (oldTipoOfIngresoListNewIngreso != null && !oldTipoOfIngresoListNewIngreso.equals(tipovehiculo)) {
                        oldTipoOfIngresoListNewIngreso.getIngresoList().remove(ingresoListNewIngreso);
                        oldTipoOfIngresoListNewIngreso = em.merge(oldTipoOfIngresoListNewIngreso);
                    }
                }
            }
            for (Tarjeta tarjetaListOldTarjeta : tarjetaListOld) {
                if (!tarjetaListNew.contains(tarjetaListOldTarjeta)) {
                    tarjetaListOldTarjeta.setTipovehiculo(null);
                    tarjetaListOldTarjeta = em.merge(tarjetaListOldTarjeta);
                }
            }
            for (Tarjeta tarjetaListNewTarjeta : tarjetaListNew) {
                if (!tarjetaListOld.contains(tarjetaListNewTarjeta)) {
                    Tipovehiculo oldTipovehiculoOfTarjetaListNewTarjeta = tarjetaListNewTarjeta.getTipovehiculo();
                    tarjetaListNewTarjeta.setTipovehiculo(tipovehiculo);
                    tarjetaListNewTarjeta = em.merge(tarjetaListNewTarjeta);
                    if (oldTipovehiculoOfTarjetaListNewTarjeta != null && !oldTipovehiculoOfTarjetaListNewTarjeta.equals(tipovehiculo)) {
                        oldTipovehiculoOfTarjetaListNewTarjeta.getTarjetaList().remove(tarjetaListNewTarjeta);
                        oldTipovehiculoOfTarjetaListNewTarjeta = em.merge(oldTipovehiculoOfTarjetaListNewTarjeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipovehiculo.getId();
                if (findTipovehiculo(id) == null) {
                    throw new NonexistentEntityException("The tipovehiculo with id " + id + " no longer exists.");
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
            Tipovehiculo tipovehiculo;
            try {
                tipovehiculo = em.getReference(Tipovehiculo.class, id);
                tipovehiculo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipovehiculo with id " + id + " no longer exists.", enfe);
            }
            List<Tarifa> tarifaList = tipovehiculo.getTarifaList();
            for (Tarifa tarifaListTarifa : tarifaList) {
                tarifaListTarifa.setTipovehiculo(null);
                tarifaListTarifa = em.merge(tarifaListTarifa);
            }
            List<Ingreso> ingresoList = tipovehiculo.getIngresoList();
            for (Ingreso ingresoListIngreso : ingresoList) {
                ingresoListIngreso.setTipo(null);
                ingresoListIngreso = em.merge(ingresoListIngreso);
            }
            List<Tarjeta> tarjetaList = tipovehiculo.getTarjetaList();
            for (Tarjeta tarjetaListTarjeta : tarjetaList) {
                tarjetaListTarjeta.setTipovehiculo(null);
                tarjetaListTarjeta = em.merge(tarjetaListTarjeta);
            }
            em.remove(tipovehiculo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipovehiculo> findTipovehiculoEntities() {
        return findTipovehiculoEntities(true, -1, -1);
    }

    public List<Tipovehiculo> findTipovehiculoEntities(int maxResults, int firstResult) {
        return findTipovehiculoEntities(false, maxResults, firstResult);
    }

    private List<Tipovehiculo> findTipovehiculoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipovehiculo.class));
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

    public Tipovehiculo findTipovehiculo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipovehiculo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipovehiculoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipovehiculo> rt = cq.from(Tipovehiculo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
