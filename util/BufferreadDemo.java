package util;
import java.io.*;
public class BufferreadDemo {
	 static long n;
	static int num = 0;
	//���ļ����xȡ���D��������
	public int[] transform(int size) {
		String s =null;
		
		try {
		RandomAccessFile in = new RandomAccessFile("./data/realdata.txt","r");
		
	    in.seek(n);
	    //���ļ��ж����ַ���
	    s = in.readLine();
	    num++;
	    System.out.println("�����������ǣ�"+num);
	    n=in.getFilePointer();
		   } catch(IOException e){
			   e.printStackTrace();
		   }
		System.out.println(s);
		    //System.out.println(n);
			if(s!=null){
			System.out.println("�������ַ����ǣ�"+s);
			//ÿ���ַ����ĳ���
			int le=s.length();
			//System.out.println("�������ַ��������ǣ�"+le);
			byte[] byteBuffer = new byte[le];
			
			int[] a = new int[size];
			int count = 0;
			//��Stringת��Ϊbyte����
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
						System.out.println("����������");
						
					}	
				}
				
				
				
				return a;
			}
			else{
				
				System.out.println("�ѽ����ļ�ĩβ");
				n=0;
				return null;
			}
		}
	
	//�xȡһ�е��L��
	public int getlength() throws IOException{
		BufferedReader in = new BufferedReader(new FileReader("E:\\ff.txt"));
	    String s;
	    //���ļ��ж����ַ���
	    s = in.readLine();
		
		//ÿ���ַ����ĳ���
		int le=s.length();
		in.close();
		return le;
	}
	
	//�D�����ַ���
	public String transString(int[] a) throws UnsupportedEncodingException{
		byte[] b = new byte[a.length]; 
		for(int i=0;i<a.length;i++){
			b[i]=(byte) a[i];
		}
		String s = new String(b, "GB2312");
		return s;
	}
	}


