package cn.image.processing;

public class JY {
	public static String SZ_string[]={"","",""};

	////////////////////////////////////////////////������У��///////////////////////////
	public static void HMM(int[] s) {
			SZ_string[0]="";
			SZ_string[1]="";
			SZ_string[2]="";
			char[] str1=new char[100];
			char[] str2=new char[100];
			boolean �״α�־λ= false;
			String string ="";
			int[] SZ=s; 
			int x0 = 0,j = 0,s1 = 0;
			str2[0]='0';
			
			for(int i=0;i<7;i++)
			{
				if(i==6)
					string=Integer.toBinaryString(SZ[6-i])+string;
				else
					string=����7λ(Integer.toBinaryString(SZ[6-i]))+string;}		                  //int[]	 ת��   	String
			str1=string.toCharArray();	
			s1=str1.length;//String 	 ת��   	char[]
			for(int i=1;i<str1.length+6;i++)														  //�������      str2
			{
				if(i==1||i==2||i==4||i==8||i==16||i==32)                           		  
					str2[i]='0';
				else 
					{str2[i]=str1[j];j++;}
				if(str2[i]=='1')                                                       
					if(!�״α�־λ)
					{x0=i;�״α�־λ=true;}
					else
					{x0=x0^i;}
			}
			string ="";
			string=����5λ(Integer.toBinaryString(x0));		
			str1=string.toCharArray();
			x0 = 0;j= 5;	
			
			for(int i=1;i<s1+6;i++)
			{
				if(i==1||i==2||i==4||i==8||i==16)                             
					{j--;str2[i]=str1[j];}
				SZ_string[x0]=SZ_string[x0]+str2[i];
			}                                                           
	}
	
	public static String ����7λ(String str){
			String a="000";
			while(true){
				if(str.length()!=a.length())
					str='0'+str;
				else return str;
			}}
	
	public static String ����5λ(String str){
		String a="00000";
		while(true){
			if(str.length()!=a.length())
				str='0'+str;
			else return str;
		}}
	////////////////////////////////////////////////������У��//////////////////////////////////////////////
	
//	
//	////////////////////////////////////////////////������У��///////////////////////////
//	public static int[] HMM(int[] s) {
//			char[] str1=new char[100];
//			char[] str2=new char[100];
//			boolean �״α�־λ= false;
//			String string ="";
//			int[] SZ=s; 
//			int x0 = 0,j = 0;
//			str2[0]='0';
//			
//			for(int i=0;i<6;i++)
//			{string=string+����7λ(Integer.toBinaryString(SZ[i]));}		                  //int[]	 ת��   	String
//			str1=string.toCharArray();	                    							  //String 	 ת��   	char[]
//			for(int i=1;i<49;i++)														  //�������      str2
//			{
//				if(i==1||i==2||i==4||i==8||i==16||i==32)                           		  
//					str2[i]='0';
//				else 
//					{str2[i]=str1[j];j++;}
//				if(str2[i]=='1')                                                       
//					if(!�״α�־λ)
//					{x0=i;�״α�־λ=true;}
//					else
//					{x0=x0^i;}
//			}
//			string ="";
//			string=����7λ(Integer.toBinaryString(x0));		
//			str1=string.toCharArray();
//			x0 = 0;j= 7;	
//			String SZ_string[]={"","","","","","",""};
//			
//			for(int i=1;i<49;i++)
//			{
//				if(i==1||i==2||i==4||i==8||i==16||i==32)                             
//					{j--;str2[i]=str1[j];}
//				SZ_string[x0]=SZ_string[x0]+str2[i];
//				if(i%8==0)
//					{SZ[x0]=Integer.parseInt(Integer.valueOf(SZ_string[x0],2).toString());
//					x0++;}
//			}
//			return SZ;                                                             
//	}
//	
//	public static String ����7λ(String str){
//			String a="0000000";
//			while(true){
//				if(str.length()!=a.length())
//					str='0'+str;
//				else return str;
//			}}
//	////////////////////////////////////////////////������У��//////////////////////////////////////////////
	
	///////////////////////////////////////////////CRCУ��ԭ��////////////////////////////////////////////////
	
