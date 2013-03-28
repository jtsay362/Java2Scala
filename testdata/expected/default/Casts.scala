import java.util.Locale

class Casts
{
  def
  yo
  (
    arg : Object
  ) : Unit =
  {
    var casted : String = arg.asInstanceOf[String]
    var original : Object = casted.asInstanceOf[java.lang.Object]
  
    System.out.println(arg.toString().toUpperCase(Locale.US))
  
    var f : Float = 4.5f
    var i : Int = f.toInt
    var l : Long = i.toLong
    var d : Double = f.toDouble
    var f2 : Float = d.toFloat
    var b : Byte = i.toByte
    var s : Short = i.toShort    
  }
}