/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ccjsh
 */
@Entity
@Table(name = "inventory")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Inventory.findAll", query = "SELECT i FROM Inventory i"),
    @NamedQuery(name = "Inventory.findByProdId", query = "SELECT i FROM Inventory i WHERE i.prodId = :prodId"),
    @NamedQuery(name = "Inventory.findByInvQty", query = "SELECT i FROM Inventory i WHERE i.invQty = :invQty"),
    @NamedQuery(name = "Inventory.findByInvLow", query = "SELECT i FROM Inventory i WHERE i.invLow = :invLow"),
    @NamedQuery(name = "Inventory.findByInvOrder", query = "SELECT i FROM Inventory i WHERE i.invOrder = :invOrder"),
})
public class Inventory implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "prod_id")
    private Integer prodId;
    @Basic(optional = false)
    @Column(name = "inv_qty")
    private int invQty;
    @Basic(optional = false)
    @Column(name = "inv_low")
    private int invLow;
    @Basic(optional = false)
    @Column(name = "inv_order")
    private int invOrder;
    @JoinColumn(name = "prod_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Product product;

    public Inventory()
    {
    }

    public Inventory(Integer prodId)
    {
        this.prodId = prodId;
    }

    public Inventory(Integer prodId, int invQty, int invLow, int invOrder)
    {
        this.prodId = prodId;
        this.invQty = invQty;
        this.invLow = invLow;
        this.invOrder = invOrder;
    }

    public Integer getProdId()
    {
        return prodId;
    }

    public void setProdId(Integer prodId)
    {
        this.prodId = prodId;
    }

    public int getInvQty()
    {
        return invQty;
    }

    public void setInvQty(int invQty)
    {
        this.invQty = invQty;
    }

    public int getInvLow()
    {
        return invLow;
    }

    public void setInvLow(int invLow)
    {
        this.invLow = invLow;
    }

    public int getInvOrder()
    {
        return invOrder;
    }

    public void setInvOrder(int invOrder)
    {
        this.invOrder = invOrder;
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
        hash += (prodId != null ? prodId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventory))
        {
            return false;
        }
        Inventory other = (Inventory) object;
        if ((this.prodId == null && other.prodId != null) || (this.prodId != null && !this.prodId.equals(other.prodId)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.Inventory[ prodId=" + prodId + " ]";
    }
    
}
