(ns samutamm.models.db
  (:require [clojure.java.jdbc :as sql]
            [environ.core :refer [env]]
            [samutamm.helpers :refer :all]))

(def db-with-password  {:subprotocol "postgresql"
                        :subname (env :db-url)
                        :user (env :db-user)
                        :password (env :db-pass)})

(def db-without-password  {:subprotocol "postgresql"
                           :subname (env :db-url)
                           :user (env :db-user)})

(defn check-env [symboli] (env symboli))

(def db
  (let [db-info (cond (not (nil? (System/getenv "DATABASE_URL")))
                      (System/getenv "DATABASE_URL")
                      (nil? (env :db-pass))
                      db-without-password
                      :else
                      db-with-password)]
    db-info))


(defn create-projects-table []
  (do (println (str "start creating projects table with db " db))
    (sql/with-connection db
      (sql/create-table
       :projects
       [:id "SERIAL PRIMARY KEY"]
       [:projectname "varchar(100)"]
       [:description "varchar(1000)"]
       [:tags "varchar(200)"]
       [:projectstart "date"]
       [:projectend "date"]
       [:created "timestamp"]))))

(defn get-project [id]
  (sql/with-connection db
    (sql/with-query-results
      res ["select * from projects where id = ?" id] (first res))))

(defn get-all-projects []
  (sql/with-connection db
    (sql/with-query-results res
      ["select * from projects ORDER BY created DESC"]
      (doall res))))

(defn update-or-create-project [id projectname description tags projectstart projectend]
  "Updates the project defined by id. If no project found with that id, new project will
  be created with descending id serial. No project should have id zero, so that can be
  used when creating new project."
  (let [timestamp (java.sql.Timestamp. (.getTime (java.util.Date.)))
        start (make-sql-date (:year projectstart) (:month projectstart) (:day projectstart))
        end (make-sql-date (:year projectend) (:month projectend) (:day projectend))]
    (sql/with-connection db
      (try
        (sql/update-or-insert-values
         :projects
         ["id=?" id]
         {:projectname projectname
          :description description
          :tags tags
          :projectstart start
          :projectend end
          :created timestamp})
        (catch Exception e (.printStackTrace (.getNextException e)))))))

(defn delete-all-projects []
  (sql/with-connection db
    (sql/delete-rows :projects ["id!=?" 0])))

(defn delete-project [id]
  (sql/with-connection db
    (try
      (sql/delete-rows :projects ["id=?" id])
    (catch Exception e (.printStackTrace (.getNextException e))))))

(defn projects-table-is-created? []
  (sql/with-connection db
    (sql/with-query-results res
      [(str "select count(*) from information_schema.tables "
                       "where table_name='projects'")]
      (pos? (:count (first res))))))

(defn migrate-db []
  (if (not (projects-table-is-created?))
    (create-projects-table)
    (delete-all-projects)))

(defn drop-projects-table []
  (sql/with-connection db
    (try
      (sql/drop-table :projects)
      (catch Exception _))))
