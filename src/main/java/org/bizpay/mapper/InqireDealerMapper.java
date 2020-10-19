package org.bizpay.mapper;

import java.util.List;

import org.bizpay.common.domain.InqireDealerParam;
import org.bizpay.domain.DealerManager;
import org.bizpay.domain.DealerRegInfo;

public interface InqireDealerMapper {
	public int totCount(InqireDealerParam param) throws Exception;
	public List<DealerManager> list(InqireDealerParam param) throws Exception;
	public List<DealerManager> pageList(InqireDealerParam param) throws Exception;
	public DealerRegInfo dealerbyId(String userId) throws Exception;
	public int updateDealerMbr(DealerRegInfo param) throws Exception;
	public int updateDealerBiz(DealerRegInfo param) throws Exception;
	public int dealerIdCheck(String userId) throws Exception;
}
