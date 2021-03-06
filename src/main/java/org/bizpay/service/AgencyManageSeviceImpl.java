package org.bizpay.service;

import java.util.HashMap;
import java.util.List;

import org.bizpay.common.domain.AgencyManageParam;
import org.bizpay.common.domain.AgencyMbrParam;
import org.bizpay.common.domain.SellerInsertParam;
import org.bizpay.common.domain.SellerManageParam;
import org.bizpay.common.util.CertUtil;
import org.bizpay.common.util.DataFormatUtil;
import org.bizpay.common.util.StringUtils;
import org.bizpay.domain.AgencyManage;
import org.bizpay.domain.MemberInfo;
import org.bizpay.domain.SellerList;
import org.bizpay.exception.SqlErrorException;
import org.bizpay.mapper.AgencyManageMapper;
import org.bizpay.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;
@Log
@Service
public class AgencyManageSeviceImpl implements AgencyManageSevice {
	@Autowired
	AgencyManageMapper mapper;
	
	@Autowired
	AuthMapper authMapper;
	
	@Autowired
	CertUtil util;
	
	@Autowired
	DataFormatUtil dataUtil;
	
	@Autowired
	StringUtils strUtil;
	
	@Override
	public List<AgencyManage> agencyList(AgencyManageParam param)throws Exception  {
		List<AgencyManage> list = mapper.agencyList(param);
		for (AgencyManage dto : list) {
			dto.setBizrno( util.decrypt( dto.getBizrno()) );

			if( dto.getBplc()!=null && dto.getBplc().length() > 50 )dto.setBplc( util.decrypt( dto.getBplc()));
			if( dto.getBizTelno()!=null && dto.getBizTelno().length() > 20 )dto.setBizTelno( util.decrypt(dto.getBizTelno() ) );
			dto.setEmail( util.decrypt( dto.getEmail() ));
			dto.setPayBizrno( util.decrypt(dto.getPayBizrno()  ) );
			dto.setPayBplc( util.decrypt( dto.getPayBplc()  )  );
			if( dto.getPayTelno()!=null && dto.getPayTelno().length() > 20 ) dto.setPayTelno( util.decrypt( dto.getPayTelno() ) );
			if( dto.getMTelno()!=null && dto.getMTelno().length() > 20 ) dto.setMTelno( util.decrypt(dto.getMTelno()  ) );
			if( dto.getBankSerial() !=null && dto.getBankSerial().length() > 20 )	dto.setBankSerial(  util.decrypt( dto.getBankSerial() ) );
			if( dto.getPayBplc() !=null)	dto.setPayBplc(  util.decrypt( dto.getPayBplc() ) );
		}
		return list;
	}
	
