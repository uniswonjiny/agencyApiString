package org.bizpay.controller;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.service.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import lombok.extern.java.Log;

@Controller
@Log
@CrossOrigin(origins={"*"})
public class HomeController {
	
	@Autowired
	ExternalService service;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("message", "Home...");
		mav.setViewName("index");
		return mav;
	}
	
	@RequestMapping(value="/qrPay", method=RequestMethod.POST)
	public RedirectView qrpay(ExternalOrderInputParam param) {
		log.info("qr코드용 외부 연동결제주문정보 입력");
		try {
			long seq = service.insertExOrder(param);
			return new RedirectView("http://dm1586000202893.fun25.co.kr/external/qrpa/"+seq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new RedirectView("http://dm1586000202893.fun25.co.kr/external/qrpa/0");
		}
	}
}
