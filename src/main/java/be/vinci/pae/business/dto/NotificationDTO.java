package be.vinci.pae.business.dto;

public interface NotificationDTO {

  public Integer getId();

  public void setId(Integer id);

  public int getObject();

  public void setObject(int object);

  public String getMessage();

  public void setMessage(String message);

  public String getType();

  public void setType(String type);
}
