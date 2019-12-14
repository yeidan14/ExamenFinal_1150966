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

public class CajaJpaController implements Serializable {

    public CajaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Caja caja) {
        if (caja.getAccesocajaList() == null) {
            caja.setAccesocajaList(new ArrayList<Accesocaja>());
        }
        if (caja.getPagoList() == null) {
            caja.setPagoList(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Accesocaja> attachedAccesocajaList = new ArrayList<Accesocaja>();
            for (Accesocaja accesocajaListAccesocajaToAttach : caja.getAccesocajaList()) {
                accesocajaListAccesocajaToAttach = em.getReference(accesocajaListAccesocajaToAttach.getClass(), accesocajaListAccesocajaToAttach.getAccesocajaPK());
                attachedAccesocajaList.add(accesocajaListAccesocajaToAttach);
            }
            caja.setAccesocajaList(attachedAccesocajaList);
            List<Pago> attachedPagoList = new ArrayList<Pago>();
            for (Pago pagoListPagoToAttach : caja.getPagoList()) {
                pagoListPagoToAttach = em.getReference(pagoListPagoToAttach.getClass(), pagoListPagoToAttach.getId());
                attachedPagoList.add(pagoListPagoToAttach);
            }
            caja.setPagoList(attachedPagoList);
            em.persist(caja);
            for (Accesocaja accesocajaListAccesocaja : caja.getAccesocajaList()) {
                Caja oldCaja1OfAccesocajaListAccesocaja = accesocajaListAccesocaja.getCaja1();
                accesocajaListAccesocaja.setCaja1(caja);
                accesocajaListAccesocaja = em.merge(accesocajaListAccesocaja);
                if (oldCaja1OfAccesocajaListAccesocaja != null) {
                    oldCaja1OfAccesocajaListAccesocaja.getAccesocajaList().remove(accesocajaListAccesocaja);
                    oldCaja1OfAccesocajaListAccesocaja = em.merge(oldCaja1OfAccesocajaListAccesocaja);
                }
            }
            for (Pago pagoListPago : caja.getPagoList()) {
                Caja oldCajaOfPagoListPago = pagoListPago.getCaja();
                pagoListPago.setCaja(caja);
                pagoListPago = em.merge(pagoListPago);
                if (oldCajaOfPagoListPago != null) {
                    oldCajaOfPagoListPago.getPagoList().remove(pagoListPago);
                    oldCajaOfPagoListPago = em.merge(oldCajaOfPagoListPago);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Caja caja) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caja persistentCaja = em.find(Caja.class, caja.getId());
            List<Accesocaja> accesocajaListOld = persistentCaja.getAccesocajaList();
            List<Accesocaja> accesocajaListNew = caja.getAccesocajaList();
            List<Pago> pagoListOld = persistentCaja.getPagoList();
            List<Pago> pagoListNew = caja.getPagoList();
            List<String> illegalOrphanMessages = null;
            for (Accesocaja accesocajaListOldAccesocaja : accesocajaListOld) {
                if (!accesocajaListNew.contains(accesocajaListOldAccesocaja)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Accesocaja " + accesocajaListOldAccesocaja + " since its caja1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Accesocaja> attachedAccesocajaListNew = new ArrayList<Accesocaja>();
            for (Accesocaja accesocajaListNewAccesocajaToAttach : accesocajaListNew) {
                accesocajaListNewAccesocajaToAttach = em.getReference(accesocajaListNewAccesocajaToAttach.getClass(), accesocajaListNewAccesocajaToAttach.getAccesocajaPK());
                attachedAccesocajaListNew.add(accesocajaListNewAccesocajaToAttach);
            }
            accesocajaListNew = attachedAccesocajaListNew;
            caja.setAccesocajaList(accesocajaListNew);
            List<Pago> attachedPagoListNew = new ArrayList<Pago>();
            for (Pago pagoListNewPagoToAttach : pagoListNew) {
                pagoListNewPagoToAttach = em.getReference(pagoListNewPagoToAttach.getClass(), pagoListNewPagoToAttach.getId());
                attachedPagoListNew.add(pagoListNewPagoToAttach);
            }
            pagoListNew = attachedPagoListNew;
            caja.setPagoList(pagoListNew);
            caja = em.merge(caja);
            for (Accesocaja accesocajaListNewAccesocaja : accesocajaListNew) {
                if (!accesocajaListOld.contains(accesocajaListNewAccesocaja)) {
                    Caja oldCaja1OfAccesocajaListNewAccesocaja = accesocajaListNewAccesocaja.getCaja1();
                    accesocajaListNewAccesocaja.setCaja1(caja);
                    accesocajaListNewAccesocaja = em.merge(accesocajaListNewAccesocaja);
                    if (oldCaja1OfAccesocajaListNewAccesocaja != null && !oldCaja1OfAccesocajaListNewAccesocaja.equals(caja)) {
                        oldCaja1OfAccesocajaListNewAccesocaja.getAccesocajaList().remove(accesocajaListNewAccesocaja);
                        oldCaja1OfAccesocajaListNewAccesocaja = em.merge(oldCaja1OfAccesocajaListNewAccesocaja);
                    }
                }
            }
            for (Pago pagoListOldPago : pagoListOld) {
                if (!pagoListNew.contains(pagoListOldPago)) {
                    pagoListOldPago.setCaja(null);
                    pagoListOldPago = em.merge(pagoListOldPago);
                }
            }
            for (Pago pagoListNewPago : pagoListNew) {
                if (!pagoListOld.contains(pagoListNewPago)) {
                    Caja oldCajaOfPagoListNewPago = pagoListNewPago.getCaja();
                    pagoListNewPago.setCaja(caja);
                    pagoListNewPago = em.merge(pagoListNewPago);
                    if (oldCajaOfPagoListNewPago != null && !oldCajaOfPagoListNewPago.equals(caja)) {
                        oldCajaOfPagoListNewPago.getPagoList().remove(pagoListNewPago);
                        oldCajaOfPagoListNewPago = em.merge(oldCajaOfPagoListNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = caja.getId();
                if (findCaja(id) == null) {
                    throw new NonexistentEntityException("The caja with id " + id + " no longer exists.");
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
            Caja caja;
            try {
                caja = em.getReference(Caja.class, id);
                caja.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The caja with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Accesocaja> accesocajaListOrphanCheck = caja.getAccesocajaList();
            for (Accesocaja accesocajaListOrphanCheckAccesocaja : accesocajaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Caja (" + caja + ") cannot be destroyed since the Accesocaja " + accesocajaListOrphanCheckAccesocaja + " in its accesocajaList field has a non-nullable caja1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Pago> pagoList = caja.getPagoList();
            for (Pago pagoListPago : pagoList) {
                pagoListPago.setCaja(null);
                pagoListPago = em.merge(pagoListPago);
            }
            em.remove(caja);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Caja> findCajaEntities() {
        return findCajaEntities(true, -1, -1);
    }

    public List<Caja> findCajaEntities(int maxResults, int firstResult) {
        return findCajaEntities(false, maxResults, firstResult);
    }

    private List<Caja> findCajaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Caja.class));
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

    public Caja findCaja(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Caja.class, id);
        } finally {
            em.close();
        }
    }

    public int getCajaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Caja> rt = cq.from(Caja.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
