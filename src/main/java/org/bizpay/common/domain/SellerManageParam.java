package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SellerManageParam {
	public String memberBizeCode; // 로그인한 비즈코드
	public String memberMberCode; // 로그인한 사용자코드 
	public String dateStart;  
	public String dateEnd; 
	public String indutyId; 
	public String bizCode; 
	public String memberName;
	public String bizNum;
	public String useAt;
	public String calculateType;
	public String depositor;
	public String nm;
	public String bizTypeName;
	public String bizItem;
}
