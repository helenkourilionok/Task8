package by.training.filmstore.entity;

public enum Quality {
	DVDRip("DVDRip"),CAMPRip("CAMPRip"),WEBDL("WEB-DL"),WEBDLRip("WEB-DLRip");
	private String nameQuality;
	private Quality(String nameQuality)
	{
		this.nameQuality = nameQuality;
	}
	public String getNameQuality()
	{
		return nameQuality;
	}
	public static Quality getQualityByName(String findNameQuality) {
	    Quality findQuality = null;
	  	for (Quality quality: Quality.values()) {
	         if (quality.nameQuality.equals(findNameQuality)) {
	             findQuality = quality;
	              break;
	         }
	    }
	    return findQuality;
	}
}