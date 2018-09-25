package cn.image.processing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;

import cn.image.processing.FileService;
/*
 * 文件编写作者
 * 林镇圳
 * 
 * 日期
 * 2017年4月9日
 */
import cn.gdit.goldsite_.MainActivity;

public class ColorShapeRecognition {

	List<MatOfPoint> Five_pointed_star_contour = new ArrayList<MatOfPoint>();
	List<MatOfPoint> Original_contour = new ArrayList<MatOfPoint>();
	List<MatOfPoint> Single_contour = new ArrayList<MatOfPoint>();
	List<MatOfPoint> Canny_contour = new ArrayList<MatOfPoint>();
	List<MatOfPoint> Line_contour = new ArrayList<MatOfPoint>();

	Point P1 = new Point(150,150);    //min_x
	Point P3 = new Point(150,150);	  //min_y
	Point P2 = new Point(0,0);	  	  //max_x
	Point P4 = new Point(0,0);	  	  //max_y
	Point P5 = new Point(0,0);	
	Point[] P = new Point[100];	      //收集所有角点

	MatOfPoint2f Five_pointed_star_FindContours;
	MatOfPoint corners;

	FileService  file = new FileService();
	
	int Five_pointed_star_numble = 0;
	int Rectangle_numble = 0;
	int Trapezoid_numble = 0;
	int Triangle_numble = 0;
	int Diamond_numble = 0;
	int Unknown_numble = 0;
	int Square_numble = 0;
	int Circle_numble = 0;
	int Shape_numble = 0;
	
	int color_x = 0;
	int color_y = 0;
	
	double x1 = 75, y1 = 75, q1 = 107;
	double x2 = 75, y2 = 75, q2 = 107;
	double x3 = 75, y3 = 75, q3 = 107;
	double x4 = 75, y4 = 75, q4 = 107;
	double Triangle_distance = 0;
	
	public boolean 完成标志位 = false;

	public static String Process = "";
	public static String Result = "";
	public static String Result1 = "";
	public static String color = "";
	public static String shape = "";
	public static String[] Every_color = new String[20];

	double corner_1, corner_2, corner_3, corner_4;
	double Line_1, Line_2, Line_3, Line_4;  
	double Every_original_contourarea;
	double Every_Line_contourarea;

	public static Bitmap Original_Bitmap;
	public static Bitmap[] Display_Bitmap = new Bitmap[28];
	public static Bitmap[] Display_color_Bitmap = new Bitmap[18];
	
	public static Mat Display_Mat_0 = new Mat(360, 640,0, Scalar.all(255)); 
	public static Mat Display_Mat_1 = new Mat(360, 640,0, Scalar.all(255));  
	public static Mat Display_Mat_2 = new Mat(360, 640,0, Scalar.all(255));  
	public static Mat Display_Mat_3 = new Mat(360, 640,0, Scalar.all(255));  
	public static Mat Display_Mat_4 = new Mat(360, 640,0, Scalar.all(255));  
	public static Mat Display_Mat_5 = new Mat(360, 640,0, Scalar.all(255)); 

	Mat Original_FindContours_Mat;
	Mat Original_MorphologyEx_Mat;
	Mat Original_medianBlur_Mat;
	Mat Original_Binary_Mat;
	Mat Original_Hsv_Mat;
	Mat Original_Mat;

	Mat Processed_Singlecolor_FindContours_Mat;
	Mat Processed_Singlecolor_Hsv_Mat;
	Mat Processed_Singlecolor_Draw_Mat; 
	Mat Processed_Original_Drew_Mat;
	Mat Processed_MorphologyEx_Mat;
	Mat Processed_FindContours_Mat;
	Mat Processed_Singlecolor_Mat;
	Mat Processed_MedianBlur_Mat;
	Mat Processed_Binary_Mat;
	Mat Processed_Canny_Mat;
	Mat Processed_Erode_Mat;
	Mat Processed_Drew_Mat;
	Mat Processed_blur_Mat;
	Mat Processed_Hsv_Mat;
	Mat Processed_Mat;

	Mat Circle_time;
	Mat Circle_Mat; 

	Mat Line_Singlecolor_Mat;
	Mat Line_FindContours_Mat;
	Mat Line_Every_Draw_Mat;
	Mat Line_Color_Mat;
	Mat Line_color_Mat;
	Mat Line_Draw_Mat;
	Mat Line_Hsv_Mat;
	Mat Line_Mat;
	Mat lines;


	Rect Processed_Rect;
	Rect Line_Rect;

	long a;
	long b;
	
	int min_h;
	int max_h;
	int min_s;
	int max_s; 
	int min_i;
	int max_i;
	
	public ColorShapeRecognition(Bitmap original_Bitmap){
		FileService  file = new FileService();
		file.createFile_(file.形状颜色);
		Bitmap b1 = original_Bitmap;//file.readPhoto(FileService.形状颜色+"1" + ".png");
		original_Bitmap = b1;
		a = System.currentTimeMillis();
		b = System.currentTimeMillis();
		Original_Bitmap = original_Bitmap;						
		Display_Bitmap[0] = Original_Bitmap;
		MainActivity.phHandler1.sendEmptyMessage(0);           											 //显示原图      	          
		init();
		file.createFile(FileService.形状颜色);					
		file.savePhoto(Original_Bitmap, Original_Bitmap+ ".png");					
		phThread.start();
	}

