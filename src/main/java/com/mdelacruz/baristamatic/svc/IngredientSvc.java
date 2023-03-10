package com.mdelacruz.baristamatic.svc;

import java.util.List;
import com.mdelacruz.baristamatic.dto.IngredientDTO;

public interface IngredientSvc {
	List<IngredientDTO> getAllIngredients();
	int updateIngredientQty(Long id, Long quantity);
	int restockIngredients();
}
