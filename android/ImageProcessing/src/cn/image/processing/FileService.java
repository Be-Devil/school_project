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
	public static String ��״��ɫ = "/sdcard/ʶ��ͼƬ/��״��ɫ/";
	public static String ���� = "/sdcard/ʶ��ͼƬ/����/";
	public static String ��ͨ�� = "/sdcard/ʶ��ͼƬ/��ͨ��/";
	public static String ��ά�� = "/sdcard/ʶ��ͼƬ/��ά��/";
	public static String ƽ�巢�� = "/sdcard/ʶ����Ϣ/ƽ�巢��/";
	public static String ƽ����� = "/sdcard/ʶ����Ϣ/ƽ�����/";
	public String FileName ="";
	
	Time t = new Time();
	
	
	/**
	 * �����ļ�
	 * 
	 * @param fileName
	 *            �ļ�����
	 * @param content
	 *            �ļ�����
	 * @throws IOException
	 */
	public void saveToSDCard(String filefolder,String fileName, byte[] content)
			throws IOException {

		// ���ǲ�ͬ�汾��sdCardĿ¼��ͬ������ϵͳ�ṩ��API��ȡSD����Ŀ¼
		File file = new File(filefolder, fileName);
		if (!file.isDirectory()) {
			file.createNewFile();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(content);
		fileOutputStream.close();
	}

	/**
	 * ��ȡ�ļ�����
	 * 
	 * @param fileName
	 *            �ļ�����
	 * @return �ļ�����
	 * @throws IOException
	 */
	public String read(String fileName) throws IOException {
		File file = new File(fileFolder, fileName);
		if (file.exists()) {
			FileInputStream fileInputStream = new FileInputStream(file);
			// ��ÿ�ζ�ȡ������д�뵽�ڴ��У�Ȼ����ڴ��л�ȡ
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			// ֻҪû���꣬���ϵĶ�ȡ
			while ((len = fileInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			// �õ��ڴ���д�����������
			byte[] data = outputStream.toByteArray();
			fileInputStream.close();
			return new String(data);
		} else
			return "";

	}

	/**
	 * ����ͼƬ
	 * 
	 * @param b
	 *            ͼƬ��Դ
	 * @param strFileName
	 *            ͼƬ����
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
	 * ��ȡͼƬ
	 * 
	 * @param strFileName
	 *            ͼƬ����
	 * @return ͼƬ����
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
	 * �½��ļ���
	 * 
	 * @param strFileName
	 *            �ļ�������
	 * @return �ļ�������
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
////		file.createFile_(file.��״��ɫ);
//		Bitmap b = file.readPhoto(FileService.��״��ɫ+"��״" + ".png");
//		ColorShapeRecognition ��״ = new ColorShapeRecognition(b);
//		while(!��״.��ɱ�־λ);
//		client.VoiceText(ColorShapeRecognition.Result);client.delay(ColorShapeRecognition.Result.length()/3*1000+2000);
//		if(ColorShapeRecognition.Result1.length() == 6)
//		MainActivity.m3 = ColorShapeRecognition.Result1;
//	}
//	public void test5(View v){		
//	Bitmap b = bitmap1;
//	FileService  file = new FileService();
//	file.createFile_(file.��״��ɫ);
//	file.savePhoto(b, "��״" + ".png");	
//	}
	
	/**
	 * ɾ���ļ����Լ��������������
	 * 
	 * @param strFileName
	 *            �ļ�������
	 * @return �ļ�������
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
