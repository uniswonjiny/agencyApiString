package org.bizpay.mapper;

import java.util.HashMap;
import java.util.List;

import org.bizpay.domain.BizInfo;
import org.bizpay.domain.DealerInfo;
import org.bizpay.domain.MemberInfo;

/**
 * @author swonjiny
 * 사용자정보 정의
 */
public interface AuthMapper {
	public MemberInfo userInfo(String id) throws Exception;
	public MemberInfo userInfo2(String mberCode) throws Exception;
	public BizInfo bizInfo(String bizCode) throws Exception;
	public List<DealerInfo> dealerList(String bizCode) throws Exception;
	public String dealerKind(String bizCode) throws Exception;
	public int updateMberUseAt (HashMap<String, Object> map) throws Exception;
	public int insertMberHistUserAt(HashMap<String, Object> map) throws Exception;
	public int updateRecommendBizCode(HashMap<String, Object> map) throws Exception;
	public int selectMemberUsidChk(String usId) throws Exception;
	public int selectBizrno(String bizrno) throws Exception;
}
