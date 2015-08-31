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
       [:image "varchar(200)"]
       [:links "varchar(400)"]
       [:created "timestamp"]))))

(defn create-tag-table []
  (sql/with-connection db
    (sql/create-table
     :tags
     [:id "SERIAL PRIMARY KEY"]
     [:text "varchar(50)"])))

(defn create-user-table []
  (sql/with-connection db
    (sql/create-table
     :users
     [:id "SERIAL PRIMARY KEY"]
     [:username "varchar(50)"]
     [:password "varchar(50)"])))

(defn get-project [id]
  (sql/with-connection db
    (sql/with-query-results
      res ["select * from projects where id = ?" id] (first res))))

(defn get-all-projects []
  (sql/with-connection db
    (sql/with-query-results res
      ["select * from projects ORDER BY created DESC"]
      (doall res))))

(defn get-all-tags []
  (sql/with-connection db
    (sql/with-query-results res
      ["select * from tags"]
      (doall res))))

(defn delete-all-tags []
  (sql/with-connection db
    (sql/delete-rows :tags ["id!=?" 0])))

(defn add-tag [tag]
  (sql/with-connection db
    (sql/update-or-insert-values
      :tags
      ["id=?" 0]
      {:text tag})))

(defn add-user [username password]
  (sql/with-connection db
    (sql/update-or-insert-values
      :users
      ["id=?" 0]
      {:username username
       :password password})))

(defn get-user [username]
  (sql/with-connection db
    (sql/with-query-results res
      ["select * from users where username = ?" username]
      (first res))))

(defn update-or-create-project [project]
  "Updates the project defined by id. If no project found with that id, new project will
  be created with descending id serial. No project should have id zero, so that can be
  used when creating new project."
  (let [timestamp (java.sql.Timestamp. (.getTime (java.util.Date.)))
        start (format-date (:projectstart project))
        end (format-date (:projectend project))]
    (sql/with-connection db
      (try
        (sql/update-or-insert-values
         :projects
         ["id=?" (:id project)]
         {:projectname (:projectname project)
          :description (:description project)
          :tags (:tags project)
          :image (:image project)
          :links (:links project)
          :projectstart start
          :projectend end
          :created timestamp})
        (catch Exception e (.printStackTrace (.getNextException e)))))))

(defn delete-all [table]
  (sql/with-connection db
    (sql/delete-rows table ["id!=?" 0])))

(defn delete-all-projects []
 (delete-all :projects))

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
  (try
    (if (not (projects-table-is-created?))
      (create-projects-table))
    (catch Exception e (.printStackTrace (.getNextException e)))))

(defn drop-table [table]
  (sql/with-connection db
    (try
      (sql/drop-table table)
      (catch Exception _))))

(defn drop-projects-table []
  (drop-table :projects))
