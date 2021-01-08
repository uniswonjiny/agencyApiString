package org.bizpay.mapper;

import java.util.List;

import org.bizpay.common.domain.AgencyManageParam;
import org.bizpay.domain.AgencyManage;

//대리점관리
public interface AgencyManageMapper {	
	// 대리점목록
	public List<AgencyManage> agencyList(AgencyManageParam param) throws Exception;
	// 하위대리점 내에 속하는 대리점확인
	public int agencyConfirm1(String bizCode) throws Exception;
	// 대리점이 존재하는 지 확인 
	public int bizCount( String bizCode )  throws Exception;	
	// 소속 대리점과 같거나 하위 대리점으로 대리점 구분
	public int agencyConfirm2(String bizCode) throws Exception;
}
