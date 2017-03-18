package com.cheesecaketornado;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base32;

public class WorkerThread extends Thread {
	
	private String target;
	private int threadNumber;
	private OnionMan om;
	
	public WorkerThread(String target, int threadNumber, OnionMan om){
		//ALL OF THE ADDRESSES RETURNED BY createAddress() are in all CAPS so uppercase the target word when it comes in for comparing
		this.target = target.toUpperCase();
		//GET THE THREAD NUMBER JUST FOR DEBUGGING PURPOSES, CAN BE TAKEN OUT IN LATER VERSIONS
		this.threadNumber = threadNumber;
		//GET ONIONMAN SO WE CAN SET THE FOUND VARIABLE
		this.om = om;
	}

	@Override
	public void run() {
		//WHILE THE TARGET WORD HASNT BEEN FOUNT
		while(!om.getFound()){
			//CREATE A KEYPAIR TO FEED INTO createADDRESS
			try{
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			//INIT THE KEYPAIRGENERATOR FOR 1024 BIT
			keyGen.initialize(1024);
			//GENERATE THE KEY PAIR
			KeyPair key = keyGen.generateKeyPair();
			//CREATE AN ADDRESS
			String addr = createAddress(key);
			om.devprint("CORE#"+ threadNumber + "TRYING: " + addr);
			//COMPARE IF THE ADDRESS STARTS WITH THE TARGET WORD
			if(addr.startsWith(target)){
				//SET THE BOOLEAN FOUND IN ONIONMAN TO TRUE
				om.setFound(true, addr, new Base32().encodeAsString(key.getPrivate().getEncoded()));
				om.devprint("TARGET ADDRESS FOUND, ADDRESS IS " + addr + ".onion");
			}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public String createAddress(KeyPair key){
		//GET THE PUBLIC 1024 BIT RSA KEY
		byte[] pubKey = key.getPublic().getEncoded();
		//GET THE SHA-1 HASH OF THE PUBLIC KEY
		byte[] pubKeySHA1 = SHA1(pubKey);
		//CREATE NEW INSTANCE OF BASE32
		Base32 base32 = new Base32();
		//RETURN THE BASE 32 ENCODING OF THE FIRST 80 BITS OF THE SHA-1 DIGEST OF THE PUBLIC KEY
		String base32SHA1 = base32.encodeAsString(pubKeySHA1);
		//GET ONLY THE FIRST 80 BYTES - I KNOW THIS IS A CRAPPY WAY AND I SHOULD DO IT IN BYTES :(
		String address = base32SHA1.substring(0, 16);
		//RETURN THE NEW ADDRESS
		return address;
	}
	
	//public byte[] getPublicRSAKey(){
	//	try{
	//		final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	//		keyGen.initialize(1024);
	//		final KeyPair key = keyGen.generateKeyPair();
	//		return key.getPublic().getEncoded();
	//		
	//	} catch(Exception e){
	//		e.printStackTrace();
	//	}
	//	return null;
	//}
	
	public byte[] SHA1(byte[] input){
		byte[] sha1 = null;
		try{
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(input);
			sha1 = crypt.digest();
		} catch (Exception e){
			e.printStackTrace();
		}
		return sha1;
	}
	
	public static String byteArrayToHexString(byte[] b){
		String result = "";
		for(int i=0; i < b.length; i++){
			result += Integer.toString( (b[i] & 0xff) + 0x100, 16 ).substring(1);
		}
		return result;
	}
	
}
