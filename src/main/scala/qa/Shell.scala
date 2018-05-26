package qa

import java.io.File
import java.nio.file.Path
import scala.util.control.NonFatal
import jline.console.ConsoleReader
import jline.console.history.FileHistory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.analysis.standard.StandardAnalyzer
import com.typesafe.scalalogging.LazyLogging
import com.typesafe.config.ConfigFactory
import ai.lum.common.ConfigUtils._

object Shell extends LazyLogging {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val indexDir = config[Path]("indexDir")
    val history = new FileHistory(config[File]("history"))

    // we must flush the history before exiting
    sys.addShutdownHook {
      history.flush()
    }

    // setup reader
    val reader = new ConsoleReader
    reader.setPrompt("> ")
    reader.setHistory(history)
    reader.setExpandEvents(false)

    // setup searcher
    val indexReader = DirectoryReader.open(FSDirectory.open(indexDir))
    val indexSearcher = new IndexSearcher(indexReader)

    val parser = new QueryParser("question", new StandardAnalyzer())

    def search(pattern: String, n: Int): Unit = {
      val query = parser.parse(pattern)
      val results = indexSearcher.search(query, n)
      println(s"found ${results.totalHits} results")
      println("---")
      for (hit <- results.scoreDocs) {
        val doc = indexSearcher.doc(hit.doc)
        val question = Option(doc.getField("question")).map(_.stringValue)
        val answer = Option(doc.getField("answer")).map(_.stringValue)
        val text = (question orElse answer).get
        println(text)
        println("---")
      }
    }

    try {
      // run the shell
      var running = true
      while (running) {
        try {
          val line = reader.readLine()
          if (line == null) {
            println(":exit")
            running = false
          } else {
            line.trim match {
              case "" => ()
              case ":exit" => running = false
              case s if s startsWith "#" => ()
              case s if s startsWith ":" => println(s"unrecognized command '$s'")
              case pattern => search(pattern, 10)
            }
          }
        } catch {
          // if the exception is non-fatal then display it and keep going
          case NonFatal(e) => e.printStackTrace()
        }
      }
    } finally {
      // manual terminal cleanup
      reader.getTerminal().restore()
      reader.shutdown()
    }

  }

}
