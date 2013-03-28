package x.y.z

import java.util.Set
import java.util.HashSet
import java.util.Map
import java.util.HashMap
        
class GenericDef {
  private var roles : Set[Role] = new HashSet[Role]()

  var f : Map[String, Role] = new HashMap[String, Role]()
}
