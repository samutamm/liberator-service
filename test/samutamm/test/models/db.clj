(ns samutamm.test.models.db
  (:use midje.sweet
        samutamm.models.db))

;;TODO refactor test code to look more pretty

(def dev-db   {:subprotocol "postgresql"
                 :subname "//localhost/projects"
                 :user "admin"
                 :password "admin"})

(def test-db   {:subprotocol "postgresql"
                 :subname "//localhost/testprojects"
                 :user "admin"
                 :password "admin"})

(with-state-changes [(before :facts (do (delete-project test-db "1")(delete-project test-db "2")))]
  (fact "test db is empty"
        (count (get-all-projects test-db)) => 0)
  (fact "dev db is not empty"
        (count (get-all-projects dev-db)) => (fn [c] (not (zero? c))))

  (fact "can add project"
        (do (update-or-create-project test-db
                                      "1" "projectname" "description" "tags"
                                      (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
            (count (get-all-projects test-db))) => 1)
  (fact "can add two projects"
        (do (update-or-create-project test-db
                                      "1" "projectname" "description" "tags"
                                      (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
            (update-or-create-project test-db
                                      "2" "projectname" "description" "tags"
                                      (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
            (count (get-all-projects test-db))) => 2)
  (fact "added project contains projectname"
        (do (update-or-create-project test-db
                                      "1" "pelle" "description" "tags"
                                      (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
          (:projectname (get-project "1" test-db))) => "pelle")
  (fact "can update project"
        (do (update-or-create-project test-db
                                      "1" "pelle" "description" "tags"
                                      (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
            (update-or-create-project test-db
                                        "1" "kizomberas" "description" "tags"
                                        (make-sql-date 2015 5 11) (make-sql-date 2015 6 26))
            (:projectname (get-project "1" test-db))) => "kizomberas"))
