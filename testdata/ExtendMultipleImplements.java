public class ExtendMultipleImplements extends Address implements Cloneable,
  java.io.Serializable
{
  @Override
  public ExtendMultipleImplements clone()
  {
    return new ExtendMultipleImplements();
  }
}
