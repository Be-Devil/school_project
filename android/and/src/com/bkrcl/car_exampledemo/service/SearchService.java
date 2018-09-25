package com.bkrcl.car_exampledemo.service;

import cn.gdit.goldsite.MainActivity;

import com.bkrcl.control_car_video.camerautil.SearchCameraUtil;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class SearchService extends Service{
	//搜索摄像头IP类
    private SearchCameraUtil searchCameraUtil=null;
  //摄像头IP
    private String IP=null;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {                                            ///onCreate
		// TODO Auto-generated method stub
		super.onCreate();
				thread.start();                                         ///启动线程thread
				
	}
	private Thread thread=new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(IP==null||IP.equals("")){                           ///判断IP是连接
				searchCameraUtil=new SearchCameraUtil();              ///创建实例
				IP=searchCameraUtil.send(); 
				if(IP==null||IP.equals(""))
					IP="192.168.16.254";
					///获取IP
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			handler.sendEmptyMessage(10);                             ///异步消息处理 msg=10
		}
	});
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==10){
				Intent intent=new Intent(MainActivity.A_S);
				intent.putExtra("IP", IP+":81");                      ///http端口
				sendBroadcast(intent);                                ///发送intent
				SearchService.this.stopSelf();                        ///结束服务
			}
		};
	};
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
