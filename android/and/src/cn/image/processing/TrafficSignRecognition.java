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
 * �ļ���д����
 * ������
 * 
 * ����
 * 2016��4��30��
 */
public class TrafficSignRecognition {

	FileService  file= new FileService();
	public TrafficSignRecognition(Bitmap b) {
		bitmap = b;
		file.createFile(file.��ͨ��);
		
		file.savePhoto(b, save + "_" + ".png");					
		save++;
		myThread.start();
	}

	public boolean ��ɱ�־λ = false;
	public String ��ͨ���ź� = "����ת";

	private Bitmap bitmap;
	public float ���ĵ�ʱ��;

	private int Rmin_h = 167;
	private int Rmax_h = 255;
	private int Rmin_s = 87;
	private int Rmax_s = 255;
	private int Rmin_I = 174;
	private int Rmax_I = 255;
	private int ��ɫ = 20;


	private int Gmin_h = 38;
	private int Gmax_h = 88;
	private int Gmin_s = 59;
	private int Gmax_s = 255;
	private int Gmin_I = 136;
	private int Gmax_I = 255;

	private int �Ƿ�������α�־λ = 0;
	int save = 0;

	private Thread myThread = new Thread(new Runnable() {
		public void run() {
			long a = System.currentTimeMillis();
			boolean �Ƿ���� = false;
			boolean �Ƿ���ɫ = true;
			��ɱ�־λ = false;
			String ���� = "�޷�ʶ��";
			�Ƿ�������α�־λ = 0;
			while (!�Ƿ����) {
				Log.i("111", "runing!");

				Mat ��ɫͼ = new Mat();

				Utils.bitmapToMat(bitmap, ��ɫͼ);

				Mat hsi = new Mat(), test = new Mat(��ɫͼ.rows(), ��ɫͼ.cols(), 0,
						Scalar.all(0));

				Imgproc.cvtColor(��ɫͼ, hsi, Imgproc.COLOR_RGB2HSV_FULL);// ��ԭͼת���ռ䵽hsi����������hsiͼ����

				if (!�Ƿ���ɫ) {
					Mat testTemp1 = new Mat(��ɫͼ.rows(), ��ɫͼ.cols(), 0,
							Scalar.all(0));
					Mat testTemp2 = new Mat(��ɫͼ.rows(), ��ɫͼ.cols(), 0,
							Scalar.all(0));

					Core.inRange(hsi, new Scalar(Rmin_h, Rmin_s, Rmin_I),
							new Scalar(Rmax_h, Rmax_s, Rmax_I), testTemp1);
					Core.inRange(hsi, new Scalar(0, Rmin_s, Rmin_I),
							new Scalar(��ɫ, Rmax_s, Rmax_I), testTemp2);

					Core.addWeighted(testTemp1, 1, testTemp2, 1, 0.0, test);
				}
				if (�Ƿ���ɫ) {
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
						Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);// Ѱ������
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
						
						Bitmap ����ͼƬ = Bitmap.createBitmap(pContourImg.cols(),
								pContourImg.rows(), Bitmap.Config.ARGB_8888);
						Utils.matToBitmap(pContourImg, ����ͼƬ);
						file.savePhoto(����ͼƬ, save + "_" + ".png");
						
						Imgproc.resize(pContourImg, pContourImg, new Size(150,
								150));
						�Ƿ���� = true;

						MatOfFloat testDescriptor = new MatOfFloat();
						HOGDescriptor hog = new HOGDescriptor(
								new Size(150, 150), new Size(20, 20), new Size(
										10, 10), new Size(10, 10), 9);

						hog.compute(pContourImg, testDescriptor);

						CvSVM svm_hog = new CvSVM();

						svm_hog.load(Environment.getExternalStorageDirectory()
								+ "/" + "back_SVM_DATA.xml");

						if (svm_hog.predict(testDescriptor) == 1) {
							���� = "��ͷ";
						} 
						else {
							svm_hog.load(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "left_SVM_DATA.xml");
							if (svm_hog.predict(testDescriptor) == 1) {
								���� = "��ת";
							} 
							else {
								svm_hog.load(Environment
										.getExternalStorageDirectory()
										+ "/"
										+ "right_SVM_DATA.xml");
								if (svm_hog.predict(testDescriptor) == 1) {
									���� = "��ת";
								}
							}
						}
					}
				}
				if (!�Ƿ����) {
					�Ƿ���ɫ = !�Ƿ���ɫ;// ֻʶ��һ��
					�Ƿ�������α�־λ++;
				}
				if (�Ƿ�������α�־λ >= 2) {
					break;
				}
			}
			if (�Ƿ���ɫ)
				��ͨ���ź� = "��" + ����;
			else
				��ͨ���ź� = "��ֹ" + ����;
			���ĵ�ʱ�� = (System.currentTimeMillis() - a) / 1000f;
			Log.i("111", "���ĵ�ʱ�䣺" + ���ĵ�ʱ�� + "��");
			��ɱ�־λ = true;
		}
	});
}