	private Thread phThread = new Thread(new Runnable() {
		public void run() {

			Imgproc.cvtColor(Original_Mat.clone(), Original_Hsv_Mat, Imgproc.COLOR_RGB2HSV_FULL);		// 将原图转换空间到hsi，并保存在hsi图像中

			青色();
			Core.inRange(Original_Hsv_Mat.clone(), new Scalar(min_h, min_s, min_i), new Scalar(			// 颜色分割（把白色的东西全部选出来）
					max_h, max_s, max_i), Original_Binary_Mat);

			Display_Bitmap(0, Original_Binary_Mat);	

			Imgproc.morphologyEx(Original_Binary_Mat.clone(), Original_MorphologyEx_Mat,
					Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(									// 开运算，去小噪点	
							Imgproc.MORPH_RECT, new Size(2, 2)));												

			Imgproc.medianBlur(Original_MorphologyEx_Mat.clone(), Original_medianBlur_Mat, 5);			// 中值滤波

			Imgproc.Canny(Original_Binary_Mat.clone(), Display_Mat_0, 20, 40);				 			// 使用Canny算子把形状的边缘找出来

			Imgproc.findContours(Display_Mat_0.clone(), Canny_contour, Display_Mat_0.clone(),		
			Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));						 		// 寻找轮廓

			Imgproc.drawContours(Original_medianBlur_Mat, Canny_contour, -1, 
					new Scalar(255),3);	
			
			Display_Mat_1 = new Mat(360, 640,0, Scalar.all(255));  
			Imgproc.drawContours(Display_Mat_1, Canny_contour, -1, 
					new Scalar(0),1);	

			Imgproc.findContours(Original_medianBlur_Mat.clone(), Original_contour, 
					Original_FindContours_Mat, Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);			// 寻找轮廓	

			int idx_ = 0,idx = 0;
			double idx_2[] = new double[1001];
			double idx_1[] = new double[1001];

			for (idx_ = 0; idx_ < Original_contour.size(); idx_++) {
				Every_original_contourarea = Imgproc.contourArea(Original_contour.get(idx_));
				if (Every_original_contourarea > 30000 && Every_original_contourarea < 200000) {
					idx_2[idx_] =  Every_original_contourarea;
					idx_1[idx_] =  Every_original_contourarea;
				}
			}
					bubbleSort(idx_2);
					for(int i = 0;i < Original_contour.size();i++)
					if(Math.abs(idx_2[i]-idx_2[i+1]) < idx_2[i]*0.2)
						idx_2[0] = idx_2[i+1];
					for(int i = 0;i < Original_contour.size();i++)
						if(idx_1[i] == idx_2[0])
							{idx = i;break;}
					a = System.currentTimeMillis() - a;
					Process =Process + "抠出外框"+ a +"ms  ";
					a = System.currentTimeMillis();
					Processed_Rect = Imgproc.boundingRect(Original_contour.get(idx));
				
					Core.bitwise_not(Original_medianBlur_Mat.clone(), Original_medianBlur_Mat);			// 将二值图反值，把全部图形突出来

					Processed_Mat = new Mat(Original_Mat,Processed_Rect);								// 抠彩色图
					Processed_Binary_Mat = new Mat(Original_medianBlur_Mat,Processed_Rect);				// 抠二值图
					
					Processed_Original_Drew_Mat = new Mat(Original_Mat.rows(), Original_Mat.cols(),
							0, Scalar.all(0)); 
					
					Imgproc.drawContours(Processed_Original_Drew_Mat, Original_contour, idx, 
							new Scalar(255),-1);
					
					Processed_Binary_Mat = new Mat(Original_Mat.rows(), Original_Mat.cols(),
							0, Scalar.all(0)); 
					
					Original_medianBlur_Mat.copyTo(Processed_Binary_Mat, Processed_Original_Drew_Mat);

					Processed_Binary_Mat =new Mat(Processed_Binary_Mat.clone(),Processed_Rect);
					
					init_();
					
					/***************************防止图像连接在一起****************************************/	
					
					/***************************1.防止相同颜色，相距过近**********************************/
					Imgproc.cvtColor(Processed_Mat.clone(), Processed_Hsv_Mat, Imgproc.COLOR_BGR2GRAY);	 // 将原图转换空间到hsi，并保存在hsi图像中

					Imgproc.blur(Processed_Hsv_Mat.clone(), Processed_blur_Mat, new Size(3,3));	
					
					Imgproc.Canny(Processed_blur_Mat.clone(), Processed_Canny_Mat, 20, 40);				 // 使用Canny算子把形状的边缘找出来

					Imgproc.findContours(Processed_Canny_Mat.clone(), Canny_contour, Processed_FindContours_Mat,		
					Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));						 // 寻找轮廓

					Processed_Drew_Mat = Processed_Binary_Mat.clone();
					
					Imgproc.drawContours(Processed_Drew_Mat, Canny_contour, -1, 
							new Scalar(0),1);	
					
					Imgproc.morphologyEx(Processed_Drew_Mat.clone(), Processed_MorphologyEx_Mat, Imgproc.MORPH_OPEN, 
							Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));			// 开运算，去小噪点	
					 
					Imgproc.medianBlur(Processed_MorphologyEx_Mat.clone(), Processed_MedianBlur_Mat, 5);		// 中值滤波

					/***************************1.相近颜色，相互接触**********************************/
					int next_color = 6;
					for(;next_color>0;next_color--)
					{
						switch (next_color)
							{
								case 1:绿色();break;
								case 2:黄色();break; 
								case 3:青色();break;
								case 4:黑色();break;
								case 5:蓝色();break;
								case 6:品色();break;
							}

						Imgproc.cvtColor(Processed_Mat.clone(), Processed_Singlecolor_Hsv_Mat, Imgproc.COLOR_RGB2HSV_FULL);				// 将原图转换空间到hsi，并保存在hsi图像中

						Core.inRange(Processed_Singlecolor_Hsv_Mat.clone(), new Scalar(min_h, min_s, min_i), new Scalar(				// 颜色分割
						max_h, max_s, max_i), Processed_Singlecolor_Mat);

						Imgproc.morphologyEx(Processed_Singlecolor_Mat.clone(), Processed_Singlecolor_Mat,
								Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(														// 开运算，去小噪点	
										Imgproc.MORPH_RECT, new Size(2, 2)));												
			
						Imgproc.medianBlur(Processed_Singlecolor_Mat.clone(), Processed_Singlecolor_Mat, 5);							// 中值滤波
						
			
						Imgproc.findContours(Processed_Singlecolor_Mat.clone(), Single_contour, Processed_Singlecolor_FindContours_Mat,		
								Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));											// 寻找轮廓
						
