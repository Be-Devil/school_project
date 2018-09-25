package cn.gdit.goldsite;

/*
 * 文件编写作者
 * Lzz
 *
 * 日期
 * 2016年11月19日
 */

import java.io.IOException;
import java.util.Arrays;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.image.processing.ColorShapeRecognition;
import cn.image.processing.IClient;

import com.bkrcl.car_exampledemo.service.SearchService;
import com.bkrcl.control_car_video.camerautil.CameraCommandUtil;


@SuppressLint("Wakelock")
public class MainActivity extends Activity {
	public String IP = null;                                              // 摄像头旋转方法类
	public CameraCommandUtil cameraCommandUtil = null;                    // 广播名称
	public static final String A_S = "com.a_s";                           // 广播接收器
	public Client client;
	public IClient iclient;
	static public char c_car ='C';
	static public char z_car ='Z';
	private String IPCar;
	private WifiManager wifiManager;
	private DhcpInfo dhcpInfo;
	private ProgressDialog progressDialog = null;                           // 搜索进度
	public String srt ="123456";
	static boolean 启动标志位 = false;
	boolean 寻找标志位 = false;
	private ImageView show_image = null;
	public byte[] rbyte0 = new byte[40];
	public byte[] rbyte1 = new byte[40];
	public byte[] rbyte2 = new byte[40];
	public byte[] rfid_buf = new byte[16];
	static boolean 完成_指令 = false;
	public static long UltraSonic = 0;// 光照
	static int flag = 0 ;
	private static ImageView imageView = null;
	public byte[] sbyte = new byte[40];
	public int i=2;
	public int a=0;
	public static Bitmap bitmap1;
	public int 档位 = 0;

	static public int m1 = 3;
	static public String m2 = "A1B2C3";
	static public String m3 = "123456";
	static public String m4 = "A1B2C3";
	static public String m4z = "g20";
	static public String m4c = "g60";

	static public boolean test_car=false;
	static public String current_zccar = "b22";



	//@SuppressWarnings("deprecation")                                       ///在这里假设 onCreate（Bundle savedInstanceState）这个方法是被弃用了的方法，加上这个注解就表示不去检测这个方法是否被弃用
	@Override
	public void onCreate(Bundle savedInstanceState) {

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
		iclient = new IClient();
		client = new Client();                                               ///创建客户端对象
		socketThread.start();                                                ///启动套接字（socket）线程
		cameraCommandUtil = new CameraCommandUtil();
		search();                                                          /// 搜索摄像IP进度条
		imageView = (ImageView) findViewById(R.id.imageView);
		show_image=(ImageView) findViewById(R.id.imageView);
		show_image.setOnClickListener(new buttonListener());
		runThread.start();					//一键启动
		runThread0.start();
		init();

	}

	   public class buttonListener implements OnClickListener{

		  public void onClick(View v)
		  {
			 /* client.VoiceText("定时器清零");	//语音播报
			  client.LED(z_car,"计时清零");*/
			//  cameraCommandUtil.postHttp(IP, 35, 0);
			  摄像头转弯("c5","d52");
		  }
		}

		public void test1(View v){
			iclient.形状();
		}

		public void test2(View v){
			client.光档位(c_car,2);
		}

		public void test3(View v){
			client.mask_M(z_car,"c41");
			client.当前坐标(z_car,"f44");
			client.目标坐标(z_car, "f20");

			client.mask_M(c_car,"c41");
			client.当前坐标(c_car,"f64");
			client.目标坐标(c_car, "d40");

			client.etc打开(c_car);
			client.目标坐标(c_car, "b44");
			client.beep(c_car);
			client.mask_M(c_car,"c40");
			client.目标坐标(c_car, "d40");
			client.目标坐标(c_car, "f64");

			client.目标坐标(z_car, "f44");
		}


		public void test4(View v){
			红外警报(z_car,"d22","d3");
		}


		public void test5(View v){
			client.档位回传(car);弹幕(""+rbyte0[3]);
		}

		public void 启动按钮(View v){
			client.OK();
		}

