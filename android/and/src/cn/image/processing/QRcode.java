package cn.image.processing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.utils.Converters;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import cn.image.processing.FileService;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class QRcode {
	
		FileService  file= new FileService();
	 public QRcode(Bitmap b) {
		bitmap = b;
		file.createFile(file.��ά��);
		
		file.savePhoto(b, save + "_" + ".png");					
		save++;
		mytThread.start();
	}
	public static Bitmap bitmap01;
	public static Mat ContourImg;
	public Bitmap bitmap;
	public static Bitmap bitmap0;
	public static Bitmap bitmap10;
	
	public int min_h = 0;  //0   255  127 
	public int max_h1 = 255;  //0   255  127 
	
	public int min_s = 0;  //0   255  127
	public int max_s1 = 255;  //0   255  127
	
	public int min_I = 0;   //0   46   23
	public int max_I1 = 50;   //0   46   23
	
	
    private String strPicPath;  
	//public Bitmap bitmap01;
	public String ��ά���� = "";
	public boolean ��ɱ�־λ = false;
	public float ���ĵ�ʱ��;
	int save = 0;
	public static Mat srcColorResize;
	private Thread mytThread = new Thread(new Runnable() {
		public void run() {
			long a = System.currentTimeMillis();
			��ɱ�־λ = false;
			Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
			hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
	
			Mat ��ά�� = new Mat();
			Mat ԭͼ = new Mat();
			Utils.bitmapToMat(bitmap, ԭͼ);
			Utils.bitmapToMat(bitmap, ��ά��);
			
			Mat hsi = new Mat(), test = new Mat(ԭͼ.rows(), ԭͼ.cols(),
					0, Scalar.all(0));

			Imgproc.cvtColor(ԭͼ, hsi, Imgproc.COLOR_RGB2HSV_FULL);// ��ԭͼת���ռ䵽hsi����������hsiͼ����

			Core.inRange(hsi, new Scalar(min_h, min_s, min_I), new Scalar(// ��ɫ�ָ����ɫ�Ķ���ȫ��ѡ������
					max_h1, max_s1, max_I1), test);
			
			Core.bitwise_not(test, test);
			Imgproc.erode(test, test, Imgproc
					.getStructuringElement(Imgproc.MORPH_RECT,
							new Size(5, 5)));// ��ʴ��Ŀ���Ƿ�ֹ��ͼ����һ��
/*			Imgproc.cvtColor(��ά��, ��ά��, Imgproc.COLOR_BGR2GRAY);
			Imgproc.GaussianBlur(��ά��,��ά��,new Size(3, 3),0);
			Imgproc.threshold(��ά��,��ά��,160,255,Imgproc.THRESH_BINARY);*/
			

			
			��ά��= test;
			Mat element=Imgproc.getStructuringElement(2,new Size(5,5));
			for(int i=0;i<10;i++)  
			{  
				Imgproc.erode(��ά��,��ά��,element);  
			    i++;  
			}
			Core.line(��ά��, new Point(0, ��ά��.rows()-10),
					new Point(��ά��.cols(), ��ά��.rows()-10),
					Scalar.all(255), 20);		
			Core.line(��ά��, new Point(0, 10),
					new Point(��ά��.cols(), 10),
					Scalar.all(255), 20);
			Core.line(��ά��, new Point(10, 0),
					new Point(10, ��ά��.rows()),
					Scalar.all(255), 20);
			Core.line(��ά��, new Point(��ά��.cols()-10, 0),
					new Point(��ά��.cols()-10, ��ά��.rows()),
					Scalar.all(255), 20);
			List<MatOfPoint> contours1 = new ArrayList<MatOfPoint>();
			Mat contoursMat = new Mat();
			Imgproc.findContours(��ά��, contours1, contoursMat,
					Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);// Ѱ������	
			Vector<Moments> mu = new Vector<Moments>(contours1.size());
			double maxArea = 100000;
			int idx;
			int max_idx =0;
			for (idx = 0; idx < contours1.size(); idx++) {
				Mat contour = contours1.get(idx);
				double contourarea = Imgproc.contourArea(contour);
				if(contourarea>maxArea)  {max_idx=1;continue;}
				if(Imgproc.contourArea(contours1.get(max_idx)) < contourarea /*&& contourarea<maxArea*/)
				{
					max_idx = idx;
				}
				
			}
			Mat MyImage = new Mat(��ά��.rows(),
					��ά��.cols(), 0, Scalar.all(0));// ����pContourImg�Ĵ�С����һ��ȫ�ڵ�ͼ��
			Imgproc.drawContours(MyImage, contours1, max_idx,
					Scalar.all(255), -1);// ����������ȫ�ڵ�ͼ���У�����-1��ʾ���������
	
			
			Rect aRect_max = Imgproc.boundingRect(contours1.get(max_idx));
			
			int mark_idx = -1;
			Rect aRect ;
			for (idx = 0; idx < contours1.size(); idx++) {

				aRect = Imgproc.boundingRect(contours1.get(idx));
				double proportion = (double)aRect.width/(double)aRect_max.width;
				if(aRect.x>aRect_max.x+10 && aRect.x>aRect_max.x+10 && aRect.y>aRect_max.y+10 && aRect.y>aRect_max.y+10 &&  proportion>0.5 && proportion<0.7)
				{
					mark_idx = idx;
					break;
				}
			}
			if(mark_idx == -1)
			{
				double value = aRect_max.width - aRect_max.width;
				if(value <20 && value >-20 )
				{
					mark_idx = max_idx;

				}
			}
			aRect =  Imgproc.boundingRect(contours1.get(mark_idx));
			aRect.x = aRect.x-10 ;
			aRect.y = aRect.y-10 ;
			aRect.width = aRect.width + 20;
			aRect.height = aRect.height + 20;

			ContourImg =new Mat(ԭͼ, aRect);
			Imgproc.resize( ContourImg,ContourImg,new Size(300, 300));
			Imgproc.cvtColor(ContourImg, ContourImg, Imgproc.COLOR_BGR2GRAY);
			Imgproc.threshold(ContourImg,ContourImg,120,255,Imgproc.THRESH_BINARY);
			
			Imgproc.copyMakeBorder(ContourImg, ContourImg, (ԭͼ.rows()-300)/2,(ԭͼ.rows()-300)/2, (ԭͼ.cols()-300)/2,(ԭͼ.cols()-300)/2, 0,Scalar.all(255));
			
			bitmap01 = Bitmap.createBitmap(ContourImg.cols(),
					ContourImg.rows(), Bitmap.Config.ARGB_8888);
			Utils.matToBitmap(ContourImg, bitmap01);
			
/*			bitmap10 = Bitmap.createBitmap(ContourImg.cols(),
					ContourImg.rows(), Bitmap.Config.ARGB_8888);
			Utils.matToBitmap(ContourImg, bitmap10);*/
			
			bitmap0 = bitmap01;
			bitmap10 = Bitmap.createBitmap(ContourImg.cols(),
					ContourImg.rows(), Bitmap.Config.ARGB_8888);
			Utils.matToBitmap(ContourImg, bitmap10);
			

			RGBLuminanceSource source = new RGBLuminanceSource(bitmap01);
			BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
			QRCodeReader reader = new QRCodeReader();
			try {
				Result result = reader.decode(bitmap1, hints);
				��ά���� = result.toString();
				//file.saveToSDCard(file.��ά��ʶ����Ϣ,"1__123.txt",��ά����);		
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (ChecksumException e) {
				e.printStackTrace();
			} catch (FormatException e) {
				e.printStackTrace();
			}
			if(��ά����.equals(""))
			{
				RGBLuminanceSource source0 = new RGBLuminanceSource(bitmap);
				BinaryBitmap bitmap10 = new BinaryBitmap(new HybridBinarizer(source0));
				QRCodeReader reader0 = new QRCodeReader();
				try {
					Result result = reader0.decode(bitmap10, hints);
					��ά���� = result.toString();
					//file.saveToSDCard(file.��ά��ʶ����Ϣ,"1__123.txt",��ά����);		
				} catch (NotFoundException e) {
					e.printStackTrace();
				} catch (ChecksumException e) {
					e.printStackTrace();
				} catch (FormatException e) {
					e.printStackTrace();
			}}
			���ĵ�ʱ�� = (System.currentTimeMillis() - a) / 1000f;
			��ɱ�־λ = true;
		}
	});

	class RGBLuminanceSource extends LuminanceSource {
		private final byte[] luminances;

		public RGBLuminanceSource(Bitmap bitmap) {
			super(bitmap.getWidth(), bitmap.getHeight());
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int[] pixels = new int[width * height];
			bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
			luminances = new byte[width * height];
			for (int y = 0; y < height; y++) {
				int offset = y * width;
				for (int x = 0; x < width; x++) {
					int pixel = pixels[offset + x];
					int r = (pixel >> 16) & 0xff;
					int g = (pixel >> 8) & 0xff;
					int b = pixel & 0xff;
					if (r == g && g == b) {
						luminances[offset + x] = (byte) r;
					} else {
						luminances[offset + x] = (byte) ((r + g + g + b) >> 2);
					}
				}
			}
		}

		@Override
		public byte[] getMatrix() {
			return luminances;
		}

		@Override
		public byte[] getRow(int arg0, byte[] arg1) {
			if (arg0 < 0 || arg0 >= getHeight()) {
				throw new IllegalArgumentException(
						"Requested row is outside the image: " + arg0);
			}
			int width = getWidth();
			if (arg1 == null || arg1.length < width) {
				arg1 = new byte[width];
			}
			System.arraycopy(luminances, arg0 * width, arg1, 0, width);
			return arg1;
		}
	}

}
