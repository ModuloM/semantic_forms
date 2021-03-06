package deductions.runtime.sparql_cache.apps

import deductions.runtime.utils.RDFPrefixes
import deductions.runtime.jena.ImplementationSettings
import deductions.runtime.jena.RDFCache
import deductions.runtime.services.DefaultConfiguration

/** download & Store Uri In self-Named Graph;
 *  remove previous Graph content */
object RDFLoaderApp extends {
  override val config = new DefaultConfiguration {
    override val useTextQuery = false
  }
} with RDFCache with App
    with ImplementationSettings.RDFCache // RDFStoreLocalJena1Provider
    with RDFPrefixes[ ImplementationSettings.Rdf] {

  import ops._
  val uris = args map { p => URI(expandOrUnchanged(p)) }
  uris map {
    uri =>
      rdfStore.removeGraph(dataset, uri)
      readStoreUriInNamedGraph(uri)
      println(s"loaded <${uri}>")
  }
}