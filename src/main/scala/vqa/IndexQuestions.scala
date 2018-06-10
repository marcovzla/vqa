package vqa

import java.nio.file.Path
import java.util.Collection
import scala.collection.JavaConverters._
import com.typesafe.scalalogging.LazyLogging
import com.typesafe.config.ConfigFactory
import ai.lum.common.ConfigUtils._
import org.apache.lucene.index.{ IndexWriter, IndexWriterConfig }
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.document.Field.Store
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{ Document, StoredField, TextField }
import IndexWriterConfig.OpenMode

object IndexQuestions extends LazyLogging {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val filename = config[String]("dataFile")
    val indexDir = config[Path]("indexDir")

    logger.info(s"reading $filename ...")
    val questions = XmlReader.loadFile(filename)

    logger.info(s"read ${questions.size} questions ...")

    logger.info(s"writing index at $indexDir ...")
    val dir = FSDirectory.open(indexDir)
    val analyzer = new StandardAnalyzer()
    val writerConfig = new IndexWriterConfig(analyzer)
    writerConfig.setOpenMode(OpenMode.CREATE)
    val writer = new IndexWriter(dir, writerConfig)
    for (q <- questions) {
      val block = mkDocumentBlock(q)
      writer.addDocuments(block)
    }
    writer.close()

  }

  def mkDocumentBlock(q: Question): Collection[Document] = {
    // the child documents must appear first, ending with the parent document
    val block = q.answers.map(mkAnswerDocument) :+ mkQuestionDocument(q)
    block.asJava
  }

  def mkQuestionDocument(q: Question): Document = {
    val doc = new Document
    doc.add(new StoredField("docId", q.docId))
    doc.add(new StoredField("qualityScore", q.qualityScore))
    doc.add(new StoredField("category", q.category))
    doc.add(new TextField("question", q.text, Store.YES))
    doc
  }

  def mkAnswerDocument(a: Answer): Document = {
    val doc = new Document
    doc.add(new TextField("answer", a.text, Store.YES))
    doc.add(new StoredField("docId", a.docId))
    doc.add(new StoredField("gold", a.gold.toString))
    doc
  }

}
