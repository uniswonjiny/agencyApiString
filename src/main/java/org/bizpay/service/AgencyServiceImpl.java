package org.bizpay.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.common.domain.DelngCancelParam;
import org.bizpay.common.domain.SellerParam;
import org.bizpay.common.util.KSPayMsgBean;
import org.bizpay.common.util.StringUtils;
import org.bizpay.common.util.TaxCalculator;
import org.bizpay.domain.AgencySales;
import org.bizpay.domain.AgencySales2;
import org.bizpay.domain.AgencySales3;
import org.bizpay.domain.SalesAdjustment;
import org.bizpay.domain.SellerSummary;
import org.bizpay.mapper.AgencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
@Log
@Service
public class AgencyServiceImpl implements AgencyService {
	@Autowired
	AgencyMapper mapper;
	
	@Autowired
	StringUtils stringUtil;
	
	@Autowired
	KSPayMsgBean ksPay;
	
	@Override
	public List<AgencySales> agencySalesList(AgencySalesParam param) throws Exception {
		log.info("대리점매출조회");
		List<AgencySales> list = mapper.summaryInfo(param);
		int distributorFee = 1;
		int agencyFee = 1;
		int dealerFee = 1;
		for (AgencySales dto : list) {

			if(dto.getT3dealerKind()==31) {}
			else if(dto.getT3dealerKind()==32) {
				dto.setSoleAgencyName(dto.getT3Cmpnm());
				dto.setSoleAgencyBizType(dto.getT3BizType());
			}
			
			if(dto.getT2dealerKind()==31) {}
			else if(dto.getT2dealerKind()==32) {
				dto.setSoleAgencyName(dto.getT2Cmpnm());
				dto.setSoleAgencyBizType(dto.getT2BizType());
			}else if(dto.getT2dealerKind()==33) {
				dto.setAgencyName(( dto.getT2Cmpnm()));
				dto.setAgencyBizType((dto.getT2BizType()) );
			}
			
			if(dto.getT1dealerKind()==31) {}
			else if(dto.getT1dealerKind()==32) {
				dto.setSoleAgencyName(dto.getT1Cmpnm());
				dto.setSoleAgencyBizType(dto.getT1BizType());
			}else if(dto.getT1dealerKind()==33) {
				dto.setAgencyName(( dto.getT1Cmpnm()));
				dto.setAgencyBizType((dto.getT1BizType()) );
			}else {
				dto.setDealerName(( dto.getT1Cmpnm()));
				dto.setDealerBizType((dto.getT1BizType()) );
			}
			
			if("N".equals(dto.getSoleAgencyBizType())) {
			
				// 세션에서 대리점 ㄹㄷㄷ
				//distributorFee = biz.get("distributorFee").equals("1") ? 1 : 0;
				//agencyFee = biz.get("agencyFee").equals("1") ? 1 : 0;
				//dealerFee = biz.get("dealerFee").equals("1") ? 1 : 0;
				//총판수익
				double soleAgencyIncome1 =  new BigDecimal( String.valueOf(dto.getTot() )).multiply( new BigDecimal(  String.valueOf(dto.getFeeDistributor() ) )) .divide(new BigDecimal(100)  ).doubleValue()  * distributorFee ;
				// 부가세공제후_총판수익
				//double soleAgencyIncome2 =  new BigDecimal( String.valueOf(soleAgencyIncome1 )).divide( new BigDecimal(1.1) ).doubleValue();
				double soleAgencyIncome2 =  soleAgencyIncome1 / 1.1;
				// 원천징수공제후_총판수익
				double soleAgencyIncome3 = soleAgencyIncome2 - new BigDecimal( String.valueOf(soleAgencyIncome2 )).multiply( new BigDecimal(0.033) ).doubleValue() ;
				dto.setSoleAgencyIncome(Math.round( soleAgencyIncome3));
			}else {
				double soleAgencyIncome1 =  new BigDecimal( String.valueOf(dto.getTot() )).multiply( new BigDecimal(  String.valueOf(dto.getFeeDistributor() ) )) .divide(new BigDecimal(100)  ).doubleValue() ;
				dto.setSoleAgencyIncome(Math.round( soleAgencyIncome1) );
			}
			
			if("N".equals( dto.getAgencyBizType()  ) ) {
				double agencyIncome1 =  new BigDecimal( String.valueOf(dto.getTot() )).multiply( new BigDecimal(  String.valueOf(dto.getFeeAgency() ) )) .divide(new BigDecimal(100)  ).doubleValue()  * agencyFee ;
				double agencyIncome2 =  agencyIncome1 / 1.1;
				double agencyIncome3 = agencyIncome2 - new BigDecimal( String.valueOf(agencyIncome2 )).multiply( new BigDecimal(0.033) ).doubleValue() ;
				dto.setAgencyIncome(Math.round( agencyIncome3) );
			}else {
				double agencyIncome1 =  new BigDecimal( String.valueOf(dto.getTot() )).multiply( new BigDecimal(  String.valueOf(dto.getFeeAgency() ) )) .divide(new BigDecimal(100)  ).doubleValue()  * agencyFee ;
				dto.setAgencyIncome(Math.round( agencyIncome1) );
			}
			
			if("N".equals( dto.getDealerBizType()  ) ) {
				double dealerIncome1 =  new BigDecimal( String.valueOf(dto.getTot() )).multiply( new BigDecimal(  String.valueOf(dto.getFeeDealer()) )) .divide(new BigDecimal(100)  ).doubleValue()  * dealerFee ;
				double dealerIncome2 =  dealerIncome1 / 1.1;
				double dealerIncome3 = dealerIncome2 - new BigDecimal( String.valueOf(dealerIncome2 )).multiply( new BigDecimal(0.033) ).doubleValue() ;
				dto.setDealerIncome(Math.round( dealerIncome3) );
			}else {
				double dealerIncome1 =  new BigDecimal( String.valueOf(dto.getTot() )).multiply( new BigDecimal(  String.valueOf(dto.getFeeDealer()) )) .divide(new BigDecimal(100)  ).doubleValue()  * dealerFee ;
				dto.setDealerIncome(Math.round( dealerIncome1) );
			}
			
			
			
			// 세금등  TaxCalculator 분석후 다시 한다.
			TaxCalculator tax=new TaxCalculator(true,false,false,dto.getFeeRate() ,dto.getTot());
			dto.setSellerIncome(tax.getSupplyAmount().subtract(tax.getTaxAmount()).doubleValue());
			dto.setSellerFee(tax.getTaxAmount().doubleValue());
		}
		
		return list;
	}

