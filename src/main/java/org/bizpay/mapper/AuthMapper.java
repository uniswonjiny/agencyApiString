package org.bizpay.mapper;

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
	public BizInfo bizInfo(String bizCode) throws Exception;
	public List<DealerInfo> dealerList(String bizCode) throws Exception;
}
