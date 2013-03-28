package x.y.z

import java.util.List
import java.util.Map

class GenericParameter
{
  def
  setAddresses
  (
    addresses : List[Address]
  ) : Unit =
  {
    this.addresses = addresses
  }


  protected def
  setAddressMap
  (
    addressMap : Map[String, Address]
  ) : Unit =
  {
  }

  private var addresses : List[Address] = null
}
