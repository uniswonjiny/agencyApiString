package org.bizpay.service;

import java.math.BigDecimal;
import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.common.util.TaxCalculator;
import org.bizpay.domain.AgencySales;
import org.bizpay.domain.AgencySales2;
import org.bizpay.mapper.AgencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
@Log
@Service
public class AgencyServiceImpl implements AgencyService {
	@Autowired
	AgencyMapper mapper;
	
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

}
