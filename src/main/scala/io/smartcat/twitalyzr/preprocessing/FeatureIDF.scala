package io.smartcat.twitalyzr.preprocessing

import io.smartcat.twitalyzr.pipeline.Pipeline
import io.smartcat.twitalyzr.util.Conf
import org.apache.spark.ml.feature.{IDF, IDFModel}
import org.apache.spark.sql.DataFrame

class FeatureIDF(idfModels: List[IDFModel]) extends Pipeline {
  override def process(df: DataFrame): DataFrame = idfModels.foldLeft(df)((dff, model) => model.transform(dff))
}

object FeatureIDF extends Serializable {
  val afterIDF : String = Conf.Preprocessing.Sufix.afterIDF


  def make(df: DataFrame, columnNames: List[String]): FeatureIDF = {
    new FeatureIDF(columnNames.map {
      column =>
        new IDF()
          .setInputCol(column)
          .setOutputCol(column + afterIDF)
          .fit(df)
    })
  }

}
