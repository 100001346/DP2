/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import control.exceptions.IllegalOrphanException;
import control.exceptions.NonexistentEntityException;
import control.exceptions.PreexistingEntityException;
import entity.Inventory;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ccjsh
 */
public class InventoryJpaController implements Serializable
{

    public InventoryJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Inventory inventory) throws IllegalOrphanException, PreexistingEntityException, Exception
    {
        List<String> illegalOrphanMessages = null;
        Product productOrphanCheck = inventory.getProduct();
        if (productOrphanCheck != null)
        {
            Inventory oldInventoryOfProduct = productOrphanCheck.getInventory();
            if (oldInventoryOfProduct != null)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Product " + productOrphanCheck + " already has an item of type Inventory whose product column cannot be null. Please make another selection for the product field.");
            }
        }
        if (illegalOrphanMessages != null)
        {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Product product = inventory.getProduct();
            if (product != null)
            {
                product = em.getReference(product.getClass(), product.getProdId());
                inventory.setProduct(product);
            }
            em.persist(inventory);
            if (product != null)
            {
                product.setInventory(inventory);
                product = em.merge(product);
            }
            em.getTransaction().commit();
        } catch (Exception ex)
        {
            if (findInventory(inventory.getProdId()) != null)
            {
                throw new PreexistingEntityException("Inventory " + inventory + " already exists.", ex);
            }
            throw ex;
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public void edit(Inventory inventory) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Inventory persistentInventory = em.find(Inventory.class, inventory.getProdId());
            Product productOld = persistentInventory.getProduct();
            Product productNew = inventory.getProduct();
            List<String> illegalOrphanMessages = null;
            if (productNew != null && !productNew.equals(productOld))
            {
                Inventory oldInventoryOfProduct = productNew.getInventory();
                if (oldInventoryOfProduct != null)
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Product " + productNew + " already has an item of type Inventory whose product column cannot be null. Please make another selection for the product field.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (productNew != null)
            {
                productNew = em.getReference(productNew.getClass(), productNew.getProdId());
                inventory.setProduct(productNew);
            }
            inventory = em.merge(inventory);
            if (productOld != null && !productOld.equals(productNew))
            {
                productOld.setInventory(null);
                productOld = em.merge(productOld);
            }
            if (productNew != null && !productNew.equals(productOld))
            {
                productNew.setInventory(inventory);
                productNew = em.merge(productNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = inventory.getProdId();
                if (findInventory(id) == null)
                {
                    throw new NonexistentEntityException("The inventory with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Inventory inventory;
            try
            {
                inventory = em.getReference(Inventory.class, id);
                inventory.getProdId();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The inventory with id " + id + " no longer exists.", enfe);
            }
            Product product = inventory.getProduct();
            if (product != null)
            {
                product.setInventory(null);
                product = em.merge(product);
            }
            em.remove(inventory);
            em.getTransaction().commit();
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public List<Inventory> findInventoryEntities()
    {
        return findInventoryEntities(true, -1, -1);
    }

    public List<Inventory> findInventoryEntities(int maxResults, int firstResult)
    {
        return findInventoryEntities(false, maxResults, firstResult);
    }

    private List<Inventory> findInventoryEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Inventory.class));
            Query q = em.createQuery(cq);
            if (!all)
            {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally
        {
            em.close();
        }
    }
    
    public List<Inventory> findLowStock()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM Inventory i WHERE i.invQty <= i.invLow");
        
        return query.getResultList();
    }
    
    public List<Inventory> findWarningStock()
    {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT i FROM Inventory i WHERE i.invQty > i.invLow AND i.invQty <= i.invOrder");
        
        return query.getResultList();
    }

    public Inventory findInventory(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Inventory.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getInventoryCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Inventory> rt = cq.from(Inventory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}
