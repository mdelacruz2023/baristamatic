package com.mdelacruz.baristamatic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdelacruz.baristamatic.dto.IngredientDTO;
import com.mdelacruz.baristamatic.dto.IngredientInventoryDTO;
import com.mdelacruz.baristamatic.svc.IngredientSvc;

@RestController
@RequestMapping("/api/v1")
public class IngredientController {
	
	@Autowired
	IngredientSvc ingredientSvc;
	
	private static final int INGREDIENT_TOTAL = 9;
	
	/**
	 * Returns the ingredients inventory
	 */
	@GetMapping(path="/ingredients",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getIngredientInventory() {
		try {
			List<IngredientDTO> ingredientDTOS = ingredientSvc.getAllIngredients();
			if (ingredientDTOS.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(ingredientDTOS, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Restock all the ingredients by setting each ingredient to a maximum of 10 units.
	 */
	@PostMapping(path = "/ingredients/restock",  
				 consumes = MediaType.APPLICATION_JSON_VALUE, 
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> restockIngredients(@RequestBody IngredientInventoryDTO ingredientInventoryDTO) {
		try {
			int updatedRow = ingredientSvc.restockIngredients();
			
			if (updatedRow == INGREDIENT_TOTAL) {
				return new ResponseEntity<>("Ingredients have been restocked successfully.", HttpStatus.OK);
			}
			return new ResponseEntity<>("Ingredients cannot be restocked.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
}
