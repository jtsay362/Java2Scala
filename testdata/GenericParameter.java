package x.y.z;

import java.util.List;
import java.util.Map;

public class GenericParameter
{
  public void setAddresses
  (
    List<Address> addresses
  )
  {
    this.addresses = addresses
  }


  protected void
  setAddressMap
  (
    Map<String, Address> addressMap
  ) 
  {
  }

  private List<Address> addresses = null;
}
