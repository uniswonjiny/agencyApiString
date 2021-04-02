package org.bizpay.mapper;

import java.util.HashMap;

import org.bizpay.exception.AppPreException;

public interface AppPreMapper {
	public int selectAppCodeCount(String val) throws AppPreException;
	public HashMap<String , String> selectMber(String usid) throws AppPreException;
	public int selectInstallment(String mberCode )throws AppPreException;
	public HashMap<String , Object> trUnicore() throws AppPreException;
	public String getBizCode(String mberCode) throws AppPreException;
	public HashMap<String , Object> selectMberBasis(String mberCode) throws AppPreException;
	public HashMap<String , Object> selectTbBiz(String bizCode) throws AppPreException;
	
}
