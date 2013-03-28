package x.y.z

import scala.collection.JavaConversions._
import java.util.Collection
import java.util.ArrayList

class EnhancedFor
{
  def doit() : Unit =
  {
    var addresses : Collection[Address] = new ArrayList[Address]()

    addresses.add(new Address())
    
    for (address <- addresses)
    {
      System.out.println("address = " + address.toString())
    }
  }
}


