package org.bizpay.common.util;

import java.math.BigDecimal;


// boolean tax 과세여부 , boolean surtax , boolean pointCut 
// true Yes false No
public class TaxCalculator {
	public BigDecimal supplyAmount;
	public BigDecimal taxAmount;
	public boolean tax; //  과세여부; 부여 true 미부여 false
	public boolean surtax ; // 부가세포함여부; 부여 true 별도 false
	public boolean pointCut; // 끝전처리;  절사 true  반올림 false
	public BigDecimal surtaxRate;// 부가세율;
	public BigDecimal unitPrice; // 단가

	public TaxCalculator(boolean tax, boolean surtax,boolean  pointCut, double surtaxRate, double unitPrice) {
		this.tax = tax;
	    this.surtax = surtax;
	    this.pointCut = pointCut;
	    this.surtaxRate = new BigDecimal(surtaxRate);
	    this.unitPrice = new BigDecimal(unitPrice);
	    
	    this.calculate(); //비용계산();
	}
	
	public BigDecimal getSupplyAmount() {
	    return this.supplyAmount;
	  }
	  public BigDecimal getTaxAmount() {
	    return this.taxAmount;
	  }
	  public BigDecimal getTotalAmount() {
	    return this.supplyAmount.add(this.taxAmount);
	  }
	  // 비용계산
	  private void calculate() {
	    if (!this.tax) {
	      this.supplyAmount = this.unitPrice;
	      this.taxAmount = BigDecimal.ZERO;
	      return;
	    }

	    if (this.surtax) {
	      BigDecimal vatnRate1 = BigDecimal.ONE.add(this.surtaxRate.divide(new BigDecimal(100)));

	      if (this.pointCut) {
	        this.supplyAmount = this.unitPrice.divide(vatnRate1, 1);
	        this.taxAmount = this.unitPrice.subtract(this.supplyAmount);
	      } else {
	        this.supplyAmount = this.unitPrice.divide(vatnRate1, 4);
	        this.taxAmount = this.unitPrice.subtract(this.supplyAmount);
	      }
	    }
	    else {
	      BigDecimal vatnRate1 = this.surtaxRate.divide(new BigDecimal(100));

	      if (this.pointCut)
	        this.taxAmount = this.unitPrice.multiply(vatnRate1).setScale(0, 1);
	      else {
	        this.taxAmount = this.unitPrice.multiply(vatnRate1).setScale(0, 4);
	      }
	      this.supplyAmount = this.unitPrice;
	    }
	  } 
}
