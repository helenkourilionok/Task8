package by.training.filmstore.dao.impl;

public final class DatabaseColumnName {
	/*name of user table columns*/
	static final String TABLE_USER_EMAIL = "us_email";
	static final String TABLE_USER_PASSWORD = "us_password";
	static final String TABLE_USER_ROLE = "us_role";
	static final String TABLE_USER_LAST_NAME = "us_last_name";
	static final String TABLE_USER_FIRST_NAME = "us_first_name";
	static final String TABLE_USER_PATRONIMIC = "us_patronymic";
	static final String TABLE_USER_PHONE = "us_phone";
	static final String TABLE_USER_BALANCE = "us_balance";
	static final String TABLE_USER_DISCOUNT = "us_discount";
	/*name of user table columns*/
	/*name of order table column*/
	static final String TABLE_ORDER_UID = "ord_uid";
	static final String TABLE_ORDER_EMAIL_USER = "ord_email_user";
	static final String TABLE_ORDER_COMMON_PRICE = "ord_common_price";
	static final String TABLE_ORDER_STATUS = "ord_status";
	static final String TABLE_ORDER_KIND_OF_DELIVERY = "ord_kind_of_delivery";
	static final String TABLE_ORDER_KIND_OF_PAYMENT = "ord_kind_of_payment";
	static final String TABLE_ORDER_DATE_OF_ORDER = "ord_date_of_order";
	static final String TABLE_ORDER_DATE_OF_DELIVERY = "ord_date_of_delivery";
	static final String TABLE_ORDER_ADDRESS = "ord_address";
	/*name of order table columns*/
	/*name of good_of_order table columns*/
	static final String TABLE_GOOD_OF_ORDER_FILM_ID  = "fm_id";
	static final String TABLE_GOOD_OF_ORDER_ORDER_ID  = "ord_id";
	static final String TABLE_GOOD_OF_ORDER_COUNT_FILMS  = "gd_count_films";
	/*name of good_of_order table columns*/
	/*name of film table columns*/
	static final String TABLE_FILM_UID = "fm_uid";
	static final String TABLE_FILM_NAME = "fm_name";
	static final String TABLE_FILM_GENRE= "fm_genre";
	static final String TABLE_FILM_COUNTRY = "fm_country";
	static final String TABLE_FILM_YEAR_OF_RELEASE = "fm_year_of_release";
	static final String TABLE_FILM_QUALITY = "fm_quality";
	static final String TABLE_FILM_FILM_DIRECTOR = "fm_film_director";
	static final String TABLE_FILM_DESCRIPTION = "fm_description";
	static final String TABLE_FILM_PRICE = "fm_price";
	static final String TABLE_FILM_COUNT_FILM = "fm_count_film";
	static final String TABLE_FILM_IMAGE = "fm_image";
	/*name of film table columns*/
	/*name of comment table columns*/
	static final String TABLE_COMMENT_EMAIL_USER = "cm_email_user";
	static final String TABLE_COMMENT_FILM_ID = "cm_film";
	static final String TABLE_COMMENT_CONTENT = "cm_content";
	static final String TABLE_COMMENT_DATE = "cm_date";
	/*name of comment table columns*/
}
