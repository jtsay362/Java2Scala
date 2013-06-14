import java.util.List
import java.util.LinkedList
import java.util.HashSet

class NestedGenerics
{
  def getList() : List[ Map[String, _] ] = new LinkedList[ Map[String, _] ]()
  
  private var something : List[ _ <: Set[NestedGenerics] ] = new LinkedList[ _ <: Set[NestedGenerics] ]()
      
  private var weird : Set[List[_ >: NestedGenerics]] = new HashSet()      
}