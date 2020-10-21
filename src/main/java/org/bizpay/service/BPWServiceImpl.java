package org.bizpay.service;

import java.util.ArrayList;
import java.util.List;

import org.bizpay.common.domain.InqireDealerParam;
import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.common.domain.InqireMberParam;
import org.bizpay.common.util.CertUtil;
import org.bizpay.common.util.TimeUtil;
import org.bizpay.domain.AtmConfig;
import org.bizpay.domain.DealerManager;
import org.bizpay.domain.DealerRegInfo;
import org.bizpay.domain.InqireAtm;
import org.bizpay.domain.InqireAtmSum;
import org.bizpay.domain.InqireDelng;
import org.bizpay.domain.InqireDelngSum;
import org.bizpay.exception.SqlErrorException;
import org.bizpay.mapper.InqireAtmMapper;
import org.bizpay.mapper.InqireDealerMapper;
import org.bizpay.mapper.InqireDelngMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;
@Log
@Service
public class BPWServiceImpl implements BPWService {
	
	@Autowired
	CertUtil cert;
	
	@Autowired
	TimeUtil timeUtil;
	
	@Autowired
	InqireAtmMapper atmMapper; //입출금
	
	@Autowired
	InqireDelngMapper delngMapper; // 거래내역
	
	@Autowired
	InqireDealerMapper dealerMapper; // 대리점관리

	@Override
	public List<InqireAtm> InqireAtm(InqireMberParam param) throws Exception {
		log.info("입출금내역관리 목록조회");
		List<InqireAtm> list = new ArrayList<InqireAtm>();
		if(param.getEndIndex() ==0 && param.getStartIndex()==0) {
			list = atmMapper.list(param);
		}else {
			list = atmMapper.pageList(param);
		}
		
		for (InqireAtm item : list) {
			if(item.getAccount() != null && item.getAccount().length() != 0 ) {
				item.setAccount(cert.decrypt( item.getAccount()));				
			}
		}
		return list;
	}

	@Override
	public InqireAtmSum InqireAtmTot(InqireMberParam param) throws Exception {
		log.info("입출금내역관리 전체정보조회");
		return atmMapper.totInfo(param);
	}

	@Override
	public List<InqireDelng> inqireDelng(InqireDelingParam param) throws Exception {
		List<InqireDelng> list = new ArrayList<InqireDelng>();		
		if(param.getEndIndex() ==0 && param.getStartIndex()==0) {
			if( "all".equals(param.getSearchType())) {
				list = delngMapper.list(param);				
			}else {
				list = delngMapper.cancelList(param);		
			}
		}else {
			if( "all".equals(param.getSearchType())) {
				list = delngMapper.pageList(param);				
			}else {
				list = delngMapper.cancelPage(param);		
			}
		}
		// 복화화 처리
		for (InqireDelng item : list) {
			String temp = cert.decrypt(item.getCardNo());
			item.setCardNo(temp);
		
			// 카드정보 변경
			if (item.getDelngSeCode() != null && item.getDelngSeCode().length() != 0) {
				if(item.getDelngSeCode().equals("CARD_ISSUE") && item.getDelngSeCode()!=null ) {
					if (item.getCardType() != null && item.getCardType().length() != 0) {
						if(item.getCardType().equals("C") && item.getCardType()!=null) {
							item.setDelngSeCodeNm("Check카드");
						}
					}
					else item.setDelngSeCodeNm("카드");
				}
				if(item.getDelngSeCode().equals("CASH_RCIPT_ISSUE") && item.getDelngSeCode()!=null ) {
					item.setDelngSeCodeNm("현금영수증");
				}
			}
			
			// 항부정보 변경
			if (item.getInstlmtMonth() != null && item.getInstlmtMonth().length() != 0) {
				if("00".equals(item.getInstlmtMonth())) item.setInstlmtMonth("일시불");
				else {
					int tempint = Integer.parseInt(item.getInstlmtMonth());
					item.setInstlmtMonth(tempint+"개월");
				}
				
			}
			
			
			// 판매자주소 adres 복호화
			if(item.getAdres() != null && item.getAdres().length() != 0 ) {
				item.setAdres(cert.decrypt( item.getAdres()));				
			}
			// 대표전화 복호화
			if(item.getMberPhone() != null && item.getMberPhone().length() != 0 ) {
				item.setMberPhone(cert.decrypt( item.getMberPhone()));				
			}
			// 휴대폰번호 복호화
			if(item.getMberMobile() != null && item.getMberMobile().length() != 0 ) {
				item.setMberMobile(cert.decrypt(item.getMberMobile()));				
			}
			// 구매자 연락처 복호화
			if(item.getPurchsrMbtlnum() != null && item.getPurchsrMbtlnum().length() != 0 ) {
				item.setPurchsrMbtlnum(cert.decrypt(item.getPurchsrMbtlnum()));				
			}
		}
		return list;
	}

	@Override
	public InqireDelngSum inqireDelngSum(InqireDelingParam param) throws Exception {
		log.info("입출금내역관리 합계정보");
		return delngMapper.totInfo(param);
	}

	@Override
	public int inqireDelngCount(InqireDelingParam param) throws Exception {
		log.info("입출금내역관리 전체목록갯수");
		if("all".equals(param.getSearchType())) {
			return delngMapper.totCount(param);
		}else return delngMapper.cancelTotCount(param);
	}

	@Override
	public AtmConfig atmConfigInfo(String gbCode) throws Exception {
		log.info("출금정지 상태 정보");
		return atmMapper.AtmConfigInfo(gbCode);
	}

