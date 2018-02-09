CREATE TABLE group_name (
    id_group serial NOT NULL,
    group_name text,
    CONSTRAINT group_name_pkey PRIMARY KEY (id_group)
)

CREATE TABLE seller (
    id_seller serial NOT NULL,
    seller_name text,
    pass text,
    CONSTRAINT seller_pkey PRIMARY KEY (id_seller)
)

CREATE TABLE names(
  id_names serial NOT NULL,
  name text,
  id_group integer,
  price_in numeric(5,2),
  price_out numeric(5,2),
  balance_shop numeric(5,1),
  balance_stor numeric(5,1),
  price_mesh numeric(15,2),
  CONSTRAINT names_pkey PRIMARY KEY (id_names),
  CONSTRAINT group_name FOREIGN KEY (id_group)
      REFERENCES group_name (id_group) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)

CREATE TABLE booking
(
  id_booking serial NOT NULL,
  id_seller integer,
  date date,
  cost_all numeric(15,2),
  CONSTRAINT booking_pkey PRIMARY KEY (id_booking),
  CONSTRAINT seller_booking FOREIGN KEY (id_seller)
      REFERENCES seller (id_seller) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION

CREATE TABLE booking_dop (
    id_booking_dop serial NOT NULL,
    id_name integer,
    weight numeric(15,1),
    id_booking integer,
    cost_this numeric(15,2),
    CONSTRAINT booking_dop_pkey PRIMARY KEY (id_booking_dop),
    CONSTRAINT booking_booking_dop FOREIGN KEY (id_booking)
        REFERENCES booking (id_booking) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT names_booking_dop FOREIGN KEY (id_name)
        REFERENCES names (id_names) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);