import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

class AnnotatedMethod
{
  @RequestMapping(value = "/register_with_external_account.do",
    method = RequestMethod.GET)
  public int hi() {
    return 2;
  } 
  
  @RequestMapping(method=RequestMethod.GET)
  public String connectionStatus(NativeWebRequest request, Model model) {
    return connectView();
  }  
}