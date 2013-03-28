package x.y.z

// Need at least one import to trigger adjustImports
import java.lang.{Boolean => JBoolean}
import java.lang.{Byte => JByte}
import java.lang.{Character => JCharacter}
import java.lang.{Double => JDouble}
import java.lang.{Float => JFloat}
import java.lang.{Integer => JInteger}
import java.lang.{Long => JLong}


import java.util.ResourceBundle

class WrapperTypes
{
  var wb : JBoolean = _
  var wbyte : JByte = _
  var wchar : JCharacter = _
  var wi : JInteger = _
  var wl : JLong = _
  var wf : JFloat = _
  var wd : JDouble = _

  var nb : Boolean = _
  var nbyte : Byte = _
  var nchar : Char = _
  var ni : Int = _
  var nl : Long = _
  var nf : Float = _
  var nd : Double = _
}
