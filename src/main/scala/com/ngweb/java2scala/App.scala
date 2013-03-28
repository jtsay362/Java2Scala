package com.ngweb.java2scala

import java.text.ParseException
import java.util.regex.Matcher

import scala.util.matching.Regex
import scala.collection.mutable

import com.taco.text.CPunctuationState
import com.taco.gut.resource._
import com.taco.gut.resource.file._
import com.taco.gut.resource.string._

object App
{
  def main(args: Array[String]) : Unit =
  {
    new App().convert(args)
  }
}

class App
{

  /**
   * @param args the command line arguments
   */
  def convert(args: Array[String]): Unit =
  {
    var inFilename : String = null
    var outFilename : String = null

    def printUsage()
    {
      println("Usage: java com.ngweb.java2scala.App inFile [-o outFile]")
      exit(0)
    }

    def readOptions()
    {
      var wantOutputFilename : Boolean = false

      args.foreach(arg =>
        {
          if (wantOutputFilename)
          {
            outFilename = arg
          }
          else if (arg == "-o")
          {
            wantOutputFilename = true
          }
          else if (arg == "-typedvals")
          {
            doAddTypesToVals = true
          }
          else if (inFilename == null)
          {
            inFilename = arg
          }
          else
          {
            println("Invalid command line")
            printUsage()
          }
        }
      )

      if (inFilename == null)
      {
        printUsage()
      }

      if (outFilename == null)
      {
         outFilename = inFilename.replaceAll("""\bjava\b""", "scala")
      }
    }


    println("Java 2 Scala")

    readOptions()

    println("Input file = '" + inFilename + "'")       
    
    val fr = FileResourceSystem.instance.get(
      inFilename, null).asInstanceOf[IDataResource]

    if (!FileResourceSystem.instance.exists(fr))
    {
      throw new RuntimeException("File '" + inFilename + "' does not exist")
    }

    val sr = new StringBufferResource()

    sr.getSystem.copy(fr, sr, null)

    val s = convertSource(sr.toString)

    //println("converted file = \n" + s)

    val system = FileResourceSystem.instance
    val ofr = system.get(outFilename, null).
      asInstanceOf[IDataResource]

    val dir = system.getParentContainer(ofr)

    println("out dir = " + dir.getFullName)
    
    if (!system.exists(dir)) 
    {
    	system.createContainer(dir, null)
    }

    //dir.asInstanceOf[FileResourceContainer].getFile().mkdirs()

    FileResourceSystem.instance.copy(new StringResource(s), ofr, null)

    FileResourceSystem.instance.shutdown()
  }
  
  def 
  convertSource
  (
    source : String
  ) : String =
  {
    var s = convertWrapperTypes(source)

    
    s = convertClasses(s)

    s = convertInterfaces(s)

    s = convertExtendsAndImplements(s)

    s = convertDefaultConstructors(s)

    s = convertMethods(s)

    s = convertEnhancedForLoops(s)

    s = convertEmptyArrayCreationExpressions(s)
    
    s = convertFilledArrayCreationExpressions(s)

    s = convertPrimitiveTypeCasts(s)
    
    s = convertUserTypeCasts(s)
    
    s = convertDeclarations(s)
          
    s = convertGenerics(s)

    s = convertClassReferences(s)

    s = convertInstanceOf(s)    
    
    s = convertCatches(s)
            
    s = adjustImports(s)

    s = removeSemicolons(s)    
    
    s
  }

  private def convertWrapperTypes
  (
    s: String
  ) : String =
  {
    var converted = s
    
    
    List("Integer", "Long", "Boolean", "Character", "Byte", "Float", "Double").
      foreach(wrapperType =>        
      {
        converted = ("""\b""" + wrapperType + """\b""").r.replaceAllIn(converted,
        md =>
        {
          wrapperImports.add(wrapperType)
          "J" + md.matched
        })  
      }
    ) 
    
    converted
  }