	@Override
	public List<AgencySales2> agencySalesList2(AgencySalesParam param) throws Exception {
		List<AgencySales2> list = mapper.summaryInfo2(param);
		
		for (AgencySales2 dto : list) {
			dto.setRecommendRate(0.33);
			int dealerKind = dto.getDealerKind();
			int t2DealerKind = dto.getT2DealerKind();
			double tempFee =  (new BigDecimal(dto.getTot()).doubleValue() * 0.0033)  ;
			tempFee = Math.round(tempFee);
			if( dealerKind == 34 ) {
				dto.setTitleDealer( dto.getCmpnm() );
				dto.setRecommendDealerFee(tempFee); //추천대리점딜러수익
				if(t2DealerKind == 33 ) {
					dto.setTitleCmpnm( dto.getT2Cmpnm() );
				}
		
			}else if(dealerKind == 33) {
				dto.setTitleCmpnm( dto.getCmpnm() );
				dto.setRecommendAgencyFee(tempFee); // 추천대리점수익
			}
		}
		return list;
	}

	@Override
	public List<AgencySales3> agencySalesList3(AgencySalesParam param) throws Exception {
		List<String> list1 = mapper.summaryInfo3in0(param);
		List<AgencySales3> list2 = mapper.summaryInfo3in1(param);
		List<AgencySales3> list3 = mapper.summaryInfo3in2(param);
		for (String bizCode : list1) {
			loop1 : for (AgencySales3 dto : list2) {
				if(dto.getTrgetBizCode().equals( bizCode) ) {
					dto.setTot(330000);// 원래 소스에 하드코딩되어있음 -- 디비에서 가져오도록은 해놓음
					continue loop1;
				}
			}
			loop2: for (AgencySales3 dto : list3) {
				if(dto.getRecommendBizCode().equals(bizCode )) {
					dto.setTot(1100000);// 원래 소스에 하드코딩되어있음 -- 디비에서 가져오도록은 해놓음
					continue loop2;
				}
			}
		}
		
		list2.addAll(list3);
		System.out.println(list2.toString());
		return list2;
	}

	@Override
	public List<SellerSummary> sellerSummaryList(SellerParam param) throws Exception {
		List<SellerSummary> list = mapper.sellerSummaryList(param);
		for (SellerSummary dto : list) {
			TaxCalculator tax=new TaxCalculator(true,false,false,dto.getFeeRate() ,dto.getTot());
			dto.setPay(tax.getSupplyAmount().subtract(tax.getTaxAmount() ).doubleValue());
			dto.setFee(tax.getTaxAmount().doubleValue());
		}
		return list;
	}

