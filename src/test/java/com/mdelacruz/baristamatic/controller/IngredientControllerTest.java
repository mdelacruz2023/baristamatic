package com.mdelacruz.baristamatic.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdelacruz.baristamatic.dto.IngredientDTO;
import com.mdelacruz.baristamatic.dto.IngredientInventoryDTO;
import com.mdelacruz.baristamatic.svc.IngredientSvc;

@RunWith(SpringRunner.class)
@WebMvcTest(value=IngredientController.class)
public class IngredientControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredientSvc ingredientSvc;
    
    private static final String GET_INGREDIENT_INVENTORY_URI = "/api/v1/ingredients";
    private static final String POST_INGREDIENTS_RESTOCK_URI = "/api/v1/ingredients/restock";
    
    private static final int VALID_INGREDIENT_TOTAL = 9;
    private static final int INVALID_INGREDIENT_TOTAL = 1;
    
    @DisplayName("JUnit test for getAllIngredients method")
	@Test
	public void givenIngredientList_whenGetAllIngredients_thenReturnIngredientDTOList() throws Exception {

		List<IngredientDTO> ingredientDTOS = new ArrayList<>();
		
		IngredientDTO ingredientDTO1 = createIngredientDTO(1L, "Coffee", 10L);
		IngredientDTO ingredientDTO2 =  createIngredientDTO(1L, "Decaf Coffee", 10L); 
		
		ingredientDTOS.add(ingredientDTO1);
		ingredientDTOS.add(ingredientDTO2);
	   
	    when(ingredientSvc.getAllIngredients()).thenReturn(ingredientDTOS);
	    
	    mockMvc.perform(get(GET_INGREDIENT_INVENTORY_URI)
	    			.contentType(MediaType.APPLICATION_JSON))
	      		.andExpect(status().isOk())
	      		.andExpect(jsonPath("$.size()").value(ingredientDTOS.size()))
	      		.andExpect(jsonPath("$[0].ingredientId", is(1)))
	      		.andExpect(jsonPath("$[0].name", is(ingredientDTO1.getName())))
	      		.andExpect(jsonPath("$[0].quantity", is(10)))
	      		.andDo(print());
	}

    @DisplayName("JUnit test for getAllIngredients method - negative scenario")
	@Test
	public void givenEmptyIngredientList_whenGetAllIngredients_thenReturnNotFound() throws Exception {
		List<IngredientDTO> ingredientDTOS = new ArrayList<>();
		ingredientDTOS = Collections.emptyList();

		when(ingredientSvc.getAllIngredients()).thenReturn(ingredientDTOS);
		
	    mockMvc.perform(get(GET_INGREDIENT_INVENTORY_URI)
	    			.contentType(MediaType.APPLICATION_JSON))
	      		.andExpect(status().isNotFound())
	      		.andDo(print());
	}
	
    @DisplayName("JUnit test for restockIngredients method")
	@Test
	public void givenIngredientList_whenRestockIngredients_thenReturnOk() throws Exception {
		IngredientInventoryDTO ingredientInventoryDTO = new IngredientInventoryDTO();
		ingredientInventoryDTO.setRestock(true);
	
		when(ingredientSvc.restockIngredients()).thenReturn(VALID_INGREDIENT_TOTAL);
		
	    mockMvc.perform(post(POST_INGREDIENTS_RESTOCK_URI)
	  	      		.content(asJsonString(ingredientInventoryDTO))
	  	      		.contentType(MediaType.APPLICATION_JSON)
	  	      		.accept(MediaType.APPLICATION_JSON))
	        	.andExpect(status().isOk())
	        	.andDo(print());
	}

    @DisplayName("JUnit test for restockIngredients method - negative scenario")
	@Test
	public void givenIngredientList_whenRestockIngredients_returnInvalidRowUpdate_thenReturnError() throws Exception {
		
		IngredientInventoryDTO ingredientInventoryDTO = new IngredientInventoryDTO();
		ingredientInventoryDTO.setRestock(true);
	
		when(ingredientSvc.restockIngredients()).thenReturn(INVALID_INGREDIENT_TOTAL);
	    
	    mockMvc.perform(post(POST_INGREDIENTS_RESTOCK_URI)
	    			.content(asJsonString(ingredientInventoryDTO))
	  	      		.contentType(MediaType.APPLICATION_JSON)
	  	      		.accept(MediaType.APPLICATION_JSON))
	        	.andExpect(status().isInternalServerError())
	        	.andDo(print());
	}
	
	//Helper Methods
	private IngredientDTO createIngredientDTO(Long ingredientId, String name, Long quantity) {
		IngredientDTO ingredientDTO = new IngredientDTO();
		ingredientDTO.setIngredientId(ingredientId);
		ingredientDTO.setName(name);
		ingredientDTO.setQuantity(quantity);
		return ingredientDTO;
	}
	
	private String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
