class DefaultConstructorWithBody
{
  public DefaultConstructorWithBody()
  {
    int i = 0;
    while (i < 3)
    {
      state[i] = "" + i;

      if (i == 2)
      {
        System.out.println("Almost done");
      }

      i++;
    }
  }

  String[] state = new String[3];
}


