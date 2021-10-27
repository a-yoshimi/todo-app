/**
 *
 * to do sample project
 *
 */
package model
import lib.model.TodoCategory._


// Todo ページのviewvalue
case class ViewValueCategory(
                          title:              String,
                          cssSrc:             Seq[String],
                          jsSrc:              Seq[String],
                          categoryList:       Seq[TodoCategory]
                        ) extends ViewValueCommon
// 画面表示用モデル
case class TodoCategory(
    id:    Id,
    name:  String,
    slug:  String,
    color: Short
                       )
