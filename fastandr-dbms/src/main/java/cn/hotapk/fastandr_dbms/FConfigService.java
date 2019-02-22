package cn.hotapk.fastandr_dbms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hotapk.fastandr_dbms.bean.ConfigSetBean;
import cn.hotapk.fastandr_dbms.bean.PathConfigBean;
import cn.hotapk.fastandr_dbms.bean.ResponseData;
import cn.hotapk.fastandr_dbms.bean.SmileConfigAdBean;
import cn.hotapk.fastandr_dbms.bean.SmileConfigBean;
import cn.hotapk.fastandr_dbms.bean.SmileConfigGoBean;
import cn.hotapk.fastandr_dbms.utils.FConstant;
import cn.hotapk.fastandr_dbms.utils.FFileUtils;

/**
 * Created by zyc on 2019/2/13.
 */
public class FConfigService
{
    /**
     * 获取标题，ip，端口，显示列数
    */
    public static ResponseData getTitle(){
        
        ResponseData responseData = new ResponseData();
        try {
            List<Map<String, Object>> datas = new ArrayList<>();
                Map<String, Object> map = new HashMap<>();
                map.put("title", ConfigSetBean.getInstance().title);
                map.put("columns_number", ConfigSetBean.getInstance().columns_number);
                map.put("comserverip", ConfigSetBean.getInstance().comserverip);
                map.put("comserverport", ConfigSetBean.getInstance().comserverport);
                map.put("text", ConfigSetBean.getInstance().text);
                map.put("background",FConstant.getURL()+ "/download?filename=" + ConfigSetBean.getInstance().background);
                datas.add(map);
            responseData.setDatas(datas);
            responseData.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccessful(false);
            responseData.setError(e.getMessage());
        }
        return responseData;
    }

    public static ResponseData setTitle(Map<String, String> parms){
        ResponseData responseData = new ResponseData();
        boolean result;
        try {
            ConfigSetBean.getInstance().setTitie(parms.get("title"));
            ConfigSetBean.getInstance().setBackground(parms.get("background"));
            ConfigSetBean.getInstance().setColumns_number(parms.get("columns_number"));
            ConfigSetBean.getInstance().setComserverip(parms.get("comserverip"));
            ConfigSetBean.getInstance().setComserverport(parms.get("comserverport"));
            ConfigSetBean.getInstance().setText(parms.get("text"));
            result = ConfigSetBean.SaveToFile(FFileUtils.getAppRootDir(), "/setting.json");
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);

        return responseData;
    }

