class ExtendMultipleImplements extends Address with Cloneable with java.io.Serializable
{
  override def clone() : ExtendMultipleImplements = new ExtendMultipleImplements()
}
