package routing.network_coding;

import java.util.*;

import core.*;
import network_coding.*;

public abstract class Network_coding {
	static final int generation=5;
	public static Message Refresh(Message m1, Message m2) { 
		String id1 = "";
		String id = "CR1";
		int payloadLen = m2.getSize();
		FiniteField ff = FiniteField.getDefaultFiniteField();
		Random r = new Random();
		FiniteFieldVector networkOutput = new FiniteFieldVector(payloadLen, ff);
		FiniteFieldVector payload = new FiniteFieldVector(payloadLen, ff);
		payload = ff.byteToVector(m2.getPayload());
		int[] coded_coef = new int[m1.getCodedid().length];
		int y = r.nextInt(ff.getCardinality());while(y==0) {y = r.nextInt(ff.getCardinality());}
		networkOutput = (ff.byteToVector(m1.getPayload()).scalarMultiply(y));
		for (int i = 0; i < m1.getCodedid().length; i++) {
			id1 = id1 + m1.getCodedid()[i];
			if ((m1.getCodedid()[i]).equals(m2.getId())) {
				int x = r.nextInt(ff.getCardinality());while(x==0) {x = r.nextInt(ff.getCardinality());}
				networkOutput = networkOutput.add(payload.scalarMultiply(x));
				/* System.out.println(x); */
				coded_coef[i] = ff.sum[x][ff.mul[m1.getCoding_Coef()[i]][y]];
				id = id + "T"+coded_coef[i];
			} else {
				coded_coef[i] = ff.mul[m1.getCoding_Coef()[i]][y];
				id = id + "T"+coded_coef[i];

			}
		}
		id = id + "T"+id1;
		int[] ret = ff.vectorToBytes(networkOutput);
		Message m = new Message(m1.getFrom(), m1.getTo(), id, payloadLen);
		m.setCoded();
		m.setPayload(ret);
		if (m1.getTtl() > m2.getTtl()) {
			int ttl = m1.getTtl();
			m.setTtl(ttl);
		} else {
			int ttl = m2.getTtl();
			m.setTtl(ttl);
		}
		m.setCoding_Coef(coded_coef);
		m.setCodedId(m1.getCodedid());
		m.setAppID(m1.getAppID());
		m.setCreation_times(m1.getCreation_times());
		List<DTNHost> host_list = m1.getHops();
		m.setHops(host_list);
		m.setGeneration(m1.getGeneration());
		return m;
	}

	public static Message Code(Vector<Message> v) {
		int blockNumber = v.size();
		int payloadLen = v.get(0).getSize();
		int generationTh=v.get(0).getGeneration();
		FiniteField ff = FiniteField.getDefaultFiniteField();
		int[][] C = new int[blockNumber][generation];
		//System.out.println("code befor packets:");
		for(int i=0;i<blockNumber;i++){
	//		System.out.println(v.get(i).getId());
			//System.out.println(Arrays.toString(v.get(i).getPayload()));
			Arrays.fill(C[i], -1);
			if(v.get(i).is_coded()==false){
				int temp=Integer.parseInt(v.get(i).getId().substring(1, v.get(i).getId().length()));
				temp=temp%generation;
				C[i][temp]=1;
			}
			else{
				String[] codeId = v.get(i).getCodedid();
				for(int j=0;j<codeId.length;j++){
					int temp=Integer.parseInt(codeId[j].substring(1,codeId[j].length()));
					temp=temp%generation;
					C[i][temp]=v.get(i).getCoding_Coef()[j];
				}
			}
		}
		FiniteFieldVector networkOutput = new FiniteFieldVector(payloadLen, ff);
		Random r = new Random();
		int[] codeVector =new int[generation];
		Arrays.fill(codeVector, 0);
		double[] creationTimesTemp = new double[generation];
		Arrays.fill(creationTimesTemp, 0);
		int e=0,f=0;
		for(int i=0;i<blockNumber;i++){
			int x = r.nextInt(ff.getCardinality());
			while (x==0) {x = r.nextInt(ff.getCardinality());}
			//System.out.println(Arrays.toString(v.get(i).getPayload()));
			//System.out.println("v.get(i).getPayload()"+v.get(i).getPayload().length+ "\tsize:"+v.get(i).getSize());
			networkOutput = networkOutput.add(ff.byteToVector(v.get(i).getPayload()).scalarMultiply(x));
			for(int j=0,h=0;j<generation;j++){				
				if(C[i][j]!=-1){
					codeVector[j]=ff.sum[codeVector[j]][ff.mul[x][C[i][j]]];
					if(v.get(i).is_coded()==false){
						creationTimesTemp[j]=v.get(i).getCreationTime();
					}
					else{						
						creationTimesTemp[j]=v.get(i).getCreation_times()[h++];
					}
				}
			}			
		}
		e=0;
		f=0;
		for(int j=0;j<generation;j++){
			if(creationTimesTemp[j]!=0){
				e++;
			}
			if(codeVector[j]!=0){
				f++;
			}
		}
		while(e!=f){
			networkOutput.setToZero();
			Arrays.fill(codeVector, 0);
			Arrays.fill(creationTimesTemp,0);
			for(int i=0;i<blockNumber;i++){
				int x = r.nextInt(ff.getCardinality());
				while (x==0) {x = r.nextInt(ff.getCardinality());}
				networkOutput = networkOutput.add(ff.byteToVector(v.get(i).getPayload()).scalarMultiply(x));
				for(int j=0,h=0;j<generation;j++){				
					if(C[i][j]!=-1){
						codeVector[j]=ff.sum[codeVector[j]][ff.mul[x][C[i][j]]];
						if(v.get(i).is_coded()==false){
							creationTimesTemp[j]=v.get(i).getCreationTime();
						}
						else{						
							creationTimesTemp[j]=v.get(i).getCreation_times()[h++];
						}
					}
				}			
			}
			e=0;
			f=0;
			for(int j=0;j<generation;j++){
				if(creationTimesTemp[j]!=0){
					e++;
				}
				if(codeVector[j]!=0){
					f++;
				}
			}
		}
		int k=0;
		for(int j=0;j<generation;j++){
			if(creationTimesTemp[j]!=0){
				k++;
			}
		}		
		double[] creation_times = new double[k];
		for(int j=0,m=0;j<generation;j++){
			if(creationTimesTemp[j]!=0){
				creation_times[m++]=creationTimesTemp[j];
			}
		}
		String id1 = "";
		String id = "C";
		String[] coded_id = new String[k];
		int[] coding_coefficients = new int[k];
		k=0;
		for(int j=0;j<generation;j++){
			if(codeVector[j]!=0){
				coded_id[k]="M"+(generationTh*generation+j);
				coding_coefficients[k]=codeVector[j];
				k++;
				id1 = id1+ "M"+(generationTh*generation+j);
				id = id + "T"+codeVector[j];				
			}
		}
		id = id + "T"+id1;
		int[] ret = ff.vectorToBytes(networkOutput);
		Message codedpacket = new Message(v.get(0).getFrom(), v.get(0).getTo(),
				id, payloadLen);
		codedpacket.setCoded();
		codedpacket.setCoding_Coef(coding_coefficients);
		codedpacket.setPayload(ret);
		codedpacket.setCodedId(coded_id);
		codedpacket.setAppID(v.get(0).getAppID());
		codedpacket.setCreation_times(creation_times);
//		if (v.get(1).getTtl() > v.get(0).getTtl()) {
//			int ttl = v.get(1).getTtl();
//			codedpacket.setTtl(ttl);
//		} else {
			int ttl = v.get(0).getTtl();
			codedpacket.setTtl(ttl);
	//	}
		List<DTNHost> host_list = v.get(0).getHops();
		codedpacket.setHops(host_list);
		codedpacket.setGeneration(v.get(0).getGeneration());
		//System.out.println("code after:\n" + Arrays.toString(codedpacket.getPayload()));
		return codedpacket;
	}
	