	// ?????? Exception ?????? ?????? ???????????? ??????
	@Override
	@Transactional(rollbackFor = {SqlErrorException.class , Exception.class })
	public void upatgeAgency(AgencyManageParam params) throws SqlErrorException , Exception {
		// 1. ??????????????? ???????????? ??????????????? ??????
		int tempCount = mapper.bizCount(params.getBizCode() );
		if(tempCount <1 ) {
			throw new SqlErrorException("??????????????? ????????? ????????? ???????????? ????????????.");
		}
		
		// ?????? ????????????
		Boolean dirtyTarget = !params.getPrevTarget().equals(params.getPrevTarget()); 
		Boolean dirtyKind = !params.getDealerKind().equals( params.getPrevDealerKind());
		
		int iDealerKind =0; // ?????? ????????? ??????
		// ???????????????, ?????????????????? ?????? ????????? ?????????
		
//		 1. ?????? ?????????, ????????? ?????? ????????? ??????????????? ??????
//	 	- ?????? ?????? ??????
//		- ?????? -> ?????????, ????????? ????????? ?????? ?????????, ?????? ????????? ??????
//		- ????????? -> ????????? ?????????  ?????? ?????? ????????? ??????
//		- ?????? -> ????????? : ?????? ????????? ????????? ??????
//		- ????????? -> ?????? : ?????? ?????? ????????? ??????		
		
		
		if(dirtyTarget || dirtyKind ) {
			if( params.getPrevTarget()==null || params.getPrevTarget().length() ==0  ) {
				throw new SqlErrorException("????????? ??????????????? ????????? ?????? ??? ??? ????????????.");
			}else if(dirtyKind) {
				
				if(params.getCDealerKind() > params.getPDearlerKind()) {
					iDealerKind = mapper.agencyConfirm1(params.getMemberBizeCode()) ; // params.getBizCode()
					if(iDealerKind !=0 &&   iDealerKind <= params.getCDealerKind() ) {
						throw new SqlErrorException("?????? ???????????? ????????? ?????? ??????????????? ????????? ????????? ?????? ??? ??? ????????????.");
					}
					
				} else {
					if (dirtyTarget) {
						iDealerKind = mapper.agencyConfirm2(params.getTrgetBizCode());
					}else {
						iDealerKind = mapper.agencyConfirm3(params.getMemberBizeCode());
					}
					if(iDealerKind >0 && iDealerKind >=params.getCDealerKind() ) {
						throw new SqlErrorException("?????? ?????? ???????????? ????????? ????????? ????????? ????????? ?????? ??? ??? ????????????.");
					}	
				}	
				if(iDealerKind >0 &&  iDealerKind <= Integer.parseInt( params.getDealerKind() )  ) {
					throw new SqlErrorException("?????? ???????????? ????????? ?????? ??????????????? ????????? ????????? ?????? ??? ??? ????????????.");
				}
			}
		} // end (dirtyTarget || dirtyKind )
		
		//2.biz????????? ???????????? ????????????.
		String dealerKind = authMapper.dealerKind( params.getMemberBizeCode() );

	
		AgencyManageParam inputParam = new AgencyManageParam();
		inputParam.setBizrno( util.encrypt(params.getBizrno()) );
		inputParam.setBizNum( util.encrypt(params.getBizNum()) );
		//inputParam.setDealerId(params.getDealerId() );
		inputParam.setCmpnm( params.getCmpnm() );
		inputParam.setBprprr( params.getBprprr()  );
		inputParam.setBplc( params.getBplc() );
		inputParam.setBizTelno(params.getBizTelno()  );
		inputParam.setCreatMberCode(params.getCreatMberCode());
		
		if("HEADOFFICE".equals(dealerKind )) {
			inputParam.setTrmnlNo( params.getTrmnlNo() );
			inputParam.setPgTrmnlNo( params.getPgTrmnlNo() );
			inputParam.setPgVan(params.getPgVan()  );
			inputParam.setPgGb( params.getPgGb());
			inputParam.setVanGb( params.getVanGb() );
			inputParam.setBizType( params.getBizType() );
		}
		//??????????????? ?????? ??????.
		if( !"0000002".equals( params.getBizCode() )) {
			inputParam.setDealerKind(params.getDealerKind() );
		}
		if(params.getPrevTarget() != null && params.getPrevTarget() .length() !=0 && params.getTrgetBizCode() != null && params.getTrgetBizCode() .length() !=0  ) {
			inputParam.setTrgetBizCode(  params.getTrgetBizCode() );
		}else {
			inputParam.setTrgetBizCode( "" );
		}
		inputParam.setRecommendBizCode( params.getRecommendBizCode());
		inputParam.setPayBprprr( params.getPayBprprr());
		inputParam.setPayTelno(  util.encrypt(params.getPayTelno() ) );
		inputParam.setPayCmpnm( params.getPayCmpnm());
		inputParam.setPayBizrno( util.encrypt(params.getPayBizrno()));
		inputParam.setPayBplc( util.encrypt( params.getPayBplc()));
		
		inputParam.setMTelno( util.encrypt(params.getMTelno() ));
		inputParam.setEmail(util.encrypt(params.getEmail()) );
		inputParam.setBankName(params.getBankName());
		inputParam.setBankSerial(util.encrypt(params.getBankSerial()));
		inputParam.setBankUser(params.getBankUser());
		inputParam.setDealerRate(params.getDealerRate());
		inputParam.setMemberInputYn(params.getMemberInputYn());
		inputParam.setMemberRate(params.getMemberRate());
		inputParam.setMemo(params.getMemo());
		inputParam.setHistoryMemo(params.getHistoryMemo());
		inputParam.setLimitOne(params.getLimitOne());
		inputParam.setLimitDay(params.getLimitDay());
		inputParam.setLimitMonth(params.getLimitMonth());
		inputParam.setLimitYear(params.getLimitYear());
		inputParam.setInstallmentMonths(params.getInstallmentMonths());
		inputParam.setBizLimitOne(params.getBizLimitOne());
		inputParam.setBizLimitDay(params.getBizLimitDay());
		inputParam.setBizLimitMonth(params.getBizLimitMonth());
		inputParam.setBizLimitYear(params.getBizLimitYear());
		inputParam.setBizInstallmentMonths(params.getBizInstallmentMonths());
		inputParam.setJoinAmt(params.getJoinAmt() );
		inputParam.setBizCode( params.getBizCode()  );
		inputParam.setCreatMberCode(params.getCreatMberCode() );
		
		if(mapper.updateBiz(inputParam)  == 0) {
			throw new SqlErrorException("????????? ?????? ??? ??? ????????????.");
		}
		
		if(mapper.insertBizHist(inputParam) == 0) {
			throw new SqlErrorException("????????? ?????? ?????? ??? ??? ????????????.");
		}
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("mberCode", params.getMberCode());
		map1.put("useAt", params.getUseAt());
		map1.put("updatedMberCode", params.getMemberMberCode());
		if(authMapper.updateMberUseAt( map1)==0 ) {
			throw new SqlErrorException("????????? ??????????????? ???????????? ????????????.");
		}
		
		if(authMapper.insertMberHistUserAt(map1) == 0 ) {
			throw new SqlErrorException("????????? ??????????????? ????????? ??? ????????????.");
		}
		// ?????????????????????
		map1.clear();
		map1.put("recommendBizCode", params.getRecommendBizCode()  );
		map1.put("bizCode" , params.getBizCode()); 
		if(authMapper.updateRecommendBizCode(map1) <0) {
			throw new SqlErrorException("??????????????? ????????? ???????????? ????????????.");
		}
	}
	@Override
	@Transactional(rollbackFor = {SqlErrorException.class , Exception.class })
	public void insertAgency(AgencyManageParam params) throws Exception , SqlErrorException {
		log.info("????????? ?????? ??????");
		// 1. biz ??????
		// 1-1 biz ?????? ?????? ?????????
		
		params.setBizrno( util.encrypt( params.getBizrno().trim().replace("-","")  ) );
		// Bizrno ??? ???????????? ??? ????????????.
		if(  mapper.selectBizCount( params.getBizrno() )  >0   ) {
			throw new SqlErrorException("?????? ????????? ??????????????? ?????????.");
		}
		params.setPgVan( strUtil.getString(params.getPgVan().toLowerCase().trim() , "VAN" )  );
		if(params.getVanGb()!=null) params.setVanGb( strUtil.getString(params.getVanGb().toLowerCase().trim() , "" )  );
		if(params.getPgGb()!=null) params.setPgGb( strUtil.getString(params.getPgGb().toLowerCase().trim() , "" )  );
		if(params.getBizType()!=null) params.setBizType( strUtil.getString(params.getBizType().toLowerCase().trim() , "" )  );
		
		if(params.getPayTelno()!=null) params.setPayTelno( util.encrypt( params.getPayTelno()  )  );
		if(params.getPayBizrno()!=null) params.setPayBizrno( util.encrypt( params.getPayBizrno()  )  );
		
		
		if(params.getPayBplc()!=null) params.setPayBplc( util.encrypt( params.getPayBplc()  )  );
		
		if(params.getEmail()!=null) params.setEmail( util.encrypt( params.getEmail())  );
		if(params.getBankSerial()!=null) params.setBankSerial(  strUtil.getString(params.getBankSerial().toLowerCase().trim() , "VAN" ) );
		if(params.getMemberInputYn()!=null) params.setMemberInputYn( strUtil.getString(params.getMemberInputYn().toLowerCase().trim() , "0" ) );
		params.setPymntRate(0); // ????????? 100 ????????? ???????????? ????????? ?????? ????????? ????????? 0 ??? ????????? ???????????? ????????? ??????.!!
		
		if(params.getMTelno()!=null) params.setMTelno( util.encrypt( params.getMTelno() ) );
		//bizCodeStirng
		int bizCode = mapper.selectBizCode();
		String bizCodeString = dataUtil.format(""+bizCode, "0000000" , dataUtil.ALIGN_RIGHT);
		params.setBizCode(bizCodeString);
		// ?????? ????????? ?????? biz
		if( mapper.insertBiz(params) < 1 ) {
			throw new SqlErrorException("??????????????? ????????? ??????????????????.");
		}
		
		// ???????????? ?????? biz_hist
		if(mapper.insertBizHist(params) <1 ) {
			throw new SqlErrorException("??????????????? ?????? ????????? ??????????????????.");
		}
		
		// mber ??????
		// biz ?????? mber ??? ????????????????????? ?????? ???????????? ????????? dto ?????????
		AgencyMbrParam mbrParam = new AgencyMbrParam();
		mbrParam.setBizCode(  params.getBizCode()  );
		mbrParam.setUsid(params.getUsid());
		mbrParam.setPassword(util.encrypt(params.getPassword()));
		mbrParam.setUseAt( "Y"  );
		mbrParam.setMberCodeSn(0);
		mbrParam.setAuthorCode("AUTHOR_DEALER");

		if(mapper.insertMber(mbrParam) <1 ) {			
			throw new SqlErrorException("??????????????? ?????? ????????? ??????????????????.");
		}
		
		// mber_detail ??????
		if(mapper.insertMberDetail(mbrParam) <1 ) {
			throw new SqlErrorException("??????????????? ?????? ????????? ??????????????????.");
		}
		mbrParam.setSn(1);
		mbrParam.setHistCode("MH_USE_AT" );
		mbrParam.setHistCn("Y");
		// mber_hist2
		if(mapper.insertMberHist(mbrParam) <1 ) {
			throw new SqlErrorException("??????????????? ?????? ????????? ??????????????????.");
		}
		
	}
	@Override
	public List<SellerList> selectSellerList(SellerManageParam param) throws Exception {
		List<SellerList> list = mapper.selectSellerList(param);
		// ????????? ??? ????????? ??????
		for (SellerList dto : list) {
			dto.setAdres( util.decrypt(  dto.getAdres()  ) );
			dto.setMberMobile( util.decrypt( dto.getMberMobile() ));
			dto.setMberPhone( util.decrypt( dto.getMberPhone() ));
			dto.setMberJumi( util.decrypt( dto.getMberJumi() ));
			dto.setAccountNo( util.decrypt( dto.getAccountNo()  ));
			dto.setEmail( util.decrypt( dto.getEmail()  ));
			if("".equals(dto.getRecommendBizCode() )) {
				dto.setRecommendBizCode(  dto.getBRecommendBizCode() );
				if( "".equals( dto.getBRecommendBizCode()  ) ) {
					dto.setRecommendBizCode(  "0000002");
				}
			}
		}
		return list;
	}
	@Override
	public List<HashMap< String, Object>> settingAgencyList(String memberCode , String agencyCode ) throws Exception {
		MemberInfo mInfo = authMapper.userInfo2(memberCode);
		HashMap< String, Object> map = new HashMap<String, Object>();
		map.put("dealerKind", mInfo.getDealerKind());
		map.put("memberBizCode", mInfo.getBizCode());
		map.put("agencyCode", agencyCode);
		List<HashMap< String, Object>> list  = mapper.agencySettingList(map);
		return list;
	}
	