  private def convertGenerics
  (
    s : String
  ) : String =
  {
    GENERICS_REGEX.replaceAllIn(s, (md) =>
    {
      val sb = new StringBuilder("[")

      TYPE_PARAMETER_REGEX.findAllIn(md.group(0)).matchData.foreach((tpmd) =>
      {
        sb ++= tpmd.group(1)

        tpmd.group(2) match
        {
          case "super" => sb ++= " >: " + tpmd.group(3)
          case "extends" => sb ++= " <: " + tpmd.group(3)
          case _ =>
        }

        sb ++= ", "
      })

      sb.setLength(sb.length - 2)

      sb ++= "]"

      sb.toString()
    })
  }

  private def convertClassReferences
  ( 
    s : String
  ) : String =
  {
    ("(" + USER_TYPE_REGEX_STRING + """)\.class""").r.replaceAllIn(s, (md) =>
    {
      Matcher.quoteReplacement("classOf[" + md.group(1) + "]")
    })
  }

  private def convertInstanceOf
  (
    s : String
  ) : String =
  {
    ("""(\w+)\s+instanceof\s+(""" + USER_TYPE_REGEX_STRING + ")").r.replaceAllIn(s,
      (md) =>
      {
        Matcher.quoteReplacement(md.group(1) + ".isInstanceOf[" + md.group(2) + "]")
      })      
  }

  private def convertPrimitiveTypeCasts
  (
    s : String
  ) : String =
  {
    ("""\(\s*(""" + PRIMITIVE_TYPE_REGEX_STRING + 
     """)\s*\)\s*([\w\.]+)\s*([;,])""").r.replaceAllIn(s, 
      md =>
      {
        Matcher.quoteReplacement(md.group(2) + ".to" + 
          md.group(1).capitalize + md.group(3))
      }
    )
  }
  
  
  private def convertUserTypeCasts
  (
    s : String
  ) : String =
  {
    ("""\(\s*(""" + USER_TYPE_REGEX_STRING + 
     """)\s*\)\s*([\w\.]+)\s*([;,])""").r.replaceAllIn(s, 
      md =>
      {
        Matcher.quoteReplacement(md.group(2) + ".asInstanceOf[" + md.group(1) + 
                                 "]" + md.group(3))
      }
    )
  }

  private def convertClasses
  (
    s : String
  ) : String =
  {
    """\bpublic\s+((?:abstract|final)\s+)*class\b""".r.replaceAllIn(s,
      "$1class")
  }

  private def convertInterfaces
  (
    s : String
  ) : String =
  {
    """\b(public\s+)?interface\b""".r.replaceAllIn(s, "trait")
  }

  private def convertExtendsAndImplements
  (
    s : String
  ) : String =
  {
    EXTENDS_IMPLEMENTS_CLAUSE_REGEX.replaceAllIn(s, (md) =>
    {
      println("Converting extends/implements clause " + md.group(0))

      for (i <- 0 to md.groupCount)
      {
        println("group " + i + " = " + md.group(i))
      }

      var foundFirstType = false

      Option(md.group(1)).map((t) =>
        {
          foundFirstType = true
          "extends " + t
        }
      ).getOrElse("") +
      Option(md.group(9)).map((a) =>
      {
        (
          if (foundFirstType)
          {
            " with "
          }
          else
          {
            foundFirstType = true
            
            " extends "
          }
        ) + a
      }).getOrElse("") +
      Option(md.group(16)).map((a) =>
      {
        ADDITIONAL_GENERIC_USER_TYPE_LIST_REGEX.replaceAllIn(a, (amd) =>
        {
          " with " + Matcher.quoteReplacement(amd.group(1))
        })
      }).getOrElse("")
    })
  }

  private def convertDefaultConstructors
  (
    s : String
  ) : String =
  {
    val DEFAULT_CONSTRUCTOR_REGEX = """public\s+[A-Z]\w+\s*\(\s*\)\s*\{""".r

    var rv = s

    var done = false

    while (!done)
    {
      DEFAULT_CONSTRUCTOR_REGEX.findFirstMatchIn(rv) match
      {
        case Some(m) =>
        {
          // Match the opening brace to get the constructor body
          try
          {
            val endIndex = new CPunctuationState().matchPunctuation(rv,
              m.end - 1)

            val constructorBody = rv.substring(m.end, endIndex)

            if (constructorBody.trim() == "")
            {
              // No body
              rv = rv.substring(0, m.start) + rv.substring(endIndex + 1)
            }
            else
            {
              // Include the opening and closing braces in case there are
              // local variable declarations
              rv = rv.substring(0, m.start) + "{" + constructorBody +
               rv.substring(endIndex)
            }
          }
          catch
          {
            case pe : ParseException => done = true
          }
        }

        case None => done = true
      }
    }

    rv
  }

