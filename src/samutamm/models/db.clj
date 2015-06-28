(ns samutamm.models.db
  (:require [clojure.java.jdbc :as sql]))

(def db {:subprotocol "postgresql"
         :subname "//localhost/projects"
         :user "admin"
         :password "admin"})

(defn make-sql-date
  "tämän voinee ekstraktoida ulos"
  [year month day]
  (java.sql.Date.
   (.getTimeInMillis
    (java.util.GregorianCalendar. year month day))))

(defn create-projects-table []
  (sql/with-connection db
    (sql/create-table
    :projects
    [:id "varchar(32) PRIMARY KEY"]
    [:projectname "varchar(100)"]
    [:description "varchar(1000)"]
    [:tags "varchar(200)"]
    [:projectstart "date"]
    [:projectend "date"]
    [:created "timestamp"])))

(defn get-project [id]
  (sql/with-connection db
  (sql/with-query-results
    res ["select * from projects where id = ?" id] (first res))))

(defn add-project [id projectname description tags projectstart projectend]
  (let [timestamp (java.sql.Timestamp. (.getTime (java.util.Date.)))]
    (sql/with-connection db
    (sql/insert-rows :projects
      [id projectname description tags projectstart projectend timestamp]))))

(defn delete-project [id]
  (sql/with-connection db
    (sql/delete-rows :projects ["id=?" id])))

(defn testi [] (str "Moikka"))
