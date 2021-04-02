package org.bizpay.service;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AgencyManageParam;
import org.bizpay.common.domain.SellerInsertParam;
import org.bizpay.common.domain.SellerManageParam;
import org.bizpay.domain.AgencyManage;
import org.bizpay.domain.SellerList;

public interface AgencyManageSevice {
	// 대리점목록
	public List<AgencyManage> agencyList(AgencyManageParam params ) throws Exception;
	// 대리점 정보 수정
	public void upatgeAgency(AgencyManageParam params ) throws Exception;
	// 대리점 정보 입력
	public void insertAgency(AgencyManageParam params ) throws Exception;
	// 판매자 목록
	public List<SellerList> selectSellerList(SellerManageParam param) throws Exception;
	// 대리점구분 정보 가져오기
	public List<HashMap< String, Object>> settingAgencyList(String memberCode , String agencyCode) throws Exception;
	// 기준대리점 정보 가져오기
	public List<HashMap< String, Object>> settingAgencyList2(String memberCode , String agencyCode) throws Exception;
	// 판매자 등록
	public void insertSellerList(List<SellerInsertParam> list);
	// 판매자 수정
	public void updateSeller(SellerInsertParam param) throws Exception;
}
