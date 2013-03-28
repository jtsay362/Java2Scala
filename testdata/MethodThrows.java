import java.text.ParseException;

class MethodThrows
{
  public void a() throws RuntimeException
  {
    System.out.println("a");
  }
  
  public String 
  b()
  throws java.io.IOException
  {
    return "b";
  }

  
  public String 
  c()
  throws java.io.IOException, RuntimeException, ParseException
  {
    return "c";
  } 
}