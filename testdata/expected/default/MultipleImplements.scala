class MultipleImplements extends Cloneable with java.io.Serializable
{
  override def clone() : MultipleImplements = new MultipleImplements()
}