  private def convertType
  (
    rt : String
  ) : String =
  {
    if (rt == "void")
    {
      "Unit"
    }
    else 
    {
      Matcher.quoteReplacement(
      ARRAY_TYPE_SUFFIX_REGEX.findFirstMatchIn(rt).map((m) =>
        "Array[" + convertType(rt.substring(0, m.start(1))) + "]").getOrElse(
          if (rt.contains("."))
          {
            rt
          }
          else
          {
            rt.capitalize
          }
        )
      )
    }
  }

  private def convertMethods
  (
    s : String
  ) : String =
  {
    METHOD_SIGNATURE_REGEX.replaceAllIn(s, md =>
    {      
      val methodParameters = md.group(METHOD_GROUP_NAME_PARAMETERS)
      
      val noArgs = Option(methodParameters).map( 
        _.trim().isEmpty ).getOrElse(true)  
        
      println("Converting method " + md.group(0) + ", no args = " + noArgs)  
        
      val sb = new StringBuilder

      val nl = "\n" + indent
      
      val declSpacing =   
        if (noArgs) 
        {
          " "
        }
        else
        { 
          nl
        }
        
      val declSpacing2 = 
        if (noArgs) 
        {
          ""
        }
        else
        { 
          nl
        }

      var doOverride = "";

      Option(md.group(METHOD_GROUP_NAME_ANNOTATIONS)).foreach(a =>
      {
        //println("matched annotations = '" + a + "'")

        sb ++= nl

        ANNOTATION_REGEX.findAllIn(a).foreach(annotation =>

        if (annotation.matches("""\s*@Override\s+"""))
        {
          doOverride = "override "
        }
        else
        {
          sb ++= indent + annotation.trim() + "\n"
        })
      })

      Option(md.group(METHOD_GROUP_NAME_THROWS)).foreach(a =>
      {
        sb ++= "\n"                

        USER_TYPE_REGEX.findAllIn(a).foreach(exClazz =>        
          sb ++= indent + "@throws(classOf[" + exClazz + "])\n"
        )
      })

      sb ++= indent + (doOverride +
       Option(md.group(METHOD_GROUP_NAME_MODIFIERS)).getOrElse("").replaceAll(
        """(public|abstract|synchronized|native)\s*+""", "").replaceAll(
        """static\s*+""", "/* static */ ").trim)
    
      appendIfEndsWithNonWhitespace(sb, " ")
        
      sb ++= "def" + declSpacing + 
       Matcher.quoteReplacement(md.group(METHOD_GROUP_NAME_NAME)) +
       Matcher.quoteReplacement(Option(md.group(METHOD_GROUP_NAME_TYPE_PARAMETERS)).getOrElse("")) +
       declSpacing2 + "(" + declSpacing2
      
      if (methodParameters != null)
      {
        METHOD_PARAMETER_REGEX.findAllIn(methodParameters).matchData.foreach(
        mpd =>
        {
          sb ++= indent + Matcher.quoteReplacement(mpd.group(1) + mpd.group(9)) + 
            " : " + convertType(mpd.group(2)) + "," + nl
        })

        sb.setLength(sb.length - ("," + nl).length)
        sb ++= nl
      }

      sb ++= ") : "

      sb ++= convertType(md.group(METHOD_GROUP_NAME_RETURN_TYPE))

      var hasImmediateReturn = false
      if (md.group(METHOD_GROUP_NAME_SEMICOLON) == null)
      {
        sb ++= " = "

        Option(md.group(METHOD_GROUP_NAME_IMMEDIATE_RETURN_VALUE)).foreach(rv =>
          {
            hasImmediateReturn = true
            sb ++= declSpacing2
            sb ++= rv
          }
        )
      }
      //println("converted method =\n" + sb.toString.trim)

        
        
      sb.toString.trim + (
        if (hasImmediateReturn)
        {
          "" 
        }
        else
        {
          nl  
        })
        
    })
  }

