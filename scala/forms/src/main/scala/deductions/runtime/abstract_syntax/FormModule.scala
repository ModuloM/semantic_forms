/* copyright the Déductions Project
under GNU Lesser General Public License
http://www.gnu.org/licenses/lgpl.html
$Id$
 */
package deductions.runtime.abstract_syntax

import org.w3.banana._
import org.w3.banana.diesel._
import org.w3.banana.syntax._
import scala.collection.mutable

trait FormModule[NODE, URI <: NODE] {
  /**
   * abstract_syntax for a semantic form , called FA (Abstract Form) :
   *  - generated from a list of URI's for properties, and a triple store
   *  - used in conjunction with HTML5 forms and Banana-RDF
   *  - could be used with N3Form(Swing) in EulerGUI,
   */
  case class FormSyntax[NODE, URI <: NODE](
      val subject: NODE,
      val fields: Seq[Entry],
      classs: URI = nullURI) {
    override def toString(): String = {
      s"""FormSyntax:
        subject: $subject
      """ + fields.mkString("\n")
    }
  }

  val nullURI: URI
  /** TODO somehow factor out value: Any ? */
  sealed abstract class Entry(val label: String, val comment: String,
      mandatory: Boolean = false) {
    override def toString(): String = {
      s""" "$label", "$comment" """
    }
  }
  case class ResourceEntry(l: String, c: String,
    property: ObjectProperty, validator: ResourceValidator,
    value: URI = nullURI, alreadyInDatabase: Boolean = true,
    possibleValues: Seq[(URI, String)] = Seq(),
    valueLabel: String = "")
      extends Entry(l, c) {
    override def toString(): String = {
      super.toString + s""" : <$value>, "$valueLabel" """
    }
  }
  case class BlankNodeEntry(l: String, c: String,
      property: ObjectProperty, validator: ResourceValidator,
      value: NODE) extends Entry(l, c) {
    override def toString(): String = {
      super.toString + ", " + value
    }
    def getId: String = value.toString
  }
  case class LiteralEntry(l: String, c: String,
      property: DatatypeProperty, validator: DatatypeValidator,
      value: String = "", widgetType: WidgetType = Text) extends Entry(l, c) {
    override def toString(): String = {
      super.toString + s""" := "$value" """
    }
  }

  type DatatypeProperty = URI
  type ObjectProperty = URI

  case class ResourceValidator(typ: Set[URI])
  case class DatatypeValidator(typ: Set[URI])

  sealed class WidgetType
  object Text extends WidgetType
  object Textarea extends WidgetType
  object Checkbox extends WidgetType
  object Choice extends WidgetType
  object Collection extends WidgetType
}