	private Thread runThread = new Thread(new Runnable() {
		@Override
		public void run() {
				while (true) {
					if(启动标志位)
					{
						switch(flag)
						{
						case 20:
							client.mask_M(z_car,"c41");
							client.当前坐标(z_car,"f64");
							flag=6;break;
						case 21:
							client.mask_M(z_car,"c41");
							flag=0;break;
						case 1:
							cameraCommandUtil.postHttp(IP, 33, 0);
							client.mask_M(z_car,"c41");
							client.mask_M(c_car,"c41");
							client.当前坐标(z_car,"f13");
							client.VoiceText("任务执行开始");	//语音播报
							client.LED("计时开");
							client.道闸();
							client.目标坐标(z_car, "d21");
							flag=2;break;
						case 2:
							client.当前坐标(z_car,"d21");
							client.当前坐标(c_car,"d43");
							client.目标坐标(c_car, "f60");
							client.语音函数(z_car);
							client.ack(z_car);
							flag=3;break;
						case 3:				// 从车跑到b2
							client.rfid打开(c_car);
							client.mask_M(z_car,"f61");
							client.目标坐标(z_car, "c60");
							client.mask_M(z_car,"d20");
							client.数据回传(z_car,"位置");
							摄像头转弯("e3", current_zccar);
							client.目标坐标(c_car, "f20");
							client.目标坐标(c_car, "b20");
							client.rfid(c_car);
							client.光档位(c_car,m1);
							flag=4;break;
						case 4:
							iclient.二维码侧();
							cameraCommandUtil.postHttp(IP, 33, 0);
							client.LED("2显示"+m2);
							flag=5;break;
						case 5:
							client.目标坐标(c_car, "f20");
							client.目标坐标(c_car, "f50");
							client.mask_M(z_car,"f60");
							client.寻迹(c_car);
							client.状态(c_car);
							client.LED("显示距离"+UltraSonic);
							client.前进(c_car, 80, 27);
							flag=6;break;
						case 6:
							cameraCommandUtil.postHttp(IP, 33, 0);
							client.目标坐标(z_car, "d44");
							client.mask_M(z_car,"c40");
							client.etc打开(z_car);
							client.目标坐标(z_car, "b44");
							iclient.形状();
							client.照片(z_car,"下");
							iclient.车牌();
							client.照片(z_car,"下");
							client.mask_M(z_car,"c41");
							flag=7;break;
						case 7:
							client.当前坐标(c_car,"f63");
							client.当前坐标(z_car,"b44");
							client.左转(c_car,3,4);
							client.前进(c_car, 80, 27);
							client.当前坐标(c_car,"e64");
							client.立体显示位置(c_car,"e5");
							client.立体显示转弯(c_car);
							client.车牌1(c_car,m3);
							client.车牌2(c_car,m3);
							client.车牌红外(c_car);
							client.立体显示转弯(c_car);
							client.寻迹(c_car);
							client.前进(c_car, 80, 27);
							flag=8;break;
						case 8:
							client.当前坐标(c_car,"d64");
							client.当前坐标(z_car,"b44");
							client.mask_M(c_car,"c41");
							client.目标坐标(z_car,"f20");
							client.目标坐标(z_car,"f40");
							client.目标坐标(z_car,"f60");
							iclient.二维码前();
							client.TFT_control("显示车牌"+m4);
							flag=9;break;
						case 9:
							client.当前坐标(c_car,"d64");
							client.当前坐标(z_car,"f63");
							client.mask_M(c_car,"c41");
							client.目标坐标(c_car,"d22");
							client.前进(c_car, 80, 12);
							client.当前坐标(c_car,"c22");
							client.立体显示位置(c_car,"c3");
							client.立体显示转弯(c_car);
							client.红外(c_car);
							client.ack(c_car);
							client.红外(c_car);
							client.立体显示转弯(c_car);
							client.寻迹(c_car);
							client.前进(c_car, 80, 27);
							flag=10;break;
						case 10:
							client.当前坐标(c_car,"f22");
							client.当前坐标(z_car,"f63");
							client.目标坐标(z_car,"d40");
							client.目标坐标(z_car,"d60");
							iclient.交通灯();
							flag=11;break;
						case 11:
							client.目标坐标(c_car,"e40");
							client.前进(c_car, 80, 12);
							client.mask_M(z_car,"e41");
							client.目标坐标(z_car,m4z);
							client.目标坐标(c_car,m4c);
							client.beep(c_car);
							flag=12;break;
						case 12:
							client.无线电();
							client.VoiceText("任务执行完毕");	//语音播报
							client.LED( "计时关");
							flag = 0;break;

						/*case 1:
							cameraCommandUtil.postHttp(IP, 35, 0);
							client.beep(c_car);
							client.LED( "计时开");
							client.VoiceText("任务执行开始");	//语音播报
							flag++;break;
						case 2://测距
							client.当前坐标(c_car,"g64");
							client.目标坐标(c_car, "d60");
							client.目标坐标(c_car, "d30");
							client.寻迹(c_car);
							client.状态(c_car);
							client.LED("显示距离"+UltraSonic);
							client.前进(c_car, 80, 27);
							flag++;break;
						case 3://扫码获取信息
							client.当前坐标(c_car,"d21");
							client.目标坐标(c_car, "b40");
							client.当前坐标(z_car,"g24");
							client.目标坐标(z_car, "d40");
							client.目标坐标(z_car, "d20");
							iclient.二维码();				//需要得到m1
							flag++;break;
						case 4://调档
							client.道闸();
							client.目标坐标(c_car,"b70");
							//测量照明档位（M2)
							client.光档位(c_car,m1);
							client.道闸();
							client.目标坐标(c_car, "b41");
							flag++;break;
						case 5://图形车牌识别
							client.目标坐标(z_car, "d40");
							client.目标坐标(z_car, "f40");
							iclient.形状();			//(M3)
							client.照片(z_car,"下");
							iclient.车牌();			//并发送车牌信息
							client.照片(z_car,"下");
							flag++;break;
						case 6://车牌立体显示
							client.目标坐标(c_car,"b20");
							client.目标坐标(c_car,"d20");
							client.前进(c_car, 80, 27);
							client.立体显示转弯(c_car);
							client.车牌1(c_car,m4);
							client.车牌2(c_car,m4);
							client.车牌红外(c_car);
							client.立体显示转弯(c_car);
							client.LED("2显示"+m3);	//zigbee显示器第二行图形信息
							client.寻迹(c_car);
							client.状态(c_car);
							client.LED("显示距离"+UltraSonic);//发送距离（LED第二行）
							client.前进(c_car, 80, 27);
							client.当前坐标(c_car, "f22");
							client.LED("2显示"+m4);//zigbee图形信息
							flag++;break;
						case 7://交通灯
							client.目标坐标(z_car,"d40");
							client.目标坐标(z_car,"d63");
							iclient.交通灯();		//交通灯识别，然后走到主车走到c60
							client.目标坐标(z_car,"d60");
							client.目标坐标(z_car,"c60");
							flag++;break;
						case 8:
							cameraCommandUtil.postHttp(IP, 33, 0);
							iclient.二维码1();
							flag++;break;
						case 9:
							client.目标坐标(c_car,"f40");
							client.目标坐标(z_car,m5z);
							client.目标坐标(c_car,m5c);
							client.LED("计时关");
							client.VoiceText("任务执行结束");	//语音播报
							flag=11;break;*/
						}

					}

			}
			}});