  private def convertEnhancedForLoops
  (
    s: String
  ) : String =
  {
    ENHANCED_FOR_REGEX.replaceAllIn(s, (md) =>
    {
      needJavaConversions = true
        
      Matcher.quoteReplacement("for (" + md.group(7) + " <- " + md.group(8) + ")")
    })    
  }


  private def convertDeclarations
  (
    s : String
  ) : String =
  {
    //  There seems to be a problem with the (Match) => String version of
    // replaceAllIn which interpolates strings such as \\d => \d, so we need
    // to run quoteReplacement()
    DECLARATION_REGEX.replaceAllIn(s, (md) =>
    {
      val isVal = md.group(1).contains("final") 
        
      Matcher.quoteReplacement(md.group(1).
       replaceAll("""(public|final)\s+""", "").
       replaceAll("""static\s+""", "/* static */ ") +
       (if (isVal) "val" else "var") + " " +
       md.group(9) + 
       (
         if (!isVal || doAddTypesToVals)
         {
           " : " + convertType(md.group(2))
         }  
         else
         {
           ""
         }
       ) +                                                                  
       " = " + Option(md.group(10)).getOrElse("_")
      ).trim
    })
  }

  // FIXME: add support for multiple dimensions like [2][3]
  private def convertEmptyArrayCreationExpressions
  (
    s : String
  ) : String =
  {
    ("""new\s+(""" + ANY_TYPE_REGEX_STRING + """)\s*\[([^\]]+)\]""").r.replaceAllIn(
      s, (md) =>
    {
      "new Array[" + md.group(1) + "](" + md.group(8) + ")"
    })
  }
  
  private def convertFilledArrayCreationExpressions
  (
    s : String
  ) : String =
  {
    val FILLED_ARRAY_REGEX = ("""new\s+(""" + ANY_TYPE_REGEX_STRING + 
      """)\s*\[\s*\]\s*\{""").r
      
    var rv = s

    var done = false

    while (!done)
    {
      FILLED_ARRAY_REGEX.findFirstMatchIn(rv) match
      {
        case Some(m) =>
        {
          // Match the opening brace to get the array contents
          try
          {
            val endIndex = new CPunctuationState().matchPunctuation(rv,
              m.end - 1)

            val arrayContents = rv.substring(m.end, endIndex)

            // Include the opening and closing braces in case there are
            // local variable declarations
            rv = rv.substring(0, m.start) + "Array[" + m.group(1) + "](" +            
              arrayContents + ")" + rv.substring(endIndex + 1)
          }
          catch
          {
            case pe : ParseException => done = true
          }
        }

        case None => done = true
      }
    }

    rv
  }

  
  

  private def convertCatches
  (
    s : String
  ) : String =
  {
    """catch\s*\((\S+)\s+(\S+)\)(\s*)\{""".r.replaceAllIn(s, (md) =>
    {
      Matcher.quoteReplacement("catch" + md.group(3) + "{" + md.group(3) + indent + "case " +
      md.group(2) + " : " + md.group(1)) + " => "
    })
  }

  private def removeSemicolons
  (
    s: String
  ) : String =
  {
    "(.+);[ \t]*(\r\n?|\n)".r.replaceAllIn(s, (md) =>
      Matcher.quoteReplacement(md.group(1) + md.group(2))      
    ) 
  }

  private def adjustImports
  (
    s : String
  ) : String =
  {
    val s1 = """(?m)^[ \t]*(import\s+.*;)""".r.replaceFirstIn(s,
      (
        {
          val sb = new StringBuilder()

          wrapperImports.toList.sorted.foreach(wrapperType =>          
          {
            sb ++= "import java.lang.{" + wrapperType + " => J" + wrapperType + "}\n"
          })

          if (!wrapperImports.isEmpty)
          {
            sb ++= "\n\n"
          }
          
          sb.toString()
        }
      ) +
      (
        if (needJavaConversions)
        {
          "import scala.collection.JavaConversions._\n"
        }
        else
        {
          ""
        }
      ) + "$1"
    )

    // Adjust package imports .* => ._
    """(?m)^[ \t]*import\s+(.*)\.\*\s*;""".r.replaceAllIn(s1, "import $1._")
  }

  //"""\$""".r.replaceAllIn(s, """\\\$""")

