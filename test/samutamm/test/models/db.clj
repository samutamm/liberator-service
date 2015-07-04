(ns samutamm.test.models.db
  (:use midje.sweet
        samutamm.models.db))

;;TODO refactor test code to look more pretty


(with-state-changes [(before :facts (do (delete-project  "1")(delete-project  "2")))]
  (fact "database is created"
        (projects-table-is-created?) => true)
  (fact "test db is empty"
        (count (get-all-projects )) => 0)

  (fact "can add project"
        (do (update-or-create-project
                                      "1" "projectname" "description" "tags"
                                      (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
            (count (get-all-projects ))) => 1)
  (fact "can add two projects"
        (do (update-or-create-project
                                      "1" "projectname" "description" "tags"
                                      (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
            (update-or-create-project
                                      "2" "projectname" "description" "tags"
                                      (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
            (count (get-all-projects ))) => 2)
  (fact "added project contains projectname"
        (do (update-or-create-project
                                      "1" "pelle" "description" "tags"
                                      (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
          (:projectname (get-project "1" ))) => "pelle")
  (fact "can update project"
        (do (update-or-create-project
                                      "1" "pelle" "description" "tags"
                                      (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
            (update-or-create-project
                                        "1" "kizomberas" "description" "tags"
                                        (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
            (:projectname (get-project "1" ))) => "kizomberas"))
