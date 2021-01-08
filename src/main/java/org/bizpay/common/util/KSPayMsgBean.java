package org.bizpay.common.util;

import java.util.Hashtable;

import org.springframework.stereotype.Component;

@Component
public class KSPayMsgBean {

	public static Hashtable sendCardCancelMsg(String ipaddr, int port ,String pStoreId, String pKeyInType ,String pTransactionNo)
	{
		//Header부 Data -----------------------------------------------------------------------------------------
		String	EncType					= "0" 																;//	0: 암화안함, 1:ssl, 2: seed
		String	Version					= "0603" 															;//	전문버전
		String	VersionType				= "00" 																;//	구분
		String	Resend					= "0" 																;//	전송구분 : 0 : 처음,  2: 재전송
		String	RequestDate				= new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());// 요청일자 :
		String	LineType				= "1" 																;//	lineType 0 : offline, 1:internet, 2:Mobile
		String	ApprovalCount			= "1"																;//	복합승인갯수
		String	GoodType				= "0" 																;//	제품구분 0 : 실물, 1 : 디지털
		String	HeadFiller				= ""																;//	예비
		
		String	StoreId					= pStoreId 															;//	*상점아이디
		String	OrderNumber				= "" 																;//	*주문번호
		String	UserName				= ""		 														;//	*주문자명
		String	IdNum					= ""	 															;//	주민번호 or 사업자번호
		String	Email					= ""	 															;//	*email
		String	GoodName				= "스마트폰사용자취소" 												;//	*제품명
		String	PhoneNo					= "" 																;//	*휴대폰번호
		String	KeyInType				= pKeyInType 														;//	KeyInType 여부 : S : Swap, K: KeyInType
		//Header end --------------------------------------------------------------------------------------------
		
		//Data Default-------------------------------------------------------------------------------------------
		String	ApprovalType			= "1010"															;//	승인구분
		String	CancelType	 			= "0"																;// CancelType		: 취소처리구분 0:거래번호 1:주문번호 
		String	TransactionNo			= pTransactionNo													;// TransactionNo	: 거래번호                           
		String	TradeDate	 			= ""																;// TradeDate		: 거래일자                           
		//String	OrderNumber	 			= ""																;// OrderNumber		: 주문번호                           
		String	Filler		 			= ""																;// Filler)			: 기타                               
		//Data end-----------------------------------------------------------------------------------------------

		String ReqHead = HeadMessage(EncType,Version,VersionType,Resend,RequestDate,StoreId,OrderNumber,UserName,IdNum,Email,GoodType,GoodName,KeyInType,LineType,PhoneNo,ApprovalCount,Filler);
		String ReqBody = CancelDataMessage(ApprovalType,CancelType,TransactionNo,TradeDate,OrderNumber,Filler);
		
		byte[] req_buf = s2b(ReqHead+ReqBody);
		
		byte[] rcv_buf = KSPayEncSocket.kspay_send_socket(ipaddr, port, req_buf, false);
		
		Hashtable rHash = ParseMessage(rcv_buf);
		
