package com.mdelacruz.baristamatic.dto;

public class IngredientDTO {

	private Long ingredientId;
	private String name;
	private Long quantity;
	
	public Long getIngredientId() {
		return ingredientId;
	}
	
	public String getName() {
		return name;
	}
	
	public Long getQuantity() {
		return quantity;
	}
	
	public void setIngredientId(Long ingredientId) {
		this.ingredientId = ingredientId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}
