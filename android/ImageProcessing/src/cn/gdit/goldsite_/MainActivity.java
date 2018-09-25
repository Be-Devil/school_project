package cn.gdit.goldsite_;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


import cn.gdit.goldsite_.R;
import cn.image.processing.ColorShapeRecognition;

import cn.image.processing.FileService;
import com.bkrcl.car_exampledemo.service.SearchService;
import com.bkrcl.control_car_video.camerautil.CameraCommandUtil;

/**
 * @author Administrator
 *
 */
public class MainActivity extends Activity {
          
	private WifiManager wifiManager;
	private DhcpInfo dhcpInfo; 
	@SuppressWarnings("unused")
	private String IPCar;
	
	ColorShapeRecognition colorShapeRecognition; 
	public CameraCommandUtil cameraCommandUtil = null;                   
	public static final String A_S = "com.a_s";   
	public String IP = null;                          
	public static Bitmap bitmap1;   
	public static int Bitmap_num = 0;
	

	public static ImageView imageView0 = null;   
	public static ImageView imageView1 = null;  
	public static ImageView imageView2 = null;  
	public static ImageView imageView3 = null;  
	public static ImageView imageView4 = null; 
	public static ImageView imageView5 = null;  
	public static ImageView imageView6 = null;  
	public static ImageView imageView7 = null; 
	public static TextView TextView = null;      
	public static TextView TextView1 = null;  
	public static TextView TextView2 = null;  
	
	public static ImageView imageView11 = null;   
	public static ImageView imageView12 = null;  
	public static ImageView imageView13 = null;  
	public static ImageView imageView14 = null;  
	public static ImageView imageView15 = null; 
	public static ImageView imageView16 = null;  
	public static ImageView imageView17 = null;  
	public static ImageView imageView18 = null; 	
	public static ImageView imageView19 = null;  
	public static ImageView imageView20 = null;  
	public static ImageView imageView21 = null;  
	public static ImageView imageView22 = null; 
	public static ImageView imageView23 = null;  
	public static ImageView imageView24 = null;
	public static ImageView imageView25 = null;  
	public static ImageView imageView26 = null;
	public static ImageView imageView27 = null;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   ///onCreate

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);                 ///���ô���ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);                       ///���ر���
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   ///����
		setContentView(R.layout.activity_main);                              ///ѡ�����ֽ���
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()     ///�Ͽ�ģʽ���̲߳���
				.detectDiskReads().detectDiskWrites().detectNetwork()        ///��д���̺ͽ���������ʣ�
				.penaltyLog().build());                                      ///д��LogCat�����棩��ʹ��penaltyDeath()�Ļ���������
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()             ///�Ͽ�ģʽ�����������
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()  ///����ڴ�й©
				.penaltyLog().penaltyDeath().build());                       ///д��LogCat�����棩�����   
		IntentFilter intentFilter = new IntentFilter();                      ///�����㲥����
		intentFilter.addAction(A_S);                                         ///����Action
		registerReceiver(myBroadcastReceiver, intentFilter);                 ///�㲥��̬ע��
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  ///ǿ��ת������ΪWIFI
		dhcpInfo = wifiManager.getDhcpInfo();                                ///���IP
		IPCar = Formatter.formatIpAddress(dhcpInfo.gateway);                 ///�������
		cameraCommandUtil = new CameraCommandUtil();
