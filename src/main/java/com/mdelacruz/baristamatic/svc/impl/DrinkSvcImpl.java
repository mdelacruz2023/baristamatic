package com.mdelacruz.baristamatic.svc.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdelacruz.baristamatic.domain.Drink;
import com.mdelacruz.baristamatic.domain.DrinkIngredient;
import com.mdelacruz.baristamatic.dto.DrinkDTO;
import com.mdelacruz.baristamatic.repository.DrinkRepository;
import com.mdelacruz.baristamatic.svc.DrinkSvc;
import com.mdelacruz.baristamatic.svc.IngredientSvc;


@Service
public class DrinkSvcImpl implements DrinkSvc {

	@Autowired
	DrinkRepository drinkRepository;
	
	@Autowired
	IngredientSvc ingredientSvc;
	
	@Override
	public List<DrinkDTO> getAllDrinks() {
		List<DrinkDTO> drinkDTOs = new ArrayList<>();
		List<Drink> drinks = drinkRepository.findAll();
		
		if (drinks != null) {
			for (Drink drink: drinks) {
				DrinkDTO drinkDTO = new DrinkDTO();
				drinkDTO.setDrinkId(drink.getDrinkId());
				drinkDTO.setName(drink.getName());
				drinkDTO.setCost(getCost(drink.getDrinkId()));
				drinkDTO.setIsAvailable(isAvailable(drink.getDrinkId()));
				
				drinkDTOs.add(drinkDTO);
			}
		}
		return drinkDTOs;
	}

	@Override
	public boolean processDrinkOrder(Long drinkId) {
		boolean isOrderProcessed = false;
		Optional<Drink> drink = drinkRepository.findById(drinkId);
		
		if (drink.isEmpty()) {
			return isOrderProcessed;
		}
	
		if (!isAvailable(drinkId)) {
			return isOrderProcessed;
		}
		
		//Start processing the order
		isOrderProcessed = updateIngredientInventory(drink.get().getDrinkIngredients());
		return isOrderProcessed;
	}

	@Override
	public Optional<Drink> getDrinkById(Long drinkId) {
		return drinkRepository.findById(drinkId);
	}
	
	//Helper methods
	private boolean updateIngredientInventory(List<DrinkIngredient> drinkIngredients) {
		boolean isUpdated = false;
		
		if (drinkIngredients != null && drinkIngredients.size() > 0) {
			for (DrinkIngredient drinkIngredient: drinkIngredients) {
				Long ingredientId = drinkIngredient.getIngredient().getIngredientId();
				Long requiredQty = drinkIngredient.getRequiredQuantity();
				Long availableQty = drinkIngredient.getIngredient().getQuantity();
					
				//Decrease the ingredient
				ingredientSvc.updateIngredientQty(ingredientId, availableQty - requiredQty);
			}
			isUpdated = true;
		}
		
		return isUpdated;

	}
	
	private boolean isAvailable(Long drinkId) {
		boolean available = true;
		
		Optional<Drink> drink = drinkRepository.findById(drinkId);
		
		if (drink.isEmpty()) {
			available = false;
			return available;
		}
		
		//Start checking if the drink has all the required ingredients
		List<DrinkIngredient> drinkIngredients =  drink.get().getDrinkIngredients();
				
		if (drinkIngredients != null && drinkIngredients.size() > 0) {
			for (DrinkIngredient ingredient: drinkIngredients) {
				if (ingredient.getRequiredQuantity() > ingredient.getIngredient().getQuantity()) {
					available = false;
					break;
				}
			}
		}
		
		return available;
	}

	
	private BigDecimal getCost(Long drinkId) {
		
		BigDecimal itemCost  = BigDecimal.ZERO;
	    BigDecimal totalCost = BigDecimal.ZERO;
	    
	    Optional<Drink> drink = drinkRepository.findById(drinkId);
	    
	    if (drink.isPresent()) {
	    	List<DrinkIngredient> drinkIngredients =  drink.get().getDrinkIngredients();
		    
			if (drinkIngredients != null && drinkIngredients.size() > 0) {
				for (DrinkIngredient drinkIngredient: drinkIngredients) {
					itemCost = drinkIngredient.getIngredient().getUnitCost()
							.multiply(BigDecimal.valueOf(drinkIngredient.getRequiredQuantity() ));
					totalCost = totalCost.add(itemCost);
				}
			}
	    }
	    
		return totalCost;
	}


}