  private def
  appendIfEndsWithNonWhitespace
  (
    sb : StringBuilder,
    s : String
  ) : StringBuilder =
  {
    if (!sb.isEmpty && !Character.isWhitespace(sb.last))
    {
      sb ++= s
    }

    sb
  }
  
  val wrapperImports = mutable.HashSet[String]()
    
  var needJavaConversions : Boolean = false

  // Captures 3 groups
  val TYPE_PARAMETER_REGEX_STRING =
    """([A-Z]\w*)(?:\s+(extends|super)\s+([A-Z]\w*))?"""

  val TYPE_PARAMETER_REGEX = TYPE_PARAMETER_REGEX_STRING.r

  // Captures 6 groups
  val GENERICS_REGEX = ("""<\s*""" + TYPE_PARAMETER_REGEX_STRING +
    """(?:\s*,\s*""" + TYPE_PARAMETER_REGEX_STRING + """)*\s*>""").r

  val ANNOTATION_REGEX_STRING = """(?:@\w.*\s*)"""

  val ANNOTATION_REGEX = ANNOTATION_REGEX_STRING.r

  // Captures 0 groups
  val USER_TYPE_REGEX_STRING = """(?:(?:[a-z][a-z0-9_]*\.)*[A-Z]\w*)"""

  val USER_TYPE_REGEX = USER_TYPE_REGEX_STRING.r

  // Captures 6 groups
  val GENERIC_USER_TYPE_REGEX_STRING = USER_TYPE_REGEX_STRING + """(?:\s*""" +
    GENERICS_REGEX + ")?"

  val ARRAY_TYPE_SUFFIX_REGEX_STRING = """(?:\s*\[\s*\]\s*)"""

  val ARRAY_TYPE_SUFFIX_REGEX = ("(" + ARRAY_TYPE_SUFFIX_REGEX_STRING + 
                                 ")$").r

  val PRIMITIVE_TYPE_REGEX_STRING = 
    "int|long|boolean|char|byte|short|float|double"
  
  // Captures 6 groups
  val ANY_TYPE_REGEX_STRING = "(?:(?:" + GENERIC_USER_TYPE_REGEX_STRING +
     "|" + PRIMITIVE_TYPE_REGEX_STRING + ")" +
     ARRAY_TYPE_SUFFIX_REGEX_STRING + "*)"

  val VARIABLE_NAME_REGEX_STRING = """(?:(?:[a-z_]\w*)|(?:[A-Z_]+))"""

  // Captures 8 groups
  // Group 1: Type
  // Group 8: Variable name
  val RAW_DECLARATION_REGEX_STRING = 
    "(" + ANY_TYPE_REGEX_STRING + """)\s+(""" + VARIABLE_NAME_REGEX_STRING +
    ")"

  // Captures 7 groups
  val ADDITIONAL_GENERIC_USER_TYPE_LIST_REGEX_STRING =
    """\s*,\s*(""" + GENERIC_USER_TYPE_REGEX_STRING + ")"

  val ADDITIONAL_GENERIC_USER_TYPE_LIST_REGEX =
     ADDITIONAL_GENERIC_USER_TYPE_LIST_REGEX_STRING.r

  // Captures 23 groups
  // Group 1: extended type (may be null)
  // Group 8: implements clause (may be null)
  // Group 9: first implemented type (may be null)
  // Group 16: all other implemented types (may be null)
  val EXTENDS_IMPLEMENTS_CLAUSE_REGEX_STRING =
    """\b(?:extends\s+(""" + GENERIC_USER_TYPE_REGEX_STRING + """))?(\s+""" +
    """implements\s+(""" + GENERIC_USER_TYPE_REGEX_STRING + ")((?:" +
    ADDITIONAL_GENERIC_USER_TYPE_LIST_REGEX_STRING  + ")*)?)"

  val EXTENDS_IMPLEMENTS_CLAUSE_REGEX = EXTENDS_IMPLEMENTS_CLAUSE_REGEX_STRING.r

  // TODO: synchronized not handled properly yet
  // Captures 1 group
  val METHOD_MODIFIERS_REGEX_STRING =
    """((?:(?:public|protected|private|abstract|final|static|synchronized|native)\b\s*)*)"""

