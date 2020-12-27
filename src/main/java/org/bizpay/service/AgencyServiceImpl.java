package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.AgencySalesParam;
import org.bizpay.domain.AgencySales;
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
				dto.setSoleAgencyBizType(dto.getT3BizCode());
			}
			if(dto.getT2dealerKind()==32) {}
			else if(dto.getT2dealerKind()==32) {
				dto.setSoleAgencyName(dto.getT2Cmpnm());
				dto.setSoleAgencyBizType(dto.getT2BizCode());
			}else if(dto.getT2dealerKind()==33) {
				dto.setAgencyName(( dto.getT2Cmpnm()));
				dto.setAgencyBizType((dto.getT2BizCode()) );
			}
			
			if(dto.getT1dealerKind()==32) {}
			else if(dto.getT1dealerKind()==32) {
				dto.setSoleAgencyName(dto.getT1Cmpnm());
				dto.setSoleAgencyBizType(dto.getT1BizCode());
			}else if(dto.getT1dealerKind()==33) {
				dto.setAgencyName(( dto.getT1Cmpnm()));
				dto.setAgencyBizType((dto.getT1BizCode()) );
			}else {
				dto.setDealerName(( dto.getT1Cmpnm()));
				dto.setDealerBizType((dto.getT1BizCode()) );
			}
			
			if("N".equals(dto.getSoleAgencyBizType())) {
			
				// 세션에서 대리점 ㄹㄷㄷ
				//distributorFee = biz.get("distributorFee").equals("1") ? 1 : 0;
				//agencyFee = biz.get("agencyFee").equals("1") ? 1 : 0;
				//dealerFee = biz.get("dealerFee").equals("1") ? 1 : 0;
				double soleAgencyIncome1 = (dto.getTot() * dto.getFeeRate() / dto.getFeeAgency() * agencyFee );
				double soleAgencyIncome2 = (soleAgencyIncome1 / 1.1);
				double soleAgencyIncome3 = (soleAgencyIncome2 - (soleAgencyIncome2 * 0.033));
				dto.setDealerIncome(soleAgencyIncome3);
			}else {
				double soleAgencyIncome1 = (dto.getTot() * dto.getFeeRate() / dto.getFeeAgency() * agencyFee );
				dto.setDealerIncome(soleAgencyIncome1);
			}
			// 세금등  TaxCalculator 분석후 다시 한다.
			//double tax = 
		}
		
		return list;
	}

}