	@Override
	public List<HashMap<String, Object>> settingAgencyList2(String memberCode, String agencyCode) throws Exception {
		MemberInfo mInfo = authMapper.userInfo2(memberCode);
		HashMap< String, Object> map = new HashMap<String, Object>();
		map.put("memberBizCode", mInfo.getBizCode());
		map.put("agencyCode", agencyCode);
		List<HashMap< String, Object>> list  = mapper.agencySettingList2(map);
		return list;
	}
	@Override
	@Transactional(rollbackFor = {SqlErrorException.class })
	public void insertSellerList(List<SellerInsertParam> list) {
		log.info("????????? ??????1  : " + list.toString());
		try {
			for (SellerInsertParam dto: list) {
				if(dto.getMbtlnum()==null || dto.getMbtlnum().length() <3) {
					if(dto.getMberMobile()!=null && dto.getMberMobile().length() >4 ) {
						dto.setMbtlnum(   dto.getMberMobile().substring( dto.getMberMobile().length()-4  ,dto.getMberMobile().length() )  );
					}
				}
				dto.setAdres(util.encrypt(   dto.getAdres()  ));
				dto.setAccountNo(util.encrypt( dto.getAccountNo()));
				dto.setMberMobile( util.encrypt(dto.getMberMobile()));
				dto.setMberJumi( util.encrypt( dto.getMberJumi()));
				dto.setEmail( util.encrypt(dto.getEmail()));
				dto.setMberPhone( util.encrypt(dto.getMberPhone() ));
				
			}
			log.info("????????? ??????2  : " + list.toString());
			if(mapper.insertSellerList(list)<1 ) {
				throw new SqlErrorException("????????? ????????? ????????????????????????.");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new SqlErrorException("????????? ????????? ????????????????????????.");
		}
		
	}
	
	@Override
	@Transactional(rollbackFor = {SqlErrorException.class , Exception.class })
	public void updateSeller(SellerInsertParam param) throws Exception {
		log.info("????????? ?????? - ????????????");
		if( param.getMbtlnum() == null  ) {
			throw new SqlErrorException("??????????????? ???????????? ??????");
		} 
		if( param.getMbtlnum().length()!=4  ) {
			throw new SqlErrorException("????????? ???????????? ??????");
		}
		if( param.getFeeRate() >100 || param.getFeeRate() <0) {
			throw new SqlErrorException("?????????????????? ??????");
		}
		// ???????????? ?????? -- ???????????? ???????????? ????????????
		if( "AUTHOR_MNGR".equals(param.getGrade() ) ||  "HEADOFFICE".equals(param.getGrade() )  || param.getBizCode().equals(  param.getMemberBizeCode()  ) ) {
			log.info("mybatis ?????? ????????????.");
		}else {
			throw new SqlErrorException("????????????");
		}
		
		log.info("????????? ??????1 - mber_basis");
		// ????????? ?????? ???????????????
		param.setMberMobile( util.encrypt( param.getMberMobile() ) );
		param.setMberPhone(util.encrypt( param.getMberPhone() ) );
		param.setAdres(util.encrypt( param.getAdres() ) );
		param.setMberJumi( util.encrypt(param.getMberJumi() ) );
		param.setAccountNo( util.encrypt( param.getAccountNo()  ) );
		param.setEmail( util.encrypt( param.getEmail()  )  );
		
		if(mapper.updateSelerMberBasis(param) <1) {
			throw new SqlErrorException("???????????????????????????");
		}
		
		log.info("????????? ??????2- mber");
		param.setMbtlnum(util.encrypt( param.getMbtlnum()));
		if(param.getMberCode()== null || param.getMberCode().length() <1) {
			throw new SqlErrorException("?????????????????? ????????? ????????????");
		}
		
		if(mapper.updateMber(param) <1 ) {
			throw new SqlErrorException("???????????????????????????2");
		}
		
		log.info("????????? ??????3- mber_hist2");
		HashMap<String, Object> mapHist2 = new HashMap<String, Object>();
		mapHist2.put("mberCode" , param.getMberCode() );
		mapHist2.put("histCode" , "MH_USE_AT");
		mapHist2.put("histCn" , param.getUseAt());
		mapHist2.put("memberMberCode" , param.getMemberMberCode());
		if(mapper.insertMberHist2(mapHist2) < 1) {
			throw new SqlErrorException("?????????????????????????????????1");
		}
		
		log.info("????????? ??????4- mber_hist2 -- ??????????????? ????????? ?????? ??????");
		double reFeeRate = mapper.selectFeeRate(param.getMberCode() ); 
		if(reFeeRate!=param.getFeeRate() ) {
			mapHist2.remove("histCode");
			mapHist2.remove("histCn");
			mapHist2.put("histCode" , "MH_FEE_RATE");
			mapHist2.put("histCn" , param.getFeeRate());	
			if(mapper.insertMberHist2(mapHist2)<1 )throw new SqlErrorException("?????????????????????????????????2");
		}
				
	}
}
