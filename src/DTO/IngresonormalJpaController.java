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

public class IngresonormalJpaController implements Serializable {

    public IngresonormalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ingresonormal ingresonormal) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Ingreso ingresoOrphanCheck = ingresonormal.getIngreso();
        if (ingresoOrphanCheck != null) {
            Ingresonormal oldIngresonormalOfIngreso = ingresoOrphanCheck.getIngresonormal();
            if (oldIngresonormalOfIngreso != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Ingreso " + ingresoOrphanCheck + " already has an item of type Ingresonormal whose ingreso column cannot be null. Please make another selection for the ingreso field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pagoservicio pagoservicio = ingresonormal.getPagoservicio();
            if (pagoservicio != null) {
                pagoservicio = em.getReference(pagoservicio.getClass(), pagoservicio.getId());
                ingresonormal.setPagoservicio(pagoservicio);
            }
            Ingreso ingreso = ingresonormal.getIngreso();
            if (ingreso != null) {
                ingreso = em.getReference(ingreso.getClass(), ingreso.getId());
                ingresonormal.setIngreso(ingreso);
            }
            em.persist(ingresonormal);
            if (pagoservicio != null) {
                Ingresonormal oldIngresonormalOfPagoservicio = pagoservicio.getIngresonormal();
                if (oldIngresonormalOfPagoservicio != null) {
                    oldIngresonormalOfPagoservicio.setPagoservicio(null);
                    oldIngresonormalOfPagoservicio = em.merge(oldIngresonormalOfPagoservicio);
                }
                pagoservicio.setIngresonormal(ingresonormal);
                pagoservicio = em.merge(pagoservicio);
            }
            if (ingreso != null) {
                ingreso.setIngresonormal(ingresonormal);
                ingreso = em.merge(ingreso);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIngresonormal(ingresonormal.getId()) != null) {
                throw new PreexistingEntityException("Ingresonormal " + ingresonormal + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ingresonormal ingresonormal) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingresonormal persistentIngresonormal = em.find(Ingresonormal.class, ingresonormal.getId());
            Pagoservicio pagoservicioOld = persistentIngresonormal.getPagoservicio();
            Pagoservicio pagoservicioNew = ingresonormal.getPagoservicio();
            Ingreso ingresoOld = persistentIngresonormal.getIngreso();
            Ingreso ingresoNew = ingresonormal.getIngreso();
            List<String> illegalOrphanMessages = null;
            if (pagoservicioOld != null && !pagoservicioOld.equals(pagoservicioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Pagoservicio " + pagoservicioOld + " since its ingresonormal field is not nullable.");
            }
            if (ingresoNew != null && !ingresoNew.equals(ingresoOld)) {
                Ingresonormal oldIngresonormalOfIngreso = ingresoNew.getIngresonormal();
                if (oldIngresonormalOfIngreso != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Ingreso " + ingresoNew + " already has an item of type Ingresonormal whose ingreso column cannot be null. Please make another selection for the ingreso field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pagoservicioNew != null) {
                pagoservicioNew = em.getReference(pagoservicioNew.getClass(), pagoservicioNew.getId());
                ingresonormal.setPagoservicio(pagoservicioNew);
            }
            if (ingresoNew != null) {
                ingresoNew = em.getReference(ingresoNew.getClass(), ingresoNew.getId());
                ingresonormal.setIngreso(ingresoNew);
            }
            ingresonormal = em.merge(ingresonormal);
            if (pagoservicioNew != null && !pagoservicioNew.equals(pagoservicioOld)) {
                Ingresonormal oldIngresonormalOfPagoservicio = pagoservicioNew.getIngresonormal();
                if (oldIngresonormalOfPagoservicio != null) {
                    oldIngresonormalOfPagoservicio.setPagoservicio(null);
                    oldIngresonormalOfPagoservicio = em.merge(oldIngresonormalOfPagoservicio);
                }
                pagoservicioNew.setIngresonormal(ingresonormal);
                pagoservicioNew = em.merge(pagoservicioNew);
            }
            if (ingresoOld != null && !ingresoOld.equals(ingresoNew)) {
                ingresoOld.setIngresonormal(null);
                ingresoOld = em.merge(ingresoOld);
            }
            if (ingresoNew != null && !ingresoNew.equals(ingresoOld)) {
                ingresoNew.setIngresonormal(ingresonormal);
                ingresoNew = em.merge(ingresoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ingresonormal.getId();
                if (findIngresonormal(id) == null) {
                    throw new NonexistentEntityException("The ingresonormal with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ingresonormal ingresonormal;
            try {
                ingresonormal = em.getReference(Ingresonormal.class, id);
                ingresonormal.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ingresonormal with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Pagoservicio pagoservicioOrphanCheck = ingresonormal.getPagoservicio();
            if (pagoservicioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ingresonormal (" + ingresonormal + ") cannot be destroyed since the Pagoservicio " + pagoservicioOrphanCheck + " in its pagoservicio field has a non-nullable ingresonormal field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Ingreso ingreso = ingresonormal.getIngreso();
            if (ingreso != null) {
                ingreso.setIngresonormal(null);
                ingreso = em.merge(ingreso);
            }
            em.remove(ingresonormal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ingresonormal> findIngresonormalEntities() {
        return findIngresonormalEntities(true, -1, -1);
    }

    public List<Ingresonormal> findIngresonormalEntities(int maxResults, int firstResult) {
        return findIngresonormalEntities(false, maxResults, firstResult);
    }

    private List<Ingresonormal> findIngresonormalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ingresonormal.class));
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

    public Ingresonormal findIngresonormal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ingresonormal.class, id);
        } finally {
            em.close();
        }
    }

    public int getIngresonormalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ingresonormal> rt = cq.from(Ingresonormal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
