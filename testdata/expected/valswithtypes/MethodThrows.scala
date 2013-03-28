import java.text.ParseException

class MethodThrows
{
  @throws(classOf[RuntimeException])
  def a() : Unit =
  {
    System.out.println("a")
  }
  
  @throws(classOf[java.io.IOException])
  def b() : String = "b"

  
  @throws(classOf[java.io.IOException])
  @throws(classOf[RuntimeException])
  @throws(classOf[ParseException])
  def c() : String = "c" 
}