package com.example.administrator.demoapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity implements SocketClientResponseInterface{
    private int CODE_FOR_WRITE_PERMISSION=0;
    BaseQuickAdapter homeAdapter;

    private String title;
    private String background;
    private Integer columns_number;
    private String ip;
    private String port;
    private List<String> TITLE;
    private List<String> IMGurl;
    private List<Integer> Length;
    private List<Integer> ID;
    private String text;


    private volatile static Long str1=System.currentTimeMillis();
    private long firstTime = 0;




    private static Thread last_close=null;
    private volatile static int FLAG=1;
    private volatile static int APP_FLAG=0;



    private static final String[] kaicodes={"FE 05 00 00 FF 00 98 35","FE 05 00 01 FF 00 C9 F5","FE 05 00 02 FF 00 39 F5",
            "FE 05 00 03 FF 00 68 35","FE 05 00 04 FF 00 D9 F4","FE 05 00 05 FF 00 88 34","FE 05 00 06 FF 00 78 34","FE 05 00 07 FF 00 29 F4"};
    private static final String[] guancodes={"FE 05 00 00 00 00 D9 C5","FE 05 00 01 00 00 88 05","FE 05 00 02 00 00 78 05",
            "FE 05 00 03 00 00 29 C5","FE 05 00 04 00 00 98 04","FE 05 00 05 00 00 C9 C4","FE 05 00 06 00 00 39 C4","FE 05 00 07 00 00 68 04"};
    private ArrayList<HomeItem> mDataList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    public volatile static SocketClient socketClient;


    private static TextView attr;
    private  TextView textView;

    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1 :
                    str1 = msg.getData().getLong("time");//接受msg传递过来的参数
                    Log.e("time","获取当前时间："+str1);
                    break;
                case 2:
                    attr.setText(msg.getData().getString("zhuangtai"));
                    FLAG=msg.getData().getInt("FLAG");
                    attr.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                case 3:
                    APP_FLAG=msg.getData().getInt("APP_FLAG");
                    attr.setText(msg.getData().getString("zhuangtai"));
                    attr.setTextColor(Color.parseColor("#FFFFFF"));
                    break;


            }
        }
    };



    private void requestPermissionList() throws IOException, JSONException {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS
        };
        // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
        List<String> mPermissionList = new ArrayList<>();
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        if (mPermissionList.isEmpty()) {
            //未授予的权限为空，表示都授予了

            //拥有权限，执行操作
            initjson();
            initsocket();
            initView();
            initAdapter();
            initData();
            try {
                click();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this,"已经授权",Toast.LENGTH_LONG).show();
        } else {
            //请求权限方法
            Log.e("ee","未授予权限不为空");
            String[] mpermissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, mpermissions, CODE_FOR_WRITE_PERMISSION);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        attr=findViewById(R.id.attr);
        try {
            requestPermissionList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initjson() throws IOException, JSONException {
        Json_config json_config=new Json_config();
        title=json_config.getTitle();
        background=json_config.getBackground();
        columns_number=json_config.getColumns_number();
        ip=json_config.getIp();
        port=json_config.getPort();
        TITLE =json_config.getTITLE();
        IMGurl = json_config.getIMGurl();
        Length=json_config.getLength();
        ID=json_config.getID();
        text=json_config.getText();



    }

    private void initsocket(){
        new Thread() {
            @Override
            public void run() {
                try {
                    socketClient = new SocketClient(MainActivity.this,ip,port);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                closeall();
                Message msg = new Message();
                msg.what = 3;
                Bundle bundle = new Bundle();
                bundle.putString("zhuangtai","当前连接状态：正 常      ");  //往Bundle中存放数据
                bundle.putInt("APP_FLAG",1);  //往Bundle中存放数据

//                    bundle.putString("text2","Time：2016-11-03");  //往Bundle中put数据
                msg.setData(bundle);//mes利用Bundle传递数据
                MainActivity.handler.sendMessage(msg);//用activity中的handler发送消息

                Log.e("dd", "socketClient = new SocketClient完成");
                final Thread currentThread = Thread.currentThread();
                final String oldName = currentThread.getName();
                currentThread.setName("lastReceiveTime");
                while (true) {
                    try {

//                        Log.e("WWWWWWWW", String.valueOf(lastReceiveTime));
                        long curr = str1;
                        Thread.sleep(7000);
                        if (curr == str1) {
//                            Message msg = new Message();
//                            msg.what = 2;
//                            Bundle bundle = new Bundle();
//                            bundle.putString("zhuangtai","当前连接状态：断 开      ");  //往Bundle中存放数据
//                            msg.setData(bundle);//mes利用Bundle传递数据
//                            MainActivity.handler.sendMessage(msg);//用activity中的handler发送消息
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "连接已断开", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.e("ddd", "用户掉线了。。。。");
                            socketClient.stopSocket();
                        } else {
                            Log.e("ddd", "用户连接正常。。。。");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    private void send(int position){
        String content = kaicodes[ID.get(position) - 1];
        if (last_close != null) {
            Log.e("dd", "last_close!=null");
            last_close.interrupt();
        } else {
            Log.e("dd", "last_close==null");
        }
        Thread send = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                socketClient.sendData(content);
            }
        });
        send.start();

        last_close = new Thread() {
            public void run() {
                try {
                    Thread.sleep(Length.get(position)); //休眠一秒
                    String content = guancodes[ID.get(position) - 1];
                    socketClient.sendData(content);
                } catch (InterruptedException e) {
                    Log.e("InterruptedException", "中断上次");
                    e.printStackTrace();
                }
            }
        };
        last_close.start();
    }

    private void click() throws InterruptedException {
        Log.e("dd","点击事件开启");
        //APP_FLAG 代表 socketclient对象 是否建立成功
        //FLAG 代表 socket 是否连接成功
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (APP_FLAG!=1){
                    Toast.makeText(MainActivity.this, "匹配连接中", LENGTH_SHORT).show();
                    TTSUtility.getInstance(getApplicationContext()).speaking("匹配连接中");
                }else{
                    if (FLAG!=1){
                        TTSUtility.getInstance(getApplicationContext()).speaking("当前连接中断");
                        Toast.makeText(MainActivity.this, "当前连接中断", LENGTH_SHORT).show();
                    } else {
                        TTSUtility.getInstance(getApplicationContext()).speaking(text);

                        Toast.makeText(MainActivity.this, "点击了第" + (position + 1) + "条目", LENGTH_SHORT).show();
                        HomeItem item = (HomeItem) adapter.getItem(position);

                        closeall();
                        send(position);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent=new Intent(view.getContext(), OtherActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("text",text);
                        bundle.putString("background",background);
                        bundle.putString("IMGurl",IMGurl.get(position));

                        intent.putExtras(bundle);
                        view.getContext().startActivity(
                                intent,
                                // 注意这里的sharedView
                                // Content，View（动画作用view），String（和XML一样）
                                ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), view, "sharedView").toBundle());
                    }
                }
            }
        });
    }

    private void closeall() {
        //此线程运行完了可以直接结束
        new Thread(){
            public void run() {
                String close="FE 0F 00 00 00 08 01 00 B1 91";
                socketClient.sendData(close);
            }
        }.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        //设置recycleview的空隙，以及个数，布局方向
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        GridLayoutManager ms= new GridLayoutManager(this,columns_number);
        ms.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(ms);
        int spanCount = columns_number; // 3 columns
        int spacing = 40; // 50px
        boolean includeEdge = true;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        // 设置背景图片，标题
        @SuppressLint("SdCardPath") Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/dengdai/"+background);
        Resources resources = getResources();
        BitmapDrawable drawable = new BitmapDrawable(resources,
                bitmap);
        LinearLayout linearLayout=findViewById(R.id.main);
        linearLayout.setBackground(drawable);
        textView=findViewById(R.id.title);
        textView.setText(title);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        if (requestCode == CODE_FOR_WRITE_PERMISSION){
            Log.e("ee","请求结果回调");
            Log.e("grantResults.length：", String.valueOf(grantResults.length));
            Log.e("grantResults[0]：", String.valueOf(grantResults[0]));

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.e("ee","权限请求成功");
                //用户同意，执行操作
                try {
                    initjson();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initsocket();
                initView();
                initAdapter();
                initData();
                try {
                    click();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                Log.e("dd","//用户不同意，向用户展示该权限作用\n");
                finish();
            }
        }
    }

    private void initAdapter() {
        homeAdapter = new HomeAdapter(R.layout.home_item_view, mDataList);
        homeAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(homeAdapter);
    }

    private void initData() {
        for (int i = 0; i < IMGurl.size(); i++) {
            HomeItem item = new HomeItem();
            item.setimageUrl(IMGurl.get(i));
            mDataList.add(item);
        }
        homeAdapter.notifyDataSetChanged();

    }

    public static byte[] hexStrToBinaryStr(String hexString) {
        if (TextUtils.isEmpty(hexString)) {
            return null;
        }
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;
        byte[] bytes = new byte[len / 2];
        while (index < len) {
            String sub = hexString.substring(index, index + 2);
            bytes[index/2] = (byte)Integer.parseInt(sub,16);
            index += 2;
        }
        return bytes;
    }

    @Override
    public void onSocketConnect() {

    }

    @Override
    public void onSocketReceive(Object socketResult, int code) {

    }

    @Override
    public void onSocketDisable(String msg, int code) {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (APP_FLAG==1) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                closeall();
            }
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(MainActivity.this, "再按一次退出程序", LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    finish();
                }
            }
        }else {
            Toast.makeText(MainActivity.this, "程序正在初始化", LENGTH_SHORT).show();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected void onResume() {
        Log.e("dd","onResume");
        if (homeAdapter!=null){
            Log.e("dd","homeAdapter!=null");
            Log.e("dd","强制homeadapter刷新");
            mDataList.clear();
            initData();
        }else {
            Log.e("dd","homeAdapter==null");
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 清空缓存
        ClearData clearData=new ClearData(getApplicationContext());
        clearData.clearAllDataOfApplication();
        Log.e("Dd","清空数据");
        System.exit(0);
        super.onDestroy();
    }
}
