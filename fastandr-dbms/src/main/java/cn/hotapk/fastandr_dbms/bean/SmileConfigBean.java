package cn.hotapk.fastandr_dbms.bean;

import com.google.gson.Gson;

import java.io.File;

import cn.hotapk.fastandr_dbms.utils.FFileUtils;

/**
 * Created by zyc on 2019/2/15.
 */
public class SmileConfigBean
{
    

    public SmileConfigAdBean ad;
    public SmileConfigGoBean go;

    private static SmileConfigBean fSmileConfigBean;
    
    public SmileConfigBean(){
        ad = new SmileConfigAdBean();
        go = new SmileConfigGoBean();
    }

    public static SmileConfigBean getInstance(){
        return fSmileConfigBean;
    }

    public static boolean SaveToFile(String filepath, String filename){
        FFileUtils.delFile(filepath+filename);
        Gson gson = new Gson();
        String sConfig = gson.toJson(fSmileConfigBean);

        return FFileUtils.writeTxtToFile(sConfig,filepath,filename);
    }

    public static SmileConfigBean init(String filepath, String filename){
        if(fSmileConfigBean == null)
        {
            fSmileConfigBean = new SmileConfigBean();
        }
        Gson gson = new Gson();
        File file = new File(filepath + "/" + filename);
        if(file.exists())
        {
            String sjson = FFileUtils.getFileContent(file);


            if(!sjson.isEmpty())
            {
                fSmileConfigBean = gson.fromJson(sjson, SmileConfigBean.class);
            }
        }

        return fSmileConfigBean;
    }

    public SmileConfigAdBean getAd()
    {
        return ad;
    }

    public void setAd(SmileConfigAdBean ad)
    {
        this.ad = ad;
    }

    public SmileConfigGoBean getGo()
    {
        return go;
    }

    public void setGo(SmileConfigGoBean go)
    {
        this.go = go;
    }
}
