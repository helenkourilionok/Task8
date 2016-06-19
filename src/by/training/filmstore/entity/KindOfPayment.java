package by.training.filmstore.entity;

public enum KindOfPayment {
	PAYMENT_IN_CASH("наличными"),PAYMENT_BY_CARD("банковская карта");
	private final String nameKindOfPAyment;
	public String getNameKindOfPAyment()
	{
		return nameKindOfPAyment;
	}
	private KindOfPayment(String nameKindOfPAyment)
	{
		this.nameKindOfPAyment = nameKindOfPAyment;
	}
    public static KindOfPayment getKindOfPaymentByName(String findNameKindPayment) {
    	KindOfPayment findKindOfPayment = null;
     	for (KindOfPayment kindPayment: KindOfPayment.values()) {
             if (kindPayment.nameKindOfPAyment.equals(findNameKindPayment)) {
                 findKindOfPayment = kindPayment;
                 break;
             }
         }
         return findKindOfPayment;
     }
}
