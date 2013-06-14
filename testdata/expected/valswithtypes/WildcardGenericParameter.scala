package x.y.z

import java.util.List
import java.util.Map

class WildcardGenericParameter
{
  def
  setList
  (
    addresses : List[_]
  ) : Unit =
  {
    this.addresses = addresses
  }


  protected def
  setAddressMap
  (
    addressMap : Map[String, _]
  ) : Unit =
  {
  }
  
  /**
   * Adds a ConnectInterceptor to receive callbacks during the connection process.
   * Useful for programmatic configuration.
   * @param interceptor the connect interceptor to add
   */    
  def
  addInterceptor
  (
    interceptor : ConnectInterceptor[_]
  ) : Unit =
  {
    var serviceApiType : Class[_] = GenericTypeResolver.resolveTypeArgument(interceptor.getClass(), classOf[ConnectInterceptor])
    connectInterceptors.add(serviceApiType, interceptor)
  }
    
  private List[_] addresses = null
}
