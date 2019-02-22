package cn.hotapk.fastandr_dbms.bean;

/**
 * Created by zyc on 2019/2/14.
 */
public class PathConfigBean
{
    public int id;
    public String img_path;
    public int time;
    
    public PathConfigBean(int id, String img, int time){
        this.id = id;
        this.img_path = img;
        this.time = time;
    }
}
