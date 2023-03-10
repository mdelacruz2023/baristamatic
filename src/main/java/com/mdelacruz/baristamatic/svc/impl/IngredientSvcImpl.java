package com.mdelacruz.baristamatic.svc.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdelacruz.baristamatic.domain.Ingredient;
import com.mdelacruz.baristamatic.dto.IngredientDTO;
import com.mdelacruz.baristamatic.repository.IngredientRepository;
import com.mdelacruz.baristamatic.svc.IngredientSvc;

@Service
public class IngredientSvcImpl implements IngredientSvc {
	
	@Autowired
	IngredientRepository ingredientRepository;
	
	private static final Long MAX_UNITS = 10L;

	@Override
	public List<IngredientDTO> getAllIngredients() {
		List<IngredientDTO> ingredientDTOS = new ArrayList<>();
		
		List<Ingredient> ingredients =  ingredientRepository.findAll();
		
		if (ingredients != null) {
			for (Ingredient ingredient: ingredients) {
				IngredientDTO ingredientDTO = new IngredientDTO();
				ingredientDTO.setIngredientId(ingredient.getIngredientId());
				ingredientDTO.setName(ingredient.getName());
				ingredientDTO.setQuantity(ingredient.getQuantity());
				
				ingredientDTOS.add(ingredientDTO);
			}
		}
		
		return ingredientDTOS;
	}

	@Override
	public int updateIngredientQty(Long id, Long quantity) {
		int updated = 0;
		Ingredient ingredient = ingredientRepository.findById(id).orElse(null);
		
		if (ingredient != null) {
			ingredient.setQuantity(quantity);
			ingredientRepository.save(ingredient);
			updated = 1;
		}
		return updated;
	}

	@Override
	public int restockIngredients() {
		int countUpdatedIngredients = 0;
		
		List<Ingredient> ingredients =  ingredientRepository.findAll();
		if (ingredients != null) {
			for (Ingredient ingredient: ingredients) {
				ingredient.setQuantity(MAX_UNITS);
				ingredientRepository.save(ingredient);
				countUpdatedIngredients++;
			}
		}
		return countUpdatedIngredients;
	}

}

