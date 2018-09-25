package com.bkrcl.car_exampledemo.service;

import cn.gdit.goldsite.MainActivity;

import com.bkrcl.control_car_video.camerautil.SearchCameraUtil;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class SearchService extends Service{
	//��������ͷIP��
    private SearchCameraUtil searchCameraUtil=null;
  //����ͷIP
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
				thread.start();                                         ///�����߳�thread
				
	}
	private Thread thread=new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(IP==null||IP.equals("")){                           ///�ж�IP������
				searchCameraUtil=new SearchCameraUtil();              ///����ʵ��
				IP=searchCameraUtil.send(); 
				if(IP==null||IP.equals(""))
					IP="192.168.16.254";
					///��ȡIP
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			handler.sendEmptyMessage(10);                             ///�첽��Ϣ���� msg=10
		}
	});
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==10){
				Intent intent=new Intent(MainActivity.A_S);
				intent.putExtra("IP", IP+":81");                      ///http�˿�
				sendBroadcast(intent);                                ///����intent
				SearchService.this.stopSelf();                        ///��������
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
