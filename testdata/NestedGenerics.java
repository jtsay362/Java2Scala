import java.util.List;
import java.util.LinkedList;
import java.util.HashSet;

class NestedGenerics
{
  public List< Map<String, ?> > getList() {
    return new LinkedList< Map<String, ?> >(); 
  }
  
  private List< ? extends Set<NestedGenerics> > something = new LinkedList< ? extends Set<NestedGenerics> >();
      
  private Set<List<? super NestedGenerics>> weird = new HashSet<>();      
}