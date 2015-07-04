(ns samutamm.test.routes.projects
  (:use midje.sweet
        samutamm.routes.projects))

(fact "db-config is 'projects' db"
      db-config => (fn[conf] (.contains (:subname conf)"projects")))
