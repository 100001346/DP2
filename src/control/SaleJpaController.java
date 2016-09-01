/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import control.exceptions.NonexistentEntityException;
import control.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Product;
import entity.Sale;
import entity.SalePK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.xml.transform.OutputKeys;

/**
 *
 * @author ccjsh
 */
public class SaleJpaController implements Serializable
{

    public SaleJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(Sale sale) throws PreexistingEntityException, Exception
    {
        if (sale.getSalePK() == null)
        {
            sale.setSalePK(new SalePK());
        }
//        sale.getSalePK().setProdId(sale.getProduct().getProdId());
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Product product = sale.getProduct();
            if (product != null)
            {
                product = em.getReference(product.getClass(), product.getProdId());
                sale.setProduct(product);
            }
            em.persist(sale);
            if (product != null)
            {
                product.getSaleCollection().add(sale);
                product = em.merge(product);
            }
            em.getTransaction().commit();
        } catch (Exception ex)
        {
            if (findSale(sale.getSalePK()) != null)
            {
                throw new PreexistingEntityException("Sale " + sale + " already exists.", ex);
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

    public void edit(Sale sale) throws NonexistentEntityException, Exception
    {
        sale.getSalePK().setProdId(sale.getProduct().getProdId());
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Sale persistentSale = em.find(Sale.class, sale.getSalePK());
            Product productOld = persistentSale.getProduct();
            Product productNew = sale.getProduct();
            if (productNew != null)
            {
                productNew = em.getReference(productNew.getClass(), productNew.getProdId());
                sale.setProduct(productNew);
            }
            sale = em.merge(sale);
            if (productOld != null && !productOld.equals(productNew))
            {
                productOld.getSaleCollection().remove(sale);
                productOld = em.merge(productOld);
            }
            if (productNew != null && !productNew.equals(productOld))
            {
                productNew.getSaleCollection().add(sale);
                productNew = em.merge(productNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                SalePK id = sale.getSalePK();
                if (findSale(id) == null)
                {
                    throw new NonexistentEntityException("The sale with id " + id + " no longer exists.");
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

    public void destroy(SalePK id) throws NonexistentEntityException
    {
        EntityManager em = null;
        try
        {
            em = getEntityManager();
            em.getTransaction().begin();
            Sale sale;
            try
            {
                sale = em.getReference(Sale.class, id);
                sale.getSalePK();
            } catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The sale with id " + id + " no longer exists.", enfe);
            }
            Product product = sale.getProduct();
            if (product != null)
            {
                product.getSaleCollection().remove(sale);
                product = em.merge(product);
            }
            em.remove(sale);
            em.getTransaction().commit();
        } finally
        {
            if (em != null)
            {
                em.close();
            }
        }
    }

    public List<Sale> findSaleEntities()
    {
        return findSaleEntities(true, -1, -1);
    }

    public List<Sale> findSaleEntities(int maxResults, int firstResult)
    {
        return findSaleEntities(false, maxResults, firstResult);
    }

    private List<Sale> findSaleEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sale.class));
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

    public Sale findSale(SalePK id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(Sale.class, id);
        } finally
        {
            em.close();
        }
    }

    public int getSaleCount()
    {
        EntityManager em = getEntityManager();
        try
        {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sale> rt = cq.from(Sale.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally
        {
            em.close();
        }
    }
    
}
