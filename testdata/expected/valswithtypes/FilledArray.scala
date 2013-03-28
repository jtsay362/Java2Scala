class FilledArray
{
  var a : Array[Int] = Array[int]( 1, 2, 3 )
  
  var b : Array[Object] = Array[Object]( 
    "1", 
    "2", 
    "3" 
  )

  var inner : Array[Object] = Array[Object]( "1", "2",
    Array[Object]( "a", "b" ) )  
}