(ns samutamm.test.routes.projects
  (:use midje.sweet
        samutamm.routes.projects
        samutamm.test.handler))


(fact "project validation works"
      (project-is-valid testproject) => true
      (project-is-valid (dissoc testproject :tags)) => false
      (project-is-valid (assoc testproject :projectend nil)) => false)


(fact "parsing json works"
      (let [request-mock {:request (create-request testproject)}]
        (parse-project request-mock) => (fn[project] (project-is-valid project))))
