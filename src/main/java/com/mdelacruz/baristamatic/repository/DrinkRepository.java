package com.mdelacruz.baristamatic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mdelacruz.baristamatic.domain.Drink;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {

}
