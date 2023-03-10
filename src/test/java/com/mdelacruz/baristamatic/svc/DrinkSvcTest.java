package com.mdelacruz.baristamatic.svc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mdelacruz.baristamatic.domain.Drink;
import com.mdelacruz.baristamatic.domain.DrinkIngredient;
import com.mdelacruz.baristamatic.domain.Ingredient;
import com.mdelacruz.baristamatic.dto.DrinkDTO;
import com.mdelacruz.baristamatic.repository.DrinkRepository;
import com.mdelacruz.baristamatic.svc.impl.DrinkSvcImpl;
import com.mdelacruz.baristamatic.svc.impl.IngredientSvcImpl;

@ExtendWith(MockitoExtension.class)
public class DrinkSvcTest {

	@Mock
	private DrinkRepository drinkRepository;
    
	@Mock
    private IngredientSvcImpl ingredientSvc;
    
    @InjectMocks
    private DrinkSvcImpl drinkSvc;
    
    private List<Drink> drinkList = new ArrayList<>();
    private BigDecimal coffeeDrinkCost = new BigDecimal(2.75);
    
    @BeforeEach
    public void setup() {
    	//Ingredient
    	Ingredient coffee = createIngredient(1L, "Coffee", new BigDecimal(0.75), 10L);
    	Ingredient decafCoffee = createIngredient(2L, "Decaf Coffee", new BigDecimal(0.75), 10L);
    	Ingredient sugar = createIngredient(3L, "Sugar", new BigDecimal(0.25), 10L);
    	Ingredient cream = createIngredient(4L, "Cream", new BigDecimal(0.25), 10L);
    	
    	//Coffee
    	List<DrinkIngredient> coffeeIngredientList = new ArrayList<>();
    	DrinkIngredient coffeeIngredient1 = createDrinkIngredient(3L, coffee);
    	DrinkIngredient coffeeIngredient2 = createDrinkIngredient(1L, sugar);
    	DrinkIngredient coffeeIngredient3 = createDrinkIngredient(1L, cream);
    	
    	coffeeIngredientList.add(coffeeIngredient1);
    	coffeeIngredientList.add(coffeeIngredient2);
    	coffeeIngredientList.add(coffeeIngredient3);
    	
    	Drink drink1 = createDrink(1L, "Coffee", coffeeIngredientList);
    	
    	//Decaf Coffee
    	List<DrinkIngredient> decafCoffeeIngredientList = new ArrayList<>();
    	DrinkIngredient decafCoffeeIngredient1 = createDrinkIngredient(3L, decafCoffee);
    	DrinkIngredient decafCoffeeIngredient2 = createDrinkIngredient(1L, sugar);
    	DrinkIngredient decafCoffeeIngredient3 = createDrinkIngredient(1L, cream);
    	
    	decafCoffeeIngredientList.add(decafCoffeeIngredient1);
    	decafCoffeeIngredientList.add(decafCoffeeIngredient2);
    	decafCoffeeIngredientList.add(decafCoffeeIngredient3);
    	
    	Drink drink2 = createDrink(2L, "Decaf Coffee", decafCoffeeIngredientList);
    	
    	drinkList.add(drink1);
    	drinkList.add(drink2);

    }
    
    //JUnit test for getAllDrinks method
    @DisplayName("JUnit test for getAllDrinks method")
    @Test
    public void givenDrinkList_whenGetAllDrinks_thenReturnDrinkDTOList(){
    	
        // given - precondition or setup
    	given(drinkRepository.findAll()).willReturn(drinkList);
    	given(drinkRepository.findById(1L)).willReturn(Optional.of(drinkList.get(0)));
    	
        // when -  action or the behavior that we are going test
        List<DrinkDTO> drinkDTOList = drinkSvc.getAllDrinks();
        
        // then - verify the output
        assertThat(drinkDTOList).isNotNull();
        assertThat(drinkDTOList.size()).isEqualTo(2);
        assertThat(drinkDTOList.get(0).getDrinkId()).isEqualTo(1);
        assertThat(drinkDTOList.get(0).getName()).isEqualTo("Coffee");
        assertThat(drinkDTOList.get(0).getCost()).isEqualTo(coffeeDrinkCost);
        assertThat(drinkDTOList.get(0).getIsAvailable()).isEqualTo(true);
    }

    //JUnit test for getAllDrinks method (negative scenario)
    @DisplayName("JUnit test for getAllDrinks method - negative scenario")
    @Test
    public void givenEmptyDrinkList_whenGetAllDrinks_thenReturnEmptyDrinkDTOList(){
    	
        // given - precondition or setup
    	given(drinkRepository.findAll()).willReturn(Collections.emptyList());
    
        // when -  action or the behavior that we are going test
    	List<DrinkDTO> drinkDTOList = drinkSvc.getAllDrinks();

        // then - verify the output
        assertThat(drinkDTOList).isEmpty();
        assertThat(drinkDTOList.size()).isEqualTo(0);
    }
    
    // JUnit test for processDrinkOrder method
    @DisplayName("JUnit test for processDrinkOrder method")
    @Test
    public void givenDrinkObjectWithAvailableIngredients_whenProcessDrinkOrder_thenReturnTrue(){
        // given - precondition or setup
    	given(drinkRepository.findById(1L)).willReturn(Optional.of(drinkList.get(0)));
    	
        // when -  action or the behavior that we are going test
    	boolean isOrderProcessed = drinkSvc.processDrinkOrder(1L);

        // then - verify the output
        assertThat(isOrderProcessed == true);
    }
    
    // JUnit test for processDrinkOrder method (negative case)
    @DisplayName("JUnit test for processDrinkOrder method - negative scenario")
    @Test
    public void givenInvalidDrinkObject_whenprocessDrinkOrdery_thenReturnFalse(){
        // given - precondition or setup
    	given(drinkRepository.findById(1L)).willReturn((Optional.empty()));
    	
    	// when -  action or the behavior that we are going test
    	boolean isOrderProcessed = drinkSvc.processDrinkOrder(1L);

        // then - verify the output
        assertThat(isOrderProcessed == false);
    }
    
    //Helper Methods
    private Drink createDrink(Long drinkId, String name, List<DrinkIngredient> drinkIngredientList) {
    	Drink drink = new Drink();
    	drink.setDrinkId(drinkId);
    	drink.setName(name);
    	drink.setDrinkIngredients(drinkIngredientList);
    	
    	return drink;
    }
    
    private DrinkIngredient createDrinkIngredient(Long getRequiredQuantity, Ingredient ingredient) {
    	DrinkIngredient drinkIngredient = new DrinkIngredient();
    	drinkIngredient.setRequiredQuantity(getRequiredQuantity);
    	drinkIngredient.setIngredient(ingredient);
    	
    	return drinkIngredient;
    }
    
    private Ingredient createIngredient(Long ingredientId, String name, BigDecimal unitCost, Long quantity) {
    	Ingredient ingredient = new Ingredient();
    	ingredient.setIngredientId(ingredientId);
    	ingredient.setName(name);
    	ingredient.setQuantity(quantity);
    	ingredient.setUnitCost(unitCost);
    	
    	return ingredient;
    }
}
