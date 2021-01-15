package org.bizpay.service;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AgencyManageParam;
import org.bizpay.domain.AgencyManage;

public interface AgencyManageSevice {
	// 대리점목록
	public List<AgencyManage> agencyList(AgencyManageParam params ) throws Exception;
	// 대리점 정보 수정
	public HashMap<String, Object> upatgeAgency(AgencyManageParam params ) throws Exception;
	// 대리점 정보 입력
	public HashMap<String, Object> insertAgency(AgencyManageParam params ) throws Exception;
}
