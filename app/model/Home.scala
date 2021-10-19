/**
 *
 * to do sample project
 *
 */

package model

// Topページのviewvalue
case class ViewValueHome(
                          title:    String,
                          cssSrc:   Seq[String],
                          jsSrc:    Seq[String],
                          todoList: Seq[ToDo]
) extends ViewValueCommon

