package deductions.runtime.jena

import org.scalatest.FunSuite

import org.apache.log4j.Logger

import org.apache.jena.tdb.TDBFactory
import org.apache.jena.riot.RDFDataMgr
import org.scalatest.Ignore

object TestJenaRDFDataApp extends App with TestJenaRDFDataMgrRaw {
  test()
}

//@Ignore
class TestJenaRDFDataMgr extends FunSuite with TestJenaRDFDataMgrRaw {
  test("JenaHelpers.storeURI") { test() }
}

trait TestJenaRDFDataMgrRaw extends ImplementationSettings.RDFModule {
  import ops._

  def test() {
    val uri = ops.makeUri("file:///home/jmv/ontologies/foaf.n3")
    val graphUri = ops.makeUri("urn:foaf")
    lazy val dataset = TDBFactory.createDataset("TDB")
    //    lazy val store = JenaStore(dataset, defensiveCopy = false)
    //    store.writeTransaction {
    rdfStore.rw(dataset, {
      Logger.getRootLogger().info(s"storeURI uri $uri ")
      try {
        val gForStore = rdfStore.getGraph(dataset, graphUri).getOrElse(emptyGraph)
        //      val gForStore = store.addGraph(graphUri)
        RDFDataMgr.read(gForStore, uri.toString())
        Logger.getRootLogger().info(s"storeURI uri $uri : stored")
      } catch {
        case t: Throwable => Logger.getRootLogger().error("ERROR: " + t)
      }
    })
    dataset.close
  }
}