	public static Map<Message,Integer> DeCodeFei(Vector<Message> m){
		int blockNumber = m.size();
		int payloadSize = m.get(0).getPayload().length;
		int[][] A = new int[blockNumber][generation+payloadSize];
		int[][] C = new int[blockNumber][generation+payloadSize];
		Map<Message,Integer> messageAndPosi = new HashMap<Message,Integer>();
		for (int l = 0; l < blockNumber; l++) {
			//System.out.println(m.get(l).getId()+"\t\tiscoded:"+m.get(l).is_coded());
			Arrays.fill(A[l], 0);
			Arrays.fill(C[l], 0);
			if(m.get(l).is_coded()==false){
				int temp=Integer.parseInt(m.get(l).getId().substring(1, m.get(l).getId().length()));
				temp=temp%generation;
				A[l][temp]=1;
				C[l][temp]=1;
			}
			else{
				String[] codeId = m.get(l).getCodedid();
				for(int j=0;j<codeId.length;j++){
					int temp=Integer.parseInt(codeId[j].substring(1,codeId[j].length()));
					temp=temp%generation;
					A[l][temp]=m.get(l).getCoding_Coef()[j];
					C[l][temp]=m.get(l).getCoding_Coef()[j];
				}
			}
			for(int j=generation;j<generation+payloadSize;j++){
				A[l][j] = m.get(l).getPayload()[j-generation];
				C[l][j] = m.get(l).getPayload()[j-generation];
			}
		}
		A = Matrix_inversion.GaussElimiate(A,blockNumber,generation+payloadSize);
		ArrayList<int[]> positionList = new ArrayList<int[]>();
		positionList = MatrixRowOnly1.onlyOne(A, blockNumber, generation); 
		for(int i=0;i<positionList.size();i++){
			String id="M"+(m.get(i).getGeneration()*generation+positionList.get(i)[1]);
			boolean decodedAble = true;
			for(int c=0;c<blockNumber;c++){
				if(m.get(c).getId().equals(id)){
					decodedAble = false;
					break;
				}
			}
			if(decodedAble){
				//System.out.println("id="+id);
				Message mRecover = new Message(m.get(i).getFrom(), m.get(i).getTo(),id, payloadSize);
				//System.out.println("i="+i+"\tj="+positionList.get(i)[1]);

				int y=positionList.get(i)[1];
				for(int b=0;b<blockNumber;b++){
					if(C[b][y]!=0){
						for(int j=0;j<m.get(b).getCodedid().length;j++){
							if(m.get(b).getCodedid()[j].equals(id)){
								mRecover.setCreationTime(m.get(b).getCreation_times()[j]);
								break;
							}
						}
						break;
					}
					
				}
				mRecover.setUnCoded();
				mRecover.setHops(m.get(i).getHops());
				int[] decodePayload = new int[payloadSize];
				System.arraycopy(A[i], generation, decodePayload, 0, payloadSize);
				mRecover.setPayload(decodePayload);
				messageAndPosi.put(mRecover, i);
			}
		}
		return messageAndPosi;
	}
}
