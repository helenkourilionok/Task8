package by.training.filmstore.entity;

public enum KindOfDelivery {
	MAILING("пересылка по почте"),COURIER("курьером"),
	SELFDELIVERY("самовывоз"),ANOTHER("другое по согласованию с менеджером");
	private final String nameKindOfDelivery;
	public String getNameKindOfDelivery()
	{
		return nameKindOfDelivery;
	}
	private KindOfDelivery(String nameKindOfDelivery) {
		this.nameKindOfDelivery = nameKindOfDelivery;
	}
    public static KindOfDelivery getKindOfDeliveryByName(String findNameKindOfDelivery) {
       KindOfDelivery findKindDelivery = null;
    	for (KindOfDelivery kindDelivery: KindOfDelivery.values()) {
            if (kindDelivery.nameKindOfDelivery.equals(findNameKindOfDelivery)) {
                findKindDelivery = kindDelivery;
                break;
            }
        }
        return findKindDelivery;
    }
}
