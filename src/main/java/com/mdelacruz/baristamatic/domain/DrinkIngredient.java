package com.mdelacruz.baristamatic.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="drink_ingredient")
public class DrinkIngredient {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "drink_id")
    @JsonIgnore
    private Drink drink;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
    
	@Column(name="required_quantity")
	private Long requiredQuantity;

	//Getter and setter methods
	public Long getId() {
		return id;
	}
	
	public Drink getDrink() {
		return drink;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public Long getRequiredQuantity() {
		return requiredQuantity;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDrink(Drink drink) {
		this.drink = drink;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public void setRequiredQuantity(Long requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}
}
