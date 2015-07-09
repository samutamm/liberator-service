(ns samutamm.test.routes.projects
  (:use midje.sweet
        samutamm.routes.projects))

(def testproject {:id "77" :projectname "Big-T" :description "cool" :tags "#"
                            :projectstart "2000-03-19T22:00:00.000-00:00" :projectend "2000-03-19T22:00:00.000-00:00"})

(fact "project validation works"
      (project-is-valid testproject) => true
      (project-is-valid (dissoc testproject :tags)) => false
      (project-is-valid (assoc testproject :projectend nil)) => false)
