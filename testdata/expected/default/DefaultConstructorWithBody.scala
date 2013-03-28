class DefaultConstructorWithBody
{
  {
    var i : Int = 0
    while (i < 3)
    {
      state[i] = "" + i

      if (i == 2)
      {
        System.out.println("Almost done")
      }

      i++
    }
  }

  var state : Array[String] = new Array[String](3)
}


