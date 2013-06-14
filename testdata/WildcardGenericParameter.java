package x.y.z;

import java.util.List;
import java.util.Map;

public class WildcardGenericParameter
{
  public void setList
  (
    List<?> addresses
  )
  {
    this.addresses = addresses
  }


  protected void
  setAddressMap
  (
    Map<String, ?> addressMap
  ) 
  {
  }
  
  /**
   * Adds a ConnectInterceptor to receive callbacks during the connection process.
   * Useful for programmatic configuration.
   * @param interceptor the connect interceptor to add
   */    
  public void addInterceptor(ConnectInterceptor<?> interceptor) {
    Class<?> serviceApiType = GenericTypeResolver.resolveTypeArgument(interceptor.getClass(), classOf[ConnectInterceptor])
    connectInterceptors.add(serviceApiType, interceptor)
  }
    
  private List<?> addresses = null;
}
