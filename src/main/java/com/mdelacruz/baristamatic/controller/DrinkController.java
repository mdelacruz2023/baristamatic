package com.mdelacruz.baristamatic.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdelacruz.baristamatic.domain.Drink;
import com.mdelacruz.baristamatic.dto.DrinkDTO;
import com.mdelacruz.baristamatic.dto.DrinkOrderDTO;
import com.mdelacruz.baristamatic.dto.MessageDTO;
import com.mdelacruz.baristamatic.svc.DrinkSvc;

@RestController
@RequestMapping("/api/v1")
public class DrinkController {

	@Autowired
	DrinkSvc drinkService;
	
	private static final String SUCCESS_MESSAGE_KEY = "Success";
	private static final String ERROR_MESSAGE_KEY = "Error";
	
	/**
	 * Returns the list of drinks with the following information: drink id, name, cost and availability.
	 */
	@GetMapping(path="/drinks",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDrinkMenu() {
		try {
			List<DrinkDTO> drinkDTOs = drinkService.getAllDrinks();
			
			if (drinkDTOs.isEmpty()) {
				MessageDTO messageDTO = new MessageDTO();
				messageDTO.setKey(ERROR_MESSAGE_KEY);
				messageDTO.setMessage("Drink list is not available");
				return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(drinkDTOs, HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Returns whether the order can be processed or not.
	 */
	@PostMapping(path = "/drinks/orders",  
				 consumes = MediaType.APPLICATION_JSON_VALUE, 
				 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> orderDrink(@RequestBody DrinkOrderDTO orderDTO) {
		try {
			//Validate the input
			Optional<Drink> drink = drinkService.getDrinkById(orderDTO.getDrinkId());
			if (drink.isEmpty()) {
				MessageDTO messageDTO = new MessageDTO();
				messageDTO.setKey(ERROR_MESSAGE_KEY);
				messageDTO.setMessage("Drink ID is not valid.");
				return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
			}
			
			boolean isOrderProcessed = drinkService.processDrinkOrder(orderDTO.getDrinkId());
			
			if (isOrderProcessed) {
				MessageDTO messageDTO = new MessageDTO();
				messageDTO.setKey(SUCCESS_MESSAGE_KEY);
				messageDTO.setMessage("The order has been processed successfully.");
				return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
			}
			
			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setKey(ERROR_MESSAGE_KEY);
			messageDTO.setMessage("Order cannot be processed. One or more ingredients are not available.");
			return new ResponseEntity<>(messageDTO, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
