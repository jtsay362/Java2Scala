package com.ngweb.java2scala

import java.io.File

import org.testng.Assert._
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import org.scalatest.Assertions
import org.scalatest.matchers.ShouldMatchers

import com.taco.gut.resource._
import com.taco.gut.resource.file._
import com.taco.gut.resource.string._

class AppTest extends Assertions with ShouldMatchers
{
  @Test
  def testPackageImport() : Unit = testFile("PackageImport")

  @Test
  def testWrapperTypes() : Unit = testFile("WrapperTypes")

  @Test
  def testBasicClass() : Unit = testFile("BasicClass")
  
  @Test
  def testStaticInnerClass() : Unit = testFile("StaticInnerClass")  

  @Test
  def testSingleImplement() : Unit = testFile("SingleImplement")

  @Test
  def testExtendSingleImplement() : Unit = testFile("ExtendSingleImplement")

  @Test
  def testMultipleImplements() : Unit = testFile("MultipleImplements")

  @Test
  def testExtendMultipleImplements() : Unit = testFile("ExtendMultipleImplements")

  @Test
  def testEmptyDefaultConstructor() : Unit = testFile("EmptyDefaultConstructor")

  @Test
  def testDefaultConstructorWithBody() : Unit =
    testFile("DefaultConstructorWithBody")

  @Test
  def testPrimitiveField() : Unit = testFile("PrimitiveField")

  @Test
  def testMethodParameters() : Unit = testFile("MethodParameters")

  @Test
  def testAnnotatedMethod() : Unit = testFile("AnnotatedMethod")    
  
  @Test
  def testAnnotatedMethodParameters() : Unit = testFile("AnnotatedMethodParameters")  
  
  @Test
  def testMethodThrows() : Unit = testFile("MethodThrows")
  
  @Test
  def testOverrideMethod() : Unit = testFile("OverrideMethod")

  @Test
  def testVoidMethod() : Unit = testFile("VoidMethod")

  @Test
  def testStaticMethods() : Unit = testFile("StaticMethods")  
  
  @Test
  def testArrayDef() : Unit = testFile("ArrayDeclarations")
  
  @Test
  def testMultilineDef() : Unit = testFile("MultilineDeclaration") 

  @Test
  def testGenericDef() : Unit = testFile("GenericDef")

  @Test
  def testGenericParameter() : Unit = testFile("GenericParameter")
  
  @Test
  def testWildcardGenericParameter() : Unit = testFile("WildcardGenericParameter")  

  @Test
  def testGenericReturnType() : Unit = testFile("GenericReturnType")
   
  @Test
  def testNestedGenerics() : Unit = testFile("NestedGenerics")
    
  @Test
  def testStaticfields() : Unit = testFile("StaticFields")
  
  @Test
  def testEnhancedFor() : Unit = testFile("EnhancedFor")

  @Test
  def testDollar() : Unit = testFile("Dollar")

  @Test
  def testStringConversion() : Unit = testFile("String")

  @Test
  def testStringAssignment() : Unit = testFile("StringAssignment")

  @Test
  def testFilledArray() : Unit = testFile("FilledArray")
  
  @Test
  def testCasts() : Unit = testFile("Casts")
  
  @Test
  def testStaticImports() : Unit = testFile("ImportStatic")
  
  protected def testFile
  (
    filename : String
  ) : Unit =
  {
    List("default", "valswithtypes").foreach(profile =>
    {        
      val outDirName = "target" +  File.separator + "testout" + File.separator +
        profile
        
      new File(outDirName).mkdirs()

      val inFilename = "testdata" + File.separator + filename + ".java"
      val outFilename = outDirName + File.separator + filename + ".scala"

      App.main(
        profile match
        {
          case "valswithtypes" =>  Array(inFilename, "-typedvals", "-o", 
                                         outFilename)
          case _ => Array(inFilename, "-o", outFilename)
        }
      )  

      val fr = FileResourceSystem.instance.get(
        outFilename, null).asInstanceOf[IDataResource]

      assertTrue(FileResourceSystem.instance.exists(fr))

      val sr = new StringBufferResource()

      sr.getSystem.copy(fr, sr, null)

      val expectedFilename = "testdata/expected/" + profile + "/" + filename + 
                             ".scala"

      val expectedFr = FileResourceSystem.instance.get(
        expectedFilename, null).asInstanceOf[IDataResource]

      assertTrue(FileResourceSystem.instance.exists(expectedFr),
        "Can't find expected file '" + expectedFilename + "'")

      val expectedSr = new StringBufferResource()

      expectedSr.getSystem.copy(expectedFr, expectedSr, null)

      val output = sr.toString

      output.trim should equal (expectedSr.toString.trim)

      output
    })
  }
}


