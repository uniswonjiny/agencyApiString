package org.bizpay.mapper;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AgencyManageParam;
import org.bizpay.common.domain.SellerManageParam;
import org.bizpay.domain.AgencyManage;
import org.bizpay.domain.SellerList;

//대리점관리
public interface AgencyManageMapper {	
	// 대리점목록
	public List<AgencyManage> agencyList(AgencyManageParam param) throws Exception;
	// 하위대리점 내에 속하는 대리점확인
	public int agencyConfirm1(String bizCode) throws Exception;
	// 대리점이 존재하는 지 확인 
	public int bizCount( String bizCode )  throws Exception;	
	// trget_biz_code
	public int agencyConfirm2(String bizCode) throws Exception;
	// member_biz_code
	public int agencyConfirm3(String bizCode) throws Exception;
	// biz update
	public int updateBiz(AgencyManageParam param) throws Exception;
	// biz_hist insert
	public int insertBizHist(AgencyManageParam param) throws Exception;
	// biz insert
	public int insertBiz(AgencyManageParam param) throws Exception;
	// bizCode 추출
	public int selectBizCode() throws Exception;
	// 사업자 번호 존재 확인
	public int selectBizCount(String bizno) throws Exception;
	// 판매자목록
	public List<SellerList> selectSellerList(SellerManageParam param) throws Exception;
	// agencySettingList
	public List<HashMap<String, Object>> agencySettingList(HashMap< String, Object> map) throws Exception;
	// 기준대리점목록
	public List<HashMap<String, Object>> agencySettingList2(HashMap< String, Object> map) throws Exception;
}