    public static ResponseData addPath(Map<String, String> parms){
        ResponseData responseData = new ResponseData();
        boolean result;
        try {
            ConfigSetBean.getInstance().addPath(Integer.parseInt(parms.get("id")),parms.get("img_path"),Integer.parseInt(parms.get("time")));
            result = ConfigSetBean.SaveToFile(FFileUtils.getAppRootDir(), "/setting.json");
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);

        return responseData;
    }
    /*删除路径*/
    public static ResponseData delPath(int pathid){
        ResponseData responseData = new ResponseData();
        boolean result;
        try {
            result = true;
            ConfigSetBean.getInstance().delPath(pathid);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);

        return responseData;
    }
    /*获取路径列表*/
    public static ResponseData getPathList(){
        ResponseData responseData = new ResponseData();
        try {
            List<Map<String, Object>> datas = new ArrayList<>();

            ArrayList<PathConfigBean> listBean = ConfigSetBean.getInstance().getImg_list();
            for(int i=0; i<listBean.size(); i++)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("id", listBean.get(i).id);
                map.put("img_path", FConstant.getURL()+ "/download?filename="+listBean.get(i).img_path);
                map.put("time", listBean.get(i).time);

                datas.add(map);
            }
                        
            responseData.setDatas(datas);
            responseData.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccessful(false);
            responseData.setError(e.getMessage());
        }
        return responseData;
    }
    /*设置广告*/
    public static ResponseData setAd(Map<String, String> parms){
        ResponseData responseData = new ResponseData();
        boolean result;
        try {
            SmileConfigBean.getInstance().ad.playtype = Integer.parseInt(parms.get("playtype"));
            SmileConfigBean.getInstance().ad.video = parms.get("video");

            SmileConfigBean.SaveToFile(FFileUtils.getAppRootDir(), "/set.txt");

            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);

        return responseData;
    }

    public static ResponseData getAd(){

        ResponseData responseData = new ResponseData();
        try {
            List<Map<String, Object>> datas = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("playtype", SmileConfigBean.getInstance().ad.playtype);
            map.put("video", SmileConfigBean.getInstance().ad.video);
           
            datas.add(map);
            responseData.setDatas(datas);
            responseData.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccessful(false);
            responseData.setError(e.getMessage());
        }
        return responseData;
    }
    
    public static ResponseData addAdImg(String img){
        ResponseData responseData = new ResponseData();
        boolean result;
        try {
            SmileConfigBean.getInstance().ad.addImg(img);
            SmileConfigBean.SaveToFile(FFileUtils.getAppRootDir(), "/set.txt");
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);

        return responseData;
    }

    public static ResponseData delAdImg(String img){
        ResponseData responseData = new ResponseData();
        boolean result;
        try {
            SmileConfigBean.getInstance().ad.delImg(img);
            SmileConfigBean.SaveToFile(FFileUtils.getAppRootDir(), "/set.txt");
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);

        return responseData;
    }

    public static ResponseData getAdImgList(){
        ResponseData responseData = new ResponseData();
        try {
            List<String> datas = new ArrayList<>();

            SmileConfigAdBean adBean = SmileConfigBean.getInstance().ad;
            for(int i=0; i<adBean.img.size(); i++)
            {                 
                datas.add(FConstant.getURL()+ "/download?filename="+adBean.img.get(i));
            }

            responseData.setRows(datas);
            responseData.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccessful(false);
            responseData.setError(e.getMessage());
        }
        return responseData;
    }

    public static ResponseData getGoList(){

        ResponseData responseData = new ResponseData();
        try {
            List<Map<String, Object>> datas = new ArrayList<>();
            

            SmileConfigGoBean goBean = SmileConfigBean.getInstance().go;
            for(int i=0; i<goBean.item.size(); i++)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("smaile", goBean.item.get(i).smaile);
                map.put("price", goBean.item.get(i).price);
                map.put("goods", FConstant.getURL()+ "/download?filename="+goBean.item.get(i).goods);
              
                datas.add(map);
            }
            
            responseData.setDatas(datas);
            responseData.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseData.setSuccessful(false);
            responseData.setError(e.getMessage());
        }
        return responseData;
    }

    public static ResponseData addGo(Map<String, String> parms){
        ResponseData responseData = new ResponseData();
        boolean result;
        try {
            SmileConfigBean.getInstance().go.addGo(Integer.parseInt(parms.get("smaile")),parms.get("price"),parms.get("goods"));
            SmileConfigBean.SaveToFile(FFileUtils.getAppRootDir(), "/set.txt");
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);

        return responseData;
    }

    public static ResponseData delGo(int smile){
        ResponseData responseData = new ResponseData();
        boolean result;
        try {
            SmileConfigBean.getInstance().go.delGo(smile);
            SmileConfigBean.SaveToFile(FFileUtils.getAppRootDir(), "/set.txt");
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            responseData.setError(e.getMessage());
        }
        responseData.setSuccessful(result);

        return responseData;
    }
    
//    /**
//     * 数据库下载
//     *
//     * @param databaseName
//     * @return
//     */
//    public static InputStream downloaddb(String databaseName) {
//        if (databaseName.contains(FConstant.SHAREDPREFS_XML)) {
//            return FFileUtils.file2Inp(FSharedPrefsUtils.getSharedPreferencePath(databaseName.replace(FConstant.SHAREDPREFS_XML, "")));
//        }
//
//        return FFileUtils.file2Inp(FDbManager.getAppContext().getDatabasePath(databaseName).getAbsolutePath());
//    }
}
