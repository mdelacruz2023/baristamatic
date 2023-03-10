package com.mdelacruz.baristamatic.dto;

import java.math.BigDecimal;

public class DrinkDTO {
	
	private Long drinkId;
	private String name;
	private BigDecimal cost;
	private Boolean isAvailable;
	
	public Long getDrinkId() {
		return drinkId;
	}
	
	public String getName() {
		return name;
	}
	
	public BigDecimal getCost() {
		return cost;
	}
	
	public Boolean getIsAvailable() {
		return isAvailable;
	}
	
	public void setDrinkId(Long drinkId) {
		this.drinkId = drinkId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
}