	private Thread runThread0 = new Thread(new Runnable() {
		@Override
		public void run() {
			while(true){
				if(Client.connect_flag)
				完成指令();
			}
		}
		});

		public void 完成指令(){
				try {
					Arrays.fill(rbyte0, (byte) 0);
					client.bInputStream.read(rbyte0);
					if (rbyte0[0] == (byte) 0x55 && (rbyte0[1] == (byte) 0xAA||rbyte0[1] == (byte) 0x02)
							&&rbyte0[6]==(rbyte0[5]+rbyte0[4]+rbyte0[3]+rbyte0[2])%256&&rbyte0[2] == (byte) 0x37)
					{
						完成_指令 = true;
					}
					else if(rbyte0[0] == (byte)0x55 && (rbyte0[1] == (byte)0x82))
						//&&rbyte0[6]==(rbyte0[2]+rbyte0[3]+rbyte0[4]+rbyte0[5])%256)
					{
						if(rbyte0[2]==0x01)
						{
							current_zccar=(char)rbyte0[3]+""+(char)rbyte0[4]+""+rbyte0[5]+"";
						}
						if(rbyte0[2]==0x02)
						{
							档位 = rbyte0[3];//档位回传，rbyte0[3]为挡位信息
						}
						完成_指令 = true;
					}
					else if (rbyte0[10] == (byte) 0xbb&&rbyte0[1] != (byte) 0x81)
					{
						for(int i=0;i<10;i++)
							rbyte2[i] = rbyte0[i];
						UltraSonic = rbyte2[5] & 0xff;
						UltraSonic = UltraSonic << 8;
						UltraSonic += rbyte2[4] & 0xff;
						完成_指令 = true;
					}
					else if (rbyte0[18] == (byte) 0xbb&&rbyte0[1] == (byte) 0x81)
					{
						for(int i=0;i<16;i++)
							rfid_buf[i] = rbyte0[i+2];
//						client.send_voice(client.bytesend(rfid_buf));
						if((short)rfid_buf[0]!=0)
						m1=(short)rfid_buf[0];
//						client.VoiceText(""+m1);client.delay(500);
						完成_指令 = true;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

		}
		}

		public void 摄像头转弯(String x,String y)			//标志物地点，小车地点方向
		{
			char current_y=y.charAt(0);
			char current_x=y.charAt(1);
			char current_d=y.charAt(2);

			char LITISHOW_Y=x.charAt(0);
			char LITISHOW_X=x.charAt(1);

			if( ((current_y<LITISHOW_Y)&&(current_d=='1'))||
				((current_y>LITISHOW_Y)&&(current_d=='3'))||
				((current_x<LITISHOW_X)&&(current_d=='2'))||
				((current_x>LITISHOW_X)&&(current_d=='4')))
				cameraCommandUtil.postHttp(IP, 35, 0);

			else  if( ((current_y>LITISHOW_Y)&&(current_d=='1'))||
					  ((current_y<LITISHOW_Y)&&(current_d=='3'))||
					  ((current_x>LITISHOW_X)&&(current_d=='2'))||
					  ((current_x<LITISHOW_X)&&(current_d=='4')))
				cameraCommandUtil.postHttp(IP,39 , 0);
		}


	public void yanchi(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void 控制(View v){
		setContentView(R.layout.second);
		init();
		imageView = (ImageView) findViewById(R.id.imageView);
	}


	////////////////////////固定搭配/////////////////////////////////////////////////
	private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {   ///创建广播对象

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			IP = arg1.getStringExtra("IP");                               ///IP接收
//			progressDialog.dismiss();                                     ///让进度条停下来
			phThread1.start();                                            ///图像接收

		}
	};

	private Thread socketThread = new Thread(new Runnable() {              ///创建子线程
		@Override
		public void run() {
			client.connect( IPCar);
		}
	});

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

	// 显示图片
	public static Handler phHandler1 = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 10) {
				imageView.setImageBitmap(bitmap1);                               ///显示图片bitmap1
			}
		};
	};

	private Thread phThread1 = new Thread(new Runnable() {                    ///匿名类

		@Override
		public void run() {
			while (true) {
				bitmap1 = cameraCommandUtil.httpForImage(IP);                ///接受摄像头当前图片
				phHandler1.sendEmptyMessage(10);                             ///异步消息处理 msg=10
			}
		}
	});
	public static String 显示 = "";
	public static String speed = "";
	public static String coded_disc = "";
	public static char car = 'Z';
	public static TextView TextView = null;
	private EditText speed_data, coded_disc_data,test1,test2,test3;

	public void init(){
	TextView = (TextView) findViewById(R.id.TextView);
	speed_data = (EditText)findViewById(R.id.speed_data);
	coded_disc_data = (EditText)findViewById(R.id.coded_disc_data);
	test1 = (EditText)findViewById(R.id.test1);
	test2 = (EditText)findViewById(R.id.test2);
	test3 = (EditText)findViewById(R.id.test3);
	}

	private int getSpeed() {
		String src = speed_data.getText().toString();
		int speed = 40;
		if (!src.equals("")) {
			speed = Integer.parseInt(src);
		} else {
			Toast.makeText(MainActivity.this, "请输入速度值", 500).show();
		}
		return speed;
	}

	private int getEncoder() {
		String src = coded_disc_data.getText().toString();
		int encoder = 70;
		if (!src.equals("")) {
			encoder = Integer.parseInt(src);
		} else {
			Toast.makeText(MainActivity.this, "请输入码盘值", 500).show();
		}
		return encoder;
	}

	public void 弹幕(String x) {
			Toast.makeText(MainActivity.this, x, 500).show();
	}
	
	private void 显示(String x) {
		显示 = 显示 + x +"    ";phHandler2.sendEmptyMessage(1);
}


	private String gettest1() {
		String src = test1.getText().toString();
		return src;
	}

	private String gettest2() {
		String src = test2.getText().toString();
		return src;
	}

	private String gettest3() {
		String src = test3.getText().toString();
		return src;
	}
	public static Handler phHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				TextView.setText(显示);
			}
		};
	};

	public void 清空(View v){显示 = "";phHandler2.sendEmptyMessage(1);}
	public void 主车控制(View v){car = z_car;显示("主车控制");}
	public void 从车控制(View v){car = c_car;显示("从车控制");}
	public void 前进(View v){client.前进(car,getSpeed(),getEncoder());}
	public void 左转(View v){client.左转(car,1,4);}
	public void 右转(View v){client.左转(car,1,2);}
	public void 后退(View v){client.后退(car,getSpeed(),getEncoder());}
	public void 寻迹(View v){client.寻迹(car);}
	public void ack(View v){client.ack(car);}
	public void 状态(View v){client.状态(car);显示("距离为"+UltraSonic); }
	public void 道闸(View v){client.道闸();}
	public void 随机语音(View v){client.语音控制("随机语音","");}
	public void 特定语音(View v){client.语音控制("特定语音","语音唤醒");}
	public void 无线电(View v){client.无线电();}
	public void 计时开(View v){client.LED("计时开");}
	public void 计时关(View v){client.LED("计时关");}
	public void 计时清零(View v){client.LED("计时清零");}
	public void LED显示(View v){if(gettest1().length() == 6)client.LED("2显示"+gettest1());else 弹幕("在参数1输入xxxxxx,eg:123456");}
	public void LED显示距离(View v){client.LED("显示距离"+UltraSonic);}
	public void 红外警报(View v){if(gettest1().length() == 3&&gettest2().length() == 2)红外警报(car,gettest1(),gettest2());else 弹幕("在参数1输入xxx,eg:b21,参数2输入xx,eg:d3");}
	public void 立体车牌(View v){if(gettest1().length() == 3&&gettest2().length() == 2&&gettest3().length()==6)立体车牌(car,gettest1(),gettest2(),gettest3());else 弹幕("在参数1输入xxx,eg:b21,参数2输入xx,eg:d3,参数3输入xxxxxx,eg:123456");}
	public void 红外(View v){client.红外(car);}
	public void beep(View v){client.beep(car);}
	public void 当前坐标(View v){if(gettest1().length() == 3)client.当前坐标(car,gettest1());else 弹幕("在参数1输入xxx,eg:b20");显示("当前坐标为"+ gettest1());}
	public void 目标坐标(View v){if(gettest1().length() == 3)client.目标坐标(car,gettest1());else 弹幕("在参数1输入xxx,eg:b20");显示("目标坐标为"+ gettest1());}
	public void mask_M(View v){if(gettest1().length() == 3)client.mask_M(car,gettest1());else 弹幕("在参数1输入xxx,eg:b20");}
	public void dir_change(View v){if(gettest1().length() == 2)client.dir_change(car,gettest1());else 弹幕("在参数1输入xxx,eg:20");}
	public void 车牌红外(View v){client.车牌红外(car);}
	public void 光档位(View v){if(gettest1().length() == 1)client.光档位(car,gettest1().charAt(0)-'0');else 弹幕("在参数1输入x,eg:1");}
	public void etc打开(View v){client.etc打开(car);}
	public void rfid打开(View v){client.rfid打开(car);}
	public void rfid(View v){client.rfid(car);}
	public void 位置回传(View v){client.数据回传(car,"位置");显示("当前位置为"+ current_zccar);}
	public void 照片下翻(View v){client.照片(car,"下");}
	public void 档位回传(View v){client.档位回传(car);显示("档位为"+ 档位);}
	public void 语音控制(View v){client.语音函数(car);}
	public void TFT指定图片(View v){if(gettest1().length() == 2)client.TFT_control("指定图片"+gettest1());else 弹幕("在参数1输入xx,eg:01");}
	public void TFT上翻(View v){client.TFT_control("图片上翻");}
	public void TFT显示距离(View v){client.TFT_control("显示距离"+UltraSonic);}
	public void TFT显示数据(View v){if(gettest1().length() == 6)client.TFT_control("显示数据"+gettest1());else 弹幕("在参数1输入xxxxxx,eg:012345");}
	public void TFT计时开(View v){client.TFT_control("计时开");}
	public void TFT计时关(View v){client.TFT_control("计时关");}
	public void 二维码前(View v){iclient.二维码前();}
	public void 二维码侧(View v){iclient.二维码侧();}
	public void 形状颜色(View v){iclient.形状();}
	public void 车牌(View v){iclient.车牌();}

	public void 左(View v){cameraCommandUtil.postHttp(IP, 4, 1);}
	public void 右(View v){cameraCommandUtil.postHttp(IP, 6, 1);}
	public void 上(View v){cameraCommandUtil.postHttp(IP, 0, 1);}
	public void 下(View v){cameraCommandUtil.postHttp(IP, 2, 1);}
	
	public void set1(View v){cameraCommandUtil.postHttp(IP, 34, 0);}
	public void set2(View v){cameraCommandUtil.postHttp(IP, 36, 0);}
	public void set3(View v){cameraCommandUtil.postHttp(IP, 38, 0);}
	public void set4(View v){cameraCommandUtil.postHttp(IP, 40, 0);}

	public void wei1(View v){cameraCommandUtil.postHttp(IP, 33, 0);}
	public void wei2(View v){cameraCommandUtil.postHttp(IP, 35, 0);}
	public void wei3(View v){cameraCommandUtil.postHttp(IP, 37, 0);}
	public void wei4(View v){cameraCommandUtil.postHttp(IP, 39, 0);}
	
	
	public void 返回(View v){
	setContentView(R.layout.activity_main);
	imageView = (ImageView) findViewById(R.id.imageView);
	show_image=(ImageView) findViewById(R.id.imageView); }

	public void 红外警报(char x,String y,String y1){	
	client.当前坐标(x,y);
	client.前进(x, 80, 12);
	if((y.charAt(2)-'0')==1)
		y=y.charAt(0)+""+(char)(y.charAt(1)-1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==2)
		y=(char)(y.charAt(0)+1)+""+y.charAt(1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==3)
		y=y.charAt(0)+""+(char)(y.charAt(1)+1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==4)
		y=(char)(y.charAt(0)-1)+""+y.charAt(1)+""+y.charAt(2);
	client.当前坐标(x,y);
	client.立体显示位置(x,y1);
	client.立体显示转弯(x);
	client.红外(x);
	client.ack(x);
	client.红外(x);
	client.立体显示转弯(x);
	client.寻迹(x);
	client.前进(x, 80, 27);
	if((y.charAt(2)-'0')==1)
		y=y.charAt(0)+""+(char)(y.charAt(1)-1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==2)
		y=(char)(y.charAt(0)+1)+""+y.charAt(1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==3)
		y=y.charAt(0)+""+(char)(y.charAt(1)+1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==4)
		y=(char)(y.charAt(0)-1)+""+y.charAt(1)+""+y.charAt(2);
	client.当前坐标(x,y);
	}
	
	public void 立体车牌(char x,String y,String y1,String y2){	
		client.当前坐标(x,y);
		client.前进(x, 80, 12);
		if((y.charAt(2)-'0')==1)
			y=y.charAt(0)+""+(char)(y.charAt(1)-1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==2)
			y=(char)(y.charAt(0)+1)+""+y.charAt(1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==3)
			y=y.charAt(0)+""+(char)(y.charAt(1)+1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==4)
			y=(char)(y.charAt(0)-1)+""+y.charAt(1)+""+y.charAt(2);
		client.当前坐标(x,y);
		client.立体显示位置(x,y1);
		client.立体显示转弯(x);
		client.车牌1(x,y2);
		client.车牌2(x,y2);
		client.车牌红外(x);
		client.立体显示转弯(x);
		client.寻迹(x);
		client.前进(x, 80, 27);
		if((y.charAt(2)-'0')==1)
			y=y.charAt(0)+""+(char)(y.charAt(1)-1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==2)
			y=(char)(y.charAt(0)+1)+""+y.charAt(1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==3)
			y=y.charAt(0)+""+(char)(y.charAt(1)+1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==4)
			y=(char)(y.charAt(0)-1)+""+y.charAt(1)+""+y.charAt(2);
		client.当前坐标(x,y);
	}
//	public void mask_M(View v){client.mask_M();}

}


