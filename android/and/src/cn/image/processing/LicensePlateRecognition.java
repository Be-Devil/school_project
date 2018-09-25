package cn.image.processing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import cn.image.processing.FileService;
import com.googlecode.tesseract.android.TessBaseAPI;

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

public class LicensePlateRecognition {
	FileService  file= new FileService();
	public LicensePlateRecognition(Bitmap b) {
		bitmap = b;
		file.createFile(file.����);
		file.savePhoto(b, save + "_" + ".png");
		save++;
		myThread.start();
	}
 
	public LicensePlateRecognition() {}
	
	private Bitmap bitmap;

	private String LANGUAGE = "char";
	public float ���ĵ�ʱ��;
	public String ʶ���� = "123456";
	public boolean ��ɱ�־λ = false;
	int save = 0;
	
	private Thread myThread = new Thread(new Runnable() {

		public void run() {
			��ɱ�־λ = false;
			ʶ���� = "123456";
			long a = System.currentTimeMillis();

			Mat rgbMat = new Mat();

			Utils.bitmapToMat(bitmap, rgbMat);
			Imgproc.resize(rgbMat, rgbMat, new Size(720, 360));
			Mat hsi = new Mat(), test = new Mat(rgbMat.rows(), rgbMat.cols(),
					0, Scalar.all(0));

			Imgproc.cvtColor(rgbMat, hsi, Imgproc.COLOR_RGB2HSV);// ��ԭͼת���ռ䵽hsi����������hsiͼ����

			// --------------������ֵ��ֵ--------------------------------------------------

			int min_h = 101;
			int max_h = 129;
			int min_s = 150;
			int max_s = 255;
			int min_I = 16;
			int max_I = 240;

			Core.inRange(hsi, new Scalar(min_h, min_s, min_I), new Scalar(
					max_h, max_s, max_I), test);

			Imgproc.morphologyEx(test, test, Imgproc.MORPH_OPEN, Imgproc
					.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));// �����㣬Ŀ����ȥ��С����
			Imgproc.medianBlur(test, test, 3);// ��ֵ�˲�

			Imgproc.dilate(test, test, Imgproc.getStructuringElement(
					Imgproc.MORPH_RECT, new Size(20, 20)));// ����

			Imgproc.erode(test, test, Imgproc.getStructuringElement(
					Imgproc.MORPH_RECT, new Size(25, 25)));
			Imgproc.medianBlur(test, test, 7);// ��ֵ�˲�

			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Mat contoursMat = new Mat();
			Mat mat = test.clone();
			Imgproc.findContours(mat, contours, contoursMat, Imgproc.RETR_TREE,
					Imgproc.CHAIN_APPROX_SIMPLE);// Ѱ������
			Vector<Moments> mu = new Vector<Moments>(contours.size());

			for (int idx = 0; idx < contours.size(); idx++) {
				mu.add(idx, Imgproc.moments(contours.get(idx), false));
				Mat contour = contours.get(idx);
				double contourarea = Imgproc.contourArea(contour);
				if (contourarea > 6000 && contourarea < 80000) {

					Rect aRect = Imgproc.boundingRect(contours.get(idx));

					Mat t1 = new Mat(rgbMat, aRect);
					Log.i("111", "cols:" + t1.cols() + " rows:" + t1.rows());
					Log.i("111", "contourarea:" + contourarea);
					if (t1.cols() < 470 && t1.cols() > 100 && t1.rows() < 150) {
						aRect.y = aRect.y - 3;
						Mat pContourImg = new Mat(rgbMat, aRect);

						int min_h1 = 0;
						int max_h1 = 255;
						int min_s1 = 110;
						int max_s1 = 255;
						int min_I1 = 0;
						int max_I1 = 255;

						Mat hsi1 = new Mat(), test1 = new Mat(pContourImg
								.rows() + 20, pContourImg.cols()
								- (int) (pContourImg.cols() * 0.15) + 20, 0,
								Scalar.all(0));
						;
						Imgproc.cvtColor(pContourImg, hsi1,
								Imgproc.COLOR_RGB2HSV);// ��ԭͼת���ռ䵽hsi����������hsiͼ����
						for (int m = 0; m < pContourImg.rows(); m++) {

							for (int n = (int) (pContourImg.cols() * 0.15); n < pContourImg
									.cols(); n++) {
								double data[] = hsi1.get(m, n);
								if (data[0] >= min_h1 && data[0] <= max_h1
										&& data[1] >= min_s1
										&& data[1] <= max_s1
										&& data[2] >= min_I1
										&& data[2] <= max_I1) {
									test1.put(
											m + 10,
											n
													- (int) (pContourImg.cols() * 0.15)
													+ 10, 0);
								} else {
									test1.put(
											m + 10,
											n
													- (int) (pContourImg.cols() * 0.15)
													+ 10, 255);
								}
							}
						}

						Imgproc.morphologyEx(test1, test1, Imgproc.MORPH_OPEN,
								Imgproc.getStructuringElement(
										Imgproc.MORPH_RECT, new Size(5, 5)));// �����㣬Ŀ����ȥ��С����

						Imgproc.medianBlur(test1, test1, 3);// ��ֵ�˲�
						
						Bitmap ����ͼƬ = Bitmap.createBitmap(test1.cols(),
								test1.rows(), Bitmap.Config.ARGB_8888);
						Utils.matToBitmap(test1, ����ͼƬ);
						file.savePhoto(����ͼƬ, save + "_" + ".png");
						save++;
						
						Mat fMat = test1.clone();
						Core.line(test1, new Point(32, test1.rows() / 2),
								new Point(test1.cols() - 26, test1.rows() / 2),
								Scalar.all(255), 20);

						List<MatOfPoint> contours1 = new ArrayList<MatOfPoint>();
						Mat contoursMat1 = new Mat();
						Mat mat1 = test1.clone();
						Imgproc.findContours(mat1, contours1, contoursMat1,
								Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);// Ѱ������
						Vector<Moments> mu1 = new Vector<Moments>(contours1
								.size());

						for (int idx1 = 0; idx1 < contours1.size(); idx1++) {
							mu1.add(idx1,
									Imgproc.moments(contours1.get(idx1), false));
							Mat contour1 = contours1.get(idx1);
							double contourarea1 = Imgproc.contourArea(contour1);
							if (contourarea1 > 1000 && contourarea1 < 200000) {

								Rect aRect1 = Imgproc.boundingRect(contours1
										.get(idx1));
								Mat pContourImg1 = new Mat(fMat, aRect1);

								Bitmap bmp2 = Bitmap.createBitmap(
										pContourImg1.cols(),
										pContourImg1.rows(),
										Bitmap.Config.ARGB_8888);
								Utils.matToBitmap(pContourImg1, bmp2);

								Mat mat2 = new Mat(pContourImg1.rows() + 40,
										pContourImg1.cols() + 40, 0, Scalar
												.all(0));

								Bitmap bitmaptemp = Bitmap.createBitmap(
										bmp2.getWidth() + 40,
										bmp2.getHeight() + 40,
										Bitmap.Config.ARGB_8888);
								Utils.matToBitmap(mat2, bitmaptemp);
								for (int i = 0; i < bmp2.getWidth(); i++) {
									for (int j = 0; j < bmp2.getHeight(); j++) {
										bitmaptemp.setPixel(i + 20, j + 20,
												bmp2.getPixel(i, j));
									}
								}
								

								ʶ���� = doOcr(bitmaptemp, LANGUAGE);
								ʶ���� = ʶ����.replaceAll(" ", "");
								if(ʶ����.length() == 6)
								{
								String ��һ�� = ʶ����.substring(0, 1);
								��һ�� = ��һ��.replaceAll("0", "O");
								��һ�� = ��һ��.replaceAll("1", "I");
								��һ�� = ��һ��.replaceAll("5", "S");
								��һ�� = ��һ��.replaceAll("8", "B");
								
								String �ڶ��� = ʶ����.substring(1, 4);
								�ڶ��� = �ڶ���.replaceAll("O", "0");
								�ڶ��� = �ڶ���.replaceAll("I", "1");
								�ڶ��� = �ڶ���.replaceAll("S", "5");
								�ڶ��� = �ڶ���.replaceAll("B", "8");
								
								String ������ = ʶ����.substring(4, 5);
								������ = ������.replaceAll("0", "O");
								������ = ������.replaceAll("1", "I");
								������ = ������.replaceAll("5", "S");
								������ = ������.replaceAll("8", "B");
								
								String ������ = ʶ����.substring(5, 6);
								������ = ������.replaceAll("O", "0");
								������ = ������.replaceAll("I", "1");
								������ = ������.replaceAll("S", "5");
								������ = ������.replaceAll("B", "8");
								
								ʶ���� = ��һ�� + �ڶ��� + ������ + ������;
								}else
									ʶ���� = "123456";
								file.savePhoto(bitmaptemp, ʶ����+ "_" + ".png");
								
							}
						}
					}
				}
			}
			���ĵ�ʱ�� = (System.currentTimeMillis() - a) / 1000f;
			Log.i("111", "���ĵ�ʱ�䣺" + ���ĵ�ʱ�� + "��");
			��ɱ�־λ = true;
		}
	});

	/**
	 * ����ͼƬʶ��
	 * 
	 * @param bitmap
	 *            ��ʶ��ͼƬ
	 * @param language
	 *            ʶ������
	 * @return ʶ�����ַ���
	 */
	TessBaseAPI baseApi = new TessBaseAPI();

	private String doOcr(Bitmap bitmap, String language) {

		baseApi.init(getSDPath(), language);

		// ����Ӵ��У�tess-twoҪ��BMP����Ϊ������
		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		baseApi.setImage(bitmap);

		String text = baseApi.getUTF8Text();

		baseApi.clear();
		baseApi.end();

		return text;
	}

	/**
	 * ��ȡsd����·��
	 * 
	 * @return ·�����ַ���
	 */
	private String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ���Ŀ¼
		}
		return sdDir.toString();
	}

}
