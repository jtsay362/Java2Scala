package x.y.z

import java.util.List

class GenericReturnType
{
  def getAddresses() : List[Address] = addresses

  private var addresses : List[Address] = null
}