	@Override
	public List<SalesAdjustment> salesAdjustment(SellerParam param) throws Exception {
		List<SalesAdjustment> list = mapper.salesAdjustmentList(param);
		for (SalesAdjustment dto : list) {
			TaxCalculator tax=new TaxCalculator(true,false,false,dto.getFeeRate() ,dto.getTot());
			dto.setPay(tax.getSupplyAmount().subtract(tax.getTaxAmount() ).doubleValue());
			dto.setFee(tax.getTaxAmount().doubleValue());
		}
		return list;
	}

	@Override
	public HashMap<String, Object> delngCancel(DelngCancelParam param) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("flag", true);
		map.put("message", "");
		
		DelngCancelParam p2 = mapper.delngConfirm(param);
		
		if(p2==null) {
			map.put("flag", false);
			map.put("message", "수정할 데이터가 존재하지 않습니다.");
			return map;
		}
		
		if("CARD_CNCL".equals(p2.getDelngSeCode())) {
			map.put("flag", false);
			map.put("message", "이미 취소된 거래입니다.");
			return map;
		}
			
		if("CASH_RCIPT_CNCL".equals(p2.getDelngSeCode())) {
			map.put("flag", false);
			map.put("message", "이미 취소된 거래입니다.");
			return map;
		}
		// 카드결제인경우
		if("CARD_ISSUE".equals(p2.getDelngSeCode())) {
			if(param.getConfmDt()==null || param.getConfmDt().length() ==0 ) {
				map.put("flag", false);
				map.put("message", "결제확정일이 없습니다.");
				return map;
			}
			
			DelngCancelParam p3 = mapper.delngCardConfirm(param);
			param.setPgRciptNo(p3.getPgRciptNo() );
			param.setTId(p3.getTId());
			// 관리자인 경우 모든경우 취소가능  -- 세션관리 기능 개발이후 처리한다.
			
			
			
			// 관리자가 아닌경우 가능한 경우만 취소
			if( (p3.getPgRciptNo() != null && !"".equals(p3.getPgRciptNo() ) )  && ( p3.getTId()!=null && !"".equals( p3.getTId() ))) {
				String payType = mapper.getPayType(param.getMberCode()  );
				int gapDate = stringUtil.getGapDay( param.getConfmDt().substring(0,8));
				//익일인경우. 당일취소만.
				if("T".equals(payType)) {
					if(0 != gapDate ) {
						map.put("flag", false);
						map.put("message", "취소할수 없는 거래입니다.");
						return map;
					}
				}
				//5일입금인경우 4일까지 가능.
				if("N".equals(payType)) {
					if( gapDate <0 || gapDate > 5  ) {
						map.put("flag", false);
						map.put("message", "취소할수 없는 거래입니다.");
						return map;
					}
				}
					
			}
						
			
			
		
			
			
		}
		// 현금결제인경우
		if("CASH_RCIPT_ISSUE".equals(p2.getDelngSeCode())) {
			
		}
		
		
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		///////1. 추후 에러 처리 처리 핸들러 개발후 변경한다. 
		////// 2. 로그인 인증관리 부분 개발후 권한처리관련 핸들러나 인터셉터로 처리해야 한다. 수정 삭제 입력 여부부분
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		if(mapper.delngCancel(param) <1) {
			map.put("flag", false);
			map.put("message", "변경된 정보가 없습니다.");
		}
		return map;
	}

	@Override
	public Hashtable<String, Object> ksPayCancel(String pgRciptNo, String tId) throws Exception {
		/////////////////// 결제 관련 부분은 모두 다시 개발한다. !!!!!!!!!!! 이부분은 패스 결제성공 실패 이후 로 개발만 진행함
		String KSNET_PG_IP = "210.181.28.116";	//-필수- ipaddr X(15)   *KSNET_IP(개발:210.181.28.116, 운영:210.181.28.137)
		int KSNET_PG_PORT = 21001;	
		String pKeyInType = "K";
		final Hashtable xht = ksPay.sendCardCancelMsg(
				KSNET_PG_IP, 				// -필수- ipaddr  X(15)   *KSNET_IP(개발:210.181.28.116, 운영:210.181.28.137)  
				KSNET_PG_PORT,			// -필수- port   9( 5)   *KSNET_PORT(21001)  
				tId, 								// -필수- pStoreId  X(10)   *상점아이디(개발:2999199999, 운영:?)  
				pKeyInType, 							// -필수- pKeyInType   X(12)  KEY-IN유형(K:직접입력,S:리더기사용입력)  
				pgRciptNo							// -필수- pTransactionNo  X( 1)  *거래번호(승인응답시의 KEY:1로시작되는 12자리숫자)  
			);
		
		
		return null;
	}

}
