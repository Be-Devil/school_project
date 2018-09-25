package cn.gdit.goldsite;

/*
 * �ļ���д����
 * Lzz
 *
 * ����
 * 2016��11��19��
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
	public String IP = null;                                              // ����ͷ��ת������
	public CameraCommandUtil cameraCommandUtil = null;                    // �㲥����
	public static final String A_S = "com.a_s";                           // �㲥������
	public Client client;
	public IClient iclient;
	static public char c_car ='C';
	static public char z_car ='Z';
	private String IPCar;
	private WifiManager wifiManager;
	private DhcpInfo dhcpInfo;
	private ProgressDialog progressDialog = null;                           // ��������
	public String srt ="123456";
	static boolean ������־λ = false;
	boolean Ѱ�ұ�־λ = false;
	private ImageView show_image = null;
	public byte[] rbyte0 = new byte[40];
	public byte[] rbyte1 = new byte[40];
	public byte[] rbyte2 = new byte[40];
	public byte[] rfid_buf = new byte[16];
	static boolean ���_ָ�� = false;
	public static long UltraSonic = 0;// ����
	static int flag = 0 ;
	private static ImageView imageView = null;
	public byte[] sbyte = new byte[40];
	public int i=2;
	public int a=0;
	public static Bitmap bitmap1;
	public int ��λ = 0;

	static public int m1 = 3;
	static public String m2 = "A1B2C3";
	static public String m3 = "123456";
	static public String m4 = "A1B2C3";
	static public String m4z = "g20";
	static public String m4c = "g60";

	static public boolean test_car=false;
	static public String current_zccar = "b22";



	//@SuppressWarnings("deprecation")                                       ///��������� onCreate��Bundle savedInstanceState����������Ǳ������˵ķ������������ע��ͱ�ʾ��ȥ�����������Ƿ�����
	@Override
	public void onCreate(Bundle savedInstanceState) {

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
		iclient = new IClient();
		client = new Client();                                               ///�����ͻ��˶���
		socketThread.start();                                                ///�����׽��֣�socket���߳�
		cameraCommandUtil = new CameraCommandUtil();
		search();                                                          /// ��������IP������
		imageView = (ImageView) findViewById(R.id.imageView);
		show_image=(ImageView) findViewById(R.id.imageView);
		show_image.setOnClickListener(new buttonListener());
		runThread.start();					//һ������
		runThread0.start();
		init();

	}

	   public class buttonListener implements OnClickListener{

		  public void onClick(View v)
		  {
			 /* client.VoiceText("��ʱ������");	//��������
			  client.LED(z_car,"��ʱ����");*/
			//  cameraCommandUtil.postHttp(IP, 35, 0);
			  ����ͷת��("c5","d52");
		  }
		}

		public void test1(View v){
			iclient.��״();
		}

		public void test2(View v){
			client.�⵵λ(c_car,2);
		}

		public void test3(View v){
			client.mask_M(z_car,"c41");
			client.��ǰ����(z_car,"f44");
			client.Ŀ������(z_car, "f20");

			client.mask_M(c_car,"c41");
			client.��ǰ����(c_car,"f64");
			client.Ŀ������(c_car, "d40");

			client.etc��(c_car);
			client.Ŀ������(c_car, "b44");
			client.beep(c_car);
			client.mask_M(c_car,"c40");
			client.Ŀ������(c_car, "d40");
			client.Ŀ������(c_car, "f64");

			client.Ŀ������(z_car, "f44");
		}


		public void test4(View v){
			���⾯��(z_car,"d22","d3");
		}


		public void test5(View v){
			client.��λ�ش�(car);��Ļ(""+rbyte0[3]);
		}

		public void ������ť(View v){
			client.OK();
		}

	private Thread runThread = new Thread(new Runnable() {
		@Override
		public void run() {
				while (true) {
					if(������־λ)
					{
						switch(flag)
						{
						case 20:
							client.mask_M(z_car,"c41");
							client.��ǰ����(z_car,"f64");
							flag=6;break;
						case 21:
							client.mask_M(z_car,"c41");
							flag=0;break;
						case 1:
							cameraCommandUtil.postHttp(IP, 33, 0);
							client.mask_M(z_car,"c41");
							client.mask_M(c_car,"c41");
							client.��ǰ����(z_car,"f13");
							client.VoiceText("����ִ�п�ʼ");	//��������
							client.LED("��ʱ��");
							client.��բ();
							client.Ŀ������(z_car, "d21");
							flag=2;break;
						case 2:
							client.��ǰ����(z_car,"d21");
							client.��ǰ����(c_car,"d43");
							client.Ŀ������(c_car, "f60");
							client.��������(z_car);
							client.ack(z_car);
							flag=3;break;
						case 3:				// �ӳ��ܵ�b2
							client.rfid��(c_car);
							client.mask_M(z_car,"f61");
							client.Ŀ������(z_car, "c60");
							client.mask_M(z_car,"d20");
							client.���ݻش�(z_car,"λ��");
							����ͷת��("e3", current_zccar);
							client.Ŀ������(c_car, "f20");
							client.Ŀ������(c_car, "b20");
							client.rfid(c_car);
							client.�⵵λ(c_car,m1);
							flag=4;break;
						case 4:
							iclient.��ά���();
							cameraCommandUtil.postHttp(IP, 33, 0);
							client.LED("2��ʾ"+m2);
							flag=5;break;
						case 5:
							client.Ŀ������(c_car, "f20");
							client.Ŀ������(c_car, "f50");
							client.mask_M(z_car,"f60");
							client.Ѱ��(c_car);
							client.״̬(c_car);
							client.LED("��ʾ����"+UltraSonic);
							client.ǰ��(c_car, 80, 27);
							flag=6;break;
						case 6:
							cameraCommandUtil.postHttp(IP, 33, 0);
							client.Ŀ������(z_car, "d44");
							client.mask_M(z_car,"c40");
							client.etc��(z_car);
							client.Ŀ������(z_car, "b44");
							iclient.��״();
							client.��Ƭ(z_car,"��");
							iclient.����();
							client.��Ƭ(z_car,"��");
							client.mask_M(z_car,"c41");
							flag=7;break;
						case 7:
							client.��ǰ����(c_car,"f63");
							client.��ǰ����(z_car,"b44");
							client.��ת(c_car,3,4);
							client.ǰ��(c_car, 80, 27);
							client.��ǰ����(c_car,"e64");
							client.������ʾλ��(c_car,"e5");
							client.������ʾת��(c_car);
							client.����1(c_car,m3);
							client.����2(c_car,m3);
							client.���ƺ���(c_car);
							client.������ʾת��(c_car);
							client.Ѱ��(c_car);
							client.ǰ��(c_car, 80, 27);
							flag=8;break;
						case 8:
							client.��ǰ����(c_car,"d64");
							client.��ǰ����(z_car,"b44");
							client.mask_M(c_car,"c41");
							client.Ŀ������(z_car,"f20");
							client.Ŀ������(z_car,"f40");
							client.Ŀ������(z_car,"f60");
							iclient.��ά��ǰ();
							client.TFT_control("��ʾ����"+m4);
							flag=9;break;
						case 9:
							client.��ǰ����(c_car,"d64");
							client.��ǰ����(z_car,"f63");
							client.mask_M(c_car,"c41");
							client.Ŀ������(c_car,"d22");
							client.ǰ��(c_car, 80, 12);
							client.��ǰ����(c_car,"c22");
							client.������ʾλ��(c_car,"c3");
							client.������ʾת��(c_car);
							client.����(c_car);
							client.ack(c_car);
							client.����(c_car);
							client.������ʾת��(c_car);
							client.Ѱ��(c_car);
							client.ǰ��(c_car, 80, 27);
							flag=10;break;
						case 10:
							client.��ǰ����(c_car,"f22");
							client.��ǰ����(z_car,"f63");
							client.Ŀ������(z_car,"d40");
							client.Ŀ������(z_car,"d60");
							iclient.��ͨ��();
							flag=11;break;
						case 11:
							client.Ŀ������(c_car,"e40");
							client.ǰ��(c_car, 80, 12);
							client.mask_M(z_car,"e41");
							client.Ŀ������(z_car,m4z);
							client.Ŀ������(c_car,m4c);
							client.beep(c_car);
							flag=12;break;
						case 12:
							client.���ߵ�();
							client.VoiceText("����ִ�����");	//��������
							client.LED( "��ʱ��");
							flag = 0;break;

						/*case 1:
							cameraCommandUtil.postHttp(IP, 35, 0);
							client.beep(c_car);
							client.LED( "��ʱ��");
							client.VoiceText("����ִ�п�ʼ");	//��������
							flag++;break;
						case 2://���
							client.��ǰ����(c_car,"g64");
							client.Ŀ������(c_car, "d60");
							client.Ŀ������(c_car, "d30");
							client.Ѱ��(c_car);
							client.״̬(c_car);
							client.LED("��ʾ����"+UltraSonic);
							client.ǰ��(c_car, 80, 27);
							flag++;break;
						case 3://ɨ���ȡ��Ϣ
							client.��ǰ����(c_car,"d21");
							client.Ŀ������(c_car, "b40");
							client.��ǰ����(z_car,"g24");
							client.Ŀ������(z_car, "d40");
							client.Ŀ������(z_car, "d20");
							iclient.��ά��();				//��Ҫ�õ�m1
							flag++;break;
						case 4://����
							client.��բ();
							client.Ŀ������(c_car,"b70");
							//����������λ��M2)
							client.�⵵λ(c_car,m1);
							client.��բ();
							client.Ŀ������(c_car, "b41");
							flag++;break;
						case 5://ͼ�γ���ʶ��
							client.Ŀ������(z_car, "d40");
							client.Ŀ������(z_car, "f40");
							iclient.��״();			//(M3)
							client.��Ƭ(z_car,"��");
							iclient.����();			//�����ͳ�����Ϣ
							client.��Ƭ(z_car,"��");
							flag++;break;
						case 6://����������ʾ
							client.Ŀ������(c_car,"b20");
							client.Ŀ������(c_car,"d20");
							client.ǰ��(c_car, 80, 27);
							client.������ʾת��(c_car);
							client.����1(c_car,m4);
							client.����2(c_car,m4);
							client.���ƺ���(c_car);
							client.������ʾת��(c_car);
							client.LED("2��ʾ"+m3);	//zigbee��ʾ���ڶ���ͼ����Ϣ
							client.Ѱ��(c_car);
							client.״̬(c_car);
							client.LED("��ʾ����"+UltraSonic);//���;��루LED�ڶ��У�
							client.ǰ��(c_car, 80, 27);
							client.��ǰ����(c_car, "f22");
							client.LED("2��ʾ"+m4);//zigbeeͼ����Ϣ
							flag++;break;
						case 7://��ͨ��
							client.Ŀ������(z_car,"d40");
							client.Ŀ������(z_car,"d63");
							iclient.��ͨ��();		//��ͨ��ʶ��Ȼ���ߵ������ߵ�c60
							client.Ŀ������(z_car,"d60");
							client.Ŀ������(z_car,"c60");
							flag++;break;
						case 8:
							cameraCommandUtil.postHttp(IP, 33, 0);
							iclient.��ά��1();
							flag++;break;
						case 9:
							client.Ŀ������(c_car,"f40");
							client.Ŀ������(z_car,m5z);
							client.Ŀ������(c_car,m5c);
							client.LED("��ʱ��");
							client.VoiceText("����ִ�н���");	//��������
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
				���ָ��();
			}
		}
		});

		public void ���ָ��(){
				try {
					Arrays.fill(rbyte0, (byte) 0);
					client.bInputStream.read(rbyte0);
					if (rbyte0[0] == (byte) 0x55 && (rbyte0[1] == (byte) 0xAA||rbyte0[1] == (byte) 0x02)
							&&rbyte0[6]==(rbyte0[5]+rbyte0[4]+rbyte0[3]+rbyte0[2])%256&&rbyte0[2] == (byte) 0x37)
					{
						���_ָ�� = true;
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
							��λ = rbyte0[3];//��λ�ش���rbyte0[3]Ϊ��λ��Ϣ
						}
						���_ָ�� = true;
					}
					else if (rbyte0[10] == (byte) 0xbb&&rbyte0[1] != (byte) 0x81)
					{
						for(int i=0;i<10;i++)
							rbyte2[i] = rbyte0[i];
						UltraSonic = rbyte2[5] & 0xff;
						UltraSonic = UltraSonic << 8;
						UltraSonic += rbyte2[4] & 0xff;
						���_ָ�� = true;
					}
					else if (rbyte0[18] == (byte) 0xbb&&rbyte0[1] == (byte) 0x81)
					{
						for(int i=0;i<16;i++)
							rfid_buf[i] = rbyte0[i+2];
//						client.send_voice(client.bytesend(rfid_buf));
						if((short)rfid_buf[0]!=0)
						m1=(short)rfid_buf[0];
//						client.VoiceText(""+m1);client.delay(500);
						���_ָ�� = true;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

		}
		}

		public void ����ͷת��(String x,String y)			//��־��ص㣬С���ص㷽��
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

	public void ����(View v){
		setContentView(R.layout.second);
		init();
		imageView = (ImageView) findViewById(R.id.imageView);
	}


	////////////////////////�̶�����/////////////////////////////////////////////////
	private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {   ///�����㲥����

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			IP = arg1.getStringExtra("IP");                               ///IP����
//			progressDialog.dismiss();                                     ///�ý�����ͣ����
			phThread1.start();                                            ///ͼ�����

		}
	};

	private Thread socketThread = new Thread(new Runnable() {              ///�������߳�
		@Override
		public void run() {
			client.connect( IPCar);
		}
	});

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

	// ��ʾͼƬ
	public static Handler phHandler1 = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 10) {
				imageView.setImageBitmap(bitmap1);                               ///��ʾͼƬbitmap1
			}
		};
	};

	private Thread phThread1 = new Thread(new Runnable() {                    ///������

		@Override
		public void run() {
			while (true) {
				bitmap1 = cameraCommandUtil.httpForImage(IP);                ///��������ͷ��ǰͼƬ
				phHandler1.sendEmptyMessage(10);                             ///�첽��Ϣ���� msg=10
			}
		}
	});
	public static String ��ʾ = "";
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
			Toast.makeText(MainActivity.this, "�������ٶ�ֵ", 500).show();
		}
		return speed;
	}

	private int getEncoder() {
		String src = coded_disc_data.getText().toString();
		int encoder = 70;
		if (!src.equals("")) {
			encoder = Integer.parseInt(src);
		} else {
			Toast.makeText(MainActivity.this, "����������ֵ", 500).show();
		}
		return encoder;
	}

	public void ��Ļ(String x) {
			Toast.makeText(MainActivity.this, x, 500).show();
	}
	
	private void ��ʾ(String x) {
		��ʾ = ��ʾ + x +"    ";phHandler2.sendEmptyMessage(1);
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
				TextView.setText(��ʾ);
			}
		};
	};

	public void ���(View v){��ʾ = "";phHandler2.sendEmptyMessage(1);}
	public void ��������(View v){car = z_car;��ʾ("��������");}
	public void �ӳ�����(View v){car = c_car;��ʾ("�ӳ�����");}
	public void ǰ��(View v){client.ǰ��(car,getSpeed(),getEncoder());}
	public void ��ת(View v){client.��ת(car,1,4);}
	public void ��ת(View v){client.��ת(car,1,2);}
	public void ����(View v){client.����(car,getSpeed(),getEncoder());}
	public void Ѱ��(View v){client.Ѱ��(car);}
	public void ack(View v){client.ack(car);}
	public void ״̬(View v){client.״̬(car);��ʾ("����Ϊ"+UltraSonic); }
	public void ��բ(View v){client.��բ();}
	public void �������(View v){client.��������("�������","");}
	public void �ض�����(View v){client.��������("�ض�����","��������");}
	public void ���ߵ�(View v){client.���ߵ�();}
	public void ��ʱ��(View v){client.LED("��ʱ��");}
	public void ��ʱ��(View v){client.LED("��ʱ��");}
	public void ��ʱ����(View v){client.LED("��ʱ����");}
	public void LED��ʾ(View v){if(gettest1().length() == 6)client.LED("2��ʾ"+gettest1());else ��Ļ("�ڲ���1����xxxxxx,eg:123456");}
	public void LED��ʾ����(View v){client.LED("��ʾ����"+UltraSonic);}
	public void ���⾯��(View v){if(gettest1().length() == 3&&gettest2().length() == 2)���⾯��(car,gettest1(),gettest2());else ��Ļ("�ڲ���1����xxx,eg:b21,����2����xx,eg:d3");}
	public void ���峵��(View v){if(gettest1().length() == 3&&gettest2().length() == 2&&gettest3().length()==6)���峵��(car,gettest1(),gettest2(),gettest3());else ��Ļ("�ڲ���1����xxx,eg:b21,����2����xx,eg:d3,����3����xxxxxx,eg:123456");}
	public void ����(View v){client.����(car);}
	public void beep(View v){client.beep(car);}
	public void ��ǰ����(View v){if(gettest1().length() == 3)client.��ǰ����(car,gettest1());else ��Ļ("�ڲ���1����xxx,eg:b20");��ʾ("��ǰ����Ϊ"+ gettest1());}
	public void Ŀ������(View v){if(gettest1().length() == 3)client.Ŀ������(car,gettest1());else ��Ļ("�ڲ���1����xxx,eg:b20");��ʾ("Ŀ������Ϊ"+ gettest1());}
	public void mask_M(View v){if(gettest1().length() == 3)client.mask_M(car,gettest1());else ��Ļ("�ڲ���1����xxx,eg:b20");}
	public void dir_change(View v){if(gettest1().length() == 2)client.dir_change(car,gettest1());else ��Ļ("�ڲ���1����xxx,eg:20");}
	public void ���ƺ���(View v){client.���ƺ���(car);}
	public void �⵵λ(View v){if(gettest1().length() == 1)client.�⵵λ(car,gettest1().charAt(0)-'0');else ��Ļ("�ڲ���1����x,eg:1");}
	public void etc��(View v){client.etc��(car);}
	public void rfid��(View v){client.rfid��(car);}
	public void rfid(View v){client.rfid(car);}
	public void λ�ûش�(View v){client.���ݻش�(car,"λ��");��ʾ("��ǰλ��Ϊ"+ current_zccar);}
	public void ��Ƭ�·�(View v){client.��Ƭ(car,"��");}
	public void ��λ�ش�(View v){client.��λ�ش�(car);��ʾ("��λΪ"+ ��λ);}
	public void ��������(View v){client.��������(car);}
	public void TFTָ��ͼƬ(View v){if(gettest1().length() == 2)client.TFT_control("ָ��ͼƬ"+gettest1());else ��Ļ("�ڲ���1����xx,eg:01");}
	public void TFT�Ϸ�(View v){client.TFT_control("ͼƬ�Ϸ�");}
	public void TFT��ʾ����(View v){client.TFT_control("��ʾ����"+UltraSonic);}
	public void TFT��ʾ����(View v){if(gettest1().length() == 6)client.TFT_control("��ʾ����"+gettest1());else ��Ļ("�ڲ���1����xxxxxx,eg:012345");}
	public void TFT��ʱ��(View v){client.TFT_control("��ʱ��");}
	public void TFT��ʱ��(View v){client.TFT_control("��ʱ��");}
	public void ��ά��ǰ(View v){iclient.��ά��ǰ();}
	public void ��ά���(View v){iclient.��ά���();}
	public void ��״��ɫ(View v){iclient.��״();}
	public void ����(View v){iclient.����();}

	public void ��(View v){cameraCommandUtil.postHttp(IP, 4, 1);}
	public void ��(View v){cameraCommandUtil.postHttp(IP, 6, 1);}
	public void ��(View v){cameraCommandUtil.postHttp(IP, 0, 1);}
	public void ��(View v){cameraCommandUtil.postHttp(IP, 2, 1);}
	
	public void set1(View v){cameraCommandUtil.postHttp(IP, 34, 0);}
	public void set2(View v){cameraCommandUtil.postHttp(IP, 36, 0);}
	public void set3(View v){cameraCommandUtil.postHttp(IP, 38, 0);}
	public void set4(View v){cameraCommandUtil.postHttp(IP, 40, 0);}

	public void wei1(View v){cameraCommandUtil.postHttp(IP, 33, 0);}
	public void wei2(View v){cameraCommandUtil.postHttp(IP, 35, 0);}
	public void wei3(View v){cameraCommandUtil.postHttp(IP, 37, 0);}
	public void wei4(View v){cameraCommandUtil.postHttp(IP, 39, 0);}
	
	
	public void ����(View v){
	setContentView(R.layout.activity_main);
	imageView = (ImageView) findViewById(R.id.imageView);
	show_image=(ImageView) findViewById(R.id.imageView); }

	public void ���⾯��(char x,String y,String y1){	
	client.��ǰ����(x,y);
	client.ǰ��(x, 80, 12);
	if((y.charAt(2)-'0')==1)
		y=y.charAt(0)+""+(char)(y.charAt(1)-1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==2)
		y=(char)(y.charAt(0)+1)+""+y.charAt(1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==3)
		y=y.charAt(0)+""+(char)(y.charAt(1)+1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==4)
		y=(char)(y.charAt(0)-1)+""+y.charAt(1)+""+y.charAt(2);
	client.��ǰ����(x,y);
	client.������ʾλ��(x,y1);
	client.������ʾת��(x);
	client.����(x);
	client.ack(x);
	client.����(x);
	client.������ʾת��(x);
	client.Ѱ��(x);
	client.ǰ��(x, 80, 27);
	if((y.charAt(2)-'0')==1)
		y=y.charAt(0)+""+(char)(y.charAt(1)-1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==2)
		y=(char)(y.charAt(0)+1)+""+y.charAt(1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==3)
		y=y.charAt(0)+""+(char)(y.charAt(1)+1)+""+y.charAt(2);
	else if((y.charAt(2)-'0')==4)
		y=(char)(y.charAt(0)-1)+""+y.charAt(1)+""+y.charAt(2);
	client.��ǰ����(x,y);
	}
	
	public void ���峵��(char x,String y,String y1,String y2){	
		client.��ǰ����(x,y);
		client.ǰ��(x, 80, 12);
		if((y.charAt(2)-'0')==1)
			y=y.charAt(0)+""+(char)(y.charAt(1)-1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==2)
			y=(char)(y.charAt(0)+1)+""+y.charAt(1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==3)
			y=y.charAt(0)+""+(char)(y.charAt(1)+1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==4)
			y=(char)(y.charAt(0)-1)+""+y.charAt(1)+""+y.charAt(2);
		client.��ǰ����(x,y);
		client.������ʾλ��(x,y1);
		client.������ʾת��(x);
		client.����1(x,y2);
		client.����2(x,y2);
		client.���ƺ���(x);
		client.������ʾת��(x);
		client.Ѱ��(x);
		client.ǰ��(x, 80, 27);
		if((y.charAt(2)-'0')==1)
			y=y.charAt(0)+""+(char)(y.charAt(1)-1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==2)
			y=(char)(y.charAt(0)+1)+""+y.charAt(1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==3)
			y=y.charAt(0)+""+(char)(y.charAt(1)+1)+""+y.charAt(2);
		else if((y.charAt(2)-'0')==4)
			y=(char)(y.charAt(0)-1)+""+y.charAt(1)+""+y.charAt(2);
		client.��ǰ����(x,y);
	}
//	public void mask_M(View v){client.mask_M();}

}


