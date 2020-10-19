package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.InqireDealerParam;
import org.bizpay.common.domain.InqireDelingParam;
import org.bizpay.common.domain.InqireMberParam;
import org.bizpay.domain.AtmConfig;
import org.bizpay.domain.DealerManager;
import org.bizpay.domain.DealerRegInfo;
import org.bizpay.domain.InqireAtm;
import org.bizpay.domain.InqireAtmSum;
import org.bizpay.domain.InqireDelng;
import org.bizpay.domain.InqireDelngSum;

public interface BPWService {
	// 입출금내역목록 - 전체
	public List<InqireAtm> InqireAtm(InqireMberParam param) throws Exception;
	// 입출금 전체합계
	public InqireAtmSum InqireAtmTot(InqireMberParam param) throws Exception;
	
	
	// 거래조회
	public List<InqireDelng> inqireDelng(InqireDelingParam param) throws Exception;
	// 거래조회 합계정보
	public InqireDelngSum inqireDelngSum(InqireDelingParam param) throws Exception;
	// 거래조회 전체갯수
	public int inqireDelngCount(InqireDelingParam param) throws Exception;
	
	// 출금정지 상태 정보
	public AtmConfig atmConfigInfo(String gbCode) throws Exception;
	// 출금정지 상태 변경
	public boolean atmConfigUpdate(AtmConfig ac) throws Exception;
	
	// 대리점관리목록
	public List<DealerManager> inqireDealer(InqireDealerParam param) throws Exception;
	// 대리점관리전체갯수
	public int inqireDealerCount(InqireDealerParam param) throws Exception;
	// 대리점상세정보 확인
	public DealerRegInfo dealerbyId(String userId) throws Exception;
	// 대리점 정보 수정
	public void updateDealer(DealerRegInfo param);
	// 대리점 정보 저장
	public void insertDealer(DealerRegInfo param);
	// 딜러아이디확인
	public int dealerIdCheck(String userId) throws Exception;
}