						Imgproc.drawContours(Processed_MedianBlur_Mat, Single_contour, -1, 
								new Scalar(0),2);	
					}
					Imgproc.morphologyEx(Processed_MedianBlur_Mat.clone(), Processed_MedianBlur_Mat,
							Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(															// 开运算，去小噪点	
									Imgproc.MORPH_RECT, new Size(2, 2)));		
					a = System.currentTimeMillis() - a;					
					Process =Process + "图像处理" + a + "ms  ";			
					a = System.currentTimeMillis();		


					/***************************线性图形逐个检测****************************************/
					
					Imgproc.findContours(Processed_MedianBlur_Mat.clone(), Line_contour, 
							Line_FindContours_Mat, Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));						// 寻找轮廓	

					int idx1;
					int idx1_[] = new int[1000];
					int flag;
					for (idx1 = 0; idx1 < Line_contour.size(); idx1++) {
						Every_Line_contourarea = Imgproc.contourArea(Line_contour.get(idx1));
						idx1_[idx1] = (int) Every_Line_contourarea;
						}
					
					for (idx1 = 0; idx1 < Line_contour.size(); idx1++) {
						shape = "";
						color_x = 0;
						color_y = 0; 
						flag = 0;
						Every_Line_contourarea = idx1_[idx1];
						if (Every_Line_contourarea > 200 && Every_Line_contourarea < 20000) {
							Shape_numble++;
							Line_Rect = Imgproc.boundingRect(Line_contour.get(idx1));
							System.out.println(Line_contour.get(idx1));
							if(Line_Rect.x > 8)
								Line_Rect.x = Line_Rect.x - 8;
							else
								Line_Rect.x = 0;
							
							if(Line_Rect.y > 16)
								Line_Rect.y = Line_Rect.y - 16;
							else
								Line_Rect.y = 0;
								
							Line_Rect.width = Line_Rect.width + 16;					
							Line_Rect.height = Line_Rect.height + 32;
							
							if(Line_Rect.x + Line_Rect.width > Processed_Binary_Mat.cols())
								Line_Rect.width =Processed_Binary_Mat.cols() - Line_Rect.x;

							if(Line_Rect.y + Line_Rect.height > Processed_Binary_Mat.rows())
								Line_Rect.height = Processed_Binary_Mat.rows() - Line_Rect.y;
							
							Line_Color_Mat = new Mat(Processed_Binary_Mat.rows(), Processed_Binary_Mat.cols(),
									0, Scalar.all(0)); 
							
							Imgproc.drawContours(Line_Color_Mat, Line_contour, idx1, 
									new Scalar(255),-1);
							
							Line_Singlecolor_Mat = new Mat(Processed_Binary_Mat.rows(), Processed_Binary_Mat.cols(),
									0, Scalar.all(0)); 

							Processed_Binary_Mat.copyTo(Line_Singlecolor_Mat, Line_Color_Mat);

							Line_Singlecolor_Mat =new Mat(Line_Singlecolor_Mat.clone(),Line_Rect);
							
							Line_color_Mat = new Mat(Processed_Binary_Mat.rows(), Processed_Binary_Mat.cols(),
									0, Scalar.all(0)); 
							
							Processed_Mat.copyTo(Line_color_Mat, Line_Color_Mat);

							Line_color_Mat =new Mat(Line_color_Mat.clone(),Line_Rect);
							
							/**********************************旋转图像****************************************/
							Line_Draw_Mat = new Mat(Processed_Binary_Mat.rows(), Processed_Binary_Mat.cols(),
									0, Scalar.all(0)); 
							
							Imgproc.drawContours(Line_Draw_Mat, Line_contour, idx1, 
									new Scalar(255),2);
							
						    Imgproc.threshold(Line_Draw_Mat.clone(), Line_Mat, 128, 255, Imgproc.THRESH_BINARY);
						    
						    Imgproc.drawContours(Line_Draw_Mat, Line_contour, idx1, 
									new Scalar(255),-1);
						    lines = new Mat();
						    Imgproc.HoughLinesP(Line_Mat,lines,1,Math.PI/180,20,20,500); 
						    
							double resultAngle = 0;
							double lenth = 0;
							int LID = 1000;
							for (int i = 0; i < lines.cols(); i++) {
								double[] temp = lines.get(0, i);
								double x1 = temp[0], y1 = temp[1], x2 = temp[2], y2 = temp[3];
								double lenth_ = Math
										.sqrt((x1 - x2) * (x1 - x2)
												+ (y1 - y2) * (y1 - y2));
								if (lenth_ > lenth) {
									lenth = lenth_;
									LID = i;
								}
							}
							
							if (LID != 1000) {
								double[] temp = lines.get(0, LID);
								double x1 = temp[0], y1 = temp[1], x2 = temp[2], y2 = temp[3];
								double mathtemp = 0.0;
								mathtemp = Math.abs(y1 - y2)
										/ Math.abs(x1 - x2);
								resultAngle = Math.atan(mathtemp) * 180
										/ Math.PI;
								if ((y1 > y2 && x2 < x1)
										|| (y2 > y1 && x1 < x2)) {
									resultAngle = -resultAngle;
								}
							}// 算出角度
							Line_Singlecolor_Mat = Rotate(Line_Singlecolor_Mat, resultAngle);							// 旋转图像

							Imgproc.resize(Line_Singlecolor_Mat, Line_Singlecolor_Mat, new Size(150, 150));				// 把图像限定到150x150

							Line_color_Mat = Rotate_color(Line_color_Mat, resultAngle);							// 旋转图像

							Imgproc.resize(Line_color_Mat, Line_color_Mat, new Size(150, 150));				// 把图像限定到150x150

							switch (flag)
							{
								case 0://圆形检测

									Imgproc.HoughCircles(Line_Singlecolor_Mat.clone(), Circle_time,
											Imgproc.CV_HOUGH_GRADIENT, 1.5, 150, 3, 30, 15, 95);						//霍夫变换检测圆-200以下

								    if(Circle_time.total()!=0)
								    {
										/***************************测试代码*************************************/								
										long list = 0;
									    for(; list < Circle_time.total(); list++)										//把霍夫变换检测出的圆画出来  
									    {
											/***************************测试代码*************************************/
									        Point center = new Point(Circle_time.get( 0, (int) list)[0], 
									        					Circle_time.get( 0, (int) list)[1]);  
									        long radius =  (long) Circle_time.get( 0, (int) list)[2];  	
									        Core.circle( Line_Singlecolor_Mat, center, 0, new Scalar(0, 255, 0), -1, 8, 0 );  
									        Core.circle( Line_Singlecolor_Mat, center, (int) radius, new Scalar(0, 0, 255), 1, 8, 0 ); 
									    }  
										Line_Mat = Line_Singlecolor_Mat.clone();
										Circle_numble++;
										shape = "圆形";
										break;
								    }
									
								case 1://图像处理	
									corners = new MatOfPoint();
									
									Line_Mat = Line_Singlecolor_Mat.clone();
									
									Imgproc.goodFeaturesToTrack(Line_Mat, corners,
											50, 0.01, 1); 																// 角点检测、找出图像所有角点
									Line_Every_Draw_Mat = Line_Mat.clone();
									P1 = new Point(150,150);    //min_x
									P2 = new Point(0,0);	  	  //max_x
									P3 = new Point(150,150);	  //min_y
									P4 = new Point(0,0);	  	  //max_y
									if(corners.rows()>4)
										for (int i = 0; i < corners.rows(); i++) {
											double[] temp = corners.get(i, 0);
											
											if (temp[0] < P1.x ) {
												P1.x = temp[0];
												P1.y = temp[1]; 
											}
											
											if (temp[0] > P2.x ) {
												P2.x = temp[0];
												P2.y = temp[1]; 
											}
											
											if (temp[1] < P3.y ) {
												P3.x = temp[0];
												P3.y = temp[1]; 
											}
											
											if (temp[1] > P4.y ) {
												P4.x = temp[0];
												P4.y = temp[1]; 
											}
											P[i] = new Point(0,0);
											P[i].x = temp[0];
											P[i].y = temp[1];
//									/***************************测试代码*************************************/
//							        Core.circle( Line_Every_Draw_Mat,new Point (temp[0],temp[1]), 5, new Scalar(251, 12, 100), 1, 8, 0 );
											
										}
									else
										Unknown_numble++;
									/***************************测试代码*************************************/
//							        Core.circle( Line_Mat, P1, 3, new Scalar(251, 12, 100), 1, 8, 0 ); 
//							        Core.circle( Line_Mat, P2, 3, new Scalar(251, 12, 100), 1, 8, 0 ); 
//							        Core.circle( Line_Mat, P3, 3, new Scalar(251, 12, 100), 1, 8, 0 ); 
//							        Core.circle( Line_Mat, P4, 3, new Scalar(251, 12, 100), 1, 8, 0 ); 
							        
								case 2://三角形检测
									/***************************Triangle****************************************/
									P5 = P4.clone();
									if(pointToLine(P1.x,P1.y,P2.x,P2.y,P3.x,P3.y)>
									pointToLine(P1.x,P1.y,P2.x,P2.y,P4.x,P4.y))
										P5 = P3;
									Triangle_distance = 0;
									for(int i = 0;i<corners.rows(); i++)
										if(!(pointToLine(P1.x,P1.y,P2.x,P2.y,P[i].x,P[i].y)<10||
										     pointToLine(P1.x,P1.y,P5.x,P5.y,P[i].x,P[i].y)<10||
										     pointToLine(P5.x,P5.y,P2.x,P2.y,P[i].x,P[i].y)<10))
											 {Triangle_distance++;break;}
									if(Triangle_distance == 0)
									{	
									/***************************测试代码*************************************/
										Core.circle( Line_Mat, P1, 5, new Scalar(251, 12, 100), 1, 8, 0 ); 
								        Core.circle( Line_Mat, P2, 5, new Scalar(251, 12, 100), 1, 8, 0 ); 
								        Core.circle( Line_Mat, P5, 5, new Scalar(251, 12, 100), 1, 8, 0 ); 
								        color_y = 10;
								        if(P5 == P3)
								        	color_y = -10;
										Triangle_numble++;
										shape = "三角形";
										break;
									}
								case 3://图像处理
									
									x1 = 75; y1 = 75; q1 = 107;
									x2 = 75; y2 = 75; q2 = 107;
									x3 = 75; y3 = 75; q3 = 107;
									x4 = 75; y4 = 75; q4 = 107;
									
									for (int i = 0; i < corners.rows(); i++) {
										double[] temp = corners.get(i, 0);

										// 找四个顶点
										if (temp[0] < 75 && temp[1] < 75) {  //左上
											double t = Math.sqrt((temp[0] - 0)
													* (temp[0] - 0)
													+ (temp[1] - 0)
													* (temp[1] - 0));
											if (t < q1) {
												q1 = t;
												x1 = temp[0];
												y1 = temp[1];
											}
										}
										if (temp[0] > 75 && temp[1] < 75) {  //右上
											double t = Math
													.sqrt((temp[0] - 150)
															* (temp[0] - 150)
															+ (temp[1] - 0)
															* (temp[1] - 0));
											if (t < q2) {
												q2 = t;
												x2 = temp[0];
												y2 = temp[1];
											}
										}
										if (temp[0] > 75 && temp[1] > 75) {  //右下
											double t = Math
													.sqrt((temp[0] - 150)
															* (temp[0] - 150)
															+ (temp[1] - 150)
															* (temp[1] - 150));
											if (t < q3) {
												q3 = t;
												x3 = temp[0];
												y3 = temp[1];
											}
										}
										if (temp[0] < 75 && temp[1] > 75) {  //左下
											double t = Math.sqrt((temp[0] - 0)
													* (temp[0] - 0)
													+ (temp[1] - 150)
													* (temp[1] - 150));
											if (t < q4) {
												q4 = t;
												x4 = temp[0];
												y4 = temp[1];
											}
										}
									}
									
									/***************************测试代码*************************************/
							        Core.circle( Line_Mat, new Point(x1,y1), 10, new Scalar(251, 12, 100), 1, 8, 0 ); 
							        Core.circle( Line_Mat, new Point(x2,y2), 10, new Scalar(251, 12, 100), 1, 8, 0 ); 
							        Core.circle( Line_Mat, new Point(x3,y3), 10, new Scalar(251, 12, 100), 1, 8, 0 ); 
							        Core.circle( Line_Mat, new Point(x4,y4), 10, new Scalar(251, 12, 100), 1, 8, 0 ); 
									
									
									// 算出两条边的长度和底部两个角的角度
									Line_1 = Math.sqrt(Math.pow(x1 - x2, 2)
											+ Math.pow(y1 - y2, 2));
									Line_2 = Math.sqrt(Math.pow(x2 - x3, 2)
											+ Math.pow(y2 - y3, 2));
									Line_3 = Math.sqrt(Math.pow(x3 - x4, 2)
											+ Math.pow(y3 - y4, 2));
									Line_4 = Math.sqrt(Math.pow(x4 - x1, 2)
											+ Math.pow(y4 - y1, 2));

									corner_1 = 180
											/ Math.PI
											* Math.atan(Math.abs(x1 - x2)
													/ Math.abs(y1 - y2));
									corner_2 = 180
											/ Math.PI
											* Math.atan(Math.abs(y2 - y3)
													/ Math.abs(x2 - x3));
									corner_3 = 180
											/ Math.PI
											* Math.atan(Math.abs(x3 - x4)
													/ Math.abs(y3 - y4));
									corner_4 = 180
											/ Math.PI
											* Math.atan(Math.abs(y4 - y1)
													/ Math.abs(x4 - x1));
								case 4://五角星检测
									int Five_pointed_star_distance = 0;
									for(int i = 0;i<corners.rows(); i++)
										if(pointToLine(x1,y1,x2,y2,P[i].x,P[i].y)>15&&
									       pointToLine(x2,y2,x3,y3,P[i].x,P[i].y)>15&&
									       pointToLine(x3,y3,x4,y4,P[i].x,P[i].y)>15&&
									       pointToLine(x4,y4,x1,y1,P[i].x,P[i].y)>15)
											{
									        	Core.circle( Line_Mat, P[i], 5, new Scalar(251, 12, 100), 1, 8, 0 ); 
												Five_pointed_star_distance++;
												shape = "五角星";
												break;
											}
											if(Five_pointed_star_distance > 0&& corners.rows()>4)
											{		
												Five_pointed_star_numble++;
												break;
											}
								case 5://正方形检测
									/***************************Square****************************************/
									if ( Math.abs(corner_1 - 90) < 10 && 
										 Math.abs(corner_2 - 90) < 10 &&
										 Math.abs(corner_3 - 90) < 10 &&
										 Math.abs(corner_4 - 90) < 10 &&
										 Math.abs(Line_1 - Line_2) < 15 &&
										 Math.abs(Line_2 - Line_3) < 15 &&
										 Math.abs(Line_3 - Line_4) < 15 &&
										 Math.abs(Line_1 - Line_4) < 15
										 )
										{
											shape = "正方形";
											Square_numble++;
											break;
										}
								case 6://长方形检测
									/***************************Rectangle****************************************/
									if ( Math.abs(corner_1 - 90) < 15 && 
										 Math.abs(corner_2 - 90) < 15 &&
										 Math.abs(corner_3 - 90) < 15 &&
										 Math.abs(corner_4 - 90) < 15 &&
										 Math.abs(Line_1 - Line_2) > 10 &&
										 Math.abs(Line_1 - Line_3) < 10 &&
										 Math.abs(Line_3 - Line_4) > 10 &&
										 Math.abs(Line_2 - Line_4) < 10
										 )
										{
											shape = "长方形";
											Rectangle_numble++;
											break;
										}
								case 7://菱形检测
									/***************************Diamond****************************************/
									if(Math.abs(corner_1 - corner_3) < 10 &&
									   Math.abs(corner_2 - corner_4) < 10 &&
									   Math.abs(Line_1 - Line_3) < 10 &&
									   Math.abs(Line_2 - Line_4) < 10 )
										{
											shape = "菱形";
											Diamond_numble++;
											break;
										}
//								case 8://梯形检测
//									/***************************Trapezoid****************************************/
//									if((Math.abs(corner_1 + corner_2 - 180) < 45 && Math.abs(Line_1 - Line_3) > 15)||
//									   (Math.abs(corner_2 + corner_3 - 180) < 45 && Math.abs(Line_2 - Line_4) > 15))
//										{
//											shape = "梯形";
//											Trapezoid_numble++;
//											break;
//										}

								case 9:
//									shape = "未知形状";
//									Unknown_numble++;
									shape = "菱形";
									Diamond_numble++;
									break;
							}
							


							a = System.currentTimeMillis() - a;
							Process = Process+"第"+Shape_numble+"个形状:"+a+"ms  ";
							a = System.currentTimeMillis();
							
							color_x = color_x + Line_color_Mat.cols() / 2;
							color_y = color_y + Line_color_Mat.rows() / 2; 
							
							颜色判断(Line_color_Mat,new Point(color_x,color_y));
					        Core.circle( Line_color_Mat, new Point(color_x,color_y), 2, new Scalar(0, 255, 200), 1, 8, 0 );
					        Core.circle( Line_Mat, new Point(color_x,color_y), 2, new Scalar(0, 0, 0), 1, 8, 0 );

							Display_Bitmap(Shape_numble+10, Line_Mat);	
					        Display_color_Bitmap[Shape_numble] = Bitmap.createBitmap(Line_color_Mat.cols(),
									Line_color_Mat.rows(), Bitmap.Config.ARGB_8888);						//图像显示——1
							Utils.matToBitmap(Line_color_Mat, Display_color_Bitmap[Shape_numble]);
							}
						}
					
					       
			a = System.currentTimeMillis() - a;
			b = System.currentTimeMillis() - b;
			Process =Process + "形状个数"+Shape_numble;
			MainActivity.phHandler1.sendEmptyMessage(1);  	
			Result = "圆形个数"+Circle_numble+" "+"三角形个数"+Triangle_numble+"                                "+
					"正方形个数"+Square_numble+" "+"长方形个数"+Rectangle_numble+"                               "+
					"五角星个数"+Five_pointed_star_numble+" "+"梯形个数"+Trapezoid_numble+"                                "+
					"菱形个数"+Diamond_numble+" "+"未知形状个数"+Unknown_numble+"                                 "+b;
			
			数据收集();
			
			Result1 ="A"+Rectangle_numble+"B"+Circle_numble+"C"+Triangle_numble;
			完成标志位 = true;    
	}
	});
	
	public void init()
	{
		Original_Binary_Mat = new Mat(360, 640,0, Scalar.all(255)); 
		Original_MorphologyEx_Mat = Original_Binary_Mat.clone(); 
		Original_FindContours_Mat = Original_Binary_Mat.clone();
		Original_medianBlur_Mat = Original_Binary_Mat.clone();
		Original_Hsv_Mat = Original_Binary_Mat.clone();
		
		for(int i = 0; i<27 ; i++)
			Display_Bitmap[i] = null;
		
		for(int i = 0; i<18 ; i++)		
			Display_color_Bitmap[i] = null;

		Five_pointed_star_numble = 0;
		Rectangle_numble = 0;
		Trapezoid_numble = 0;
		Triangle_numble = 0;
		Diamond_numble = 0;
		Unknown_numble = 0;
		Circle_numble = 0;
		Square_numble = 0;
		Shape_numble = 0;
		
		完成标志位 = false;
		Process = "";
		Result = "";
		Result1 = "";
				
		Original_Mat = new Mat();
		Utils.bitmapToMat(Original_Bitmap, Original_Mat);	
		Imgproc.resize(Original_Mat, Original_Mat, new Size(640, 360));
		
	}
	
	public void init_()
	{
		Processed_Hsv_Mat = new Mat(Processed_Mat.rows(), Processed_Mat.cols(),0, Scalar.all(255)); 
		Processed_Singlecolor_FindContours_Mat = Processed_Hsv_Mat.clone();
		Processed_Singlecolor_Draw_Mat = Processed_Hsv_Mat.clone(); 
		Processed_Singlecolor_Hsv_Mat = Processed_Hsv_Mat.clone();
		Processed_FindContours_Mat = Processed_Hsv_Mat.clone();
		Processed_MorphologyEx_Mat = Processed_Hsv_Mat.clone();
		Processed_Singlecolor_Mat = Processed_Hsv_Mat.clone();
		Processed_MedianBlur_Mat = Processed_Hsv_Mat.clone();
		Processed_Canny_Mat = Processed_Hsv_Mat.clone();
		Processed_Erode_Mat = Processed_Hsv_Mat.clone();
		Processed_blur_Mat = Processed_Hsv_Mat.clone();

		Line_FindContours_Mat = new Mat(Processed_Binary_Mat.rows(), Processed_Binary_Mat.cols(),0, Scalar.all(0)); 
		Line_Singlecolor_Mat = new Mat(Processed_Binary_Mat.rows(), Processed_Binary_Mat.cols(),0, Scalar.all(255));
		Line_Hsv_Mat = new Mat(150, 150,0, Scalar.all(255)); 
		Line_Every_Draw_Mat = Line_FindContours_Mat.clone(); 
		Circle_Mat = new Mat(150, 150,0, Scalar.all(255));
		Line_Draw_Mat = Line_FindContours_Mat.clone();
		Line_Color_Mat= Line_FindContours_Mat.clone();
		Line_Mat = Line_FindContours_Mat.clone();
		Circle_time = Processed_Hsv_Mat.clone();
		lines = Line_FindContours_Mat.clone();

		corner_1 = 0.0; corner_2 = 0.0;
		corner_3 = 0.0; corner_4 = 0.0;
		Line_1 = 0.0;Line_2 = 0.0;
		Line_3 = 0.0; Line_4 = 0.0; 
	}
	
	public void 监视()
	{
		Mat air =new Mat(Processed_Binary_Mat.rows(), Processed_Binary_Mat.cols(),0, Scalar.all(255));
		Display_Bitmap(0 , Processed_Mat); 
		Display_Bitmap(1 , Processed_Binary_Mat); 
		Display_Bitmap(2 , Processed_MedianBlur_Mat); 
		Display_Bitmap(3 , Original_Mat); 
		Display_Bitmap(4 , air); 
		Display_Bitmap(5 , air); 
		Display_Bitmap(6 , air); 
		
	}
	
	public void Display_Bitmap(int x, Mat bitmap)
	{
		Display_Bitmap[x] = Bitmap.createBitmap(bitmap.cols(),
				bitmap.rows(), Bitmap.Config.ARGB_8888);						//图像显示——1
		Utils.matToBitmap(bitmap, Display_Bitmap[x]);
	}
	
    private double pointToLine(double x, double y, double x2, double y2, double x3,double y3) {
         double space = 0;
         double a, b, c;

         a = lineSpace(x, y, x2, y2);// 线段的长度
         b = lineSpace(x, y, x3, y3);// (x1,y1)到点的距离
         c = lineSpace(x2, y2, x3, y3);// (x2,y2)到点的距离

         if (c <= 0.000001 || b <= 0.000001) {
            space = 0;
            return space;
         }

         if (a <= 0.000001) {
            space = b;
            return space;
         }

         if (c * c >= a * a + b * b) {
            space = b;
            return space;
         }

         if (b * b >= a * a + c * c) {
            space = c;
            return space;
         }

         double p = (a + b + c) / 2;// 半周长
         double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
         space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
         return space;

     }

     // 计算两点之间的距离

     private double lineSpace(double x, double y, double x2, double y2) {
         double lineLength = 0;
         lineLength = Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
         return lineLength;
     }
	
	
	
	/**
	 * 将矩阵纠正到正确方向（仿射变换）
	 * 
	 * @param mat
	 *            ： 需纠正方向的矩阵
	 * @param Angle
	 *            ： 旋转的角度
	 * @return 纠向后的矩阵
	 */
	private Mat Rotate(Mat mat, double Angle) {
		Mat tempmat = mat.clone();
		int length = (int) (Math
				.sqrt((double) (tempmat.cols() * tempmat.cols() + tempmat
						.rows() * tempmat.rows())) * 1);
		Mat tempImg = new Mat(length, length, 0, Scalar.all(0));
		Mat dst = new Mat(length, length, 0, Scalar.all(0));// 输出图像

		int ROI_x = length / 2 - tempmat.cols() / 2;// ROI矩形左上角的x坐标
		int ROI_y = length / 2 - tempmat.rows() / 2;// ROI矩形左上角的y坐标
		Rect ROIRect = new Rect(ROI_x, ROI_y, tempmat.cols(), tempmat.rows());// ROI矩形
		Mat tempImgROI2 = new Mat(tempImg, ROIRect);// tempImg的中间部分
		tempmat.copyTo(tempImgROI2);// 将原图复制到tempImg的中心
		Point center = new Point(length / 2, length / 2);// 旋转中心
		Mat M = Imgproc.getRotationMatrix2D(center, -Angle, 1);// 计算旋转的仿射变换矩阵
		Imgproc.warpAffine(tempImg, dst, M, new Size(length, length));// 仿射变换
		return dst;
	}

	/**
	 * 将矩阵纠正到正确方向（仿射变换）
	 * 
	 * @param mat
	 *            ： 需纠正方向的矩阵
	 * @param Angle
	 *            ： 旋转的角度
	 * @return 纠向后的矩阵
	 */
	private Mat Rotate_color(Mat mat, double Angle) {
		Mat tempmat = mat.clone();
		int length = (int) (Math
				.sqrt((double) (tempmat.cols() * tempmat.cols() + tempmat
						.rows() * tempmat.rows())) * 1);
		Mat tempImg = new Mat(length, length, 24, Scalar.all(0));
		Mat dst = new Mat(length, length, 24, Scalar.all(0));// 输出图像

		int ROI_x = length / 2 - tempmat.cols() / 2;// ROI矩形左上角的x坐标
		int ROI_y = length / 2 - tempmat.rows() / 2;// ROI矩形左上角的y坐标
		Rect ROIRect = new Rect(ROI_x, ROI_y, tempmat.cols(), tempmat.rows());// ROI矩形
		Mat tempImgROI2 = new Mat(tempImg, ROIRect);// tempImg的中间部分
		tempmat.copyTo(tempImgROI2);// 将原图复制到tempImg的中心
		Point center = new Point(length / 2, length / 2);// 旋转中心
		Mat M = Imgproc.getRotationMatrix2D(center, -Angle, 1);// 计算旋转的仿射变换矩阵
		Imgproc.warpAffine(tempImg, dst, M, new Size(length, length));// 仿射变换
		return dst;
	}



	int qwe = 0;

	/**
	 * 颜色判断
	 * 
	 * @param mat
	 *            ：需要被判断颜色的图片
	 * @param point
	 *            ：选取一个点
	 * @return ：颜色
	 */
	private String 颜色判断(Mat mat, Point point) {
		Mat tempmat = mat.clone();
		String color = "未知颜色";
		Mat hsi1 = new Mat();
		Imgproc.cvtColor(tempmat, hsi1, Imgproc.COLOR_RGB2HSV_FULL);
		int t1[][] = { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 },
				{ 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 },
				{ 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
		// 围绕这个点的上下左右选取颜色参数
		t1[0][0] = (int) hsi1.get((int) point.y, (int) point.x)[0];
		t1[0][1] = (int) hsi1.get((int) point.y, (int) point.x)[1];
		t1[0][2] = (int) hsi1.get((int) point.y, (int) point.x)[2];

		t1[1][0] = (int) hsi1.get((int) point.y + 1, (int) point.x)[0];
		t1[1][1] = (int) hsi1.get((int) point.y + 1, (int) point.x)[1];
		t1[1][2] = (int) hsi1.get((int) point.y + 1, (int) point.x)[2];

		t1[2][0] = (int) hsi1.get((int) point.y, (int) point.x + 1)[0];
		t1[2][1] = (int) hsi1.get((int) point.y, (int) point.x + 1)[1];
		t1[2][2] = (int) hsi1.get((int) point.y, (int) point.x + 1)[2];

		t1[3][0] = (int) hsi1.get((int) point.y - 1, (int) point.x)[0];
		t1[3][1] = (int) hsi1.get((int) point.y - 1, (int) point.x)[1];
		t1[3][2] = (int) hsi1.get((int) point.y - 1, (int) point.x)[2];

		t1[4][0] = (int) hsi1.get((int) point.y, (int) point.x - 1)[0];
		t1[4][1] = (int) hsi1.get((int) point.y, (int) point.x - 1)[1];
		t1[4][2] = (int) hsi1.get((int) point.y, (int) point.x - 1)[2];

		t1[5][0] = (int) hsi1.get((int) point.y + 1, (int) point.x - 1)[0];
		t1[5][1] = (int) hsi1.get((int) point.y + 1, (int) point.x - 1)[1];
		t1[5][2] = (int) hsi1.get((int) point.y + 1, (int) point.x - 1)[2];

		t1[6][0] = (int) hsi1.get((int) point.y - 1, (int) point.x + 1)[0];
		t1[6][1] = (int) hsi1.get((int) point.y - 1, (int) point.x + 1)[1];
		t1[6][2] = (int) hsi1.get((int) point.y - 1, (int) point.x + 1)[2];

		t1[7][0] = (int) hsi1.get((int) point.y - 1, (int) point.x - 1)[0];
		t1[7][1] = (int) hsi1.get((int) point.y - 1, (int) point.x - 1)[1];
		t1[7][2] = (int) hsi1.get((int) point.y - 1, (int) point.x - 1)[2];

		t1[8][0] = (int) hsi1.get((int) point.y + 1, (int) point.x + 1)[0];
		t1[8][1] = (int) hsi1.get((int) point.y + 1, (int) point.x + 1)[1];
		t1[8][2] = (int) hsi1.get((int) point.y + 1, (int) point.x + 1)[2];

		int h = 0, s = 0, v = 0;
		int[] htemp1 = new int[9];
		int[] stemp1 = new int[9];
		int[] itemp1 = new int[9];
		for (int i = 0; i < 9; i++) {
			htemp1[i] = t1[i][0];
			if (htemp1[i] >= 235) {
				htemp1[i] = 255 - htemp1[i];
			}
			stemp1[i] = t1[i][1];
			itemp1[i] = t1[i][2];
		}
		bubbleSort(htemp1);// 排序
		bubbleSort(stemp1);
		bubbleSort(itemp1);
		for (int i = 2; i < 6; i++) {// 选取中间4个点
			h = h + htemp1[i];
			s = s + stemp1[i];
			v = v + itemp1[i];
		}

		h = h / 4;// 求平均
		s = s / 4;
		v = v / 4;

		for(int i = 1; i<7 ; i++)
		{
			switch (i)
			{
			case 1:绿色();break;
			case 2:黄色();break; 
			case 3:青色();break;
			case 4:黑色();break;
			case 5:蓝色();break;
			case 6:品色();break;
			case 7:红色1();break;
			case 8:红色2();break;
			}
			if ((min_h <= h&& max_h >= h) && 
				(min_s <= s&& max_s >= s) && 
				(min_i <= v&& max_i >= v))
				switch (i)
				{
					case 0:color = "红色";break;
					case 1:color = "绿色";break;
					case 2:color = "蓝色";break;
					case 3:color = "黄色";break;
					case 4:color = "品红";break;
					case 5:color = "青色";break;
					case 6:color = "黑色";break;
					case 7:color = "白色";break;
					case 8:color = "紫色";break;
					case 9:color = "橙色";break;
				}
		}
		Every_color[Shape_numble] ="(" + t1[0][0] + " " + t1[0][1]+ " " + t1[0][2] + ")" + 
								   "(" + t1[1][0] + " " + t1[1][1]+ " " + t1[1][2] + ")" + "	   	  		  			 		 	" +
								   "(" + t1[2][0] + " " + t1[2][1]+ " " + t1[2][2] + ")" +
								   "(" + t1[3][0] + " " + t1[3][1]+ " " + t1[3][2] + ")" + " 	  			  					 	" +
								   "(" + t1[4][0] + " " + t1[4][1]+ " " + t1[4][2] + ")" + 
								   "(" + t1[5][0] + " " + t1[5][1]+ " " + t1[5][2] + ")" + " 	  								 	" +
								   "(" + t1[6][0] + " " + t1[6][1]+ " " + t1[6][2] + ")" + 
								   "(" + t1[7][0] + " " + t1[7][1]+ " " + t1[7][2] + ")" + " 	  		    					 	" +
								   "(" + t1[8][0] + " " + t1[8][1]+ " " + t1[8][2] + ")" + 
								   "(" + 	h 	  + " " + 	 s	  + " " +	 v 	   + ")" + " 	  			 					 	" + 
								   	"结果：" + color + "的" + shape;
		return color;
	}


//	public void  黑色() {
//		min_h = (int) (0 * 1.416);  
//		max_h = (int) (180 * 1.416);   
//		
//		min_s = 0;  
//		max_s = 255;  
//		
//		min_i = 0;   
//		max_i = 46;   
//	}
//
//	public void  灰色() {
//		min_h = (int) (0 * 1.416);  
//		max_h = (int) (180 * 1.416);   
//		
//		min_s = 0; 
//		max_s = 43; 
//		
//		min_i = 46;   
//		max_i = 220;   
//	}
//	
//	public void  白色() {
//		min_h = (int) (0 * 1.416); 
//		max_h = (int) (180 * 1.416);  
//		
//		min_s = 0;   
//		max_s = 50;   
//		
//		min_i = 150;  
//		max_i = 255;  
//	}
//	
//	public void 红色() {
//		min_h = (int) (0 * 1.416);     
//		max_h = (int) (10 * 1.416);     
//		
//		min_s = 43;   
//		max_s = 255;   
//		
//		min_i = 46;   
//		max_i = 255;   
//	}
//
//	public void  品红() { 
//		min_h = (int) (156 * 1.416);  
//		max_h = (int) (180 * 1.416);  
//		
//		min_s = 43;  
//		max_s = 255;  
//		
//		min_i = 46; 
//		max_i = 255;  
//	}
//
//
//	public void  橙色() { 
//		min_h = (int) (11 * 1.416);  
//		max_h = (int) (25 * 1.416);  
//		
//		min_s = 43;  
//		max_s = 255;  
//		
//		min_i = 46; 
//		max_i = 255;  
//	}
//
//	public void  黄色() {
//		min_h = (int) (34);    
//		max_h = (int) (59);   
//		
//		min_s = 100;   
//		max_s = 255;  
//		
//		min_i = 100;  
//		max_i = 255;  
//	}
//
//	public void  绿色 () {                  
//		min_h = (int) (40 * 1.416);    
//		max_h = (int) (77 * 1.416);    
//		
//		min_s = 100;  
//		max_s = 255;   
//		
//		min_i = 100;   
//		max_i = 255;   
//	}
//
//	public void   青色() {
//		min_h = (int) (78 * 1.416);
//		max_h = (int) (110 * 1.416);
//		
//		min_s = 43; 
//		max_s = 255; 
//		
//		min_i = 46; 
//		max_i = 255; 
//	}
//
//	public void  蓝色() {
//		min_h= (int) (110 * 1.416);  
//		max_h= (int) (124 * 1.416);   
//		
//		min_s = 43;   
//		max_s = 255;   
//		
//		min_i = 46;  
//		max_i = 255;   
//	}
//
//	public void  紫色() {
//		min_h = (int) (125 * 1.416);  
//		max_h = (int) (155 * 1.416); 
//		
//		min_s = 43;  
//		max_s = 255;  
//		
//		min_i = 46;  
//		max_i = 255; 
//	}
//	
//
//	public void   绿色1() {
//		min_h = 60;
//		max_h = 115;
//		
//		min_s = 49; 
//		max_s = 172; 
//		
//		min_i = 123; 
//		max_i = 255; 
//	}

	public void   背景颜色() {
		min_h = 100;
		max_h = 140;
		
		min_s = 25; 
		max_s = 255; 
		
		min_i = 25; 
		max_i = 255; 
	}
	
	public void   红色1() {
		min_h = 0;
		max_h = 14;
		
		min_s = 25; 
		max_s = 255; 
		
		min_i = 25; 
		max_i = 255; 
	}		
	public void   红色2() {
		min_h = 218;
		max_h = 255;
		
		min_s = 25; 
		max_s = 255; 
		
		min_i = 25; 
		max_i = 255; 
	}	
	
	public void  绿色 () {                  
		min_h = 56;    
		max_h = 107;    
		
		min_s = 100;  
		max_s = 255;   
		
		min_i = 100;   
		max_i = 255;   
	}	
	
	public void  黄色 () {                  
		min_h = 56;    
		max_h = 107;    
		
		min_s = 100;  
		max_s = 255;   
		
		min_i = 100;   
		max_i = 255;   
	}	
	
	public void  蓝色 () {                  
		min_h= 154;  
		max_h= 173;   
		
		min_s = 43;   
		max_s = 255;   
		
		min_i = 46;  
		max_i = 255;  
	}
	
	public void  品色 () {                  
		min_h= 180;  
		max_h= 200;   
		
		min_s = 43;   
		max_s = 255;   
		
		min_i = 46;  
		max_i = 255;  
	}
	
	public void   青色() {
		min_h = 110;
		max_h = 150;
		
		min_s = 43; 
		max_s = 255; 
		
		min_i = 46; 
		max_i = 255; 
	}
	public void  黑色() {
	min_h = 0;  
	max_h = 255;   
	
	min_s = 0;  
	max_s = 255;  
	
	min_i = 0;   
	max_i = 46;   
}


public void  白色() {
	min_h = 0; 
	max_h = 255;  
	
	min_s = 0;   
	max_s = 50;   
	
	min_i = 150;  
	max_i = 255;  
}
	/**
	 * 将图片矩阵纠正到正确方向
	 * 
	 * @param degree
	 *            ： 图片矩阵被系统旋转的角度
	 * @param mat
	 *            ： 需纠正方向的图片矩阵
	 * @return 纠向后的图片矩阵
	 */
//	private Mat rotateMat(float degree, Mat mat) {
//
//		Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(),
//				Bitmap.Config.ARGB_8888);
//		Utils.matToBitmap(mat, bitmap);
//
//		Matrix matrix = new Matrix();
//		matrix.postRotate(degree);
//
//		Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//				bitmap.getHeight(), matrix, true);
//
//		Mat rMat = new Mat(bm.getHeight(), bm.getWidth(), 1, Scalar.all(0));
//		Utils.bitmapToMat(bm, rMat);
//
//		return rMat;
//	}
 
	/**
	 * 冒泡法排序
	 * 
	 * @param idx_2
	 *            ：需要排序的数组
	 */
	private void bubbleSort(double[] idx_2) {
		double temp; // 记录临时中间值
		int size = idx_2.length; // 数组大小
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (idx_2[i] < idx_2[j]) { // 交换两数的位置
					temp = idx_2[i];
					idx_2[i] = idx_2[j];
					idx_2[j] = temp;
				}
			}
		}
	}
	
	/**
	 * 冒泡法排序
	 * 
	 * @param idx_2
	 *            ：需要排序的数组
	 */
	private void bubbleSort(int[] idx_2) {
		int temp; // 记录临时中间值
		int size = idx_2.length; // 数组大小
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (idx_2[i] < idx_2[j]) { // 交换两数的位置
					temp = idx_2[i];
					idx_2[i] = idx_2[j];
					idx_2[j] = temp;
				}
			}
		}
	}
	
	private void 数据收集() {
		if(Circle_numble != 0)
		Result = "圆形"+Circle_numble + "个,";

		if(Triangle_numble != 0)
			Result = "三角形"+Triangle_numble  + "个," + Result;
			
		if(Square_numble != 0)
			Result = "正方形"+Square_numble  + "个," + Result;

		if(Rectangle_numble != 0)
			Result = "长方形"+Rectangle_numble  + "个," + Result;

		if(Five_pointed_star_numble != 0)
			Result = "五角星"+Five_pointed_star_numble  + "个," + Result;

		if(Trapezoid_numble != 0)
			Result = "梯形"+Trapezoid_numble  + "个," + Result;
		
		if(Diamond_numble != 0)
			Result = "菱形"+Diamond_numble  + "个," + Result;
						
	}
}
