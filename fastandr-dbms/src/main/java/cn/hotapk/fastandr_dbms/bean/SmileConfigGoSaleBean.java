package cn.hotapk.fastandr_dbms.bean;

/**
 * Created by zyc on 2019/2/15.
 */
public class SmileConfigGoSaleBean
{
    /*"smaile":1,
                    "price":1,
                    "goods":"timg (3).jpg"*/
    public int smaile;
    public String price;
    public String goods;
    
    public SmileConfigGoSaleBean(int smaile, String price, String goods)
    {
        this.smaile = smaile;
        this.goods = goods;
        this.price = price;
    }
    
    public int getSmaile()
    {
        return smaile;
    }

    public void setSmaile(int smaile)
    {
        this.smaile = smaile;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getGoods()
    {
        return goods;
    }

    public void setGoods(String goods)
    {
        this.goods = goods;
    }

    
}