  // Captures 9 groups
  val METHOD_PARAMETER_REGEX_STRING =
    """((?:@[\w\.]+\s*(?:\([^\)]*\)\s*)?\s*)*)(?:final\s+)?""" + RAW_DECLARATION_REGEX_STRING

  val METHOD_PARAMETER_REGEX = METHOD_PARAMETER_REGEX_STRING.r

  val IMMEDIATE_RETURN_BODY_REGEX_STRING = """\{\s*return\s+([^};]+);\s*\}"""

  val METHOD_SIGNATURE_REGEX_STRING = 
    "(" + ANNOTATION_REGEX_STRING + "*)" +
    METHOD_MODIFIERS_REGEX_STRING + 
    """\b\s*(\[.+?\]\s*)?\b(""" + ANY_TYPE_REGEX_STRING +
    """|void)\s+([a-z_]\w*)\s*\(\s*(""" +
    METHOD_PARAMETER_REGEX_STRING +
    """(?:\s*,\s*""" + METHOD_PARAMETER_REGEX_STRING  +
    """)*\s*+)?\)\s*""" +
    """(?:\bthrows\s+(""" + USER_TYPE_REGEX_STRING + """(?:\s*,\s*""" +
    USER_TYPE_REGEX_STRING + """)*)\s*)?""" +
    """(?:(;)|(?:""" + IMMEDIATE_RETURN_BODY_REGEX_STRING +  """))?"""

  
  val METHOD_GROUP_NAME_ANNOTATIONS = "annotations"
  val METHOD_GROUP_NAME_MODIFIERS = "modifiers"
  val METHOD_GROUP_NAME_TYPE_PARAMETERS = "typeParameters"
  val METHOD_GROUP_NAME_RETURN_TYPE = "returnType"
  val METHOD_GROUP_NAME_NAME = "name"
  val METHOD_GROUP_NAME_PARAMETERS = "parameters"
  val METHOD_GROUP_NAME_THROWS = "throws"
  val METHOD_GROUP_NAME_SEMICOLON = "semicolon"
  val METHOD_GROUP_NAME_IMMEDIATE_RETURN_VALUE = "immediateReturnValue"

  val METHOD_SIGNATURE_REGEX = new Regex(METHOD_SIGNATURE_REGEX_STRING,
    METHOD_GROUP_NAME_ANNOTATIONS,
    METHOD_GROUP_NAME_MODIFIERS,
    METHOD_GROUP_NAME_TYPE_PARAMETERS,
    METHOD_GROUP_NAME_RETURN_TYPE,
    "5", "6", "7", "8", "9", "10",               
    METHOD_GROUP_NAME_NAME,
    METHOD_GROUP_NAME_PARAMETERS, 
    // 9 groups per method parameter
    "13", "14", "15", "16", "17", "18", "19", "20", "21",
    "22", "23", "24", "25", "26", "27", "28", "21", "22",
    METHOD_GROUP_NAME_THROWS,
    METHOD_GROUP_NAME_SEMICOLON,
    METHOD_GROUP_NAME_IMMEDIATE_RETURN_VALUE)

  // Captures 8 groups
  // Group 7: Element variable name
  // Group 8: Collection variable name
  val ENHANCED_FOR_REGEX = new Regex(
    """for\s*\(\s*""" + GENERIC_USER_TYPE_REGEX_STRING + """\s+(""" +
    VARIABLE_NAME_REGEX_STRING + """)\s*:\s*(""" + VARIABLE_NAME_REGEX_STRING +
    """)\s*\)"""
  )

  // Captures 1 group, which is everything
  val DECLARATION_MODIFIERS_REGEX_STRING =
    """((?:(?:public|protected|private|static|final|volatile|strictfp)\s++)*)"""

  // Captures 11 groups
  // Group 1: Modifiers
  // Group 2: Type
  // Group 9: Variable name
  // Group 10: Assigned value
  val DECLARATION_REGEX_STRING =
    DECLARATION_MODIFIERS_REGEX_STRING + 
    RAW_DECLARATION_REGEX_STRING + """\s*(?:=\s*([^;]+))?;"""

  val DECLARATION_REGEX = DECLARATION_REGEX_STRING.r

  var indent = "  "
  
  var doAddTypesToVals = false
}
