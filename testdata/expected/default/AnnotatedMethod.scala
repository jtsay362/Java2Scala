import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

class AnnotatedMethod
{
  @RequestMapping(value = "/register_with_external_account.do",
    method = RequestMethod.GET)
  def hi() : Int = 2 
  
  @RequestMapping(method=RequestMethod.GET)
  def
  connectionStatus
  (
    request : NativeWebRequest,
    model : Model
  ) : String = 
  connectView()  
}