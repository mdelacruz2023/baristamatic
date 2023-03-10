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
import com.mdelacruz.baristamatic.svc.DrinkSvc;

@RestController
@RequestMapping("/api/v1")
public class DrinkController {

	@Autowired
	DrinkSvc drinkService;
	
	/**
	 * Returns the list of drinks with the following information: drink id, name, cost and availability.
	 */
	@GetMapping(path="/drinks",
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getDrinkMenu() {
		try {
			List<DrinkDTO> drinkDTOs = drinkService.getAllDrinks();
			if (drinkDTOs.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
	public ResponseEntity<String> orderDrink(@RequestBody DrinkOrderDTO orderDTO) {
		try {
			//Validate the input
			Optional<Drink> drink = drinkService.getDrinkById(orderDTO.getDrinkId());
			if (drink.isEmpty()) {
				return new ResponseEntity<>("Drink ID is not valid.", HttpStatus.NOT_FOUND);
			}
			
			boolean isOrderProcessed = drinkService.processDrinkOrder(orderDTO.getDrinkId());
			
			if (isOrderProcessed) {
				return new ResponseEntity<>("The order has been processed successfully.", HttpStatus.CREATED);
			}
			return new ResponseEntity<>("Order cannot be processed. One or more ingredients are not available.", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