		return rHash;
	}
	
	public static Hashtable sendCardMsg(String ipaddr, int port
									,String pStoreId, String pOrderNumber, String pUserName, String pIdNum, String pEmail, String pGoodName, String pPhoneNo, String pKeyInType
									,String pInterestType, String pTrackII, String pInstallment, String pAmount, String pPasswd, String pLastIdNum)
	{

		//Header부 Data -----------------------------------------------------------------------------------------
		String	EncType					= "0" 																;//	0: 암화안함, 1:ssl, 2: seed
		String	Version					= "0603" 															;//	전문버전
		String	VersionType				= "00" 																;//	구분
		String	Resend					= "0" 																;//	전송구분 : 0 : 처음,  2: 재전송
		String	RequestDate				= new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());// 요청일자 :
		String	LineType				= "1" 																;//	lineType 0 : offline, 1:internet, 2:Mobile
		String	ApprovalCount			= "1"																;//	복합승인갯수
		String	GoodType				= "0" 																;//	제품구분 0 : 실물, 1 : 디지털
		String	HeadFiller				= ""																;//	예비
		
		String	StoreId					= pStoreId 															;//	*상점아이디
		String	OrderNumber				= pOrderNumber 														;//	*주문번호
		String	UserName				= pUserName 														;//	*주문자명
		String	IdNum					= pIdNum 															;//	주민번호 or 사업자번호
		String	Email					= pEmail 															;//	*email
		String	GoodName				= pGoodName 														;//	*제품명
		String	PhoneNo					= pPhoneNo 															;//	*휴대폰번호
		String	KeyInType				= pKeyInType 														;//	KeyInType 여부 : S : Swap, K: KeyInType
		//Header end --------------------------------------------------------------------------------------------
		
		//Data Default-------------------------------------------------------------------------------------------
		String	ApprovalType			= (pLastIdNum == null || pLastIdNum.length() == 0) ? "1000" : "1300";//	승인구분
		String	InterestType			= pInterestType 													;//	일반/무이자구분 1:일반 2:무이자
		String	TrackII					= pTrackII															;// KEY-IN시 카드번호=유효기간(YYMM)
		
		String	Installment				= pInstallment														;//	할부  00일시불
		String	Amount					= pAmount															;//	금액
		String	Passwd					= pPasswd															;//	비밀번호 앞2자리
		String	LastIdNum				= pLastIdNum														;//	주민번호  뒤7자리, 사업자번호10
		String	CurrencyType			= "0"																;//	통화구분 0:원화 1: 미화
		
		String	BatchUseType			= "0"																;//	거래번호배치사용구분  0:미사용 1:사용
		String	CardSendType			= "2"																;//	카드정보전송유무
		String	VisaAuthYn				= "7"																;//	비자인증유무 0:사용안함,7:SSL,9:비자인증
		String	Domain					= ""																;//	도메인 자체가맹점(PG업체용)
		String	IpAddr					= ""																;//	IP ADDRESS 자체가맹점(PG업체용)
		String	BusinessNumber			= ""																;//	사업자 번호 자체가맹점(PG업체용)
		String	Filler					= ""																;//	예비
		String	AuthType				= ""																;//	ISP : ISP거래, MP1, MP2 : MPI거래, SPACE : 일반거래
		String	MPIPositionType			= ""																;//	K : KSNET, R : Remote, C : 제3기관, SPACE : 일반거래
		String	MPIReUseType			= ""																;//	Y : 재사용, N : 재사용아님
		String	EncData					= ""																;//	MPI, ISP 데이터
		//Data end-----------------------------------------------------------------------------------------------

		String ReqHead = HeadMessage(EncType,Version,VersionType,Resend,RequestDate,StoreId,OrderNumber,UserName,IdNum,Email,GoodType,GoodName,KeyInType,LineType,PhoneNo,ApprovalCount,Filler);
		String ReqBody = CreditDataMessage(ApprovalType,InterestType,TrackII,Installment,Amount,Passwd,IdNum,CurrencyType,BatchUseType,CardSendType,VisaAuthYn,Domain,IpAddr,BusinessNumber,Filler,AuthType,MPIPositionType,MPIReUseType,EncData);
		
		byte[] req_buf = s2b(ReqHead+ReqBody);
		
		byte[] rcv_buf = KSPayEncSocket.kspay_send_socket(ipaddr, port, req_buf, false);
		
		Hashtable rHash = ParseMessage(rcv_buf);
		
