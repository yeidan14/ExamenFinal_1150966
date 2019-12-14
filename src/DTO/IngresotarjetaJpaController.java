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

public class IngresotarjetaJpaController implements Serializable {

    public IngresotarjetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ingresotarjeta ingresotarjeta) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Ingreso ingresoOrphanCheck = ingresotarjeta.getIngreso();
        if (ingresoOrphanCheck != null) {
            Ingresotarjeta oldIngresotarjetaOfIngreso = ingresoOrphanCheck.getIngresotarjeta();
            if (oldIngresotarjetaOfIngreso != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Ingreso " + ingresoOrphanCheck + " already has an item of type Ingresotarjeta whose ingreso column cannot be null. Please make another selection for the ingreso field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingreso ingreso = ingresotarjeta.getIngreso();
            if (ingreso != null) {
                ingreso = em.getReference(ingreso.getClass(), ingreso.getId());
                ingresotarjeta.setIngreso(ingreso);
            }
            Tarjeta tarjeta = ingresotarjeta.getTarjeta();
            if (tarjeta != null) {
                tarjeta = em.getReference(tarjeta.getClass(), tarjeta.getRfid());
                ingresotarjeta.setTarjeta(tarjeta);
            }
            em.persist(ingresotarjeta);
            if (ingreso != null) {
                ingreso.setIngresotarjeta(ingresotarjeta);
                ingreso = em.merge(ingreso);
            }
            if (tarjeta != null) {
                tarjeta.getIngresotarjetaList().add(ingresotarjeta);
                tarjeta = em.merge(tarjeta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIngresotarjeta(ingresotarjeta.getId()) != null) {
                throw new PreexistingEntityException("Ingresotarjeta " + ingresotarjeta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ingresotarjeta ingresotarjeta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingresotarjeta persistentIngresotarjeta = em.find(Ingresotarjeta.class, ingresotarjeta.getId());
            Ingreso ingresoOld = persistentIngresotarjeta.getIngreso();
            Ingreso ingresoNew = ingresotarjeta.getIngreso();
            Tarjeta tarjetaOld = persistentIngresotarjeta.getTarjeta();
            Tarjeta tarjetaNew = ingresotarjeta.getTarjeta();
            List<String> illegalOrphanMessages = null;
            if (ingresoNew != null && !ingresoNew.equals(ingresoOld)) {
                Ingresotarjeta oldIngresotarjetaOfIngreso = ingresoNew.getIngresotarjeta();
                if (oldIngresotarjetaOfIngreso != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Ingreso " + ingresoNew + " already has an item of type Ingresotarjeta whose ingreso column cannot be null. Please make another selection for the ingreso field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (ingresoNew != null) {
                ingresoNew = em.getReference(ingresoNew.getClass(), ingresoNew.getId());
                ingresotarjeta.setIngreso(ingresoNew);
            }
            if (tarjetaNew != null) {
                tarjetaNew = em.getReference(tarjetaNew.getClass(), tarjetaNew.getRfid());
                ingresotarjeta.setTarjeta(tarjetaNew);
            }
            ingresotarjeta = em.merge(ingresotarjeta);
            if (ingresoOld != null && !ingresoOld.equals(ingresoNew)) {
                ingresoOld.setIngresotarjeta(null);
                ingresoOld = em.merge(ingresoOld);
            }
            if (ingresoNew != null && !ingresoNew.equals(ingresoOld)) {
                ingresoNew.setIngresotarjeta(ingresotarjeta);
                ingresoNew = em.merge(ingresoNew);
            }
            if (tarjetaOld != null && !tarjetaOld.equals(tarjetaNew)) {
                tarjetaOld.getIngresotarjetaList().remove(ingresotarjeta);
                tarjetaOld = em.merge(tarjetaOld);
            }
            if (tarjetaNew != null && !tarjetaNew.equals(tarjetaOld)) {
                tarjetaNew.getIngresotarjetaList().add(ingresotarjeta);
                tarjetaNew = em.merge(tarjetaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ingresotarjeta.getId();
                if (findIngresotarjeta(id) == null) {
                    throw new NonexistentEntityException("The ingresotarjeta with id " + id + " no longer exists.");
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
            Ingresotarjeta ingresotarjeta;
            try {
                ingresotarjeta = em.getReference(Ingresotarjeta.class, id);
                ingresotarjeta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ingresotarjeta with id " + id + " no longer exists.", enfe);
            }
            Ingreso ingreso = ingresotarjeta.getIngreso();
            if (ingreso != null) {
                ingreso.setIngresotarjeta(null);
                ingreso = em.merge(ingreso);
            }
            Tarjeta tarjeta = ingresotarjeta.getTarjeta();
            if (tarjeta != null) {
                tarjeta.getIngresotarjetaList().remove(ingresotarjeta);
                tarjeta = em.merge(tarjeta);
            }
            em.remove(ingresotarjeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ingresotarjeta> findIngresotarjetaEntities() {
        return findIngresotarjetaEntities(true, -1, -1);
    }

    public List<Ingresotarjeta> findIngresotarjetaEntities(int maxResults, int firstResult) {
        return findIngresotarjetaEntities(false, maxResults, firstResult);
    }

    private List<Ingresotarjeta> findIngresotarjetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ingresotarjeta.class));
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

    public Ingresotarjeta findIngresotarjeta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ingresotarjeta.class, id);
        } finally {
            em.close();
        }
    }

    public int getIngresotarjetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ingresotarjeta> rt = cq.from(Ingresotarjeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
