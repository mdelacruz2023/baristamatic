package com.mdelacruz.baristamatic.svc;

import java.util.List;

import java.util.Optional;

import com.mdelacruz.baristamatic.domain.Drink;
import com.mdelacruz.baristamatic.dto.DrinkDTO;

public interface DrinkSvc {
	
	List<DrinkDTO> getAllDrinks();
	
	boolean processDrinkOrder(Long drinkId);
	
	Optional<Drink> getDrinkById(Long drinkId);
}
