package cn.hotapk.fastandr_dbms.bean;

import java.util.ArrayList;

/**
 * Created by zyc on 2019/2/15.
 */
public class SmileConfigAdBean
{
    /*"playtype":0,
            "img":[
                "timg (1).jpg",
                "timg (2).jpg"
            ],
            "video":""
        * */
    public int playtype;
    public ArrayList<String> img;
    public String video;
    
    public SmileConfigAdBean(){
        img = new ArrayList<String>();
    }
    
    public void addImg(String adimg){
        img.add(adimg);
    }
    
    public void delImg(String adimg){
        int i = img.indexOf(adimg);
        if(i >= 0)
        {
            img.remove(i);
        }
    }
    
    public int getPlaytype()
    {
        return playtype;
    }

    public void setPlaytype(int playtype)
    {
        this.playtype = playtype;
    }

    public ArrayList<String> getImg()
    {
        return img;
    }

    public void setImg(ArrayList<String> img)
    {
        this.img = img;
    }

    public String getVideo()
    {
        return video;
    }

    public void setVideo(String video)
    {
        this.video = video;
    }

    
}
