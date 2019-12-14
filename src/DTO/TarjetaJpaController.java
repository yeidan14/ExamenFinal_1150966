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

public class TarjetaJpaController implements Serializable {

    public TarjetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarjeta tarjeta) throws PreexistingEntityException, Exception {
        if (tarjeta.getPagomensualList() == null) {
            tarjeta.setPagomensualList(new ArrayList<Pagomensual>());
        }
        if (tarjeta.getIngresotarjetaList() == null) {
            tarjeta.setIngresotarjetaList(new ArrayList<Ingresotarjeta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente = tarjeta.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getId());
                tarjeta.setCliente(cliente);
            }
            Tipovehiculo tipovehiculo = tarjeta.getTipovehiculo();
            if (tipovehiculo != null) {
                tipovehiculo = em.getReference(tipovehiculo.getClass(), tipovehiculo.getId());
                tarjeta.setTipovehiculo(tipovehiculo);
            }
            Usuario usuarioactivo = tarjeta.getUsuarioactivo();
            if (usuarioactivo != null) {
                usuarioactivo = em.getReference(usuarioactivo.getClass(), usuarioactivo.getId());
                tarjeta.setUsuarioactivo(usuarioactivo);
            }
            List<Pagomensual> attachedPagomensualList = new ArrayList<Pagomensual>();
            for (Pagomensual pagomensualListPagomensualToAttach : tarjeta.getPagomensualList()) {
                pagomensualListPagomensualToAttach = em.getReference(pagomensualListPagomensualToAttach.getClass(), pagomensualListPagomensualToAttach.getId());
                attachedPagomensualList.add(pagomensualListPagomensualToAttach);
            }
            tarjeta.setPagomensualList(attachedPagomensualList);
            List<Ingresotarjeta> attachedIngresotarjetaList = new ArrayList<Ingresotarjeta>();
            for (Ingresotarjeta ingresotarjetaListIngresotarjetaToAttach : tarjeta.getIngresotarjetaList()) {
                ingresotarjetaListIngresotarjetaToAttach = em.getReference(ingresotarjetaListIngresotarjetaToAttach.getClass(), ingresotarjetaListIngresotarjetaToAttach.getId());
                attachedIngresotarjetaList.add(ingresotarjetaListIngresotarjetaToAttach);
            }
            tarjeta.setIngresotarjetaList(attachedIngresotarjetaList);
            em.persist(tarjeta);
            if (cliente != null) {
                cliente.getTarjetaList().add(tarjeta);
                cliente = em.merge(cliente);
            }
            if (tipovehiculo != null) {
                tipovehiculo.getTarjetaList().add(tarjeta);
                tipovehiculo = em.merge(tipovehiculo);
            }
            if (usuarioactivo != null) {
                usuarioactivo.getTarjetaList().add(tarjeta);
                usuarioactivo = em.merge(usuarioactivo);
            }
            for (Pagomensual pagomensualListPagomensual : tarjeta.getPagomensualList()) {
                Tarjeta oldTarjetaOfPagomensualListPagomensual = pagomensualListPagomensual.getTarjeta();
                pagomensualListPagomensual.setTarjeta(tarjeta);
                pagomensualListPagomensual = em.merge(pagomensualListPagomensual);
                if (oldTarjetaOfPagomensualListPagomensual != null) {
                    oldTarjetaOfPagomensualListPagomensual.getPagomensualList().remove(pagomensualListPagomensual);
                    oldTarjetaOfPagomensualListPagomensual = em.merge(oldTarjetaOfPagomensualListPagomensual);
                }
            }
            for (Ingresotarjeta ingresotarjetaListIngresotarjeta : tarjeta.getIngresotarjetaList()) {
                Tarjeta oldTarjetaOfIngresotarjetaListIngresotarjeta = ingresotarjetaListIngresotarjeta.getTarjeta();
                ingresotarjetaListIngresotarjeta.setTarjeta(tarjeta);
                ingresotarjetaListIngresotarjeta = em.merge(ingresotarjetaListIngresotarjeta);
                if (oldTarjetaOfIngresotarjetaListIngresotarjeta != null) {
                    oldTarjetaOfIngresotarjetaListIngresotarjeta.getIngresotarjetaList().remove(ingresotarjetaListIngresotarjeta);
                    oldTarjetaOfIngresotarjetaListIngresotarjeta = em.merge(oldTarjetaOfIngresotarjetaListIngresotarjeta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTarjeta(tarjeta.getRfid()) != null) {
                throw new PreexistingEntityException("Tarjeta " + tarjeta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarjeta tarjeta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjeta persistentTarjeta = em.find(Tarjeta.class, tarjeta.getRfid());
            Cliente clienteOld = persistentTarjeta.getCliente();
            Cliente clienteNew = tarjeta.getCliente();
            Tipovehiculo tipovehiculoOld = persistentTarjeta.getTipovehiculo();
            Tipovehiculo tipovehiculoNew = tarjeta.getTipovehiculo();
            Usuario usuarioactivoOld = persistentTarjeta.getUsuarioactivo();
            Usuario usuarioactivoNew = tarjeta.getUsuarioactivo();
            List<Pagomensual> pagomensualListOld = persistentTarjeta.getPagomensualList();
            List<Pagomensual> pagomensualListNew = tarjeta.getPagomensualList();
            List<Ingresotarjeta> ingresotarjetaListOld = persistentTarjeta.getIngresotarjetaList();
            List<Ingresotarjeta> ingresotarjetaListNew = tarjeta.getIngresotarjetaList();
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getId());
                tarjeta.setCliente(clienteNew);
            }
            if (tipovehiculoNew != null) {
                tipovehiculoNew = em.getReference(tipovehiculoNew.getClass(), tipovehiculoNew.getId());
                tarjeta.setTipovehiculo(tipovehiculoNew);
            }
            if (usuarioactivoNew != null) {
                usuarioactivoNew = em.getReference(usuarioactivoNew.getClass(), usuarioactivoNew.getId());
                tarjeta.setUsuarioactivo(usuarioactivoNew);
            }
            List<Pagomensual> attachedPagomensualListNew = new ArrayList<Pagomensual>();
            for (Pagomensual pagomensualListNewPagomensualToAttach : pagomensualListNew) {
                pagomensualListNewPagomensualToAttach = em.getReference(pagomensualListNewPagomensualToAttach.getClass(), pagomensualListNewPagomensualToAttach.getId());
                attachedPagomensualListNew.add(pagomensualListNewPagomensualToAttach);
            }
            pagomensualListNew = attachedPagomensualListNew;
            tarjeta.setPagomensualList(pagomensualListNew);
            List<Ingresotarjeta> attachedIngresotarjetaListNew = new ArrayList<Ingresotarjeta>();
            for (Ingresotarjeta ingresotarjetaListNewIngresotarjetaToAttach : ingresotarjetaListNew) {
                ingresotarjetaListNewIngresotarjetaToAttach = em.getReference(ingresotarjetaListNewIngresotarjetaToAttach.getClass(), ingresotarjetaListNewIngresotarjetaToAttach.getId());
                attachedIngresotarjetaListNew.add(ingresotarjetaListNewIngresotarjetaToAttach);
            }
            ingresotarjetaListNew = attachedIngresotarjetaListNew;
            tarjeta.setIngresotarjetaList(ingresotarjetaListNew);
            tarjeta = em.merge(tarjeta);
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.getTarjetaList().remove(tarjeta);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.getTarjetaList().add(tarjeta);
                clienteNew = em.merge(clienteNew);
            }
            if (tipovehiculoOld != null && !tipovehiculoOld.equals(tipovehiculoNew)) {
                tipovehiculoOld.getTarjetaList().remove(tarjeta);
                tipovehiculoOld = em.merge(tipovehiculoOld);
            }
            if (tipovehiculoNew != null && !tipovehiculoNew.equals(tipovehiculoOld)) {
                tipovehiculoNew.getTarjetaList().add(tarjeta);
                tipovehiculoNew = em.merge(tipovehiculoNew);
            }
            if (usuarioactivoOld != null && !usuarioactivoOld.equals(usuarioactivoNew)) {
                usuarioactivoOld.getTarjetaList().remove(tarjeta);
                usuarioactivoOld = em.merge(usuarioactivoOld);
            }
            if (usuarioactivoNew != null && !usuarioactivoNew.equals(usuarioactivoOld)) {
                usuarioactivoNew.getTarjetaList().add(tarjeta);
                usuarioactivoNew = em.merge(usuarioactivoNew);
            }
            for (Pagomensual pagomensualListOldPagomensual : pagomensualListOld) {
                if (!pagomensualListNew.contains(pagomensualListOldPagomensual)) {
                    pagomensualListOldPagomensual.setTarjeta(null);
                    pagomensualListOldPagomensual = em.merge(pagomensualListOldPagomensual);
                }
            }
            for (Pagomensual pagomensualListNewPagomensual : pagomensualListNew) {
                if (!pagomensualListOld.contains(pagomensualListNewPagomensual)) {
                    Tarjeta oldTarjetaOfPagomensualListNewPagomensual = pagomensualListNewPagomensual.getTarjeta();
                    pagomensualListNewPagomensual.setTarjeta(tarjeta);
                    pagomensualListNewPagomensual = em.merge(pagomensualListNewPagomensual);
                    if (oldTarjetaOfPagomensualListNewPagomensual != null && !oldTarjetaOfPagomensualListNewPagomensual.equals(tarjeta)) {
                        oldTarjetaOfPagomensualListNewPagomensual.getPagomensualList().remove(pagomensualListNewPagomensual);
                        oldTarjetaOfPagomensualListNewPagomensual = em.merge(oldTarjetaOfPagomensualListNewPagomensual);
                    }
                }
            }
            for (Ingresotarjeta ingresotarjetaListOldIngresotarjeta : ingresotarjetaListOld) {
                if (!ingresotarjetaListNew.contains(ingresotarjetaListOldIngresotarjeta)) {
                    ingresotarjetaListOldIngresotarjeta.setTarjeta(null);
                    ingresotarjetaListOldIngresotarjeta = em.merge(ingresotarjetaListOldIngresotarjeta);
                }
            }
            for (Ingresotarjeta ingresotarjetaListNewIngresotarjeta : ingresotarjetaListNew) {
                if (!ingresotarjetaListOld.contains(ingresotarjetaListNewIngresotarjeta)) {
                    Tarjeta oldTarjetaOfIngresotarjetaListNewIngresotarjeta = ingresotarjetaListNewIngresotarjeta.getTarjeta();
                    ingresotarjetaListNewIngresotarjeta.setTarjeta(tarjeta);
                    ingresotarjetaListNewIngresotarjeta = em.merge(ingresotarjetaListNewIngresotarjeta);
                    if (oldTarjetaOfIngresotarjetaListNewIngresotarjeta != null && !oldTarjetaOfIngresotarjetaListNewIngresotarjeta.equals(tarjeta)) {
                        oldTarjetaOfIngresotarjetaListNewIngresotarjeta.getIngresotarjetaList().remove(ingresotarjetaListNewIngresotarjeta);
                        oldTarjetaOfIngresotarjetaListNewIngresotarjeta = em.merge(oldTarjetaOfIngresotarjetaListNewIngresotarjeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tarjeta.getRfid();
                if (findTarjeta(id) == null) {
                    throw new NonexistentEntityException("The tarjeta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjeta tarjeta;
            try {
                tarjeta = em.getReference(Tarjeta.class, id);
                tarjeta.getRfid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjeta with id " + id + " no longer exists.", enfe);
            }
            Cliente cliente = tarjeta.getCliente();
            if (cliente != null) {
                cliente.getTarjetaList().remove(tarjeta);
                cliente = em.merge(cliente);
            }
            Tipovehiculo tipovehiculo = tarjeta.getTipovehiculo();
            if (tipovehiculo != null) {
                tipovehiculo.getTarjetaList().remove(tarjeta);
                tipovehiculo = em.merge(tipovehiculo);
            }
            Usuario usuarioactivo = tarjeta.getUsuarioactivo();
            if (usuarioactivo != null) {
                usuarioactivo.getTarjetaList().remove(tarjeta);
                usuarioactivo = em.merge(usuarioactivo);
            }
            List<Pagomensual> pagomensualList = tarjeta.getPagomensualList();
            for (Pagomensual pagomensualListPagomensual : pagomensualList) {
                pagomensualListPagomensual.setTarjeta(null);
                pagomensualListPagomensual = em.merge(pagomensualListPagomensual);
            }
            List<Ingresotarjeta> ingresotarjetaList = tarjeta.getIngresotarjetaList();
            for (Ingresotarjeta ingresotarjetaListIngresotarjeta : ingresotarjetaList) {
                ingresotarjetaListIngresotarjeta.setTarjeta(null);
                ingresotarjetaListIngresotarjeta = em.merge(ingresotarjetaListIngresotarjeta);
            }
            em.remove(tarjeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarjeta> findTarjetaEntities() {
        return findTarjetaEntities(true, -1, -1);
    }

    public List<Tarjeta> findTarjetaEntities(int maxResults, int firstResult) {
        return findTarjetaEntities(false, maxResults, firstResult);
    }

    private List<Tarjeta> findTarjetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarjeta.class));
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

    public Tarjeta findTarjeta(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarjeta.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarjeta> rt = cq.from(Tarjeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
