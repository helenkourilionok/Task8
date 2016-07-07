package by.training.filmstore.entity;

public enum KindOfDelivery {
	MAILING("��������� �� �����"),COURIER("��������"),
	SELFDELIVERY("���������"),ANOTHER("������ �� ������������ � ����������");
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
