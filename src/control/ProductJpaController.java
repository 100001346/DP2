/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import control.exceptions.IllegalOrphanException;
import control.exceptions.NonexistentEntityException;
import control.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Inventory;
import entity.Product;
import entity.Sale;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ccjsh
 */
public class ProductJpaController implements Serializable
{

    public ProductJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Product product) throws PreexistingEntityException, Exception
    {
        if (product.getSaleCollection() == null)
        {
            product.setSaleCollection(new ArrayList<Sale>());
        }
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Inventory inventory = product.getInventory();
            if (inventory != null)
            {
                inventory = em.getReference(inventory.getClass(), inventory.getProdId());
                product.setInventory(inventory);
            }
            Collection<Sale> attachedSaleCollection = new ArrayList<Sale>();
            for (Sale saleCollectionSaleToAttach : product.getSaleCollection())
            {
                saleCollectionSaleToAttach = em.getReference(saleCollectionSaleToAttach.getClass(), saleCollectionSaleToAttach.getSalePK());
                attachedSaleCollection.add(saleCollectionSaleToAttach);
            }
            product.setSaleCollection(attachedSaleCollection);
            em.persist(product);
            if (inventory != null)
            {
                Product oldProductOfInventory = inventory.getProduct();
                if (oldProductOfInventory != null)
                {
                    oldProductOfInventory.setInventory(null);
                    oldProductOfInventory = em.merge(oldProductOfInventory);
                }
                inventory.setProduct(product);
                inventory = em.merge(inventory);
            }
            for (Sale saleCollectionSale : product.getSaleCollection())
            {
                Product oldProductOfSaleCollectionSale = saleCollectionSale.getProduct();
                saleCollectionSale.setProduct(product);
                saleCollectionSale = em.merge(saleCollectionSale);
                if (oldProductOfSaleCollectionSale != null)
                {
                    oldProductOfSaleCollectionSale.getSaleCollection().remove(saleCollectionSale);
                    oldProductOfSaleCollectionSale = em.merge(oldProductOfSaleCollectionSale);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex)
        {
            if (findProduct(product.getProdId()) != null)
            {
                throw new PreexistingEntityException("Product " + product + " already exists.", ex);
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

    public void edit(Product product) throws IllegalOrphanException, NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Product persistentProduct = em.find(Product.class, product.getProdId());
            Inventory inventoryOld = persistentProduct.getInventory();
            Inventory inventoryNew = product.getInventory();
            Collection<Sale> saleCollectionOld = persistentProduct.getSaleCollection();
            Collection<Sale> saleCollectionNew = product.getSaleCollection();
            List<String> illegalOrphanMessages = null;
            if (inventoryOld != null && !inventoryOld.equals(inventoryNew))
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Inventory " + inventoryOld + " since its product field is not nullable.");
            }
            for (Sale saleCollectionOldSale : saleCollectionOld)
            {
                if (!saleCollectionNew.contains(saleCollectionOldSale))
                {
                    if (illegalOrphanMessages == null)
                    {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sale " + saleCollectionOldSale + " since its product field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (inventoryNew != null)
            {
                inventoryNew = em.getReference(inventoryNew.getClass(), inventoryNew.getProdId());
                product.setInventory(inventoryNew);
            }
            Collection<Sale> attachedSaleCollectionNew = new ArrayList<Sale>();
            for (Sale saleCollectionNewSaleToAttach : saleCollectionNew)
            {
                saleCollectionNewSaleToAttach = em.getReference(saleCollectionNewSaleToAttach.getClass(), saleCollectionNewSaleToAttach.getSalePK());
                attachedSaleCollectionNew.add(saleCollectionNewSaleToAttach);
            }
            saleCollectionNew = attachedSaleCollectionNew;
            product.setSaleCollection(saleCollectionNew);
            product = em.merge(product);
            if (inventoryNew != null && !inventoryNew.equals(inventoryOld))
            {
                Product oldProductOfInventory = inventoryNew.getProduct();
                if (oldProductOfInventory != null)
                {
                    oldProductOfInventory.setInventory(null);
                    oldProductOfInventory = em.merge(oldProductOfInventory);
                }
                inventoryNew.setProduct(product);
                inventoryNew = em.merge(inventoryNew);
            }
            for (Sale saleCollectionNewSale : saleCollectionNew)
            {
                if (!saleCollectionOld.contains(saleCollectionNewSale))
                {
                    Product oldProductOfSaleCollectionNewSale = saleCollectionNewSale.getProduct();
                    saleCollectionNewSale.setProduct(product);
                    saleCollectionNewSale = em.merge(saleCollectionNewSale);
                    if (oldProductOfSaleCollectionNewSale != null && !oldProductOfSaleCollectionNewSale.equals(product))
                    {
                        oldProductOfSaleCollectionNewSale.getSaleCollection().remove(saleCollectionNewSale);
                        oldProductOfSaleCollectionNewSale = em.merge(oldProductOfSaleCollectionNewSale);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = product.getProdId();
                if (findProduct(id) == null)
                {
                    throw new NonexistentEntityException("The product with id " + id + " no longer exists.");
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Product product;
            try
            {
                product = em.getReference(Product.class, id);
                product.getProdId();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The product with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Inventory inventoryOrphanCheck = product.getInventory();
            if (inventoryOrphanCheck != null)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Product (" + product + ") cannot be destroyed since the Inventory " + inventoryOrphanCheck + " in its inventory field has a non-nullable product field.");
            }
            Collection<Sale> saleCollectionOrphanCheck = product.getSaleCollection();
            for (Sale saleCollectionOrphanCheckSale : saleCollectionOrphanCheck)
            {
                if (illegalOrphanMessages == null)
                {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Product (" + product + ") cannot be destroyed since the Sale " + saleCollectionOrphanCheckSale + " in its saleCollection field has a non-nullable product field.");
            }
            if (illegalOrphanMessages != null)
            {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(product);
            em.getTransaction().commit();
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public List<Product> findProductEntities()
    {
        return findProductEntities(true, -1, -1);
    }

    public List<Product> findProductEntities(int maxResults, int firstResult)
    {
        return findProductEntities(false, maxResults, firstResult);
    }

    private List<Product> findProductEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Product.class));
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

    public Product findProduct(Integer id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Product.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getProductCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Product> rt = cq.from(Product.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}
