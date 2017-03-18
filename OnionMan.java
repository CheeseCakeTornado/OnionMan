package com.cheesecaketornado;

public class OnionMan {
	
	//CONFIGURATION
	public final boolean dev = false;
	
	private boolean found = false;
	
	private String addr = "";
	private String key;
	
	public String getPrivateKey(){
		return key;
	}
	
	Thread[] threads;
	
	public boolean getFound(){
		return found;
	}
	
	public void setFound(boolean b, String address, String privateKey){
		found = b;
		this.addr = address;
		this.key = privateKey;
		killAllThreads();
	}
	
	public void killAllThreads(){
		found = true;
		for(int i = 0; i < threads.length; i++){
			devprint("Killing Thread: " + i);
			threads[i].interrupt();
		}
	}
	
	public String generateAddress(String target){
		//GET NUMBER OF CPU CORES
		int coreCount = Runtime.getRuntime().availableProcessors();
		//CREATE AN LIST TO STORE ALL THE THREADS
		threads = new Thread[coreCount];
		//CREATE A THREAD FOR EACH CORE
		for(int i = 0; i < coreCount; i++){
			//CREATE A NEW THREAD FOR EACH CORE AND PASS THROUGH THE TARGET WORD
			WorkerThread wt = new WorkerThread(target, i, this);
			//ADD THE NEW THREAD TO THE ARRAY
			threads[i] = wt;
			//START THE THREAD
			wt.start();
		}
		
		for(int b = 0; b < threads.length; b++){
			try {
				threads[b].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return addr;
		
	}
	
	public static void main(String args[]){
		GUI gui = new GUI();
	}
	
	public void devprint(String s){
		if(dev){
			System.out.println(s);
		}
	}

}
