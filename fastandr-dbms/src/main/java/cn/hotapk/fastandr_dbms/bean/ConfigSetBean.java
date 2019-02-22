package cn.hotapk.fastandr_dbms.bean;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import cn.hotapk.fastandr_dbms.utils.FFileUtils;

/**
 * Created by zyc on 2019/2/14.
 */

public class ConfigSetBean
{
    public String title;
    public String columns_number;
    public String comserverip;
    public String comserverport;
    public String background;
    public String text;
    public ArrayList<PathConfigBean> img_list;
    
    private static ConfigSetBean fConfigSetBean;

    public ConfigSetBean()
    {
        img_list = new ArrayList<PathConfigBean>();
    }
    
    public static ConfigSetBean getInstance(){
        return fConfigSetBean;
    }
    
    public static boolean SaveToFile(String filepath, String filename){
        FFileUtils.delFile(filepath+filename);
        Gson gson = new Gson();
        String sConfig = gson.toJson(fConfigSetBean);

        return FFileUtils.writeTxtToFile(sConfig,filepath,filename);
    }

    public static ConfigSetBean init(String filepath, String filename){
        if(fConfigSetBean == null)
        {
            fConfigSetBean = new ConfigSetBean();
        }
        Gson gson = new Gson();
        File file = new File(filepath + "/" + filename);
        if(file.exists())
        {
            String sjson = FFileUtils.getFileContent(file);


            if(!sjson.isEmpty())
            {
                fConfigSetBean = gson.fromJson(sjson, ConfigSetBean.class);
            }    
        }
        
        return fConfigSetBean;
    }
    
    public void addPath(int id, String img, int showTime){
        delPath(id);
        PathConfigBean pb = new PathConfigBean(id, img, showTime);
        img_list.add(pb);
    }
    
    public boolean delPath(int id){
        for(int i=0; i < img_list.size(); i++)
        {
            if(img_list.get(i).id == id)
            {
                img_list.remove(i);
                break;
            }
        }
        return true;
    }
    
    public String getTitie()
    {
        return title;
    }

    public void setTitie(String title)
    {
        this.title = title;
    }

    public ArrayList<PathConfigBean> getImg_list()
    {
        return img_list;
    }

    public void setImg_list(ArrayList<PathConfigBean> img_list)
    {
        this.img_list = img_list;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
    

    public String getColumns_number()
    {
        return columns_number;
    }

    public void setColumns_number(String columns_number)
    {
        this.columns_number = columns_number;
    }

    public String getComserverip()
    {
        return comserverip;
    }

    public void setComserverip(String comserverip)
    {
        this.comserverip = comserverip;
    }

    public String getComserverport()
    {
        return comserverport;
    }

    public void setComserverport(String comserverport)
    {
        this.comserverport = comserverport;
    }

    public String getBackground()
    {
        return background;
    }

    public void setBackground(String background)
    {
        this.background = background;
    }
    
    
}