	@Override
	public boolean atmConfigUpdate(AtmConfig ac) throws Exception {
		log.info("출금정지 상태 변경");
		if(atmMapper.AtmConfigUpdate(ac) > 0 )return true;
		else return false;
	}

	@Override
	public List<DealerManager> inqireDealer(InqireDealerParam param) throws Exception {
		log.info("대리점관리 목록조회");
		// 사업자 번호가 디비에 암호화 비암호화 섞여 있으므로 둘다 비교한다.
		if(param.getBizrno() != null && param.getBizrno().length() != 0 ) {
			param.setBizrnoEnc(cert.encrypt(param.getBizrno()));			
		}
		List<DealerManager> list = new ArrayList<DealerManager>();
		if(param.getEndIndex() ==0 && param.getStartIndex()==0) {
			list = dealerMapper.list(param);
		}else {
			list = dealerMapper.pageList(param);
		}
		for (DealerManager item : list) {
			if(item.getBizTelno() != null && item.getBizTelno().length() != 0 ) {
				item.setBizTelno(cert.decrypt( item.getBizTelno()));	
			}
			
			if(item.getMTelno() != null && item.getMTelno().length() != 0 ) {
				item.setMTelno(cert.decrypt( item.getMTelno()));	
			}
			
			if(item.getBankSerial() != null && item.getBankSerial().length() != 0 ) {
				item.setBankSerial(cert.decrypt( item.getBankSerial()));	
			}
			
			if(item.getBizrno() != null && item.getBizrno().length() != 0 ) {
				item.setBizrno(cert.decrypt( item.getBizrno()));	
			}
		}
		
		return list;
	}

	@Override
	public int inqireDealerCount(InqireDealerParam param) throws Exception {
		log.info("대리점관리전체갯수 조회");
		return dealerMapper.totCount(param);
	}

	@Override
	public DealerRegInfo dealerbyId(String userId) throws Exception {
		log.info("대리점정보상세확인");
		DealerRegInfo info = dealerMapper.dealerbyId(userId);
		if(info.getBizrno() != null && info.getBizrno().length() != 0 ) {
			info.setBizrno(cert.decrypt( info.getBizrno()));	
		}
		
		if(info.getBizTelno() != null && info.getBizTelno().length() != 0 ) {
			info.setBizTelno(cert.decrypt( info.getBizTelno()));	
		}
		
		if(info.getEmail() != null && info.getEmail().length() != 0 ) {
			info.setEmail(cert.decrypt( info.getEmail()));	
		}
		
		if(info.getMTelno() != null && info.getMTelno().length() != 0 ) {
			info.setMTelno(cert.decrypt( info.getMTelno()));	
		}
		
		if(info.getBplc() != null && info.getBplc().length() != 0 ) {
			info.setBplc(cert.decrypt( info.getBplc()));	
		}
		
		if(info.getPayBizrno() != null && info.getPayBizrno().length() != 0 ) {
			info.setPayBizrno(cert.decrypt( info.getPayBizrno()));	
		}
		
		if(info.getPayTelno() != null && info.getPayTelno().length() != 0 ) {
			info.setPayTelno(cert.decrypt( info.getPayTelno()));	
		}
		
		if(info.getBankSerial() != null && info.getBankSerial().length() != 0 ) {
			info.setBankSerial(cert.decrypt( info.getBankSerial()));	
		}
		
		if(info.getPayBplc() != null && info.getPayBplc().length() != 0 ) {
			info.setPayBplc(cert.decrypt( info.getPayBplc()));	
		}
		
		return info;
	}

	@Override
	@Transactional
	public void updateDealer(DealerRegInfo param){
		try {
			// 디비에 암호화후 입력
			if(param.getBizrno() != null && param.getBizrno().length() != 0 ) {
				param.setBizrno(cert.decrypt( param.getBizrno()));	
			}
			
			if(param.getBplc() != null && param.getBplc().length() != 0 ) {
				param.setBplc(cert.decrypt( param.getBplc()));	
			}
			
			if(param.getBankSerial() != null && param.getBankSerial().length() != 0 ) {
				param.setBankSerial(cert.decrypt( param.getBankSerial()));	
			}
			
			if(param.getBizTelno() != null && param.getBizTelno().length() != 0 ) {
				param.setBizTelno(cert.decrypt( param.getBizTelno()));	
			}
			
			if(param.getPayTelno() != null && param.getPayTelno().length() != 0 ) {
				param.setPayTelno(cert.decrypt( param.getPayTelno()));	
			}
			
			if(param.getPayBizrno() != null && param.getPayBizrno().length() != 0 ) {
				param.setPayBizrno(cert.decrypt( param.getPayBizrno()));	
			}
			
			if(param.getPayBplc() != null && param.getPayBplc().length() != 0 ) {
				param.setPayBplc(cert.decrypt( param.getPayBplc()));	
			}
			
			if(param.getMTelno() != null && param.getMTelno().length() != 0 ) {
				param.setMTelno(cert.decrypt( param.getMTelno()));	
			}
			
			if(param.getEmail() != null && param.getEmail().length() != 0 ) {
				param.setEmail(cert.decrypt( param.getEmail()));	
			}
			
			dealerMapper.updateDealerMbr(param);
			dealerMapper.updateDealerBiz(param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SqlErrorException("sqlerr");
		}
		
	}

	@Override
	public void insertDealer(DealerRegInfo param) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int dealerIdCheck(String userId) throws Exception {
		return dealerMapper.dealerIdCheck(userId);
	}
}