	/////////////////////////////////////////////CRC8/16У��ԭ��///////////////////////
	public static int d;
	public static int CRC16(int[] s){
	int[] SZ16=s;
	long sz16=0x18005L;
	String string = "";
	for(int i=0;i<SZ16.length;i++)
	string =string+Integer.toBinaryString(SZ16[i]);//"1110101";
	String string0=Long.toBinaryString(sz16);
	String string1=��λ(string,string0);
	String string2;
	char[] str= string1.toCharArray();
	char[] str3= string1.toCharArray();
	char[] str1;
	int str0=Integer.parseInt(Integer.valueOf(string0,2).toString());
	int c;
	for(int i=0;i<=string1.length()-string0.length();i++)
	{
		if(str[i]=='1')
		{
			string2="";c=0;
			for(int j=i;j<i+string0.length();j++)
				string2=string2+str[j];
			str1=����(Integer.toBinaryString(((Integer.parseInt(Integer.valueOf(string2,2).toString()))^str0)),string0).toCharArray();
			for(int j=i;j<i+string0.length();j++)
				{str[j]=str1[c];c++;}
		}
	}
	
	for(int i=string1.length()-d;i<string1.length();i++)
	str3[i]=str[i];
	
//	for(int i=0;i<string1.length()-string0.length();i++)
//	{
//		if(str3[i]=='1')
//		{
//			string2="";c=0;
//			for(int j=i;j<i+string0.length();j++)
//				string2=string2+str3[j];
//			str1=����(Integer.toBinaryString(Integer.parseInt(Integer.valueOf(string2,2).toString())^str0),string0).toCharArray();
//			for(int j=i;j<i+string0.length();j++)
//				{str3[j]=str1[c];c++;}
//		}
//	}
	string=new String(str);
	return Integer.parseInt(string);
	}
	
	public static String ��λ(String str,String str0){
		for(int i=1;i<str0.length();i++)
			{str=str+'0';d++;}
		return str;
	}
	
	public static String ����(String str,String a){
		while(true){
			if(str.length()!=a.length())
				str='0'+str;
			else return str;
		}}
	public static String ����8λ(String str){
		String a="00000000";
		while(true){
			if(str.length()!=a.length())
				str='0'+str;
			else return str;
		}}
	/////////////////////////////////////////////CRC8/16У��ԭ��/////////////////////////////////////////
	
	/////////////////////////////////////////////CRC32У��ԭ��/////////////////////////////////////
	
	public static long CRC32(int[] s){
		int[] SZ16=s;
		long sz16=0x104c11db7l;
		String string = "";
		for(int i=0;i<SZ16.length;i++)
		if(string=="")
			string=Long.toBinaryString(SZ16[i]);
		else
			string =string+����8λ(Long.toBinaryString(SZ16[i]));//"1110101";
		String string0=Long.toBinaryString(sz16);//"1110101"
		String string1=��λ(string,string0);
		String string2;
		char[] str= string1.toCharArray();
		char[] str3= string1.toCharArray();
		char[] str1;
		long str0=Long.parseLong(Long.valueOf(string0,2).toString());
		long s0=0l;
		int c;
		for(int i=0;i<=string1.length()-string0.length();i++)
		{
			if(str[i]=='1')
			{
				string2="";c=0;
				for(int j=i;j<i+string0.length();j++)
					string2=string2+str[j];
				str1=����(Long.toBinaryString(((Long.parseLong(Long.valueOf(string2,2).toString()))^str0)),string0).toCharArray();
				for(int j=i;j<i+string0.length();j++)
					{str[j]=str1[c];c++;}
			}
		}
		
		for(int i=string1.length()-d;i<string1.length();i++)
		str3[i]=str[i];
//		
//		for(int i=0;i<string1.length()-string0.length();i++)
//		{
//			if(str3[i]=='1')
//			{
//				string2="";c=0;
//				for(int j=i;j<i+string0.length();j++)
//					string2=string2+str3[j];
//				str1=����(Long.toBinaryString(Long.parseLong(Long.valueOf(string2,2).toString())^str0),string0).toCharArray();
//				for(int j=i;j<i+string0.length();j++)
//					{str3[j]=str1[c];c++;}
//			}
//		}
		string=new String(str);
		s0=Long.parseLong(Long.valueOf(string,2).toString());
		return s0;
		}


	
	////////////////////////////////////////////CRC32У��ԭ��/////////////////////////////////////////

	//////////////////////////////////////////////CRCУ��ԭ��/////////////////////////////////////////






}
