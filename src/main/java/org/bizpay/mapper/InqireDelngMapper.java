package org.bizpay.mapper;

import java.util.List;

import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.domain.InqireDelng;
import org.bizpay.domain.InqireDelngSum;
//거래내역
public interface InqireDelngMapper {
	// 전체합계정보
	public InqireDelngSum totInfo(InqireDelingParam param) throws Exception;
	// 모든목록 -엑셀 , 화면 데이터테이블용도 
	public List<InqireDelng> list(InqireDelingParam param) throws Exception;
	// 페이징처리된 목록
	public List<InqireDelng> pageList(InqireDelingParam param) throws Exception;
	// 페이징 처리를 위한 전체갯수 
	public int totCount(InqireDelingParam param) throws Exception;
	
	// 취소조회용
	public List<InqireDelng> cancelList(InqireDelingParam param) throws Exception;

	public List<InqireDelng> cancelPage(InqireDelingParam param) throws Exception;
 
	public int cancelTotCount(InqireDelingParam param) throws Exception;
}
