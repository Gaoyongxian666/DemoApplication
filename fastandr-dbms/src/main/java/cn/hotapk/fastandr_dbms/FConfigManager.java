package cn.hotapk.fastandr_dbms;

import android.content.Context;

import cn.hotapk.fastandr_dbms.bean.ConfigSetBean;
import cn.hotapk.fastandr_dbms.bean.SmileConfigBean;
import cn.hotapk.fastandr_dbms.utils.FFileUtils;

/**
 * Created by zyc on 2019/2/13.
 */
public class FConfigManager
{
    private Context context;

    private volatile static FConfigManager configManager;
    private FHttpManager fHttpManager;
    private ConfigSetBean fConfigSetBean;
    private SmileConfigBean fSmileConfigBean;
    private int port = 8888;

    private FConfigManager(Context context) {
        this.context = context;
        fHttpManager = FHttpManager.init(context, FConfigController.class);
        fConfigSetBean = ConfigSetBean.init(FFileUtils.getAppRootDir(),"setting.json");
        fSmileConfigBean = SmileConfigBean.init(FFileUtils.getAppRootDir(),"set.txt");
    }

    /**
     * 初始化
     *
     * @param context
     * @return
     */
    public static FConfigManager init(Context context) {
        if (configManager == null) {
            synchronized (FConfigManager.class) {
                if (configManager == null) {
                    configManager = new FConfigManager(context.getApplicationContext());
                }
            }
        }
        return configManager;
    }

    public static Context getAppContext() {
        if (configManager != null) return configManager.context;
        throw new NullPointerException("To initialize first");
    }

    /**
     * 设置端口号
     *
     * @param port
     * @throws Exception
     */
    public void setPort(int port) throws Exception {
        this.port = port;
        throw new Exception("please init FHttpManager");
    }

    /**
     * 启动服务
     */
    public void startServer() throws Exception {
        if (fHttpManager == null) {
            throw new Exception("please init FHttpManager");
        } else {
            fHttpManager.setPort(port);
            fHttpManager.setAllowCross(true);
            fHttpManager.setIndexName("sqlindex.html");
            fHttpManager.startServer();
        }
    }

    /**
     * 关闭服务
     *
     * @throws Exception
     */
    public void stopServer() throws Exception {
        if (fHttpManager == null) {
            throw new Exception("please init FHttpManager");
        } else {
            fHttpManager.stopServer();
        }
    }

    /**
     * 获取http服务
     */
    public FHttpManager getFHttpManager() {
        return fHttpManager;
    }

}
