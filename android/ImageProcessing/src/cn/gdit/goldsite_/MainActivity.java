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
				WindowManager.LayoutParams.FLAG_FULLSCREEN);                 ///设置窗体全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);                       ///隐藏标题
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   ///竖屏
		setContentView(R.layout.activity_main);                              ///选定布局界面
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()     ///严苛模式：线程策略
				.detectDiskReads().detectDiskWrites().detectNetwork()        ///读写磁盘和进行网络访问，
				.penaltyLog().build());                                      ///写入LogCat（警告）如使用penaltyDeath()的话，崩溃。
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()             ///严苛模式：虚拟机策略
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()  ///检查内存泄漏
				.penaltyLog().penaltyDeath().build());                       ///写入LogCat（警告）或崩溃   
		IntentFilter intentFilter = new IntentFilter();                      ///创建广播对象
		intentFilter.addAction(A_S);                                         ///传入Action
		registerReceiver(myBroadcastReceiver, intentFilter);                 ///广播动态注册
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  ///强制转换对象为WIFI
		dhcpInfo = wifiManager.getDhcpInfo();                                ///获得IP
		IPCar = Formatter.formatIpAddress(dhcpInfo.gateway);                 ///获得网关
		cameraCommandUtil = new CameraCommandUtil();
//		phThread1.start(); 
		search();                                                          /// 搜索摄像IP进度条
		
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
	
	public void 查(View v){      
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

	public void 返回(View v){      
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
	
	public void 逐个识别(View v){    
//		FileService  file = new FileService();
//		file.createFile_(file.形状颜色);
//		Bitmap b1 = original_Bitmap;//file.readPhoto(FileService.形状颜色+"1" + ".png");
		colorShapeRecognition=new ColorShapeRecognition(cameraCommandUtil.httpForImage(IP));
		while(!colorShapeRecognition.完成标志位); 
		TextView = (TextView) findViewById(R.id.TextView);    
		phHandler1.sendEmptyMessage(2);                             ///异步消息处理 msg=10                    
	}
	
	public void 识别(View v){      
//		FileService  file = new FileService();
//		file.createFile_(file.形状颜色);
//		Bitmap b1 = original_Bitmap;//file.readPhoto(FileService.形状颜色+"1" + ".png");
		colorShapeRecognition=new ColorShapeRecognition(cameraCommandUtil.httpForImage(IP));
		while(!colorShapeRecognition.完成标志位);                        
	}
	
	public void 监控(View v){
//		FileService  file = new FileService();
//		file.createFile_(file.形状颜色);
//		Bitmap b1 = original_Bitmap;//file.readPhoto(FileService.形状颜色+"1" + ".png");
		colorShapeRecognition=new ColorShapeRecognition(cameraCommandUtil.httpForImage(IP));
		while(!colorShapeRecognition.完成标志位);      
		colorShapeRecognition.监视();
	}

	// 搜索摄像IP进度条
	private void search() {
//		progressDialog = new ProgressDialog(this);
//		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);         ///设置进度条风格，风格为圆形，旋转的。
//		progressDialog.setMessage("等待摄像头返回数据中");                        ///设置提示信息
//		progressDialog.show();                                                 ///显示ProgressDialog
		Intent intent = new Intent();                                          ///创建广播对象
		intent.setClass(MainActivity.this, SearchService.class);               ///显式intent
		startService(intent);                                                  ///开启服务                                                
	}

	
	private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {   ///创建广播对象

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			IP = arg1.getStringExtra("IP");                               ///IP接收
//			progressDialog.dismiss();                                     ///让进度条停下来
			phThread1.start();                                            ///图像接收
		}
	};

	// 开启线程接受摄像头当前图片
	private Thread phThread1 = new Thread(new Runnable() {                    ///匿名类

		@Override
		public void run() {
			while (true) {
				bitmap1 = cameraCommandUtil.httpForImage(IP);                ///接受摄像头当前图片
				phHandler1.sendEmptyMessage(8);                             ///异步消息处理 msg=10
			}
		}
	});

	
	// 显示图片
	public static Handler phHandler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				imageView1.setImageBitmap(ColorShapeRecognition.Display_Bitmap[0]);                               ///显示图片bitmap1
				imageView2.setImageBitmap(ColorShapeRecognition.Display_Bitmap[1]);                               ///显示图片bitmap1
				imageView3.setImageBitmap(ColorShapeRecognition.Display_Bitmap[2]);                               ///显示图片bitmap1
				imageView4.setImageBitmap(ColorShapeRecognition.Display_Bitmap[3]);                               ///显示图片bitmap1
				imageView5.setImageBitmap(ColorShapeRecognition.Display_Bitmap[4]);                               ///显示图片bitmap1
				imageView6.setImageBitmap(ColorShapeRecognition.Display_Bitmap[5]);                               ///显示图片bitmap1
				imageView7.setImageBitmap(ColorShapeRecognition.Display_Bitmap[6]);                               ///显示图片bitmap1
				TextView.setText(ColorShapeRecognition.Result);
				TextView1.setText(ColorShapeRecognition.Process);
			}
			if (msg.what == 2) {
				imageView11.setImageBitmap(ColorShapeRecognition.Display_Bitmap[11]);                               ///显示图片bitmap1
				imageView12.setImageBitmap(ColorShapeRecognition.Display_Bitmap[12]);                               ///显示图片bitmap1
				imageView13.setImageBitmap(ColorShapeRecognition.Display_Bitmap[13]);                               ///显示图片bitmap1
				imageView14.setImageBitmap(ColorShapeRecognition.Display_Bitmap[14]);                               ///显示图片bitmap1
				imageView15.setImageBitmap(ColorShapeRecognition.Display_Bitmap[15]);                               ///显示图片bitmap1
				imageView16.setImageBitmap(ColorShapeRecognition.Display_Bitmap[16]);                               ///显示图片bitmap1
				imageView17.setImageBitmap(ColorShapeRecognition.Display_Bitmap[17]);                               ///显示图片bitmap1
				imageView18.setImageBitmap(ColorShapeRecognition.Display_Bitmap[18]);                               ///显示图片bitmap1
				imageView19.setImageBitmap(ColorShapeRecognition.Display_Bitmap[19]);                               ///显示图片bitmap1
				imageView20.setImageBitmap(ColorShapeRecognition.Display_Bitmap[20]);                               ///显示图片bitmap1
				imageView21.setImageBitmap(ColorShapeRecognition.Display_Bitmap[21]);                               ///显示图片bitmap1
				imageView22.setImageBitmap(ColorShapeRecognition.Display_Bitmap[22]);                               ///显示图片bitmap1
				imageView23.setImageBitmap(ColorShapeRecognition.Display_Bitmap[23]);                               ///显示图片bitmap1
				imageView24.setImageBitmap(ColorShapeRecognition.Display_Bitmap[24]);                               ///显示图片bitmap1
				imageView25.setImageBitmap(ColorShapeRecognition.Display_Bitmap[25]);                               ///显示图片bitmap1
				imageView26.setImageBitmap(ColorShapeRecognition.Display_Bitmap[26]);                               ///显示图片bitmap1
			}
			if (msg.what == 3) {
				imageView27.setImageBitmap(ColorShapeRecognition.Display_color_Bitmap[Bitmap_num]);                               ///显示图片bitmap1
				TextView2.setText(ColorShapeRecognition.Every_color[Bitmap_num]);
			}
			if (msg.what == 8) {
				imageView0.setImageBitmap(bitmap1);                               ///显示图片bitmap1
			}
		};
	};

	@Override
	public void onResume() {
		super.onResume();
		// 通过OpenCV引擎服务加载并初始化OpenCV类库，所谓OpenCV引擎服务即是
		//OpenCV_2.4.3.2_Manager_2.4_*.apk程序包，存在于OpenCV安装包的apk目录中
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this,
				mLoaderCallback);
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
		}

	// OpenCV类库加载并初始化成功后的回调函数，在此我们不进行任何操作
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {                          ///捕捉键盘被按下的事件
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {       ///按下的如果是BACK，同时没有重复（点后退键的时候，为了防止点得过快，触发两次后退事件，故做此设置）
			android.os.Process.killProcess(android.os.Process.myPid());              ///当应用不再使用时，通常需要关闭应用，首先获取当前进程的id，然后杀死该进程。 
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
