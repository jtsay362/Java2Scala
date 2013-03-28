class FilledArray
{
  int[] a = new int[] { 1, 2, 3 };
  
  Object[] b = new Object[] { 
    "1", 
    "2", 
    "3" 
  };

  Object[] inner = new Object[] { "1", "2",
    new Object[] { "a", "b" } };  
}