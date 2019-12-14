/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import DTO.Exepcion.*;
import DAO.Accesocaja;
import DAO.*; 
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alexander
 */
public class AccesocajaJpaController implements Serializable {

    public AccesocajaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Accesocaja accesocaja) throws PreexistingEntityException, Exception {
        if (accesocaja.getAccesocajaPK() == null) {
            accesocaja.setAccesocajaPK(new AccesocajaPK());
        }
        accesocaja.getAccesocajaPK().setUsuario(accesocaja.getUsuario1().getId());
        accesocaja.getAccesocajaPK().setCaja(accesocaja.getCaja1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caja caja1 = accesocaja.getCaja1();
            if (caja1 != null) {
                caja1 = em.getReference(caja1.getClass(), caja1.getId());
                accesocaja.setCaja1(caja1);
            }
            Usuario usuario1 = accesocaja.getUsuario1();
            if (usuario1 != null) {
                usuario1 = em.getReference(usuario1.getClass(), usuario1.getId());
                accesocaja.setUsuario1(usuario1);
            }
            em.persist(accesocaja);
            if (caja1 != null) {
                caja1.getAccesocajaList().add(accesocaja);
                caja1 = em.merge(caja1);
            }
            if (usuario1 != null) {
                usuario1.getAccesocajaList().add(accesocaja);
                usuario1 = em.merge(usuario1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAccesocaja(accesocaja.getAccesocajaPK()) != null) {
                throw new PreexistingEntityException("Accesocaja " + accesocaja + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Accesocaja accesocaja) throws NonexistentEntityException, Exception {
        accesocaja.getAccesocajaPK().setUsuario(accesocaja.getUsuario1().getId());
        accesocaja.getAccesocajaPK().setCaja(accesocaja.getCaja1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Accesocaja persistentAccesocaja = em.find(Accesocaja.class, accesocaja.getAccesocajaPK());
            Caja caja1Old = persistentAccesocaja.getCaja1();
            Caja caja1New = accesocaja.getCaja1();
            Usuario usuario1Old = persistentAccesocaja.getUsuario1();
            Usuario usuario1New = accesocaja.getUsuario1();
            if (caja1New != null) {
                caja1New = em.getReference(caja1New.getClass(), caja1New.getId());
                accesocaja.setCaja1(caja1New);
            }
            if (usuario1New != null) {
                usuario1New = em.getReference(usuario1New.getClass(), usuario1New.getId());
                accesocaja.setUsuario1(usuario1New);
            }
            accesocaja = em.merge(accesocaja);
            if (caja1Old != null && !caja1Old.equals(caja1New)) {
                caja1Old.getAccesocajaList().remove(accesocaja);
                caja1Old = em.merge(caja1Old);
            }
            if (caja1New != null && !caja1New.equals(caja1Old)) {
                caja1New.getAccesocajaList().add(accesocaja);
                caja1New = em.merge(caja1New);
            }
            if (usuario1Old != null && !usuario1Old.equals(usuario1New)) {
                usuario1Old.getAccesocajaList().remove(accesocaja);
                usuario1Old = em.merge(usuario1Old);
            }
            if (usuario1New != null && !usuario1New.equals(usuario1Old)) {
                usuario1New.getAccesocajaList().add(accesocaja);
                usuario1New = em.merge(usuario1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AccesocajaPK id = accesocaja.getAccesocajaPK();
                if (findAccesocaja(id) == null) {
                    throw new NonexistentEntityException("The accesocaja with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AccesocajaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Accesocaja accesocaja;
            try {
                accesocaja = em.getReference(Accesocaja.class, id);
                accesocaja.getAccesocajaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accesocaja with id " + id + " no longer exists.", enfe);
            }
            Caja caja1 = accesocaja.getCaja1();
            if (caja1 != null) {
                caja1.getAccesocajaList().remove(accesocaja);
                caja1 = em.merge(caja1);
            }
            Usuario usuario1 = accesocaja.getUsuario1();
            if (usuario1 != null) {
                usuario1.getAccesocajaList().remove(accesocaja);
                usuario1 = em.merge(usuario1);
            }
            em.remove(accesocaja);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Accesocaja> findAccesocajaEntities() {
        return findAccesocajaEntities(true, -1, -1);
    }

    public List<Accesocaja> findAccesocajaEntities(int maxResults, int firstResult) {
        return findAccesocajaEntities(false, maxResults, firstResult);
    }

    private List<Accesocaja> findAccesocajaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Accesocaja.class));
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

    public Accesocaja findAccesocaja(AccesocajaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Accesocaja.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccesocajaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Accesocaja> rt = cq.from(Accesocaja.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
