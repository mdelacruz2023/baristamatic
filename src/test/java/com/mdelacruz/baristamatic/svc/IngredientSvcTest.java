package com.mdelacruz.baristamatic.svc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mdelacruz.baristamatic.domain.Ingredient;
import com.mdelacruz.baristamatic.dto.IngredientDTO;
import com.mdelacruz.baristamatic.repository.IngredientRepository;

import com.mdelacruz.baristamatic.svc.impl.IngredientSvcImp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class IngredientSvcTest {

	@Mock
	private IngredientRepository ingredientRepository;
    
    @InjectMocks
    private IngredientSvcImp ingredientSvc;

    private List<Ingredient> ingredientList = new ArrayList<>();
        
    @BeforeEach
    public void setup(){
    	Ingredient ingredient1 = createIngredient(1L, "Coffee", new BigDecimal(0.75), 10L);
    	Ingredient ingredient2 = createIngredient(2L, "Sugar", new BigDecimal(0.25), 10L);
    	Ingredient ingredient3 = createIngredient(3L, "Cream", new BigDecimal(0.25), 10L);
    	
    	ingredientList.add(ingredient1);
    	ingredientList.add(ingredient2);
    	ingredientList.add(ingredient3);
    }
    
    //JUnit test for getAllIngredients method
    @DisplayName("JUnit test for getAllIngredients method")
    @Test
    public void givenIngredientList_whenGetAllIngredients_thenReturnIngredientDTOList(){
    	
        // given - precondition or setup
    	given(ingredientRepository.findAll()).willReturn(ingredientList);

        // when -  action or the behavior that we are going test
        List<IngredientDTO> ingredientDTOList = ingredientSvc.getAllIngredients();

        // then - verify the output
        assertThat(ingredientDTOList).isNotNull();
        assertThat(ingredientDTOList.size()).isEqualTo(3);
    }
    
    //JUnit test for getAllIngredients method (negative scenario)
    @DisplayName("JUnit test for getAllIngredients method - negative scenario")
    @Test
    public void givenEmptyIngredientList_whenGetAllIngredients_thenReturnEmptyIngredientDTOList(){
    	
        // given - precondition or setup
    	given(ingredientRepository.findAll()).willReturn(Collections.emptyList());

        // when -  action or the behavior that we are going test
        List<IngredientDTO> ingredientDTOList = ingredientSvc.getAllIngredients();

        // then - verify the output
        assertThat(ingredientDTOList).isEmpty();
        assertThat(ingredientDTOList.size()).isEqualTo(0);
    }
    
    // JUnit test for updateIngredientQty method
    @DisplayName("JUnit test for updateIngredientQty method")
    @Test
    public void givenIngredientObject_whenUpdateIngredientQty_thenReturnOneUpdatedRowCount(){
        // given - precondition or setup
    	given(ingredientRepository.findById(1L)).willReturn(Optional.of(ingredientList.get(0)));
    	
        // when -  action or the behavior that we are going test
    	int updateRowCount = ingredientSvc.updateIngredientQty(1L, 10L);

        // then - verify the output
        assertThat(updateRowCount == 1);
    }
    
    // JUnit test for updateIngredientQty method (negative case)
    @DisplayName("JUnit test for updateIngredientQty method - negative scenario")
    @Test
    public void givenEmptyIngredientObject_whenUpdateIngredientQty_thenReturnZeroUpdatedRowCount(){
        // given - precondition or setup
    	given(ingredientRepository.findById(10L)).willReturn(Optional.empty());
    	
        // when -  action or the behavior that we are going test
    	int updateRowCount = ingredientSvc.updateIngredientQty(10L, 10L);

        // then - verify the output
        assertThat(updateRowCount == 0);
    }

    // JUnit test for restockIngredients method
    @DisplayName("JUnit test for restockIngredients method")
    @Test
    public void givenIngredientList_whenRestockIngredients_thenReturnThreeUpdatedRowCount(){
        // given - precondition or setup
    	given(ingredientRepository.findAll()).willReturn(ingredientList);
    	
        // when -  action or the behavior that we are going test
    	int updateRowCount = ingredientSvc.restockIngredients();

        // then - verify the output
        assertThat(updateRowCount == 3);
    }
    
    // JUnit test for restockIngredients method (negative case)
    @DisplayName("JUnit test for restockIngredients method - negative scenario")
    @Test
    public void givenEmptyIngredientList_whenRestockIngredients_thenReturnZeroUpdatedRowCount(){
        // given - precondition or setup
    	given(ingredientRepository.findAll()).willReturn(Collections.emptyList());
    	
        // when -  action or the behavior that we are going test
    	int updateRowCount = ingredientSvc.restockIngredients();

        // then - verify the output
        assertThat(updateRowCount == 0);
    }
    
    //Helper Methods
    private Ingredient createIngredient(Long ingredientId, String name, BigDecimal unitCost, Long quantity) {
    	Ingredient ingredient = new Ingredient();
    	ingredient.setIngredientId(ingredientId);
    	ingredient.setName(name);
    	ingredient.setQuantity(quantity);
    	ingredient.setUnitCost(unitCost);
    	
    	return ingredient;
    }
}
