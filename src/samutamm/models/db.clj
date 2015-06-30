(ns samutamm.models.db
  (:require [clojure.java.jdbc :as sql]))

(def is-dev-db (atom true))

(def db-config (or (System/getenv "DATABASE_URL")
        {:subprotocol "postgresql"
         :subname "//localhost/projects"
         :user "admin"
         :password "admin"}))

(defn make-sql-date
  [year month day]
  (java.sql.Date.
   (.getTimeInMillis
    (java.util.GregorianCalendar. year month day))))

(defn create-projects-table [db]
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

(defn get-project [id db]
  (sql/with-connection db
    (sql/with-query-results
      res ["select * from projects where id = ?" id] (first res))))

(defn get-all-projects [db]
  (sql/with-connection db
    (sql/with-query-results res
      ["select * from projects ORDER BY created DESC"]
      (doall res))))

(defn update-or-create-project [db id projectname description tags projectstart projectend]
  (let [timestamp (java.sql.Timestamp. (.getTime (java.util.Date.)))]
    (sql/with-connection db
      (sql/update-or-insert-values
      :projects
      ["id=?" id]
      {:id id
       :projectname projectname
       :description description
       :tags tags
       :projectstart projectstart
       :projectend projectend
       :created timestamp}))))

(defn delete-project [db id]
  (sql/with-connection db
    (sql/delete-rows :projects ["id=?" id])))

(defn testi [] (str "Moikka"))
