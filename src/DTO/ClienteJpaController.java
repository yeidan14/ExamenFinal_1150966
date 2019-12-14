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

public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getTarjetaList() == null) {
            cliente.setTarjetaList(new ArrayList<Tarjeta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipocliente tipocliente = cliente.getTipocliente();
            if (tipocliente != null) {
                tipocliente = em.getReference(tipocliente.getClass(), tipocliente.getId());
                cliente.setTipocliente(tipocliente);
            }
            Usuario usuario = cliente.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getId());
                cliente.setUsuario(usuario);
            }
            List<Tarjeta> attachedTarjetaList = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaListTarjetaToAttach : cliente.getTarjetaList()) {
                tarjetaListTarjetaToAttach = em.getReference(tarjetaListTarjetaToAttach.getClass(), tarjetaListTarjetaToAttach.getRfid());
                attachedTarjetaList.add(tarjetaListTarjetaToAttach);
            }
            cliente.setTarjetaList(attachedTarjetaList);
            em.persist(cliente);
            if (tipocliente != null) {
                tipocliente.getClienteList().add(cliente);
                tipocliente = em.merge(tipocliente);
            }
            if (usuario != null) {
                usuario.getClienteList().add(cliente);
                usuario = em.merge(usuario);
            }
            for (Tarjeta tarjetaListTarjeta : cliente.getTarjetaList()) {
                Cliente oldClienteOfTarjetaListTarjeta = tarjetaListTarjeta.getCliente();
                tarjetaListTarjeta.setCliente(cliente);
                tarjetaListTarjeta = em.merge(tarjetaListTarjeta);
                if (oldClienteOfTarjetaListTarjeta != null) {
                    oldClienteOfTarjetaListTarjeta.getTarjetaList().remove(tarjetaListTarjeta);
                    oldClienteOfTarjetaListTarjeta = em.merge(oldClienteOfTarjetaListTarjeta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getId());
            Tipocliente tipoclienteOld = persistentCliente.getTipocliente();
            Tipocliente tipoclienteNew = cliente.getTipocliente();
            Usuario usuarioOld = persistentCliente.getUsuario();
            Usuario usuarioNew = cliente.getUsuario();
            List<Tarjeta> tarjetaListOld = persistentCliente.getTarjetaList();
            List<Tarjeta> tarjetaListNew = cliente.getTarjetaList();
            if (tipoclienteNew != null) {
                tipoclienteNew = em.getReference(tipoclienteNew.getClass(), tipoclienteNew.getId());
                cliente.setTipocliente(tipoclienteNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getId());
                cliente.setUsuario(usuarioNew);
            }
            List<Tarjeta> attachedTarjetaListNew = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaListNewTarjetaToAttach : tarjetaListNew) {
                tarjetaListNewTarjetaToAttach = em.getReference(tarjetaListNewTarjetaToAttach.getClass(), tarjetaListNewTarjetaToAttach.getRfid());
                attachedTarjetaListNew.add(tarjetaListNewTarjetaToAttach);
            }
            tarjetaListNew = attachedTarjetaListNew;
            cliente.setTarjetaList(tarjetaListNew);
            cliente = em.merge(cliente);
            if (tipoclienteOld != null && !tipoclienteOld.equals(tipoclienteNew)) {
                tipoclienteOld.getClienteList().remove(cliente);
                tipoclienteOld = em.merge(tipoclienteOld);
            }
            if (tipoclienteNew != null && !tipoclienteNew.equals(tipoclienteOld)) {
                tipoclienteNew.getClienteList().add(cliente);
                tipoclienteNew = em.merge(tipoclienteNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getClienteList().remove(cliente);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getClienteList().add(cliente);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Tarjeta tarjetaListOldTarjeta : tarjetaListOld) {
                if (!tarjetaListNew.contains(tarjetaListOldTarjeta)) {
                    tarjetaListOldTarjeta.setCliente(null);
                    tarjetaListOldTarjeta = em.merge(tarjetaListOldTarjeta);
                }
            }
            for (Tarjeta tarjetaListNewTarjeta : tarjetaListNew) {
                if (!tarjetaListOld.contains(tarjetaListNewTarjeta)) {
                    Cliente oldClienteOfTarjetaListNewTarjeta = tarjetaListNewTarjeta.getCliente();
                    tarjetaListNewTarjeta.setCliente(cliente);
                    tarjetaListNewTarjeta = em.merge(tarjetaListNewTarjeta);
                    if (oldClienteOfTarjetaListNewTarjeta != null && !oldClienteOfTarjetaListNewTarjeta.equals(cliente)) {
                        oldClienteOfTarjetaListNewTarjeta.getTarjetaList().remove(tarjetaListNewTarjeta);
                        oldClienteOfTarjetaListNewTarjeta = em.merge(oldClienteOfTarjetaListNewTarjeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            Tipocliente tipocliente = cliente.getTipocliente();
            if (tipocliente != null) {
                tipocliente.getClienteList().remove(cliente);
                tipocliente = em.merge(tipocliente);
            }
            Usuario usuario = cliente.getUsuario();
            if (usuario != null) {
                usuario.getClienteList().remove(cliente);
                usuario = em.merge(usuario);
            }
            List<Tarjeta> tarjetaList = cliente.getTarjetaList();
            for (Tarjeta tarjetaListTarjeta : tarjetaList) {
                tarjetaListTarjeta.setCliente(null);
                tarjetaListTarjeta = em.merge(tarjetaListTarjeta);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
