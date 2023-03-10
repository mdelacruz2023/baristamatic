package com.mdelacruz.baristamatic.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdelacruz.baristamatic.domain.Drink;
import com.mdelacruz.baristamatic.dto.DrinkDTO;
import com.mdelacruz.baristamatic.dto.DrinkOrderDTO;
import com.mdelacruz.baristamatic.svc.DrinkSvc;

@RunWith(SpringRunner.class)
@WebMvcTest(value=DrinkController.class)
public class DrinkControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private DrinkSvc drinkService;
   
    private static final String GET_DRINK_MENU_URI = "/api/v1/drinks";
    private static final String POST_DRINK_ORDER_URI = "/api/v1/drinks/orders";
    
	@Test
	public void getAllDrinks_shouldReturnListOfDrinks() throws Exception {

		List<DrinkDTO> allDrinkDTOs = new ArrayList<>();
		
	    DrinkDTO drinkDTO1 = createDrinkDTO(1L, "Coffee", true, new BigDecimal(2.75));
	    DrinkDTO drinkDTO2 = createDrinkDTO(2L, "Decaf Coffee", true, new BigDecimal(2.75));
	    
	    allDrinkDTOs.add(drinkDTO1);
	    allDrinkDTOs.add(drinkDTO2);
	    
	    when(drinkService.getAllDrinks()).thenReturn(allDrinkDTOs);
	    
	    mockMvc.perform(get(GET_DRINK_MENU_URI)
	    			.contentType(MediaType.APPLICATION_JSON))
	      		.andExpect(status().isOk())
	      		.andExpect(jsonPath("$.size()").value(allDrinkDTOs.size()))
	      		.andExpect(jsonPath("$[0].drinkId", is(1)))
	      		.andExpect(jsonPath("$[0].name", is(drinkDTO1.getName())))
	      		.andExpect(jsonPath("$[0].cost", is(2.75)))
	      		.andExpect(jsonPath("$[0].isAvailable", is(drinkDTO1.getIsAvailable())))
	      		.andDo(print());
	}

	@Test
	public void getAllDrinks_shouldReturnNotFoundWhenEmptyList() throws Exception {

		List<DrinkDTO> allDrinkDTOs = new ArrayList<>();
		allDrinkDTOs = Collections.emptyList();

		when(drinkService.getAllDrinks()).thenReturn(allDrinkDTOs);
	    
	    mockMvc.perform(get(GET_DRINK_MENU_URI)
	    			.contentType(MediaType.APPLICATION_JSON))
	      		.andExpect(status().isNotFound())
	      		.andDo(print());
	}
	
	@Test
	public void orderDrink_shouldReturnNotFoundWhenInvalidDrinkId() throws Exception {
		Long invalidDrinkId = 10L;
		
		DrinkOrderDTO orderDTO = new DrinkOrderDTO();
		orderDTO.setDrinkId(invalidDrinkId);
		
		when(drinkService.getDrinkById(invalidDrinkId)).thenReturn(Optional.empty());
	    
	    mockMvc.perform(post(POST_DRINK_ORDER_URI)
	    			.content(asJsonString(orderDTO))
	  	      		.contentType(MediaType.APPLICATION_JSON)
	  	      		.accept(MediaType.APPLICATION_JSON))
	        	.andExpect(status().isNotFound())
	        	.andDo(print());
	}
	
	@Test
	public void orderDrink_shouldReturnOKWhenValidAndAvailableDrinkId() throws Exception {
		Long validDrinkId = 1L;
		
		DrinkOrderDTO orderDTO = new DrinkOrderDTO();
		orderDTO.setDrinkId(validDrinkId);
		
		Optional<Drink> drink = Optional.of(new Drink());
		
		when(drinkService.getDrinkById(validDrinkId)).thenReturn(drink);
		when(drinkService.processDrinkOrder(validDrinkId)).thenReturn(true);
		
	    mockMvc.perform(post(POST_DRINK_ORDER_URI)
	  	      		.content(asJsonString(orderDTO))
	  	      		.contentType(MediaType.APPLICATION_JSON)
	  	      		.accept(MediaType.APPLICATION_JSON))
	        	.andExpect(status().isCreated())
	        	.andDo(print());
	}

	@Test
	public void orderDrink_shouldReturnBadRequestWhenValidButUnavailableDrinkId() throws Exception {
		Long validDrinkId = 1L;
		
		DrinkOrderDTO orderDTO = new DrinkOrderDTO();
		orderDTO.setDrinkId(validDrinkId);
		
		Optional<Drink> drink = Optional.of(new Drink());
		
		when(drinkService.getDrinkById(validDrinkId)).thenReturn(drink);
		when(drinkService.processDrinkOrder(validDrinkId)).thenReturn(false);
		
	    mockMvc.perform(post(POST_DRINK_ORDER_URI)
	  	      		.content(asJsonString(orderDTO))
	  	      		.contentType(MediaType.APPLICATION_JSON)
	  	      		.accept(MediaType.APPLICATION_JSON))
	        	.andExpect(status().isBadRequest())
	        	.andDo(print());
	}

	//Helper Methods
	private DrinkDTO createDrinkDTO(Long drinkId, String name, Boolean isAvailable, BigDecimal cost) {
		DrinkDTO drinkDTO = new DrinkDTO();
	    drinkDTO.setDrinkId(drinkId);
	    drinkDTO.setName(name);
	    drinkDTO.setIsAvailable(isAvailable);
	    drinkDTO.setCost(cost);
	    
	    return drinkDTO;
	}
	
	private static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
//    Ingredient ingredient1 = new Ingredient();
//    ingredient1.setIngredientId(2L);
//    ingredient1.setName("Coffee");
//    ingredient1.setQuantity(10L);
//    ingredient1.setUnitCost(new BigDecimal(0.75));
//    
//    Ingredient ingredient2 = new Ingredient();
//    ingredient2.setIngredientId(2L);
//    ingredient2.setName("Sugar");
//    ingredient2.setQuantity(10L);
//    ingredient2.setUnitCost(new BigDecimal(0.25));
//    
//    Ingredient ingredient3 = new Ingredient();
//    ingredient3.setIngredientId(3L);
//    ingredient3.setName("Cream");
//    ingredient3.setQuantity(10L);
//    ingredient3.setUnitCost(new BigDecimal(0.25));
//    
//    Drink drink1 = new Drink();
//    drink1.setDrinkId(1L);
//    drink1.setName("Coffee");
//    
//    List<DrinkIngredient> drinkIngredients1 = new ArrayList<>();
//    
//    DrinkIngredient drinkIngredient1 = new DrinkIngredient();
//    drinkIngredient1.setIngredient(ingredient1);
//    drinkIngredient1.setRequiredQuantity(3L);
//    
//    DrinkIngredient drinkIngredient2 = new DrinkIngredient();
//    drinkIngredient2.setIngredient(ingredient2);
//    drinkIngredient2.setRequiredQuantity(1L);
//    
//    DrinkIngredient drinkIngredient3 = new DrinkIngredient();
//    drinkIngredient3.setIngredient(ingredient3);
//    drinkIngredient3.setRequiredQuantity(1L);
//    
//    drinkIngredient1.setIngredient(ingredient1);
//    drinkIngredient1.setIngredient(ingredient2);
//    drinkIngredient1.setIngredient(ingredient3);
//    
//    drink1.setDrinkIngredients(drinkIngredients1);
//    
//    List<Drink> allDrinks = new ArrayList<>();
//    allDrinks.add(drink1);

	
}
