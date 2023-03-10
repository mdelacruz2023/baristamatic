package com.mdelacruz.baristamatic.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="ingredient")
public class Ingredient{
	
	@Id
	@GeneratedValue
	@Column(name="ingredient_id")
	private Long ingredientId;
	
	private String name;
	
	@Column(name="unit_cost")
	private BigDecimal unitCost;
	
	private Long quantity;

	@OneToMany(mappedBy = "ingredient")
	@JsonIgnore
    List<DrinkIngredient> drinkIngredients;

	//Getter and setter methods
	public Long getIngredientId() {
		return ingredientId;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getUnitCost() {
		return unitCost;
	}

	public Long getQuantity() {
		return quantity;
	}

	public List<DrinkIngredient> getDrinkIngredients() {
		return drinkIngredients;
	}

	public void setIngredientId(Long ingredientId) {
		this.ingredientId = ingredientId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setDrinkIngredients(List<DrinkIngredient> drinkIngredients) {
		this.drinkIngredients = drinkIngredients;
	}
}
