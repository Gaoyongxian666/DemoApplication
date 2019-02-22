package cn.hotapk.fastandr_dbms.bean;

import java.util.ArrayList;

/**
 * Created by zyc on 2019/2/15.
 */
public class SmileConfigGoBean
{
    /*"item":[
            {
                "smaile":1,
                "price":1,
                "goods":"timg (3).jpg"
            },
            {
                "smaile":12,
                "price":12,
                "goods":"timg (3).jpg"
            }
        ]*/
    public ArrayList<SmileConfigGoSaleBean> item;
    
    public SmileConfigGoBean(){
        item = new ArrayList<SmileConfigGoSaleBean>();
    }
    
    public void addGo(int smaile, String price, String goods){
        SmileConfigGoSaleBean scgsb = new SmileConfigGoSaleBean(smaile, price, goods);
        item.add(scgsb);
    }

    public void delGo(int smaile){
        for(int i =0; i < item.size(); i++)
        {
            if(item.get(i).smaile == smaile)
            {
                item.remove(i);
                break;
            }
        }
        
    }
}