//		Log.e("", "rHash : " + rHash.toString());
		
		return rHash;
	}
	
	public static String HeadMessage
    (
        String  EncType			,// EncType			: 0: 암화안함, 1:openssl, 2: seed                
        String  Version			,// Version			: 전문버전                                       
        String  VersionType		,// VersionType		: 구분                                           
        String  Resend		 	,// Resend			: 전송구분 : 0 : 처음,  1: 재전송            
        String  RequestDate		,// RequestDate		: 요청일자 : yyyymmddhhmmss                      
        String  StoreId			,// StoreId			: 상점아이디                                     
        String  OrderNumber		,// OrderNumber		: 주문번호                                       
        String  UserName	   	,// UserName		: 주문자명                                   
        String  IdNum		  	,// IdNum			: 주민번호 or 사업자번호                     
        String  Email		  	,// Email			: email                                      
        String  GoodType	   	,// GoodType		: 제품구분 0 : 실물, 1 : 디지털              
        String  GoodName	   	,// GoodName		: 제품명                                     
        String  KeyInType	  	,// KeyInType		: KeyInType 여부 : S : Swap, K: KeyInType    
        String  LineType	   	,// LineType		: lineType 0 : offline, 1:internet, 2:Mobile 
        String  PhoneNo			,// PhoneNo			: 모바일번호                                     
        String  ApprovalCount	,// ApprovalCount	: 복합승인갯수                               
        String  Filler			)// Filler			: 예비                                       
    {
        StringBuffer TmpHeadMsg = new StringBuffer();
        
        TmpHeadMsg.append(fmt(EncType		,    1, 'X'));
        TmpHeadMsg.append(fmt(Version		,    4, 'X'));
        TmpHeadMsg.append(fmt(VersionType	,    2, 'X'));
        TmpHeadMsg.append(fmt(Resend		,    1, 'X'));  
        TmpHeadMsg.append(fmt(RequestDate	,   14, 'X'));
        TmpHeadMsg.append(fmt(StoreId		,   10, 'X'));
        TmpHeadMsg.append(fmt(OrderNumber	,   50, 'X'));
        TmpHeadMsg.append(fmt(UserName		,   50, 'X'));
        TmpHeadMsg.append(fmt(IdNum			,   13, 'X'));
        TmpHeadMsg.append(fmt(Email			,   50, 'X'));
        TmpHeadMsg.append(fmt(GoodType		,    1, 'X'));
        TmpHeadMsg.append(fmt(GoodName		,   50, 'X'));
        TmpHeadMsg.append(fmt(KeyInType		,    1, 'X'));
        TmpHeadMsg.append(fmt(LineType		,    1, 'X'));
        TmpHeadMsg.append(fmt(PhoneNo		,   12, 'X'));
        TmpHeadMsg.append(fmt(ApprovalCount	,    1, 'X'));
        TmpHeadMsg.append(fmt(Filler		,	35, 'X'));  

        return TmpHeadMsg.toString();
    }

    // 신용카드승인요청 Body 1
    public static String CreditDataMessage(
        String ApprovalType   	,// ApprovalType	: 승인구분                                                                                                                     
        String InterestType   	,// InterestType	: 일반/무이자구분 1:일반 2:무이자                                                                                              
        String TrackII			,// TrackII	 		: 카드번호=유효기간  or 거래번호                                                                                               
        String Installment		,// Installment		: 할부  00일시불                                                                                                               
        String Amount		 	,// Amount			: 금액                                                                                                                     
        String Passwd		 	,// Passwd			: 비밀번호 앞2자리                                                                                                         
        String IdNum		  	,// IdNum			: 주민번호  뒤7자리, 사업자번호10                                                                                          
        String CurrencyType   	,// CurrencyType	: 통화구분 0:원화 1: 미화                                                                                                      
        String BatchUseType   	,// BatchUseType	: 거래번호배치사용구분  0:미사용 1:사용                                                                                        
        String CardSendType   	,// CardSendType	: 카드정보전송 0:미정송 1:카드번호,유효기간,할부,금액,가맹점번호 2:카드번호앞14자리 + "XXXX",유효기간,할부,금액,가맹점번호     
        String VisaAuthYn	 	,// VisaAuthYn		: 비자인증유무 0:사용안함,7:SSL,9:비자인증                                                                                 
        String Domain		 	,// Domain			: 도메인 자체가맹점(PG업체용)                                                                                              
        String IpAddr		 	,// IpAddr			: IP ADDRESS 자체가맹점(PG업체용)                                                                                          
        String BusinessNumber 	,// BusinessNumber	: 사업자 번호 자체가맹점(PG업체용)                                                                                             
        String Filler		 	,// Filler			: 예비                                                                                                                     
        String AuthType			,// AuthType		: ISP : ISP거래, MP1, MP2 : MPI거래, SPACE : 일반거래                                                                          
        String MPIPositionType	,// MPIPositionType : K : KSNET, R : Remote, C : 제3기관, SPACE : 일반거래                                                                         
        String MPIReUseType   	,// MPIReUseType	: Y :  재사용, N : 재사용아님                                                                                                  
        String EncData			)// EndData			: MPI, ISP 데이터                                                                                                              
    {
        StringBuffer TmpSendMsg = new StringBuffer();
        
        TmpSendMsg.append(fmt(ApprovalType   	,   4, 'X'));
        TmpSendMsg.append(fmt(InterestType   	,   1, 'X'));
        TmpSendMsg.append(fmt(TrackII			,  40, 'X'));
        TmpSendMsg.append(fmt(Installment		,   2, '9'));
        TmpSendMsg.append(fmt(Amount		 	,   9, '9'));
        TmpSendMsg.append(fmt(Passwd		 	,   2, 'X'));
        TmpSendMsg.append(fmt(IdNum				,  10, 'X'));
        TmpSendMsg.append(fmt(CurrencyType   	,   1, 'X'));
        TmpSendMsg.append(fmt(BatchUseType   	,   1, 'X'));
        TmpSendMsg.append(fmt(CardSendType   	,   1, 'X'));
        TmpSendMsg.append(fmt(VisaAuthYn	 	,   1, 'X'));
        TmpSendMsg.append(fmt(Domain		 	,  40, 'X'));
        TmpSendMsg.append(fmt(IpAddr		 	,  20, 'X'));
        TmpSendMsg.append(fmt(BusinessNumber 	,  10, 'X'));
        TmpSendMsg.append(fmt(Filler		 	, 135, 'X'));
        TmpSendMsg.append(fmt(AuthType			,   1, 'X'));
        TmpSendMsg.append(fmt(MPIPositionType	,   1, 'X'));
        TmpSendMsg.append(fmt(MPIReUseType   	,   1, 'X'));
        TmpSendMsg.append(EncData        					);

        return TmpSendMsg.toString();
    }

	//카드 취소 
    public static String CancelDataMessage(
        String ApprovalType		,// ApprovalType	: 승인구분                               
        String CancelType		,// CancelType		: 취소처리구분 1:거래번호 2:주문번호 
        String TransactionNo	,// TransactionNo	: 거래번호                           
        String TradeDate		,// TradeDate		: 거래일자                           
        String OrderNumber		,// OrderNumber		: 주문번호                               
        String Filler			)// Filler)			: 기타                               
    {
        StringBuffer TmpSendMsg = new StringBuffer();
        
        TmpSendMsg.append(fmt(ApprovalType		,  4, 'X'));
        TmpSendMsg.append(fmt(CancelType		,  1, 'X'));
        TmpSendMsg.append(fmt(TransactionNo		, 12, 'X'));
        TmpSendMsg.append(fmt(TradeDate			,  8, 'X'));
        TmpSendMsg.append(fmt(OrderNumber		, 50, 'X'));
        TmpSendMsg.append(fmt(Filler			, 75, 'X'));

        return TmpSendMsg.toString();
    }

    //승인이후에 결과값을 가지고 온다.    
    public static Hashtable ParseMessage(byte[] rbytes)
    {
    	if(null == rbytes) return null;
    	return ParseMessage(false, rbytes, 0, rbytes.length);
    }
    
    public static Hashtable ParseMessage(boolean useLen, byte[] rbytes, int sidx, int eidx)
    {
    	Hashtable mht = new Hashtable();
    	
    	int midx = 0, mlen = 0;
    	
		midx = sidx;
		if (useLen)
		{
			mlen =   4  ; mht.put("RecvLen"		    ,b2s(rbytes,midx,mlen));midx += mlen;
		}
		
		mlen =   1  ; mht.put("EncType"		    ,b2s(rbytes,midx,mlen));midx += mlen;// EncType		: 0: 암화안함, 1:openssl, 2: seed            
		mlen =   4  ; mht.put("Version"		    ,b2s(rbytes,midx,mlen));midx += mlen;// Version		: 전문버전                                   
		mlen =   2  ; mht.put("VersionType"	    ,b2s(rbytes,midx,mlen));midx += mlen;// VersionType	: 구분                                       
		mlen =   1  ; mht.put("Resend"			,b2s(rbytes,midx,mlen));midx += mlen;// Resend			: 전송구분 : 0 : 처음,  2: 재전송            
		mlen =  14  ; mht.put("RequestDate"	    ,b2s(rbytes,midx,mlen));midx += mlen;// RequestDate	: 요청일자 : yyyymmddhhmmss                  
		mlen =  10  ; mht.put("StoreId"		    ,b2s(rbytes,midx,mlen));midx += mlen;// StoreId		: 상점아이디                                 
		mlen =  50  ; mht.put("OrderNumber"	    ,b2s(rbytes,midx,mlen));midx += mlen;// OrderNumber	: 주문번호                                   
		mlen =  50  ; mht.put("UserName"		,b2s(rbytes,midx,mlen));midx += mlen;// UserName		: 주문자명                                       
		mlen =  13  ; mht.put("IdNum"			,b2s(rbytes,midx,mlen));midx += mlen;// IdNum			: 주민번호 or 사업자번호                         
		mlen =  50  ; mht.put("Email"			,b2s(rbytes,midx,mlen));midx += mlen;// Email			: email                                          
		mlen =   1  ; mht.put("GoodType"		,b2s(rbytes,midx,mlen));midx += mlen;// GoodType		: 제품구분 0 : 실물, 1 : 디지털                  
		mlen =  50  ; mht.put("GoodName"		,b2s(rbytes,midx,mlen));midx += mlen;// GoodName		: 제품명                                         
		mlen =   1  ; mht.put("KeyInType"		,b2s(rbytes,midx,mlen));midx += mlen;// KeyInType		: KeyInType 여부 : S : Swap, K: KeyInType        
		mlen =   1  ; mht.put("LineType"		,b2s(rbytes,midx,mlen));midx += mlen;// LineType		: lineType 0 : offline, 1:internet, 2:Mobile     
		mlen =  12  ; mht.put("PhoneNo"		    ,b2s(rbytes,midx,mlen));midx += mlen;// PhoneNo		: 모바일번호                                 
		mlen =   1  ; mht.put("ApprovalCount"	,b2s(rbytes,midx,mlen));midx += mlen;// ApprovalCount	: 복합승인갯수                                   
		mlen =  35  ; mht.put("HaedFiller"		,b2s(rbytes,midx,mlen));midx += mlen;// Filler			: 예비                                       

		int iCnt = Integer.parseInt((String)mht.get("ApprovalCount"));
		
		for(int i=0; i < iCnt; i++) 
		{
			mlen =   4  ; mht.put("ApprovalType"	,b2s(rbytes,midx,mlen));midx += mlen;// 승인구분
			
			String	ApprovalType = (String)mht.get("ApprovalType");
			
			if(ApprovalType.startsWith("15")) 
			{
				mlen =  12;  ; mht.put("TransactionNo"			,b2s(rbytes,midx,mlen));midx += mlen;// 거래번호                
				mlen =   1;  ; mht.put("Status"					,b2s(rbytes,midx,mlen));midx += mlen;// 상태 O : 승인, X : 거절 
				mlen =   8;  ; mht.put("TradeDate"				,b2s(rbytes,midx,mlen));midx += mlen;// 거래일자                
				mlen =   6;  ; mht.put("TradeTime"				,b2s(rbytes,midx,mlen));midx += mlen;// 거래시간                
				mlen =   6;  ; mht.put("IssCode"		    	,b2s(rbytes,midx,mlen));midx += mlen;// 발급사코드              
				mlen =  16;  ; mht.put("Message1"				,b2s(rbytes,midx,mlen));midx += mlen;// 메시지1                 
				mlen =  16;  ; mht.put("Message2"				,b2s(rbytes,midx,mlen));midx += mlen;// 메시지2                 					
			}else
			if(ApprovalType.startsWith("1") || ApprovalType.startsWith("I")) 
			{
				mlen =  12  ; mht.put("TransactionNo"			,b2s(rbytes,midx,mlen));midx += mlen;// 거래번호                                             
				mlen =   1  ; mht.put("Status"					,b2s(rbytes,midx,mlen));midx += mlen;// 상태 O : 승인, X : 거절                              
				mlen =   8  ; mht.put("TradeDate"				,b2s(rbytes,midx,mlen));midx += mlen;// 거래일자                                             
				mlen =   6  ; mht.put("TradeTime"				,b2s(rbytes,midx,mlen));midx += mlen;// 거래시간                                             
				mlen =   6  ; mht.put("IssCode"			    	,b2s(rbytes,midx,mlen));midx += mlen;// 발급사코드                                           
				mlen =   6  ; mht.put("AquCode"			    	,b2s(rbytes,midx,mlen));midx += mlen;// 매입사코드                                           
				mlen =  12  ; mht.put("AuthNo"					,b2s(rbytes,midx,mlen));midx += mlen;// 승인번호 or 거절시 오류코드                          
				mlen =  16  ; mht.put("Message1"				,b2s(rbytes,midx,mlen));midx += mlen;// 메시지1                                              
				mlen =  16  ; mht.put("Message2"				,b2s(rbytes,midx,mlen));midx += mlen;// 메시지2                                              
				mlen =  16  ; mht.put("CardNo"					,b2s(rbytes,midx,mlen));midx += mlen;// 카드번호                                             
				mlen =   4  ; mht.put("ExpDate"			    	,b2s(rbytes,midx,mlen));midx += mlen;// 유효기간                                             
				mlen =   2  ; mht.put("Installment"		    	,b2s(rbytes,midx,mlen));midx += mlen;// 할부                                                 
				mlen =   9  ; mht.put("Amount"					,b2s(rbytes,midx,mlen));midx += mlen;// 금액                                                 
				mlen =  15  ; mht.put("MerchantNo"				,b2s(rbytes,midx,mlen));midx += mlen;// 가맹점번호                                           
				mlen =   1  ; mht.put("AuthSendType"			,b2s(rbytes,midx,mlen));midx += mlen;// 전송구분                
				mlen =   1  ; mht.put("ApprovalSendType"		,b2s(rbytes,midx,mlen));midx += mlen;// 전송구분(0 : 거절, 1 : 승인, 2: 원카드)              
				mlen =  12  ; mht.put("Point1"					,b2s(rbytes,midx,mlen));midx += mlen;// Point1                                               
				mlen =  12  ; mht.put("Point2"					,b2s(rbytes,midx,mlen));midx += mlen;// Point2                                               
				mlen =  12  ; mht.put("Point3"					,b2s(rbytes,midx,mlen));midx += mlen;// Point3                                               
				mlen =  12  ; mht.put("Point4"					,b2s(rbytes,midx,mlen));midx += mlen;// Point4                                               
				mlen =  12  ; mht.put("VanTransactionNo"		,b2s(rbytes,midx,mlen));midx += mlen;// Point4                                               
				mlen =  82  ; mht.put("Filler"					,b2s(rbytes,midx,mlen));midx += mlen;// 예비                                                 
				mlen =   1  ; mht.put("AuthType"				,b2s(rbytes,midx,mlen));midx += mlen;// I : ISP거래, M : MPI거래, SPACE : 일반거래           
				mlen =   1  ; mht.put("MPIPositionType"	    	,b2s(rbytes,midx,mlen));midx += mlen;// K : KSNET, R : Remote, C : 제3기관, SPACE : 일반거래 
				mlen =   1  ; mht.put("MPIReUseType"			,b2s(rbytes,midx,mlen));midx += mlen;// Y : 재사용, N : 재사용아님                           
				
				String	AuthType = (String)mht.get("AuthType");
				
				if (AuthType != null && AuthType.trim().length() != 0)
				{
					mlen =   5  ; mht.put("EncLen"	    		,b2s(rbytes,midx,mlen));midx += mlen;
					
					int EncLen = Integer.parseInt((String)mht.get("EncLen"));
					mlen =EncLen; mht.put("EncData"				,b2s(rbytes,midx,mlen));midx += mlen;
				}
			}else
			{
				;
			}
		}
		
		return mht;
    }

	public static byte[] s2b(String str)
	{
		byte[] buf = null;
		try
		{
			buf = str.getBytes("ksc5601");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return buf;
	}

	public static String b2s(byte[] buf)
	{
		return (null == buf) ? null : b2s(buf,0,buf.length);
	}

	public static String b2s(byte[] buf, int bidx, int blen)
	{
		String str = null;
		try
		{
			str = new String(buf,bidx,blen,"ksc5601");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return str;
	}
	    
	public static String fmt(String str, int len, char ctype)
	{
		return format(str,len,ctype);
	}

	public static String fmt(int no, int len, char ctype)
	{
		return format(String.valueOf(no),len,ctype);
	}
	
	public static String format(String str, int len, char ctype)
	{
        byte[] buff;
        int filllen = 0;

        String			trim_str = null;
        StringBuffer	sb = new StringBuffer();
        
        buff = (str == null) ? new byte[0] : s2b(str);
        
        filllen = len - buff.length;
        if (filllen < 0)
        {
			for(int i=0, j=0; j<len-4; i++)//적당히 여유를 두고 잘라버리자
			{
				j += (str.charAt(i) > 127) ? 2 : 1;
				sb.append(str.charAt(i));
			}

			trim_str = sb.toString();
			buff = s2b(trim_str);
			filllen = len - buff.length;
			
			if (filllen <= 0) return new String(buff, 0, len);//여기는 절대로 안타겠지...
			sb.setLength(0);
        }else
        {
        	trim_str = str;
        }
        
        if(ctype == '9')	// 숫자열인 경우
        {
            for(int i = 0; i<filllen;i++) sb.append('0');            
            sb.append(trim_str);
        }else				// 문자열인 경우
        {
            for(int i = 0; i<filllen;i++) sb.append(' ');
            sb.insert(0, trim_str);
        }
        return sb.toString();
    }
}