//		phThread1.start(); 
		search();                                                          /// ��������IP������
		
		imageView0 = (ImageView) findViewById(R.id.imageView0);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		imageView3 = (ImageView) findViewById(R.id.imageView3);
		imageView4 = (ImageView) findViewById(R.id.imageView4);
		imageView5 = (ImageView) findViewById(R.id.imageView5);
		imageView6 = (ImageView) findViewById(R.id.imageView6);
		imageView7 = (ImageView) findViewById(R.id.imageView7);		

		TextView = (TextView) findViewById(R.id.TextView); 
		TextView1 = (TextView) findViewById(R.id.TextView1); 
		TextView2 = (TextView) findViewById(R.id.TextView2); 
	}
	
	public void ��(View v){      
		setContentView(R.layout.second);   
		imageView11 = (ImageView) findViewById(R.id.imageView11);
		imageView12 = (ImageView) findViewById(R.id.imageView12);
		imageView13 = (ImageView) findViewById(R.id.imageView13);
		imageView14 = (ImageView) findViewById(R.id.imageView14);
		imageView15 = (ImageView) findViewById(R.id.imageView15);
		imageView16 = (ImageView) findViewById(R.id.imageView16);
		imageView17 = (ImageView) findViewById(R.id.imageView17);
		imageView18 = (ImageView) findViewById(R.id.imageView18);		
		imageView19 = (ImageView) findViewById(R.id.imageView19);
		imageView20 = (ImageView) findViewById(R.id.imageView20);
		imageView21 = (ImageView) findViewById(R.id.imageView21);
		imageView22 = (ImageView) findViewById(R.id.imageView22);
		imageView23 = (ImageView) findViewById(R.id.imageView23);
		imageView24 = (ImageView) findViewById(R.id.imageView24);
		imageView25 = (ImageView) findViewById(R.id.imageView25);
		imageView26 = (ImageView) findViewById(R.id.imageView26);
		imageView27 = (ImageView) findViewById(R.id.imageView27);
		
		imageView11.setOnClickListener(new imageView11());
		imageView12.setOnClickListener(new imageView12());
		imageView13.setOnClickListener(new imageView13());
		imageView14.setOnClickListener(new imageView14());
		imageView15.setOnClickListener(new imageView15());
		imageView16.setOnClickListener(new imageView16());
		imageView17.setOnClickListener(new imageView17());
		imageView18.setOnClickListener(new imageView18());
		imageView19.setOnClickListener(new imageView19());
		imageView20.setOnClickListener(new imageView20());
		imageView21.setOnClickListener(new imageView21());
		imageView22.setOnClickListener(new imageView22());
		imageView23.setOnClickListener(new imageView23());
		imageView24.setOnClickListener(new imageView24());
		imageView25.setOnClickListener(new imageView25());
		imageView26.setOnClickListener(new imageView26());
		TextView2 = (TextView) findViewById(R.id.TextView2); 
	}
	
	   public class imageView11 implements OnClickListener{ public void onClick(View v){Bitmap_num =  1;phHandler1.sendEmptyMessage(3);}}
	   public class imageView12 implements OnClickListener{ public void onClick(View v){Bitmap_num =  2;phHandler1.sendEmptyMessage(3);}}
	   public class imageView13 implements OnClickListener{ public void onClick(View v){Bitmap_num =  3;phHandler1.sendEmptyMessage(3);}}
	   public class imageView14 implements OnClickListener{ public void onClick(View v){Bitmap_num =  4;phHandler1.sendEmptyMessage(3);}}
	   public class imageView15 implements OnClickListener{ public void onClick(View v){Bitmap_num =  5;phHandler1.sendEmptyMessage(3);}}
	   public class imageView16 implements OnClickListener{ public void onClick(View v){Bitmap_num =  6;phHandler1.sendEmptyMessage(3);}}
	   public class imageView17 implements OnClickListener{ public void onClick(View v){Bitmap_num =  7;phHandler1.sendEmptyMessage(3);}}
	   public class imageView18 implements OnClickListener{ public void onClick(View v){Bitmap_num =  8;phHandler1.sendEmptyMessage(3);}}
	   public class imageView19 implements OnClickListener{ public void onClick(View v){Bitmap_num =  9;phHandler1.sendEmptyMessage(3);}}
	   public class imageView20 implements OnClickListener{ public void onClick(View v){Bitmap_num = 10;phHandler1.sendEmptyMessage(3);}}
	   public class imageView21 implements OnClickListener{ public void onClick(View v){Bitmap_num = 11;phHandler1.sendEmptyMessage(3);}}
	   public class imageView22 implements OnClickListener{ public void onClick(View v){Bitmap_num = 12;phHandler1.sendEmptyMessage(3);}}
	   public class imageView23 implements OnClickListener{ public void onClick(View v){Bitmap_num = 13;phHandler1.sendEmptyMessage(3);}}
	   public class imageView24 implements OnClickListener{ public void onClick(View v){Bitmap_num = 14;phHandler1.sendEmptyMessage(3);}}
	   public class imageView25 implements OnClickListener{ public void onClick(View v){Bitmap_num = 15;phHandler1.sendEmptyMessage(3);}}
	   public class imageView26 implements OnClickListener{ public void onClick(View v){Bitmap_num = 16;phHandler1.sendEmptyMessage(3);}}

	public void ����(View v){      
		setContentView(R.layout.activity_main);    
		imageView0 = (ImageView) findViewById(R.id.imageView0);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		imageView3 = (ImageView) findViewById(R.id.imageView3);
		imageView4 = (ImageView) findViewById(R.id.imageView4);
		imageView5 = (ImageView) findViewById(R.id.imageView5);
		imageView6 = (ImageView) findViewById(R.id.imageView6);
		imageView7 = (ImageView) findViewById(R.id.imageView7);	
		TextView = (TextView) findViewById(R.id.TextView); 
		TextView1 = (TextView) findViewById(R.id.TextView1); 
	}
	
	public void ���ʶ��(View v){    
//		FileService  file = new FileService();
//		file.createFile_(file.��״��ɫ);
//		Bitmap b1 = original_Bitmap;//file.readPhoto(FileService.��״��ɫ+"1" + ".png");
		colorShapeRecognition=new ColorShapeRecognition(cameraCommandUtil.httpForImage(IP));
		while(!colorShapeRecognition.��ɱ�־λ); 
		TextView = (TextView) findViewById(R.id.TextView);    
		phHandler1.sendEmptyMessage(2);                             ///�첽��Ϣ���� msg=10                    
	}
	
	public void ʶ��(View v){      
//		FileService  file = new FileService();
//		file.createFile_(file.��״��ɫ);
//		Bitmap b1 = original_Bitmap;//file.readPhoto(FileService.��״��ɫ+"1" + ".png");
		colorShapeRecognition=new ColorShapeRecognition(cameraCommandUtil.httpForImage(IP));
		while(!colorShapeRecognition.��ɱ�־λ);                        
	}
	
	public void ���(View v){
//		FileService  file = new FileService();
//		file.createFile_(file.��״��ɫ);
//		Bitmap b1 = original_Bitmap;//file.readPhoto(FileService.��״��ɫ+"1" + ".png");
		colorShapeRecognition=new ColorShapeRecognition(cameraCommandUtil.httpForImage(IP));
		while(!colorShapeRecognition.��ɱ�־λ);      
		colorShapeRecognition.����();
	}

	// ��������IP������
	private void search() {
//		progressDialog = new ProgressDialog(this);
//		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);         ///���ý�������񣬷��ΪԲ�Σ���ת�ġ�
//		progressDialog.setMessage("�ȴ�����ͷ����������");                        ///������ʾ��Ϣ
//		progressDialog.show();                                                 ///��ʾProgressDialog
		Intent intent = new Intent();                                          ///�����㲥����
		intent.setClass(MainActivity.this, SearchService.class);               ///��ʽintent
		startService(intent);                                                  ///��������                                                
	}

	
	private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {   ///�����㲥����

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			IP = arg1.getStringExtra("IP");                               ///IP����
//			progressDialog.dismiss();                                     ///�ý�����ͣ����
			phThread1.start();                                            ///ͼ�����
		}
	};

	// �����߳̽�������ͷ��ǰͼƬ
	private Thread phThread1 = new Thread(new Runnable() {                    ///������

		@Override
		public void run() {
			while (true) {
				bitmap1 = cameraCommandUtil.httpForImage(IP);                ///��������ͷ��ǰͼƬ
				phHandler1.sendEmptyMessage(8);                             ///�첽��Ϣ���� msg=10
			}
		}
	});

	
	// ��ʾͼƬ
	public static Handler phHandler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				imageView1.setImageBitmap(ColorShapeRecognition.Display_Bitmap[0]);                               ///��ʾͼƬbitmap1
				imageView2.setImageBitmap(ColorShapeRecognition.Display_Bitmap[1]);                               ///��ʾͼƬbitmap1
				imageView3.setImageBitmap(ColorShapeRecognition.Display_Bitmap[2]);                               ///��ʾͼƬbitmap1
				imageView4.setImageBitmap(ColorShapeRecognition.Display_Bitmap[3]);                               ///��ʾͼƬbitmap1
				imageView5.setImageBitmap(ColorShapeRecognition.Display_Bitmap[4]);                               ///��ʾͼƬbitmap1
				imageView6.setImageBitmap(ColorShapeRecognition.Display_Bitmap[5]);                               ///��ʾͼƬbitmap1
				imageView7.setImageBitmap(ColorShapeRecognition.Display_Bitmap[6]);                               ///��ʾͼƬbitmap1
				TextView.setText(ColorShapeRecognition.Result);
				TextView1.setText(ColorShapeRecognition.Process);
			}
			if (msg.what == 2) {
				imageView11.setImageBitmap(ColorShapeRecognition.Display_Bitmap[11]);                               ///��ʾͼƬbitmap1
				imageView12.setImageBitmap(ColorShapeRecognition.Display_Bitmap[12]);                               ///��ʾͼƬbitmap1
				imageView13.setImageBitmap(ColorShapeRecognition.Display_Bitmap[13]);                               ///��ʾͼƬbitmap1
				imageView14.setImageBitmap(ColorShapeRecognition.Display_Bitmap[14]);                               ///��ʾͼƬbitmap1
				imageView15.setImageBitmap(ColorShapeRecognition.Display_Bitmap[15]);                               ///��ʾͼƬbitmap1
				imageView16.setImageBitmap(ColorShapeRecognition.Display_Bitmap[16]);                               ///��ʾͼƬbitmap1
				imageView17.setImageBitmap(ColorShapeRecognition.Display_Bitmap[17]);                               ///��ʾͼƬbitmap1
				imageView18.setImageBitmap(ColorShapeRecognition.Display_Bitmap[18]);                               ///��ʾͼƬbitmap1
				imageView19.setImageBitmap(ColorShapeRecognition.Display_Bitmap[19]);                               ///��ʾͼƬbitmap1
				imageView20.setImageBitmap(ColorShapeRecognition.Display_Bitmap[20]);                               ///��ʾͼƬbitmap1
				imageView21.setImageBitmap(ColorShapeRecognition.Display_Bitmap[21]);                               ///��ʾͼƬbitmap1
				imageView22.setImageBitmap(ColorShapeRecognition.Display_Bitmap[22]);                               ///��ʾͼƬbitmap1
				imageView23.setImageBitmap(ColorShapeRecognition.Display_Bitmap[23]);                               ///��ʾͼƬbitmap1
				imageView24.setImageBitmap(ColorShapeRecognition.Display_Bitmap[24]);                               ///��ʾͼƬbitmap1
				imageView25.setImageBitmap(ColorShapeRecognition.Display_Bitmap[25]);                               ///��ʾͼƬbitmap1
				imageView26.setImageBitmap(ColorShapeRecognition.Display_Bitmap[26]);                               ///��ʾͼƬbitmap1
			}
			if (msg.what == 3) {
				imageView27.setImageBitmap(ColorShapeRecognition.Display_color_Bitmap[Bitmap_num]);                               ///��ʾͼƬbitmap1
				TextView2.setText(ColorShapeRecognition.Every_color[Bitmap_num]);
			}
			if (msg.what == 8) {
				imageView0.setImageBitmap(bitmap1);                               ///��ʾͼƬbitmap1
			}
		};
	};

	@Override
	public void onResume() {
		super.onResume();
		// ͨ��OpenCV���������ز���ʼ��OpenCV��⣬��νOpenCV���������
		//OpenCV_2.4.3.2_Manager_2.4_*.apk�������������OpenCV��װ����apkĿ¼��
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this,
				mLoaderCallback);
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
		}

	// OpenCV�����ز���ʼ���ɹ���Ļص��������ڴ����ǲ������κβ���
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {                          ///��׽���̱����µ��¼�
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {       ///���µ������BACK��ͬʱû���ظ�������˼���ʱ��Ϊ�˷�ֹ��ù��죬�������κ����¼������������ã�
			android.os.Process.killProcess(android.os.Process.myPid());              ///��Ӧ�ò���ʹ��ʱ��ͨ����Ҫ�ر�Ӧ�ã����Ȼ�ȡ��ǰ���̵�id��Ȼ��ɱ���ý��̡� 
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {                                          ///onDestroy
		super.onDestroy();
		unregisterReceiver(myBroadcastReceiver);
	}


}
