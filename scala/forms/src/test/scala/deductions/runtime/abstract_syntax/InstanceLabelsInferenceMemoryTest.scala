package deductions.runtime.abstract_syntax

import java.io.FileInputStream
import java.io.FileOutputStream
import scala.util.Try

import org.hamcrest.BaseMatcher
import org.junit.Assert
import org.scalatest.FunSuite

import org.w3.banana.FOAFPrefix
import org.w3.banana.RDFModule
import org.w3.banana.RDFOpsModule
import org.w3.banana.TurtleReaderModule
import org.w3.banana.diesel._
import org.w3.banana.jena.JenaModule
import org.w3.banana.TurtleWriterModule
import org.w3.banana.SparqlOpsModule
import org.w3.banana.SparqlGraphModule
import org.w3.banana.jena.Jena

import deductions.runtime.jena.RDFStoreLocalJena1Provider
import deductions.runtime.sparql_cache.RDFCacheAlgo
import deductions.runtime.jena.ImplementationSettings

class InstanceLabelsInferenceMemoryTest extends FunSuite
    with JenaModule
    with RDFStoreLocalJena1Provider
    with InstanceLabelsInferenceMemory[ImplementationSettings.Rdf, ImplementationSettings.DATASET]
    with RDFCacheAlgo[ImplementationSettings.Rdf, ImplementationSettings.DATASET] {
  import ops._
  import rdfStore.transactorSyntax._

  test("TDB contains label") {
    val uri = URI("http://jmvanel.free.fr/jmv.rdf#me")
    storeUriInNamedGraph(uri)
    val res = {
      dataset.rw({
        val lab = instanceLabel(uri, allNamedGraph, "fr")
        println(s"label for $uri: $lab")
        // TODO check that the value is stored in TDB
      })
    }
    println(res)
  }

}