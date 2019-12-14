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

public class TipoclienteJpaController implements Serializable {

    public TipoclienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipocliente tipocliente) {
        if (tipocliente.getClienteList() == null) {
            tipocliente.setClienteList(new ArrayList<Cliente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : tipocliente.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getId());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            tipocliente.setClienteList(attachedClienteList);
            em.persist(tipocliente);
            for (Cliente clienteListCliente : tipocliente.getClienteList()) {
                Tipocliente oldTipoclienteOfClienteListCliente = clienteListCliente.getTipocliente();
                clienteListCliente.setTipocliente(tipocliente);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldTipoclienteOfClienteListCliente != null) {
                    oldTipoclienteOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldTipoclienteOfClienteListCliente = em.merge(oldTipoclienteOfClienteListCliente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipocliente tipocliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipocliente persistentTipocliente = em.find(Tipocliente.class, tipocliente.getId());
            List<Cliente> clienteListOld = persistentTipocliente.getClienteList();
            List<Cliente> clienteListNew = tipocliente.getClienteList();
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getId());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            tipocliente.setClienteList(clienteListNew);
            tipocliente = em.merge(tipocliente);
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    clienteListOldCliente.setTipocliente(null);
                    clienteListOldCliente = em.merge(clienteListOldCliente);
                }
            }
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Tipocliente oldTipoclienteOfClienteListNewCliente = clienteListNewCliente.getTipocliente();
                    clienteListNewCliente.setTipocliente(tipocliente);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldTipoclienteOfClienteListNewCliente != null && !oldTipoclienteOfClienteListNewCliente.equals(tipocliente)) {
                        oldTipoclienteOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldTipoclienteOfClienteListNewCliente = em.merge(oldTipoclienteOfClienteListNewCliente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipocliente.getId();
                if (findTipocliente(id) == null) {
                    throw new NonexistentEntityException("The tipocliente with id " + id + " no longer exists.");
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
            Tipocliente tipocliente;
            try {
                tipocliente = em.getReference(Tipocliente.class, id);
                tipocliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipocliente with id " + id + " no longer exists.", enfe);
            }
            List<Cliente> clienteList = tipocliente.getClienteList();
            for (Cliente clienteListCliente : clienteList) {
                clienteListCliente.setTipocliente(null);
                clienteListCliente = em.merge(clienteListCliente);
            }
            em.remove(tipocliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipocliente> findTipoclienteEntities() {
        return findTipoclienteEntities(true, -1, -1);
    }

    public List<Tipocliente> findTipoclienteEntities(int maxResults, int firstResult) {
        return findTipoclienteEntities(false, maxResults, firstResult);
    }

    private List<Tipocliente> findTipoclienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipocliente.class));
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

    public Tipocliente findTipocliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipocliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoclienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipocliente> rt = cq.from(Tipocliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
