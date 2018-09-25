package cn.image.processing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.text.format.Time;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileService {
	File fileFolder = new File(Environment.getExternalStorageDirectory()
			+ "/test2/");
	public static String 形状颜色 = "/sdcard/识别图片/形状颜色/";
	public static String 车牌 = "/sdcard/识别图片/车牌/";
	public static String 交通灯 = "/sdcard/识别图片/交通灯/";
	public static String 二维码 = "/sdcard/识别图片/二维码/";
	public static String 平板发送 = "/sdcard/识别信息/平板发送/";
	public static String 平板接受 = "/sdcard/识别信息/平板接受/";
	public String FileName ="";
	
	Time t = new Time();
	
	
	/**
	 * 保存文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws IOException
	 */
	public void saveToSDCard(String filefolder,String fileName, byte[] content)
			throws IOException {

		// 考虑不同版本的sdCard目录不同，采用系统提供的API获取SD卡的目录
		File file = new File(filefolder, fileName);
		if (!file.isDirectory()) {
			file.createNewFile();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(content);
		fileOutputStream.close();
	}

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 *            文件名称
	 * @return 文件内容
	 * @throws IOException
	 */
	public String read(String fileName) throws IOException {
		File file = new File(fileFolder, fileName);
		if (file.exists()) {
			FileInputStream fileInputStream = new FileInputStream(file);
			// 把每次读取的内容写入到内存中，然后从内存中获取
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			// 只要没读完，不断的读取
			while ((len = fileInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			// 得到内存中写入的所有数据
			byte[] data = outputStream.toByteArray();
			fileInputStream.close();
			return new String(data);
		} else
			return "";

	}

	/**
	 * 保存图片
	 * 
	 * @param b
	 *            图片资源
	 * @param strFileName
	 *            图片名称
	 * @throws IOException
	 */
	public void savePhoto(Bitmap b, String strFileName) {
		try {
			File file;
			 file = new File(FileName+strFileName);
			
			if (!file.isDirectory()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			if (fos != null) {
				b.compress(Bitmap.CompressFormat.PNG, 80, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 读取图片
	 * 
	 * @param strFileName
	 *            图片名称
	 * @return 图片内容
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public Bitmap readPhoto(String strFileName) {
		String path =  strFileName;
		if (path != null) {
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			return bitmap;
		} else
			return null;

	}
	
	/**
	 * 新建文件夹
	 * 
	 * @param strFileName
	 *            文件夹名称
	 * @return 文件夹内容
	 * @throws IOException
	 */
	public void createFile(Object newFileName)
	{
		t.setToNow();
		FileName=newFileName.toString()+"/"+t.hour+":"+t.minute+":"+t.second+"/";
		File parent = new File(FileName);
	    if (!parent.exists()) {
	    	parent.mkdirs();
	    }	
	}
	
	public void createFile_(Object newFileName)
	{
		t.setToNow();
		FileName=newFileName.toString();
		File parent = new File(FileName);
	    if (!parent.exists()) {
	    	parent.mkdirs();
	    }	
	}
	
//	public void test4(View v){
//		FileService  file = new FileService();
////		file.createFile_(file.形状颜色);
//		Bitmap b = file.readPhoto(FileService.形状颜色+"形状" + ".png");
//		ColorShapeRecognition 形状 = new ColorShapeRecognition(b);
//		while(!形状.完成标志位);
//		client.VoiceText(ColorShapeRecognition.Result);client.delay(ColorShapeRecognition.Result.length()/3*1000+2000);
//		if(ColorShapeRecognition.Result1.length() == 6)
//		MainActivity.m3 = ColorShapeRecognition.Result1;
//	}
//	public void test5(View v){		
//	Bitmap b = bitmap1;
//	FileService  file = new FileService();
//	file.createFile_(file.形状颜色);
//	file.savePhoto(b, "形状" + ".png");	
//	}
	
	/**
	 * 删除文件夹以及里面的所有内容
	 * 
	 * @param strFileName
	 *            文件夹名称
	 * @return 文件夹内容
	 * @throws IOException
	 */	
	
    public static void delete(File file) {  
        if (file.isFile()) {  
            file.delete();  
            return;  
        }  
  
        if(file.isDirectory()){  
            File[] childFiles = file.listFiles();  
            if (childFiles == null || childFiles.length == 0) {  
                file.delete();  
                return;  
            }  
      
            for (int i = 0; i < childFiles.length; i++) {  
                delete(childFiles[i]);  
            }  
            file.delete();  
        }  
    }
	 
	
}
