package cn.image.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.ml.CvSVM;
import org.opencv.objdetect.HOGDescriptor;

import cn.image.processing.FileService;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

/*
 * 文件编写作者
 * 梁浩炫
 * 
 * 日期
 * 2016年4月30日
 */
public class TrafficSignRecognition {

	FileService  file= new FileService();
	public TrafficSignRecognition(Bitmap b) {
		bitmap = b;
		file.createFile(file.交通灯);
		
		file.savePhoto(b, save + "_" + ".png");					
		save++;
		myThread.start();
	}

	public boolean 完成标志位 = false;
	public String 交通灯信号 = "请右转";

	private Bitmap bitmap;
	public float 消耗的时间;

	private int Rmin_h = 167;
	private int Rmax_h = 255;
	private int Rmin_s = 87;
	private int Rmax_s = 255;
	private int Rmin_I = 174;
	private int Rmax_I = 255;
	private int 红色 = 20;


	private int Gmin_h = 38;
	private int Gmax_h = 88;
	private int Gmin_s = 59;
	private int Gmax_s = 255;
	private int Gmin_I = 136;
	private int Gmax_I = 255;

	private int 是否完成两次标志位 = 0;
	int save = 0;

	private Thread myThread = new Thread(new Runnable() {
		public void run() {
			long a = System.currentTimeMillis();
			boolean 是否完成 = false;
			boolean 是否绿色 = true;
			完成标志位 = false;
			String 方向 = "无法识别";
			是否完成两次标志位 = 0;
			while (!是否完成) {
				Log.i("111", "runing!");

				Mat 彩色图 = new Mat();

				Utils.bitmapToMat(bitmap, 彩色图);

				Mat hsi = new Mat(), test = new Mat(彩色图.rows(), 彩色图.cols(), 0,
						Scalar.all(0));

				Imgproc.cvtColor(彩色图, hsi, Imgproc.COLOR_RGB2HSV_FULL);// 将原图转换空间到hsi，并保存在hsi图像中

				if (!是否绿色) {
					Mat testTemp1 = new Mat(彩色图.rows(), 彩色图.cols(), 0,
							Scalar.all(0));
					Mat testTemp2 = new Mat(彩色图.rows(), 彩色图.cols(), 0,
							Scalar.all(0));

					Core.inRange(hsi, new Scalar(Rmin_h, Rmin_s, Rmin_I),
							new Scalar(Rmax_h, Rmax_s, Rmax_I), testTemp1);
					Core.inRange(hsi, new Scalar(0, Rmin_s, Rmin_I),
							new Scalar(红色, Rmax_s, Rmax_I), testTemp2);

					Core.addWeighted(testTemp1, 1, testTemp2, 1, 0.0, test);
				}
				if (是否绿色) {
					Core.inRange(hsi, new Scalar(Gmin_h, Gmin_s, Gmin_I),
							new Scalar(Gmax_h, Gmax_s, Gmax_I), test);
				}
				Imgproc.dilate(test, test, Imgproc.getStructuringElement(
						Imgproc.MORPH_RECT, new Size(5, 5)));
				Imgproc.morphologyEx(test, test, Imgproc.MORPH_OPEN, Imgproc
						.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,
								2)));
				Imgproc.medianBlur(test, test, 9);
				Imgproc.dilate(test, test, Imgproc.getStructuringElement(
						Imgproc.MORPH_RECT, new Size(5, 5)));
				Mat tMat = test.clone();

				List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				Mat contoursMat = new Mat();
				Mat mat = test.clone();
				Imgproc.findContours(mat, contours, contoursMat,
						Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);// 寻找轮廓
				Vector<Moments> mu = new Vector<Moments>(contours.size());

				double maxArea = 4000;
				int idx;

				for (idx = 0; idx < contours.size(); idx++) {
					mu.add(idx, Imgproc.moments(contours.get(idx), false));
					Mat contour = contours.get(idx);
					double contourarea = Imgproc.contourArea(contour);
					if (contourarea > maxArea && contourarea < 100000) {
						Rect aRect = Imgproc.boundingRect(contours.get(idx));
						Mat pContourImg = new Mat(tMat, aRect);
						
						Bitmap 保存图片 = Bitmap.createBitmap(pContourImg.cols(),
								pContourImg.rows(), Bitmap.Config.ARGB_8888);
						Utils.matToBitmap(pContourImg, 保存图片);
						file.savePhoto(保存图片, save + "_" + ".png");
						
						Imgproc.resize(pContourImg, pContourImg, new Size(150,
								150));
						是否完成 = true;

						MatOfFloat testDescriptor = new MatOfFloat();
						HOGDescriptor hog = new HOGDescriptor(
								new Size(150, 150), new Size(20, 20), new Size(
										10, 10), new Size(10, 10), 9);

						hog.compute(pContourImg, testDescriptor);

						CvSVM svm_hog = new CvSVM();

						svm_hog.load(Environment.getExternalStorageDirectory()
								+ "/" + "back_SVM_DATA.xml");

						if (svm_hog.predict(testDescriptor) == 1) {
							方向 = "掉头";
						} 
						else {
							svm_hog.load(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "left_SVM_DATA.xml");
							if (svm_hog.predict(testDescriptor) == 1) {
								方向 = "左转";
							} 
							else {
								svm_hog.load(Environment
										.getExternalStorageDirectory()
										+ "/"
										+ "right_SVM_DATA.xml");
								if (svm_hog.predict(testDescriptor) == 1) {
									方向 = "右转";
								}
							}
						}
					}
				}
				if (!是否完成) {
					是否绿色 = !是否绿色;// 只识别一次
					是否完成两次标志位++;
				}
				if (是否完成两次标志位 >= 2) {
					break;
				}
			}
			if (是否绿色)
				交通灯信号 = "请" + 方向;
			else
				交通灯信号 = "禁止" + 方向;
			消耗的时间 = (System.currentTimeMillis() - a) / 1000f;
			Log.i("111", "消耗的时间：" + 消耗的时间 + "秒");
			完成标志位 = true;
		}
	});
}
