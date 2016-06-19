package by.training.filmstore.entity;

public enum Status {
	PAID("оплачено"),UNPAID("не оплачено"),ANNULED("отменено");
	private final String nameStatus;
	private Status(String nameStatus)
	{
		this.nameStatus = nameStatus;
	}
	public String getNameStatus()
	{
		return nameStatus;
	}
    public static Status getStatusByName(String findNameStatus) {
       Status findStatus = null;
    	for (Status status: Status.values()) {
            if (status.nameStatus.equals(findNameStatus)) {
                findStatus = status;
                break;
            }
        }
        return findStatus;
    }
}
