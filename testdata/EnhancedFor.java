package x.y.z;

import java.util.Collection;
import java.util.ArrayList;

public class EnhancedFor
{
  public void doit()
  {
    Collection<Address> addresses = new ArrayList<Address>();

    addresses.add(new Address());
    
    for (Address address : addresses)
    {
      System.out.println("address = " + address.toString());
    }
  }
}


