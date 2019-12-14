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

public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getRolList() == null) {
            usuario.setRolList(new ArrayList<Rol>());
        }
        if (usuario.getAccesocajaList() == null) {
            usuario.setAccesocajaList(new ArrayList<Accesocaja>());
        }
        if (usuario.getPagoList() == null) {
            usuario.setPagoList(new ArrayList<Pago>());
        }
        if (usuario.getClienteList() == null) {
            usuario.setClienteList(new ArrayList<Cliente>());
        }
        if (usuario.getTarjetaList() == null) {
            usuario.setTarjetaList(new ArrayList<Tarjeta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rol> attachedRolList = new ArrayList<Rol>();
            for (Rol rolListRolToAttach : usuario.getRolList()) {
                rolListRolToAttach = em.getReference(rolListRolToAttach.getClass(), rolListRolToAttach.getId());
                attachedRolList.add(rolListRolToAttach);
            }
            usuario.setRolList(attachedRolList);
            List<Accesocaja> attachedAccesocajaList = new ArrayList<Accesocaja>();
            for (Accesocaja accesocajaListAccesocajaToAttach : usuario.getAccesocajaList()) {
                accesocajaListAccesocajaToAttach = em.getReference(accesocajaListAccesocajaToAttach.getClass(), accesocajaListAccesocajaToAttach.getAccesocajaPK());
                attachedAccesocajaList.add(accesocajaListAccesocajaToAttach);
            }
            usuario.setAccesocajaList(attachedAccesocajaList);
            List<Pago> attachedPagoList = new ArrayList<Pago>();
            for (Pago pagoListPagoToAttach : usuario.getPagoList()) {
                pagoListPagoToAttach = em.getReference(pagoListPagoToAttach.getClass(), pagoListPagoToAttach.getId());
                attachedPagoList.add(pagoListPagoToAttach);
            }
            usuario.setPagoList(attachedPagoList);
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : usuario.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getId());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            usuario.setClienteList(attachedClienteList);
            List<Tarjeta> attachedTarjetaList = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaListTarjetaToAttach : usuario.getTarjetaList()) {
                tarjetaListTarjetaToAttach = em.getReference(tarjetaListTarjetaToAttach.getClass(), tarjetaListTarjetaToAttach.getRfid());
                attachedTarjetaList.add(tarjetaListTarjetaToAttach);
            }
            usuario.setTarjetaList(attachedTarjetaList);
            em.persist(usuario);
            for (Rol rolListRol : usuario.getRolList()) {
                rolListRol.getUsuarioList().add(usuario);
                rolListRol = em.merge(rolListRol);
            }
            for (Accesocaja accesocajaListAccesocaja : usuario.getAccesocajaList()) {
                Usuario oldUsuario1OfAccesocajaListAccesocaja = accesocajaListAccesocaja.getUsuario1();
                accesocajaListAccesocaja.setUsuario1(usuario);
                accesocajaListAccesocaja = em.merge(accesocajaListAccesocaja);
                if (oldUsuario1OfAccesocajaListAccesocaja != null) {
                    oldUsuario1OfAccesocajaListAccesocaja.getAccesocajaList().remove(accesocajaListAccesocaja);
                    oldUsuario1OfAccesocajaListAccesocaja = em.merge(oldUsuario1OfAccesocajaListAccesocaja);
                }
            }
            for (Pago pagoListPago : usuario.getPagoList()) {
                Usuario oldUsuarioOfPagoListPago = pagoListPago.getUsuario();
                pagoListPago.setUsuario(usuario);
                pagoListPago = em.merge(pagoListPago);
                if (oldUsuarioOfPagoListPago != null) {
                    oldUsuarioOfPagoListPago.getPagoList().remove(pagoListPago);
                    oldUsuarioOfPagoListPago = em.merge(oldUsuarioOfPagoListPago);
                }
            }
            for (Cliente clienteListCliente : usuario.getClienteList()) {
                Usuario oldUsuarioOfClienteListCliente = clienteListCliente.getUsuario();
                clienteListCliente.setUsuario(usuario);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldUsuarioOfClienteListCliente != null) {
                    oldUsuarioOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldUsuarioOfClienteListCliente = em.merge(oldUsuarioOfClienteListCliente);
                }
            }
            for (Tarjeta tarjetaListTarjeta : usuario.getTarjetaList()) {
                Usuario oldUsuarioactivoOfTarjetaListTarjeta = tarjetaListTarjeta.getUsuarioactivo();
                tarjetaListTarjeta.setUsuarioactivo(usuario);
                tarjetaListTarjeta = em.merge(tarjetaListTarjeta);
                if (oldUsuarioactivoOfTarjetaListTarjeta != null) {
                    oldUsuarioactivoOfTarjetaListTarjeta.getTarjetaList().remove(tarjetaListTarjeta);
                    oldUsuarioactivoOfTarjetaListTarjeta = em.merge(oldUsuarioactivoOfTarjetaListTarjeta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<Rol> rolListOld = persistentUsuario.getRolList();
            List<Rol> rolListNew = usuario.getRolList();
            List<Accesocaja> accesocajaListOld = persistentUsuario.getAccesocajaList();
            List<Accesocaja> accesocajaListNew = usuario.getAccesocajaList();
            List<Pago> pagoListOld = persistentUsuario.getPagoList();
            List<Pago> pagoListNew = usuario.getPagoList();
            List<Cliente> clienteListOld = persistentUsuario.getClienteList();
            List<Cliente> clienteListNew = usuario.getClienteList();
            List<Tarjeta> tarjetaListOld = persistentUsuario.getTarjetaList();
            List<Tarjeta> tarjetaListNew = usuario.getTarjetaList();
            List<String> illegalOrphanMessages = null;
            for (Accesocaja accesocajaListOldAccesocaja : accesocajaListOld) {
                if (!accesocajaListNew.contains(accesocajaListOldAccesocaja)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Accesocaja " + accesocajaListOldAccesocaja + " since its usuario1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Rol> attachedRolListNew = new ArrayList<Rol>();
            for (Rol rolListNewRolToAttach : rolListNew) {
                rolListNewRolToAttach = em.getReference(rolListNewRolToAttach.getClass(), rolListNewRolToAttach.getId());
                attachedRolListNew.add(rolListNewRolToAttach);
            }
            rolListNew = attachedRolListNew;
            usuario.setRolList(rolListNew);
            List<Accesocaja> attachedAccesocajaListNew = new ArrayList<Accesocaja>();
            for (Accesocaja accesocajaListNewAccesocajaToAttach : accesocajaListNew) {
                accesocajaListNewAccesocajaToAttach = em.getReference(accesocajaListNewAccesocajaToAttach.getClass(), accesocajaListNewAccesocajaToAttach.getAccesocajaPK());
                attachedAccesocajaListNew.add(accesocajaListNewAccesocajaToAttach);
            }
            accesocajaListNew = attachedAccesocajaListNew;
            usuario.setAccesocajaList(accesocajaListNew);
            List<Pago> attachedPagoListNew = new ArrayList<Pago>();
            for (Pago pagoListNewPagoToAttach : pagoListNew) {
                pagoListNewPagoToAttach = em.getReference(pagoListNewPagoToAttach.getClass(), pagoListNewPagoToAttach.getId());
                attachedPagoListNew.add(pagoListNewPagoToAttach);
            }
            pagoListNew = attachedPagoListNew;
            usuario.setPagoList(pagoListNew);
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getId());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            usuario.setClienteList(clienteListNew);
            List<Tarjeta> attachedTarjetaListNew = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaListNewTarjetaToAttach : tarjetaListNew) {
                tarjetaListNewTarjetaToAttach = em.getReference(tarjetaListNewTarjetaToAttach.getClass(), tarjetaListNewTarjetaToAttach.getRfid());
                attachedTarjetaListNew.add(tarjetaListNewTarjetaToAttach);
            }
            tarjetaListNew = attachedTarjetaListNew;
            usuario.setTarjetaList(tarjetaListNew);
            usuario = em.merge(usuario);
            for (Rol rolListOldRol : rolListOld) {
                if (!rolListNew.contains(rolListOldRol)) {
                    rolListOldRol.getUsuarioList().remove(usuario);
                    rolListOldRol = em.merge(rolListOldRol);
                }
            }
            for (Rol rolListNewRol : rolListNew) {
                if (!rolListOld.contains(rolListNewRol)) {
                    rolListNewRol.getUsuarioList().add(usuario);
                    rolListNewRol = em.merge(rolListNewRol);
                }
            }
            for (Accesocaja accesocajaListNewAccesocaja : accesocajaListNew) {
                if (!accesocajaListOld.contains(accesocajaListNewAccesocaja)) {
                    Usuario oldUsuario1OfAccesocajaListNewAccesocaja = accesocajaListNewAccesocaja.getUsuario1();
                    accesocajaListNewAccesocaja.setUsuario1(usuario);
                    accesocajaListNewAccesocaja = em.merge(accesocajaListNewAccesocaja);
                    if (oldUsuario1OfAccesocajaListNewAccesocaja != null && !oldUsuario1OfAccesocajaListNewAccesocaja.equals(usuario)) {
                        oldUsuario1OfAccesocajaListNewAccesocaja.getAccesocajaList().remove(accesocajaListNewAccesocaja);
                        oldUsuario1OfAccesocajaListNewAccesocaja = em.merge(oldUsuario1OfAccesocajaListNewAccesocaja);
                    }
                }
            }
            for (Pago pagoListOldPago : pagoListOld) {
                if (!pagoListNew.contains(pagoListOldPago)) {
                    pagoListOldPago.setUsuario(null);
                    pagoListOldPago = em.merge(pagoListOldPago);
                }
            }
            for (Pago pagoListNewPago : pagoListNew) {
                if (!pagoListOld.contains(pagoListNewPago)) {
                    Usuario oldUsuarioOfPagoListNewPago = pagoListNewPago.getUsuario();
                    pagoListNewPago.setUsuario(usuario);
                    pagoListNewPago = em.merge(pagoListNewPago);
                    if (oldUsuarioOfPagoListNewPago != null && !oldUsuarioOfPagoListNewPago.equals(usuario)) {
                        oldUsuarioOfPagoListNewPago.getPagoList().remove(pagoListNewPago);
                        oldUsuarioOfPagoListNewPago = em.merge(oldUsuarioOfPagoListNewPago);
                    }
                }
            }
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    clienteListOldCliente.setUsuario(null);
                    clienteListOldCliente = em.merge(clienteListOldCliente);
                }
            }
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Usuario oldUsuarioOfClienteListNewCliente = clienteListNewCliente.getUsuario();
                    clienteListNewCliente.setUsuario(usuario);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldUsuarioOfClienteListNewCliente != null && !oldUsuarioOfClienteListNewCliente.equals(usuario)) {
                        oldUsuarioOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldUsuarioOfClienteListNewCliente = em.merge(oldUsuarioOfClienteListNewCliente);
                    }
                }
            }
            for (Tarjeta tarjetaListOldTarjeta : tarjetaListOld) {
                if (!tarjetaListNew.contains(tarjetaListOldTarjeta)) {
                    tarjetaListOldTarjeta.setUsuarioactivo(null);
                    tarjetaListOldTarjeta = em.merge(tarjetaListOldTarjeta);
                }
            }
            for (Tarjeta tarjetaListNewTarjeta : tarjetaListNew) {
                if (!tarjetaListOld.contains(tarjetaListNewTarjeta)) {
                    Usuario oldUsuarioactivoOfTarjetaListNewTarjeta = tarjetaListNewTarjeta.getUsuarioactivo();
                    tarjetaListNewTarjeta.setUsuarioactivo(usuario);
                    tarjetaListNewTarjeta = em.merge(tarjetaListNewTarjeta);
                    if (oldUsuarioactivoOfTarjetaListNewTarjeta != null && !oldUsuarioactivoOfTarjetaListNewTarjeta.equals(usuario)) {
                        oldUsuarioactivoOfTarjetaListNewTarjeta.getTarjetaList().remove(tarjetaListNewTarjeta);
                        oldUsuarioactivoOfTarjetaListNewTarjeta = em.merge(oldUsuarioactivoOfTarjetaListNewTarjeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Accesocaja> accesocajaListOrphanCheck = usuario.getAccesocajaList();
            for (Accesocaja accesocajaListOrphanCheckAccesocaja : accesocajaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Accesocaja " + accesocajaListOrphanCheckAccesocaja + " in its accesocajaList field has a non-nullable usuario1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Rol> rolList = usuario.getRolList();
            for (Rol rolListRol : rolList) {
                rolListRol.getUsuarioList().remove(usuario);
                rolListRol = em.merge(rolListRol);
            }
            List<Pago> pagoList = usuario.getPagoList();
            for (Pago pagoListPago : pagoList) {
                pagoListPago.setUsuario(null);
                pagoListPago = em.merge(pagoListPago);
            }
            List<Cliente> clienteList = usuario.getClienteList();
            for (Cliente clienteListCliente : clienteList) {
                clienteListCliente.setUsuario(null);
                clienteListCliente = em.merge(clienteListCliente);
            }
            List<Tarjeta> tarjetaList = usuario.getTarjetaList();
            for (Tarjeta tarjetaListTarjeta : tarjetaList) {
                tarjetaListTarjeta.setUsuarioactivo(null);
                tarjetaListTarjeta = em.merge(tarjetaListTarjeta);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
