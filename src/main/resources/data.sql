--------------------
--Ingredient
--------------------

insert into ingredient (ingredient_id, name, unit_cost, quantity)
values(1, 'Coffee', 0.75, 10);

insert into ingredient (ingredient_id, name, unit_cost, quantity)
values(2, 'Decaf Coffee', 0.75, 10);

insert into ingredient (ingredient_id, name, unit_cost, quantity)
values(3, 'Sugar', 0.25, 10);

insert into ingredient (ingredient_id, name, unit_cost, quantity)
values(4, 'Cream', 0.25, 10);

insert into ingredient (ingredient_id, name, unit_cost, quantity)
values(5, 'Steamed Milk', 0.35, 10);

insert into ingredient (ingredient_id, name, unit_cost, quantity)
values(6, 'Foamed Milk', 0.35, 10);

insert into ingredient (ingredient_id, name, unit_cost, quantity)
values(7, 'Espresso', 1.10, 10);

insert into ingredient (ingredient_id, name, unit_cost, quantity)
values(8, 'Cocoa', 0.90, 10);

insert into ingredient (ingredient_id, name, unit_cost, quantity)
values(9, 'Whipped Cream', 1.00, 10);


--------------------
--Drink
--------------------

insert into drink (drink_id, name)
values(1, 'Coffee');

insert into drink (drink_id, name)
values(2, 'Decaf Coffee');

insert into drink (drink_id, name)
values(3, 'Caffe Latte');

insert into drink (drink_id, name)
values(4, 'Caffe Americano');

insert into drink (drink_id, name)
values(5, 'Caffe Mocha');

insert into drink (drink_id, name)
values(6, 'Cappuccino');


--------------------
--DrinkIngredient
--------------------

-- (1) Coffee
insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(1, 1, 3);

insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(1, 3, 1);

insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(1, 4, 1);

-- (2) Decaf Coffee
insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(2, 2, 3);

insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(2, 3, 1);

insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(2, 4, 1);

-- (3) Caffe Latte
insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(3, 7, 2);

insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(3, 5, 1);

-- (4) Caffe Americano
insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(4, 7, 3);

-- (5) Caffe Mocha
insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(5, 7, 1);

insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(5, 8, 1);

insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(5, 5, 1);

insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(5, 9, 1);

-- (6) Cappuccino
insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(6, 7, 2);

insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(6, 5, 1);

insert into drink_ingredient (drink_id, ingredient_id, required_quantity)
values(6, 6, 1);

