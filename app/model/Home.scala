/**
 *
 * to do sample project
 *
 */

package model

// Topページのviewvalue
case class ViewValueHome(
                          title:        String,
                          cssSrc:       Seq[String],
                          jsSrc:        Seq[String],
                          categoryList: Seq[TodoCategory],
                          todoList:     Seq[Todo]
) extends ViewValueCommon

