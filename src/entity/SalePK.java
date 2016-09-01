/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author ccjsh
 */
@Embeddable
public class SalePK implements Serializable
{

    @Basic(optional = false)
    @Column(name = "sale_id")
    private long saleId;
    @Basic(optional = false)
    @Column(name = "prod_id")
    private int prodId;

    public SalePK()
    {
    }

    public SalePK(long saleId, int prodId)
    {
        this.saleId = saleId;
        this.prodId = prodId;
    }

    public long getSaleId()
    {
        return saleId;
    }

    public void setSaleId(long saleId)
    {
        this.saleId = saleId;
    }

    public int getProdId()
    {
        return prodId;
    }

    public void setProdId(int prodId)
    {
        this.prodId = prodId;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (int) saleId;
        hash += (int) prodId;
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SalePK))
        {
            return false;
        }
        SalePK other = (SalePK) object;
        if (this.saleId != other.saleId)
        {
            return false;
        }
        if (this.prodId != other.prodId)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.SalePK[ saleId=" + saleId + ", prodId=" + prodId + " ]";
    }
    
}
