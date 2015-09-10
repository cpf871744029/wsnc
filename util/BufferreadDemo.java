package util;
import java.io.*;
public class BufferreadDemo {
	 static long n;
	static int num = 0;
	//從文件中讀取并轉化為整形
	public int[] transform(int size) {
		String s =null;
		
		try {
		RandomAccessFile in = new RandomAccessFile("./data/realdata.txt","r");
		
	    in.seek(n);
	    //从文件中读出字符串
	    s = in.readLine();
	    num++;
	    System.out.println("读出的数据是："+num);
	    n=in.getFilePointer();
		   } catch(IOException e){
			   e.printStackTrace();
		   }
		System.out.println(s);
		    //System.out.println(n);
			if(s!=null){
			System.out.println("读出的字符串是："+s);
			//每行字符串的长度
			int le=s.length();
			//System.out.println("读出的字符串长度是："+le);
			byte[] byteBuffer = new byte[le];
			
			int[] a = new int[size];
			int count = 0;
			//将String转换为byte数组
			byteBuffer = s.getBytes();
			for(int i=0;i<le;i++){
				int  x = byteBuffer[i];				
				}
			System.out.println();
				for(int i=0;i<le;i++){
					if (byteBuffer[i]>0&&byteBuffer[i]<256){
						a[count] = byteBuffer[i];
						count++;
					}
					else{
						System.out.println("數據異常！");
						
					}	
				}
				
				
				
				return a;
			}
			else{
				
				System.out.println("已經到文件末尾");
				n=0;
				return null;
			}
		}
	
	//讀取一行的長度
	public int getlength() throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("E:\\ff.txt"));
	    String s;
	    //从文件中读出字符串
	    s = in.readLine();
		
		//每行字符串的长度
		int le=s.length();
		in.close();
		return le;
	}
	
	//轉化回字符串
	public String transString(int[] a) throws UnsupportedEncodingException{
		byte[] b = new byte[a.length]; 
		for(int i=0;i<a.length;i++){
			b[i]=(byte) a[i];
		}
		String s = new String(b, "GB2312");
		return s;
	}
	}


