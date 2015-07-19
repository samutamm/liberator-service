(ns samutamm.test.models.db
  (:use midje.sweet
        samutamm.models.db))

(def testproject {:id 1 :projectname "Big-T" :description "cool" :tags "#"
                            :projectstart {:year 2000 :month 6 :day 10} :projectend {:year 2000 :month 8 :day 10}})

(defn make-sql-date [year month day]
  (java.sql.Date.
   (.getTimeInMillis
    (java.util.GregorianCalendar. year month day))))

(defn clean-database []
  "Before tests drop table and create new one.
  This ensures that db-schema is not outdated."
  (do
    (drop-projects-table)
    (migrate-db)))

(defn upd-or-create [field newvalue]
  (let [proj (assoc testproject field newvalue)]
    (update-or-create-project (:id proj)
                              (:projectname proj)
                              (:description proj)
                              (:tags proj)
                              (:projectstart proj)
                              (:projectend proj))))

(with-state-changes [(before :facts (clean-database))]
  (fact "database is created"
        (projects-table-is-created?) => true)
  (fact "test db is empty"
        (count (get-all-projects )) => 0)

  (fact "can add project"
        (do (upd-or-create :no "params")
            (count (get-all-projects ))) => 1)
  (fact "can add two projects"
        (do
          (upd-or-create :id 1)
          (upd-or-create :id 2)
            (count (get-all-projects ))) => 2)
  (fact "added project contains projectname"
        (do (upd-or-create :no "params")
          (:projectname (get-project 1 ))) => (:projectname testproject))
  (fact "can update project"
        (do (upd-or-create :no "params")
            (upd-or-create :projectname "kizomberas")
            (:projectname (get-project 1 ))) => "kizomberas"))
