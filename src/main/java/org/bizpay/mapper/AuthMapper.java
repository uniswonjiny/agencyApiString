package org.bizpay.mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.bizpay.domain.BizInfo;
import org.bizpay.domain.DealerInfo;
import org.bizpay.domain.MemberInfo;
import org.bizpay.exception.SqlErrorException;
import org.springframework.stereotype.Repository;

/**
 * @author swonjiny
 * 사용자정보 정의
 */
public interface AuthMapper {
	MemberInfo userInfo(String id) throws SqlErrorException;
	MemberInfo userInfo2(String mberCode) throws Exception;
	BizInfo bizInfo(String bizCode) throws Exception;
	List<DealerInfo> dealerList(String bizCode) throws Exception;
	String dealerKind(String bizCode) throws Exception;
	int updateMberUseAt (HashMap<String, Object> map) throws Exception;
	int insertMberHistUserAt(HashMap<String, Object> map) throws Exception;
	int insertMberHist2(HashMap<String, Object> map) throws Exception;
	int updateRecommendBizCode(HashMap<String, Object> map) throws Exception;
	int selectMemberUsidChk(String usId) throws Exception;
	int selectBizrno(String bizrno) throws Exception;

}
