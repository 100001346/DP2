/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ccjsh
 */
@Entity
@Table(name = "sale")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Sale.findAll", query = "SELECT s FROM Sale s"),
    @NamedQuery(name = "Sale.findBySaleId", query = "SELECT s FROM Sale s WHERE s.salePK.saleId = :saleId"),
    @NamedQuery(name = "Sale.findByProdId", query = "SELECT s FROM Sale s WHERE s.salePK.prodId = :prodId"),
    @NamedQuery(name = "Sale.findBySaleQty", query = "SELECT s FROM Sale s WHERE s.saleQty = :saleQty"),
    @NamedQuery(name = "Sale.findBySalePrice", query = "SELECT s FROM Sale s WHERE s.salePrice = :salePrice"),
    @NamedQuery(name = "Sale.findBySaleDate", query = "SELECT s FROM Sale s WHERE s.saleDate = :saleDate")
})
public class Sale implements Serializable
{

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SalePK salePK;
    @Column(name = "sale_qty")
    private Integer saleQty;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sale_price")
    private BigDecimal salePrice;
    @Column(name = "sale_date")
    @Temporal(TemporalType.DATE)
    private Date saleDate;
    @JoinColumn(name = "prod_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public Sale()
    {
    }

    public Sale(SalePK salePK)
    {
        this.salePK = salePK;
    }

    public Sale(long saleId, int prodId)
    {
        this.salePK = new SalePK(saleId, prodId);
    }

    public SalePK getSalePK()
    {
        return salePK;
    }

    public void setSalePK(SalePK salePK)
    {
        this.salePK = salePK;
    }

    public Integer getSaleQty()
    {
        return saleQty;
    }

    public void setSaleQty(Integer saleQty)
    {
        this.saleQty = saleQty;
    }

    public BigDecimal getSalePrice()
    {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice)
    {
        this.salePrice = salePrice;
    }

    public Date getSaleDate()
    {
        return saleDate;
    }

    public void setSaleDate(Date saleDate)
    {
        this.saleDate = saleDate;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (salePK != null ? salePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sale))
        {
            return false;
        }
        Sale other = (Sale) object;
        if ((this.salePK == null && other.salePK != null) || (this.salePK != null && !this.salePK.equals(other.salePK)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Sale[ salePK=" + salePK + " ]";
    }
    
}
