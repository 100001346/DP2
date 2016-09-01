/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ccjsh
 */
@Entity
@Table(name = "product")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findByProdId", query = "SELECT p FROM Product p WHERE p.prodId = :prodId"),
    @NamedQuery(name = "Product.findByProdName", query = "SELECT p FROM Product p WHERE p.prodName = :prodName"),
    @NamedQuery(name = "Product.findByProdDesc", query = "SELECT p FROM Product p WHERE p.prodDesc = :prodDesc"),
    @NamedQuery(name = "Product.findByProdPrice", query = "SELECT p FROM Product p WHERE p.prodPrice = :prodPrice")
})
public class Product implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "prod_id")
    private Integer prodId;
    @Basic(optional = false)
    @Column(name = "prod_name")
    private String prodName;
    @Column(name = "prod_desc")
    private String prodDesc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "prod_price")
    private BigDecimal prodPrice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Collection<Sale> saleCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "product")
    private Inventory inventory;

    public Product()
    {
    }

    public Product(Integer prodId)
    {
        this.prodId = prodId;
    }

    public Product(Integer prodId, String prodName, BigDecimal prodPrice)
    {
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
    }

    public Integer getProdId()
    {
        return prodId;
    }

    public void setProdId(Integer prodId)
    {
        this.prodId = prodId;
    }

    public String getProdName()
    {
        return prodName;
    }

    public void setProdName(String prodName)
    {
        this.prodName = prodName;
    }

    public String getProdDesc()
    {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc)
    {
        this.prodDesc = prodDesc;
    }

    public BigDecimal getProdPrice()
    {
        return prodPrice;
    }

    public void setProdPrice(BigDecimal prodPrice)
    {
        this.prodPrice = prodPrice;
    }

    @XmlTransient
    public Collection<Sale> getSaleCollection()
    {
        return saleCollection;
    }

    public void setSaleCollection(Collection<Sale> saleCollection)
    {
        this.saleCollection = saleCollection;
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    public void setInventory(Inventory inventory)
    {
        this.inventory = inventory;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (prodId != null ? prodId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product))
        {
            return false;
        }
        Product other = (Product) object;
        if ((this.prodId == null && other.prodId != null) || (this.prodId != null && !this.prodId.equals(other.prodId)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Product[ prodId=" + prodId + " ]";
    }
    
}
