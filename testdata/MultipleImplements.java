public class MultipleImplements implements Cloneable,
  java.io.Serializable
{
  @Override
  public MultipleImplements clone()
  {
    return new MultipleImplements();
  }
}
