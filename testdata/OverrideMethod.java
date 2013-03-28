public class OverrideMethod
{
  @Override
  public boolean equals
  (
    Object that
  )
  {
    return (this == that);
  }

  @Override
  public int hashCode()
  {
    return 17;
  }
}
