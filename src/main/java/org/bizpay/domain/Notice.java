package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notice {
	private String mode;
	private String title;
	private int idx;
	private String content;
	private String isactive;
	private String type;
}