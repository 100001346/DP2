CREATE USER 'dp2'@'localhost' IDENTIFIED BY 'swe30010';
GRANT ALL PRIVILEGES ON * . * TO 'dp2'@'localhost';
DROP DATABASE IF EXISTS pharmacy;
CREATE DATABASE pharmacy CHARACTER SET = 'utf8' COLLATE = 'utf8_general_ci';
USE pharmacy;

DROP TABLE IF EXISTS product;
CREATE TABLE product
(
	prod_id int(8),
	prod_name varchar(100) NOT NULL,
	prod_desc varchar(1000),
	prod_price decimal(5,2) NOT NULL,
	PRIMARY KEY (prod_id)
);

INSERT INTO product VALUES
	('80803940', 'Mercedes Benz Man Eau de Toilette 100ml Spray', 'Limited Edition Male Fragrance', '69.00'),
	('77510083', 'Colgate Toothpaste Sensitive Multi Protect 110g', 'Teeth Care Product', '3.99'),
	('69714312', 'Colgate Slim Soft Toothbrush Charcoal 1 Pack', 'Teeth Care Product', '2.49'),
	('94993211', 'Banana Boat After Sun Aloe Gel 250g', 'Personal Care', '5.39'),
	('11249800', 'Natures Own Odourless Fish Oil 1500mg High Strength 600 Capsules', 'Vitamins', '29.99'),
	('73436518', 'Medi Spot First Aid Kit 126 Piece', 'First Aid', '22.99'),
	('42239102', 'Panadol Rapid 20 Caplets', 'Panadol', '2.99'),
	('92219372', 'Anticol Vapour Action 10 Lozenges', 'Throat Soothers', '1.99'),
	('40973769', 'Ecostore Multipurpose Spray Cleaner Orange & Thyme 500ml', 'Household Cleaning', '5.99'),
	('25776188', 'Ecostore Toilet Cleaner Citrus 500ml', 'Household Cleaning', '5.99'),
	('64004425', 'Healthy Care Grape Seed Extract 12000 Gold Jar 300 Capsules', 'Vitamins', '25.99'),
	('55769950', 'Healthy Care Propolis 2000mg 200 Capsules', 'Vitamins', '19.99'),
	('55979102', 'Codral PE Cold & Flu Day & Night 48 Tablets', 'Cold & Flu', '18.99'),
	('82052339', 'Codral PE Cold & Flu + Cough Day & Night 24 Capsules', 'Cold & Flu', '13.99'),
	('14326397', 'Oral B 3D White Rinse Freshmint 473ml', 'Mouth Wash', '7.99'),
	('53231685', 'Plax Mouthwash Gentle Care 1 Litre', 'Mouth Wash', '9.99'),
	('76774183', 'L Oreal Excellence Creme - 5.50 Mahogany Brown', 'Hair Colours', '16.99'),
	('44023394', 'Schwarzkopf Perfect Mousse 1-0 Black', 'Hair Colours', '8.99'),
	('91721174', 'L Oreal Elvive Conditioner ReNutrition 250ml', 'Shampoo & Conditioners', '5.95'),
	('17310173', 'L Oreal Elvive Shampoo ReNutrition 250ml', 'Shampoo & Conditioners', '5.95'),
	('49205718', 'Lucas Papaw Ointment 25g', 'Personal Care', '7.60'),
	('58295601', 'Ego QV Gentle Wash 1.25kg', 'Personal Care', '18.99'),
	('19563863', 'Oral B Pro Health Mouth Rinse Freshmint 500ml', 'Teeth Care Product', '6.99'),
	('18573572', 'Holdtite Denture Powder 70g', 'Teeth Care Product', '9.69'),
	('85274582', 'Blackmores Vitamin E cream 50g', 'Vitamins', '11.99'),
	('74839275', 'Swisse Ultiboost Hair Skin Nails', 'Vitamins', '40.95'),
	('18403758', 'Calvin Klein CK One 200ml', 'Fragrence', '99.00'),
	('12579264', 'Calvin Klein Be Eau de toilette 200ml', 'Spray	Fragrence', '99.00'),
	('57294756', 'Aptamil Gold+ 4 Junior', 'Vitamins', '19.99'),
	('17382647', 'Vicks Vaporub Baby Balsam 50g', 'Baby Care', '9.99');

DROP TABLE IF EXISTS inventory;
CREATE TABLE inventory
(
	prod_id int(8),
	inv_qty int(3) NOT NULL,
	inv_low int(3) NOT NULL,
	inv_order int(3) NOT NULL,
	PRIMARY KEY (prod_id),
	FOREIGN KEY (prod_id) REFERENCES product(prod_id)
);

INSERT INTO inventory VALUES
	('80803940', '15', '5', '10'),
	('77510083', '75', '25', '35'),
	('69714312', '100', '20', '40'),
	('94993211', '30', '25', '30'),
	('11249800', '50', '15', '30'),
	('73436518', '20', '10', '15'),
	('42239102', '80', '30', '40'),
	('92219372', '100', '25', '50'),
	('40973769', '60', '25', '40'),
	('25776188', '60', '25', '40'),
	('64004425', '100', '10', '20'),
	('55769950', '50', '10', '20'),
	('55979102', '21', '10', '20'),
	('82052339', '50', '10', '20'),
	('14326397', '4', '5', '10'),
	('53231685', '5', '5', '10'),
	('76774183', '10', '5', '10'),
	('44023394', '2', '5', '10'),
	('91721174', '5', '5', '10'),
	('17310173', '8', '5', '10');

DROP TABLE IF EXISTS sale;
CREATE TABLE sale
(
	sale_id bigint(12),
	prod_id int(8),
	sale_qty int(4),
	sale_price decimal(5,2),
	sale_date date,
	PRIMARY KEY (sale_id, prod_id),
	FOREIGN KEY (prod_id) REFERENCES product(prod_id)
);

INSERT INTO sale VALUES
	('776496839426', '40973769', '1', '5.99', '2016/05/03'),
	('776496839426', '25776188', '1', '5.99', '2016/05/03'),
	('776496839426', '92219372', '3', '5.97', '2016/05/03'),
	('547230383915', '80803940', '1', '69.99','2106/06/16'),
	('249154427405', '77510083', '1', '3.99', '2016/07/13'),
	('249154427405', '69714312', '1', '2.49', '2016/07/13'),
	('221676741632', '94993211', '1', '5.39', '2016/08/24'),
	('614111329546', '11249800', '2', '59.98', '2016/12/11'),
	('248538281698', '73436518', '1', '29.99', '2016/12/31'),
	('248538281698', '42239102', '5', '14.95', '2016/12/31'),
	('834133946536', '92219372', '3', '1.99', '2016/04/25'),
	('834133946536', '42239102', '2', '2.99', '2016/04/25'),
	('834133946536', '14326397', '1', '7.99', '2016/04/25'),
	('620323960630', '55979102', '1', '18.99', '2016/05/07'),
	('406354188585', '91721174', '1', '5.95', '2016/04/20'),
	('406354188585', '17310173', '1', '5.95', '2016/04/20'),
	('226472363101', '55769950', '1', '19.99', '2016/06/21'),
	('694654601899', '55979102', '1', '18.99', '2016/08/18'),
	('715134119242', '77510083', '1', '3.99', '2016/06/10'),
	('715134119242', '53231685', '1', '9.99', '2016/06/10');
