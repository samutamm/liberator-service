(ns samutamm.test.handler
  (:use midje.sweet
        ring.mock.request
        samutamm.handler))

(defn contains-string [body string]
  (let [body-string (slurp body)]
      (.contains body-string string)))

(fact "main route"
  (let [response (app (request :get "/"))]
    (:status response) => 200
    (:body response) => (fn [body] (contains-string body "<title>samutamm</title>"))))

(fact "not-found route"
  (let [response (app (request :get "/invalidpath"))]
    (:status response) => 404))
