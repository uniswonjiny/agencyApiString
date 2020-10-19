package org.bizpay.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccessLog {
	private String requestUri;
	private String className;
	private String classSimpleName;
	private String methodName;
	private String remoteAddr;
	private Date regDate;
}
