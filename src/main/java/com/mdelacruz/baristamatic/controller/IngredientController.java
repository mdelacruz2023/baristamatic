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
import com.mdelacruz.baristamatic.dto.MessageDTO;
import com.mdelacruz.baristamatic.svc.IngredientSvc;

@RestController
@RequestMapping("/api/v1")
public class IngredientController {
	
	@Autowired
	IngredientSvc ingredientSvc;
	
	private static final int INGREDIENT_TOTAL = 9;
	private static final String SUCCESS_MESSAGE_KEY = "Success";
	private static final String ERROR_MESSAGE_KEY = "Error";
	
	/**
	 * Returns the ingredients inventory
	 */
	@GetMapping(path="/ingredients",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getIngredientInventory() {
		try {
			List<IngredientDTO> ingredientDTOS = ingredientSvc.getAllIngredients();
			
			if (ingredientDTOS.isEmpty()) {
				MessageDTO messageDTO = new MessageDTO();
				messageDTO.setKey(ERROR_MESSAGE_KEY);
				messageDTO.setMessage("Ingredient ID is not valid.");
				return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
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
	public ResponseEntity<?> restockIngredients(@RequestBody IngredientInventoryDTO ingredientInventoryDTO) {
		try {
			int updatedRow = ingredientSvc.restockIngredients();
			
			if (updatedRow == INGREDIENT_TOTAL) {
				MessageDTO messageDTO = new MessageDTO();
				messageDTO.setKey(SUCCESS_MESSAGE_KEY);
				messageDTO.setMessage("Ingredients have been restocked successfully.");
				return new ResponseEntity<>(messageDTO, HttpStatus.OK);
			}
			
			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setKey(ERROR_MESSAGE_KEY);
			messageDTO.setMessage("Ingredients cannot be restocked.");
			return new ResponseEntity<>(messageDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}
}
