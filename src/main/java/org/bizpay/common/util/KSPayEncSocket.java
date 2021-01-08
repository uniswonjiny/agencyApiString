package org.bizpay.common.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class KSPayEncSocket {

	public static byte[] kspay_send_socket(String ipaddr, int port, byte[] p_dbuf, boolean useResend)
	{
		byte[] p_rbuf = kspay_send_socket(ipaddr, port, p_dbuf);
		if (null != p_rbuf) return p_rbuf;
		
		byte[] p_nbuf = (byte[])p_dbuf.clone();
		p_nbuf[1+4+2] = (byte)'1';
		
		return kspay_send_socket(ipaddr,  port, p_nbuf);
	}

	public static byte[] kspay_send_socket(String ipaddr, int port, byte[] p_dbuf)
	{
		try
		{
			byte[] p_kbuf	= KSPayMsg.s2b(new StringBuffer().append(System.currentTimeMillis()).append(Long.MAX_VALUE).substring(0,16));
			byte[] e_kbuf	= ks_rsa_encrypt(p_kbuf);

			byte[] e_dbuf	= ks_seed_encrypt(p_kbuf, p_dbuf);
			byte[] e_sbuf	= new byte[4+1+e_kbuf.length+4+e_dbuf.length];

			int midx = 0;
			System.arraycopy("01292".getBytes()																			,0	,e_sbuf	,midx,  5			); midx+= 5				;
			System.arraycopy(e_kbuf																						,0	,e_sbuf	,midx, e_kbuf.length); midx+=e_kbuf.length	;

			System.arraycopy(("0000".substring(String.valueOf(e_dbuf.length).length(),4) + e_dbuf.length).getBytes()	,0	,e_sbuf	,midx,  4			); midx+= 4				;
			System.arraycopy(e_dbuf																						,0	,e_sbuf	,midx, e_dbuf.length); midx+=e_dbuf.length	;

			byte[] e_rbuf = send_socket(ipaddr,port,e_sbuf);

			return ks_seed_decrypt(p_kbuf,e_rbuf);
		}catch(IOException ie)
		{
			System.out.println("ERROR: kspay_send_socket 통신오류발생 ("+ipaddr+","+port+","+p_dbuf==null?"null":KSPayMsg.b2s(p_dbuf) +")");
			ie.printStackTrace();
		}catch(Exception e)
		{
			System.out.println("ERROR: kspay_send_socket 기타오류발생 ("+ipaddr+","+port+","+p_dbuf==null?"null":KSPayMsg.b2s(p_dbuf) +")");
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] send_socket(String ipaddr, int port, byte[] sbuf) throws IOException
	{
		Socket				sock	= null;
		DataOutputStream	dout	= null;
		DataInputStream		din		= null;

		try
		{
			sock	= new Socket(ipaddr, port);
			dout	= new DataOutputStream(sock.getOutputStream());
			din		= new DataInputStream(sock.getInputStream());

			dout.write(sbuf	,0	, sbuf.length);
			dout.flush();

			byte[] rbuf = new byte[8192];
			int rtn_len=0, read_len=0;
			while(0<=(rtn_len=din.read(rbuf,read_len,rbuf.length-read_len)) && read_len<rbuf.length)
			{
				read_len+=rtn_len;
			}

			System.out.println("INFO: kspay_send_socket ("+read_len+")byte 수신 length_len=("+KSPayMsg.b2s(rbuf,0,4) +")");
			
			byte[] tbuf = new byte[read_len-4];
			System.arraycopy(rbuf,4,tbuf,0,read_len-4);
			
			return tbuf;
		}finally
		{
			try{if (sock != null) sock.close(); sock = null;}catch(Exception e){}
		}
	}

	public static byte[] ks_seed_decrypt(byte[] kbuf, byte[] mbuf) throws java.security.NoSuchProviderException,javax.crypto.NoSuchPaddingException,java.security.InvalidAlgorithmParameterException,javax.crypto.BadPaddingException,java.security.NoSuchAlgorithmException,java.security.InvalidKeyException,javax.crypto.IllegalBlockSizeException
	{
		byte tdata[]	= new  KSPaySeed(kbuf).cbc_decrypt(mbuf) ;
		return tdata;
	}

	public static byte[] ks_seed_encrypt(byte[] kbuf, byte[] mbuf) throws java.security.NoSuchProviderException,javax.crypto.BadPaddingException,javax.crypto.NoSuchPaddingException,java.security.InvalidAlgorithmParameterException,java.security.NoSuchAlgorithmException,java.security.InvalidKeyException,javax.crypto.IllegalBlockSizeException
	{
		byte tdata[]	= new  KSPaySeed(kbuf).cbc_encrypt(mbuf) ;
		return tdata;
	}

	public static byte[] ks_rsa_encrypt(byte[] sbuf) throws java.security.NoSuchProviderException,javax.crypto.NoSuchPaddingException,javax.crypto.BadPaddingException,java.security.NoSuchAlgorithmException,java.security.spec.InvalidKeySpecException,java.security.InvalidKeyException,javax.crypto.IllegalBlockSizeException
	{
		BigInteger modulus			= new BigInteger("d4846c2b8228dddfab9e614da2a324c1cc7b29d848cc005624d3a09667a2aab9073290bace6aa536ddceb3c47ddda78d9954da06c83aa65b939c5ec773a3787e71bec5a1c077bb446c06b393d2537967645d386b4b0b4ec21372fdc728c56693028c1c3915c1c4279793eb3dccefd6bf49b86cc7d88a47b0d44aba9e73750fcd",16);
		BigInteger publicExponent	= new BigInteger("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010001",16);

		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, publicExponent);

		// JDK 1.4 이하에서는 BouncyCastleProvider를 사용(아래주석)
		//KeyFactory keyfactory = KeyFactory.getInstance("RSA", CIPHER_PROVIDER);
		KeyFactory keyfactory = KeyFactory.getInstance("RSA");
		java.security.PublicKey publickey = keyfactory.generatePublic(pubKeySpec);

		// JDK 1.4 이하에서는 BouncyCastleProvider를 사용(아래주석)
		//Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",CIPHER_PROVIDER);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		cipher.init(Cipher.ENCRYPT_MODE, publickey);//, fixedsecurerandom);

		byte[] rbuf = cipher.doFinal(sbuf);

		return rbuf;
	}
}
