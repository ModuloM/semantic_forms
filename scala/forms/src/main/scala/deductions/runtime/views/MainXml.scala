package deductions.runtime.views

import deductions.runtime.services.Configuration

import scala.xml.NodeSeq
import deductions.runtime.utils.I18NMessages

import scala.xml.NodeSeq.seqToNodeSeq

/** HTML page skeleton for the generic SF application */
trait MainXml extends ToolsPage with EnterButtons {

  /**
   * main Page with a single content (typically a form)
   */
  def mainPage(content: NodeSeq, userInfo: NodeSeq, lang: String = "en", title: String = "", displaySearch: Boolean = true,
      messages: NodeSeq =
        <p>New feature considered:
<a href="https://github.com/jmvanel/semantic_forms/issues/152">
Checker for good practices in RDF and OWL #152
</a>
</p>
      ) = {
    <html>
      <head>{ head(title)(lang) }</head>
      <body>
        {mainPageHeader(lang, userInfo, displaySearch)}
        { messages }
        <div class="container sf-complete-form">
        {content}
        </div>
        {pageBottom(lang)}
      </body>
    </html>
      }

  def head(title: String = "")(implicit lang: String = "en"): NodeSeq = <head></head>

  /** page bottom (overridable!) **/
  def pageBottom(lang: String = "en"): NodeSeq = linkToToolsPage(lang)

  def linkToToolsPage(lang: String = "en") =

          <footer class="navbar navbar-default navbar-fixed-bottom sf-footer">
            <a href="/tools">{ I18NMessages.get("Tools", lang) }</a> /
            <a href="https://github.com/jmvanel/semantic_forms/wiki/User_manual">User Manual</a> /
            <a href="https://github.com/jmvanel/semantic_forms/wiki/Manuel-utilisateur">Manuel utilisateur</a> /
            { I18NMessages.get("POWERED", lang) }
            <a href="https://github.com/jmvanel/semantic_forms/blob/master/scala/forms_play/README.md#play-framework-implementations">
              semantic_forms
            </a> /
            Version =timestamp=
          </footer>



  /**
   * main Page Header for generic app:
   *  enter URI, search, create instance
   */
  def mainPageHeader(implicit lang: String = "en", userInfo: NodeSeq, displaySearch: Boolean = true): NodeSeq = {

    <header>
      <div class="row">
        <div class="col col-sm-8 col-sm-offset-1">
          <a href="/" title="Open a new Semantic_forms in a new tab." target="_blank">
            <h1>
           {
              messageI18N("Welcome")
              }
            </h1>
          </a>
        </div>
        <div class="col col-sm-3">
          {userInfo}
        </div>
      </div>
    </header>

      <div class="row sf-margin-top-10">
        <div class="col-xs-12 col-sm-12 col-md-12 col-md-offset-1">
          <div class="col-xs-6 col-sm-4  col-md-1" >{
            if (displaySearch){
              <button type="button" class="form-control btn btn-primary" data-toggle="collapse" data-target="#collapseDisplay">{ messageI18N("Reduce") }</button>
            }
            }
          </div>
        </div>
      </div>
      <div class="collapse in" id="collapseDisplay">{
        if (displaySearch){
            enterURItoDownloadAndDisplay() ++
            enterSearchTerm() ++
            enterClassForCreatingInstance()
        }
        }</div>
      <hr></hr>
  }

  /**
   * main Page with a content consisting of a left panel
   * and a right panel (typically forms);
   *
   * for http://github.com/assemblee-virtuelle/semforms.git,
   * not yet used :(
   */
  def mainPageMultipleContents(contentLeft: NodeSeq,
    contentRight: NodeSeq,
    userInfo: NodeSeq, lang: String = "en") = {
    <html>
      { head(lang) }
      <body>
        {
          Seq(

            mainPageHeader(lang,userInfo),

            <div class="content">
              <div class="left-panel">{ contentLeft }</div>
              <div class="right-panel">{ contentRight }</div>
            </div>,

            linkToToolsPage(lang))
        }
      </body>
    </html>
  }

  /** creation Button for given RDF class */
  def creationButton(classe: String, label: String, formuri: String = ""): NodeSeq =
    <form role="form" action="/create">
      <input type="hidden" name="uri" id="uri" value={ classe }/>
      <input type="hidden" name="formuri" id="formuri" value={
        if (formuri != "") formuri else null
      }/>
      <input type="submit" name="create" id="create" value={ label }/>
    </form>
}
