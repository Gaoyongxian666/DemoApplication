package cn.hotapk.fastandr_dbms;

import java.util.HashMap;
import java.util.Map;

import cn.hotapk.fastandr_dbms.bean.ResponseData;
import cn.hotapk.fastandr_dbms.utils.FConstant;
import cn.hotapk.fastandr_dbms.utils.FFileUploadUtils;
import cn.hotapk.fastandr_dbms.utils.FFileUtils;
import cn.hotapk.fhttpserver.NanoHTTPD;
import cn.hotapk.fhttpserver.annotation.RequestMapping;
import cn.hotapk.fhttpserver.annotation.RequestParam;
import cn.hotapk.fhttpserver.annotation.ResponseBody;
import cn.hotapk.fhttpserver.utils.FStaticResUtils;



/**
 * Created by zyc on 2019/2/13.
 */
public class FConfigController
{
    
    
    @ResponseBody
    @RequestMapping("getTitle")
    public ResponseData getTitle() {
        return FConfigService.getTitle();
    }

    /**
     * 添加数据
     * @param parms
     * @return
     */
    @ResponseBody
    @RequestMapping("setTitle")
    public ResponseData setTitle(Map<String, String> parms) {
        return FConfigService.setTitle(parms);
    }

    @ResponseBody
    @RequestMapping("getPathList")
    public ResponseData getPathList() {
        return FConfigService.getPathList();
    }

    @ResponseBody
    @RequestMapping("addPath")
    public ResponseData addPath(Map<String, String> parms) {
        return FConfigService.addPath(parms);
    }

    @ResponseBody
    @RequestMapping("delPath")
    public ResponseData addPath(@RequestParam("id") int pathid) {
        return FConfigService.delPath(pathid);
    }

    @ResponseBody
    @RequestMapping("setAd")
    public ResponseData setAd(Map<String, String> parms) {
        return FConfigService.setAd(parms);
    }


    @ResponseBody
    @RequestMapping("getAd")
    public ResponseData getAd() {
        return FConfigService.getAd();
    }

    @ResponseBody
    @RequestMapping("getAdImgList")
    public ResponseData getAdImgList() {
        return FConfigService.getAdImgList();
    }

    @ResponseBody
    @RequestMapping("addAdImg")
    public ResponseData addAdImg(@RequestParam("img") String adImg) {
        return FConfigService.addAdImg(adImg);
    }

    @ResponseBody
    @RequestMapping("delAdImg")
    public ResponseData delAdImg(@RequestParam("img") String adImg) {
        return FConfigService.delAdImg(adImg);
    }

    @ResponseBody
    @RequestMapping("getGoList")
    public ResponseData getGoList() {
        return FConfigService.getGoList();
    }

    @ResponseBody
    @RequestMapping("addGo")
    public ResponseData addGo(Map<String, String> parms) {
        return FConfigService.addGo(parms);
    }

    @ResponseBody
    @RequestMapping("delGo")
    public ResponseData delGo(@RequestParam("smaile") int smaile) {
        return FConfigService.delGo(smaile);
    }
    
    @RequestMapping("upload")
    public String upload(NanoHTTPD.IHTTPSession session) {
        if(FFileUploadUtils.uploadFile(session, FFileUtils.getAppRootDir(), "file"))
        {
            Map<String, String> files = new HashMap<>();
            String filename = "";
           
            Map<String, String> parms = session.getParms(); 
            filename=parms.get("file");
            return FConstant.getURL()+ "/download?filename=" + filename;
        }
        else {
            return "上传失败";    
        }
        
    }

    /*
    * 从指定目录里读取图片*/
    @RequestMapping("download")
    public NanoHTTPD.Response download(@RequestParam("filename") String filename) {
        String filePath = FFileUtils.getAppRootDir()+ "/" +filename;
        return NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, "image/jpeg", FStaticResUtils.getFileInp(filePath));//这代表任意的二进制数据传输。
    }

//    /**
//     * 下载数据库
//     * @param dbname
//     * @return
//     */
//    @RequestMapping("downloaddb")
//    public NanoHTTPD.Response downloaddb(@RequestParam("dbname") String dbname) {
//        InputStream inputStream = FDbService.downloaddb(dbname);
//        NanoHTTPD.Response response = NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, "application/octet-stream", inputStream);//这代表任意的二进制数据传输。
//        response.addHeader("Accept-Ranges", "bytes");
//        if (dbname.contains(FConstant.SHAREDPREFS_XML)) {
//            dbname = dbname.replace(FConstant.SHAREDPREFS_XML, "");
//        }
//        response.addHeader("Content-Disposition", "attachment; filename=" + dbname);
//
//        return response;
//    }
}
