package deductions.runtime.html

import org.w3.banana.RDF
import org.w3.banana.RDFOps

/**
 * @author jmv
 */
trait Form2HTMLBanana[Rdf <: RDF]
extends Form2HTML[Rdf#Node, Rdf#URI]
with HTML5TypesTrait[Rdf] {

  implicit val ops: RDFOps[Rdf]

  import ops._
  override def toPlainString(n: Rdf#Node): String =
    foldNode(n)(fromUri(_),
        bn => {
          val s = fromBNode(bn)
          if( s.startsWith( "_:" ) ) s
          else "_:" + s
        },
        fromLiteral(_)._1)
}