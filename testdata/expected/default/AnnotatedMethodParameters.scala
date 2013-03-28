import javax.annotation.Resource

class AnnotatedMethodParameters
{
  def
  a
  (
    @Resource that : AnnotatedMethodParameters,
    ordinaryParam : Int
  ) : Unit =
  {
    System.out.println(that)  
  }

  def
  b
  (
    f : Float,
    @javax.annotation.Resource that : AnnotatedMethodParameters,
    ordinaryParam : Int
  ) : Unit =
  {
    System.out.println(that)  
  }

  
  def
  c
  (
    f : Float,
    @Resource that : AnnotatedMethodParameters,
    @Resource(name = "hi") foo : String
  ) : Unit =
  {
    System.out.println(that)  
  }  
}