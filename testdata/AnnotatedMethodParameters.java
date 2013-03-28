import javax.annotation.Resource;

class AnnotatedMethodParameters
{
  void a
  (
    @Resource AnnotatedMethodParameters that,
    int ordinaryParam    
  )
  {
    System.out.println(that)  
  }

  void b
  (
    float f,
    @javax.annotation.Resource AnnotatedMethodParameters that,
    int ordinaryParam    
  )
  {
    System.out.println(that)  
  }

  
  void c
  (
    float f,
    @Resource AnnotatedMethodParameters that,
    @Resource(name = "hi") String foo
  )
  {
    System.out.println(that)  
  }  
}