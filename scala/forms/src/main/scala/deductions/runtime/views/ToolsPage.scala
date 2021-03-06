package deductions.runtime.views

import scala.xml.NodeSeq
import java.net.URLEncoder

import deductions.runtime.utils.I18NMessages
import deductions.runtime.utils.HTTPrequest
import deductions.runtime.html.BasicWidgets
import scala.xml.Unparsed
import deductions.runtime.utils.URIManagement

trait ToolsPage extends EnterButtons
    with BasicWidgets
    with URIManagement {

  /** HTML page with 2 SPARQL Queries: select & construct, show Named Graphs, history, YASGUI, etc */
  def getPage(lang: String = "en", request: HTTPrequest ): NodeSeq = {
		def absoluteURI = request.absoluteURL("")
		def localSparqlEndpoint = URLEncoder.encode(absoluteURI + "/sparql", "UTF-8")

    val querySampleSelect =
    """|PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
       |PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
       |SELECT * WHERE { GRAPH ?G {?S ?P ?O . } } LIMIT 100
       |#SELECT DISTINCT ?CLASS WHERE { GRAPH ?G { [] a  ?CLASS . } } LIMIT 100
       |SELECT DISTINCT ?PROP WHERE { GRAPH ?G { [] ?PROP [] . } } LIMIT 100
    """.stripMargin
    val querySampleConstruct =
      """|PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
         |Prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>
         |CONSTRUCT { ?S ?P ?O . } WHERE { GRAPH ?G { ?S ?P ?O . } } LIMIT 10
         |#CONSTRUCT { ?sub geo:long ?LON .?sub geo:lat ?LAT . ?sub rdfs:label ?LAB.}
         |#WHERE {GRAPH ?GRAPH { ?sub geo:long ?LON .?sub geo:lat ?LAT . ?sub rdfs:label ?LAB.  } }"""
    .stripMargin

    <link href="assets/images/favicon.png" type="image/png" rel="shortcut icon"/>
    <div>
      <p>
        SPARQL select {
          // TODO: the URL here appears also in Play! route!
          sparqlQueryForm(lang,false, "", "/select-ui", Seq( querySampleSelect ))
        }

      </p>
      <p>
        SPARQL construct {
          sparqlQueryForm(lang,true, "",
        		  // TODO: the URL here appears also in Play! route!
              "/sparql-ui",
            Seq( querySampleConstruct ))
         } 
         </p>
      <p>
      <a href={
      //  ,query=${URLEncoder.encode(querySample, "UTF-8")
        s"""http://yasgui.org?endpoint=$localSparqlEndpoint"""
      } target="_blank">
        YasGUI</a>
        Yet Another SPARL GUI) 
      </p>

      <p> <a href="/showNamedGraphs">{ I18NMessages.get("showNamedGraphs", lang) }</a> </p>
      <p> <a href="/history">{ I18NMessages.get("Dashboard", lang) }</a> </p>
      {
        implicit val lang1: String = lang
        "Charger / Load" ++ enterURItoDownloadAndDisplay()
      }
      <p> <a href="..">{ I18NMessages.get("MainPage", lang) }</a> </p>
    </div>
  }

  /** HTML Form for a sparql Query, with execution buttons */
  def sparqlQueryForm(lang:String = "en",viewButton: Boolean, query: String, action: String,
      sampleQueries: Seq[String]): NodeSeq = {
    val textareaId = s"query-$action" . replaceAll("/", "-")
    println( "textareaId " + textareaId);

    val buttons = Seq(
      <input class="btn btn-primary" type="submit" value={ I18NMessages.get("View", lang) } formaction="/sparql-form"/> )

    val buttonsNextRelease = Seq(
      <input class="btn btn-primary" type="submit" value={ I18NMessages.get("View", lang) } formaction="/sparql-form"/>, // deactivate this for release
      makeLinkCarto(lang, textareaId,
        "https://advancedcartographywebcomponent.github.io/ACWC-Tree/?geo="),
      <input class="btn btn-primary" type="submit" value={ I18NMessages.get("Table", lang) }/>,
      <input class="btn btn-primary" type="submit" value={ I18NMessages.get("Tree", lang) }/>,
      makeLink(textareaId, "/assets/rdfviewer/rdfviewer.html?url="))

    <form role="form">
      <textarea name="query" id={textareaId} style="min-width:80em; min-height:8em" title="To get started, uncomment one of these lines.">{
        if (query != "")
          query          
        else
          sampleQueries .mkString ("\n" )
      }</textarea>

      <div class="container">
        <div class="btn-group">
          <input class ="btn btn-primary" type="submit" value={ I18NMessages.get("Submit", lang) }  formaction ={action} />
          {	if (viewButton) buttons }
        </div>
      </div>
    </form>
  }

  /** NOTE: for RDF Viewer this cannot work in localhost (because of rdfviewer limitations);
   *  works only on a hosted Internet server.
   *  TODO merge with function makeLinkCarto */
  private def makeLink(textareaId: String, toolURLprefix: String,
               toolname: String = "RDF Viewer",
               imgWidth: Int = 15): NodeSeq = {

    val sparqlServicePrefix = URLEncoder.encode( URLEncoder.encode("sparql?query=", "UTF-8"), "UTF-8")
    val buttonId = textareaId+"-button-1"
    val ( servicesURIPrefix, isDNS) = servicesURIPrefix2
    println(s"servicesURIPrefix $servicesURIPrefix, is DNS $isDNS")
    val servicesURIPrefixEncoded = URLEncoder.encode( URLEncoder.encode(servicesURIPrefix, "UTF-8"), "UTF-8")
    val servicesURL = s"$toolURLprefix$servicesURIPrefixEncoded$sparqlServicePrefix"
    println(s">>>> servicesURL $servicesURL")

    <button id={buttonId}
    class="btn btn-default" title={ s"Draw RDF graph with $toolname" } target="_blank">
      <img width={ imgWidth.toString() } border="0" src="https://www.w3.org/RDF/icons/rdf_flyer.svg"
      alt="RDF Resource Description Framework Flyer Icon"/>
    </button>
    <script>
{ Unparsed( s"""
(function() {
  var textarea = document.getElementById('$textareaId');
  console.log('textareaId "$textareaId", textarea ' + textarea);
  var button = document.getElementById('$buttonId');
  console.log('button ' + button);
  button.addEventListener( 'click', function() {
    console.log( 'elementById ' + textarea);
    var query = textarea.value;
    console.log( 'query in textarea ' + query);
    console.log( 'services URL $servicesURL' );
    var url = '$servicesURL' +
      window.encodeURIComponent( window.encodeURIComponent(query)) +
      '%0D%0Aurldecode';
    console.log( 'URL ' + url );
    if($isDNS)
      window.open( url , '_blank' );
    else
      console.log( 'RDFViewer works only on a hosted Internet serverURL !!!' );
  });
    console.log('After button.addEventListener');
}());
""")}
    </script>
  }

  private def makeLinkCarto(lang:String = "en", textareaId: String, toolURLprefix: String): NodeSeq = {

    val sparqlServicePrefix =
//        URLEncoder.encode(
            "sparql?query="
//        , "UTF-8")
    val buttonId = textareaId+"-button"
    val ( servicesURIPrefix, isDNS) = servicesURIPrefix2
    println(s"servicesURIPrefix $servicesURIPrefix, is DNS $isDNS")
    val servicesURL = s"$toolURLprefix$servicesURIPrefix$sparqlServicePrefix"
    println(s">>>> servicesURL $servicesURL")

    <input id={buttonId}
            class="btn btn-primary" type="submit" target="_blank" value={ I18NMessages.get("Map", lang)}>
    </input>
      <script>
        { Unparsed( s"""
(function() {
  var textarea = document.getElementById('$textareaId');
  console.log('textareaId "$textareaId", textarea ' + textarea);
  var button = document.getElementById('$buttonId');
  button.addEventListener( 'click', function() {
    console.log( 'elementById ' + textarea);
    var query = textarea.value;
    console.log( 'query in textarea ' + query);
    console.log( 'services URL $servicesURL' );
    var url = '$servicesURL' +
//      window.encodeURIComponent( 
      window.encodeURIComponent
      (query)
      // )
      ;
       console.log( 'URL ' + url );
    window.open( url , '_blank' );

  });
}());
""")}
      </script>
  }
   
                        
}
