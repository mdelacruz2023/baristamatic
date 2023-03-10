package com.mdelacruz.baristamatic.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Drink {
	
	@Id
	@Column (name="drink_id")
	private Long drinkId;
	
	private String name;
	
	@OneToMany(mappedBy = "drink")
    List<DrinkIngredient> drinkIngredients;

	//Getter and setter methods
	public Long getDrinkId() {
		return drinkId;
	}

	public String getName() {
		return name;
	}

	public List<DrinkIngredient> getDrinkIngredients() {
		return drinkIngredients;
	}

	public void setDrinkId(Long drinkId) {
		this.drinkId = drinkId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDrinkIngredients(List<DrinkIngredient> drinkIngredients) {
		this.drinkIngredients = drinkIngredients;
	}


}
