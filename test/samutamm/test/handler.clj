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

  (fact "projects route returns json"
        (let [response (app (request :get "/projects"))]
          (get (:headers response) "Content-Type") => "application/json;charset=UTF-8"))

  (fact "projects json contains all fields projects"
        (let [response (app (request :get "/projects"))
              fields ["id" "projectname" "description" "tags" "projectstart" "projectend" "created"]]
          (:body response) => (fn[body] (every? true?
                                                (map  (fn [field]
                                                        (.contains body field)) fields)))))
