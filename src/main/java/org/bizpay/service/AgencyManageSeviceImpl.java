package org.bizpay.service;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AgencyManageParam;
import org.bizpay.common.util.CertUtil;
import org.bizpay.domain.AgencyManage;
import org.bizpay.mapper.AgencyManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
@Log
@Service
public class AgencyManageSeviceImpl implements AgencyManageSevice {
	@Autowired
	AgencyManageMapper mapper;
	@Autowired
	CertUtil util;
	@Override
	public List<AgencyManage> agencyList(AgencyManageParam param)throws Exception  {
		List<AgencyManage> list = mapper.agencyList(param);
		for (AgencyManage dto : list) {
			dto.setBizrno( util.decrypt( dto.getBizrno()) );
			dto.setBplc( util.decrypt( dto.getBplc()));
			dto.setBizTelno( util.decrypt(dto.getBizTelno() ) );
			dto.setEmail( util.decrypt( dto.getEmail() ));
			dto.setPayBizrno( util.decrypt(dto.getPayBizrno()  ) );
			dto.setPayBplc( util.decrypt( dto.getPayBplc()  )  );
			dto.setPayTelno( util.decrypt( dto.getPayTelno() ) );
			dto.setMTelno( util.decrypt(dto.getMTelno()  ) );
			dto.setBankSerial(  util.decrypt( dto.getBankSerial() ) );
		}
		return list;
	}
	@Override
	public HashMap<String, Object> upatgeAgency(AgencyManageParam params) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", "ok");
		map.put("message", "");
		
		// 수정하려는 대리점이 존재하는지 검사
		int tempCount = mapper.bizCount(params.getBizCode() );
		if(tempCount <1 ) {
			map.put("code", "error");
			map.put("message", "수정하려는 대리점 정보가 존재하지 않습니다");
			return map;
		}
		
		// 내부 조건부분
		Boolean dirtyTarget = !params.getPrevTarget().equals(params.getPrevTarget()); 
		Boolean dirtyKind = !params.getDealerKind().equals( params.getPrevDealerKind());
		// 기준대리점, 대리점구분에 변경 사항이 있다면
		if(dirtyTarget || dirtyKind ) {
			if( params.getPrevTarget()==null || params.getPrevTarget().length() ==0  ) {
				map.put("code", "error");
				map.put("message", "본사는 대리점구분 정보를 수정 할 수 없습니다.");
				return map;
			}else if(dirtyKind) {
				
				
				
				
				int  iDealerKind = mapper.agencyConfirm1(params.getBizCode()) ;
				
				
				
				
				if(iDealerKind >0 &&  iDealerKind <= Integer.parseInt( params.getDealerKind() )  ) {
					map.put("code", "error");
					map.put("message", "소속 대리점과 같거나 하위 대리점으로 대리점 구분을 수정 할 수 없습니다.");
				}
			}
		}
		
		
		// 암호화 해야 값들 암호화
		params.setBizrno( util.encrypt(params.getBizrno()));
		params.setBplc( util.encrypt( params.getBplc() )  );
		params.setBizTelno( util.encrypt( params.getBizTelno() ) );
		params.setPayTelno( util.encrypt( params.getPayTelno() ) );
		params.setPayBizrno( util.encrypt( params.getPayBizrno() ) );
		params.setPayBplc( util.encrypt( params.getPayBplc() ) );
		params.setMTelno( util.encrypt( params.getMTelno() ) );
		params.setEmail( util.encrypt( params.getEmail() ) );
		params.setBankSerial( util.encrypt( params.getBankSerial() ) );
		
		
		
		
		
		return map;
	}

}
