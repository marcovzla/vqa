package vqa

import scala.xml.{ Node, XML }

case class Question(
  text: String,
  docId: String,
  qualityScore: Int,
  category: String,
  answers: Seq[Answer]
)

case class Answer(
  text: String,
  docId: String,
  gold: Boolean
)

object XmlReader {

  def loadFile(filename: String): Seq[Question] = {
    val xml = XML.loadFile(filename)
    (xml \ "question").map(mkQuestion)
  }

  def mkQuestion(q: Node): Question = {
    Question(
      (q \ "text").text,
      (q \ "docid").text,
      (q \ "quality_score").text.toInt,
      (q \ "category").text,
      (q \\ "answer").map(mkAnswer)
    )
  }

  def mkAnswer(a: Node): Answer = {
    Answer(
      (a \ "text").text,
      (a \ "docid").text,
      (a \ "gold").text.toBoolean
    )
  }

}
