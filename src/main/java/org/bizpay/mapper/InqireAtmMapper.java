package org.bizpay.mapper;

import java.util.List;

import org.bizpay.common.domain.InqireMberParam;
import org.bizpay.domain.AtmConfig;
import org.bizpay.domain.InqireAtm;
import org.bizpay.domain.InqireAtmSum;
// 입출금내역
public interface InqireAtmMapper {
	// 전체합계정보
	public InqireAtmSum totInfo(InqireMberParam param) throws Exception;
	// 모든목록 -엑셀 , 화면 데이터테이블용도
	public List<InqireAtm> list(InqireMberParam param) throws Exception;
	// 페이징처리된 목록
	public List<InqireAtm> pageList(InqireMberParam param) throws Exception;
	// 출금정지상태 확인
	public AtmConfig AtmConfigInfo(String gbCode ) throws Exception;
	// 출금정지설정
	public int AtmConfigUpdate(AtmConfig ac) throws Exception;
}
