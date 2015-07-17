(ns samutamm.test.models.db
  (:use midje.sweet
        samutamm.models.db))

(defn make-sql-date [year month day]
  (java.sql.Date.
   (.getTimeInMillis
    (java.util.GregorianCalendar. year month day))))


(with-state-changes [(before :facts (delete-all-projects))]
  (fact "database is created"
        (projects-table-is-created?) => true)
  (fact "test db is empty"
        (count (get-all-projects )) => 0)

  (fact "can add project"
        (do (update-or-create-project
                                      "1" "projectname" "description" "tags"
                                      {:year 2015 :month 5 :day 11} {:year 2015 :month 6 :day 26})
            (count (get-all-projects ))) => 1
            (generate-new-id) => 99)
  (fact "can add two projects"
        (do (update-or-create-project
                                      "1" "projectname" "description" "tags"
                                     {:year 2015 :month 5 :day 11} {:year 2015 :month 6 :day 26})
            (update-or-create-project
                                      "2" "projectname" "description" "tags"
                                     {:year 2015 :month 5 :day 11} {:year 2015 :month 6 :day 26})
            (count (get-all-projects ))) => 2)
  (fact "added project contains projectname"
        (do (update-or-create-project
                                      "1" "pelle" "description" "tags"
                                      {:year 2015 :month 5 :day 11} {:year 2015 :month 6 :day 26})
          (:projectname (get-project "1" ))) => "pelle")
  (fact "can update project"
        (do (update-or-create-project
                                      "1" "pelle" "description" "tags"
                                      {:year 2015 :month 5 :day 11} {:year 2015 :month 6 :day 26})
            (update-or-create-project
                                        "1" "kizomberas" "description" "tags"
                                       {:year 2015 :month 5 :day 11} {:year 2015 :month 6 :day 26})
            (:projectname (get-project "1" ))) => "kizomberas"))
