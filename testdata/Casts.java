import java.util.Locale;

class Casts
{
  void yo(Object arg)
  {
    String casted = (String) arg;
    Object original = (java.lang.Object) casted;
  
    System.out.println(arg.toString().toUpperCase(Locale.US));
  
    float f = 4.5f;
    int i = (int) f;
    long l = (long) i;
    double d = (double) f;
    float f2 = (float) d;
    byte b = (byte) i;
    short s = (short) i;    
  }
}